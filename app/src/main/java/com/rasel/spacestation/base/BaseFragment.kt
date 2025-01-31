package com.rasel.spacestation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rasel.spacestation.core.dialog.dismissLoadingDialog
import com.rasel.spacestation.presentation.viewmodel.BaseViewModel
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding, ViewModel : BaseViewModel> : Fragment() {

    protected lateinit var binding: VB
    protected abstract val viewModel: ViewModel

    abstract fun getViewBinding(): VB

    lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getViewBinding()
        return binding.root
    }
    protected open fun handleLoading(isLoading: Boolean) {
//        if (isLoading) showLoadingDialog() else dismissLoadingDialog()
    }

    protected fun handleLoading(progressCircular: ProgressBar, isLoading: Boolean) {
        progressCircular.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    protected open fun handleErrorMessage(message: String?) {
        if (message.isNullOrBlank()) return
        dismissLoadingDialog()
        Timber.e(message)
//        showSnackBar(binding.root, message)
    }
}
