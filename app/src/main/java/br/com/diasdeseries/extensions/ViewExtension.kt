package br.com.diasdeseries.extensions

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity


fun FragmentActivity.hideKeyboard() {
    val view = this.currentFocus
    if(view != null){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

