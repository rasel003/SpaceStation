package com.rasel.spacestation.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rasel.spacestation.R
import com.rasel.spacestation.base.BaseFragment
import com.rasel.spacestation.data.models.IssPosition
import com.rasel.spacestation.data.models.SpaceStationResponseModel
import com.rasel.spacestation.databinding.FragmentHomeBinding
import com.rasel.spacestation.presentation.viewmodel.BaseViewModel
import com.rasel.spacestation.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.spacestation.remote.utils.ApiResponse
import com.rasel.spacestation.util.CalendarConstant
import com.rasel.spacestation.util.LocationPermissionHelper.getCountryFromLatLng
import com.rasel.spacestation.util.LocationPermissionHelper.getUserCountryByGPS
import com.rasel.spacestation.util.LocationPermissionHelper.LOCATION_PERMISSION
import com.rasel.spacestation.util.toastError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {

    private var userCountry: String? = null

    private lateinit var auth: FirebaseAuth
    override val viewModel: HomeViewModel by viewModels()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()

//        fetchDataFromServer()
        Timber.tag("rsl").d("On Create : HomeFragment")
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, LOCATION_PERMISSION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, you can access the location
            getUserLocation()
        } else {
            askLocationPermission()
        }
    }

    private fun askLocationPermission() {
        // Request permission
        requestPermissionLauncher.launch(LOCATION_PERMISSION)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setCurrentAuthUserInfo()
        observeRecommendedData()

        viewModel.countdown.observe(viewLifecycleOwner) { timeLeft ->
            binding.tvCountDown.text = buildString {
                append("Refreshing in: ")
                append(timeLeft)
                append(" sec")
            }
        }

        viewModel.refreshTrigger.observe(viewLifecycleOwner) {
            fetchDataFromServer() // Refresh data every 60 sec
            Timber.d("// Refresh data every 60 sec")
        }

        initListener()

    }

    private fun initListener() {
        binding.btnRefresh.setOnClickListener {
            fetchDataFromServer()
        }
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_nav_home_to_nav_login)
        }
    }

    private fun setCurrentAuthUserInfo() {
        val user = auth.currentUser
        binding.tvUserName.text = buildString {
            append("Welcome, ")
            append(user?.displayName)
        }
        binding.tvUserName.isVisible = true
    }

    private fun fetchDataFromServer() {
        viewModel.getRecommendationList(object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                handleErrorResponse()
            }
        })
    }

    private fun observeRecommendedData() {
        viewModel.spaceStationInfo.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it is ApiResponse.Loading
            when (it) {

                is ApiResponse.Success -> {
                    lifecycleScope.launch {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoDataFound.visibility = View.GONE

                        setSpaceStationInfo(it.data)
                    }
                }

                is ApiResponse.Failure -> {
                    handleErrorResponse()
                }

                else -> {}
            }
        }
    }

    private fun handleErrorResponse() {
        binding.tvNoDataFound.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        setSpaceStationVisibility(isVisible = false)
        showRefreshAction()
    }

    private fun setSpaceStationInfo(model: SpaceStationResponseModel) {

        setLocationInfo(model.issPosition)
        setTimeInfo(model.timestamp)

        showRefreshAction()

        setSpaceStationVisibility(isVisible = true)
    }

    private fun showRefreshAction() {
        binding.apply {
            btnRefresh.isVisible = true
            tvCountDown.isVisible = true
        }
        viewModel.startTimer()
    }

    private fun setTimeInfo(timestamp: Int?) {
        timestamp?.let {
            val (utcTime, localTime) = getTimestamps(timestamp = it)
            binding.tvLocalTimeValue.text = localTime
            binding.tvUTCTimeValue.text = utcTime
        } ?: kotlin.run {
            binding.tvLocalTimeValue.text = getString(R.string.n_a)
            binding.tvUTCTimeValue.text = getString(R.string.n_a)
        }
    }

    private fun setLocationInfo(
        issPosition: IssPosition?
    ) {
        val latitude = issPosition?.latitude?.toDoubleOrNull()
        val longitude = issPosition?.longitude?.toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val latLong = "${issPosition.latitude}, ${issPosition.longitude}"
            binding.tvLatLongValue.text = latLong
            getCountryFromLatLng(requireContext(), latitude, longitude) { country ->
//            getCountryFromLatLng(requireContext(), 23.784888, 90.380714) { country ->
                if (country != null) {
                    binding.tvCountryOrRegionValue.text = country

                    if (country == userCountry) {
                        binding.tvCountryOrRegionValue.text = buildString {
                            append("The Space Station is above your Country Now!!")
                        }
                    } else {
                        binding.tvCountryOrRegionValue.text = country
                    }

                } else {
                    binding.tvCountryOrRegionValue.text = getString(R.string.failed_to_get_country)
                }
            }
        } else {
            binding.tvLatLongValue.text = getString(R.string.n_a)
            binding.tvCountryOrRegionValue.text = getString(R.string.n_a)
        }
    }

    private fun setSpaceStationVisibility(isVisible: Boolean) {
        binding.apply {
            tvTitle.isVisible = isVisible
            tvLatLong.isVisible = isVisible
            tvLatLongValue.isVisible = isVisible
            tvUTCTime.isVisible = isVisible
            tvUTCTimeValue.isVisible = isVisible
            tvLocalTime.isVisible = isVisible
            tvLocalTimeValue.isVisible = isVisible
            tvCountryOrRegion.isVisible = isVisible
            tvCountryOrRegionValue.isVisible = isVisible
        }
    }

    private fun getTimestamps(timestamp: Int): Pair<String, String> {
        val instant = Instant.ofEpochSecond(timestamp.toLong())

        // Convert to UTC
        val utcTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
        val formattedUtc =
            utcTime.format(DateTimeFormatter.ofPattern(CalendarConstant.DATE_FORMAT_01))
        // Convert to local time
        val localTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formattedLocal =
            localTime.format(DateTimeFormatter.ofPattern(CalendarConstant.DATE_FORMAT_01))

        return Pair(formattedUtc, formattedLocal)
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserLocation()
        } else {
            mContext.toastError("Permission Denied!")
            checkShouldAskAgainForPermission(mContext)
        }
    }

    private fun checkShouldAskAgainForPermission(context: Context) {
        if (shouldShowRequestPermissionRationale(LOCATION_PERMISSION)) {
            AlertDialog.Builder(context)
                .setTitle("Location Permission")
                .setMessage("Location permission required to show, appropriate message")
                .setCancelable(false)
                .setPositiveButton("Ok") { _, _ ->
                    // Requesting foreground location permission
                    requestPermissionLauncher.launch(LOCATION_PERMISSION)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Foreground location permission rationale cancelled
                }
                .show()
        }
    }

    private fun getUserLocation() {
        getUserCountryByGPS(mContext) {
            userCountry = it
        }
    }
}