package com.rasel.spacestation.core.dialog

import android.app.Dialog

/**
 * show single loading dialog
 */
var loadingDialog: Dialog? = null

fun dismissLoadingDialog() {
    if (loadingDialog?.isShowing == true) {
        loadingDialog?.dismiss()
    }
}

