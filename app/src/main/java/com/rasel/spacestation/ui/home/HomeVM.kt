package com.rasel.spacestation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rasel.spacestation.presentation.utils.CoroutineContextProvider
import com.rasel.spacestation.presentation.utils.ExceptionHandler
import com.rasel.spacestation.presentation.viewmodel.BaseViewModel
import com.rasel.spacestation.presentation.viewmodel.CoroutinesErrorHandler
import com.rasel.spacestation.remote.utils.ApiResponse
import com.rasel.spacestation.data.HomeRepository
import com.rasel.spacestation.data.models.SpaceStationResponseModel
import com.rasel.spacestation.util.cancelIfActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val homeRepository: HomeRepository
) : BaseViewModel(contextProvider) {

    private val _spaceStationInfo: MutableLiveData<ApiResponse<SpaceStationResponseModel>> =
        MutableLiveData()
    val spaceStationInfo: LiveData<ApiResponse<SpaceStationResponseModel>> get() = _spaceStationInfo

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
    }

    private val _countdown = MutableLiveData(60) // Start from 60 seconds
    val countdown: LiveData<Int> get() = _countdown

    private val _refreshTrigger = MutableLiveData<Unit>() // Triggers UI refresh
    val refreshTrigger: LiveData<Unit> get() = _refreshTrigger

    init {
        getRecommendationList(object : CoroutinesErrorHandler {
            override fun onError(message: String) {

            }
        })
        Timber.tag("rsl").d("View Model In it")

    }

    private var timerJob: Job? = null
    fun startTimer() {
        timerJob?.cancelIfActive() // Cancel previous job if active
        timerJob = viewModelScope.launch {
            repeat(60) { time ->
                _countdown.postValue(60 - time) // Countdown from 60 to 1
                delay(1000) // 1 second delay
            }
            _refreshTrigger.postValue(Unit) // Notify UI to refresh
        }
    }
    fun getRecommendationList(
        coroutinesErrorHandler: CoroutinesErrorHandler
    ) = baseRequest(
        _spaceStationInfo,
        coroutinesErrorHandler
    ) {
        homeRepository.getRecommendationList()
    }


}