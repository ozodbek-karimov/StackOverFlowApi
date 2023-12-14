package pl.ozodbek.stackoverflowapi.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

inline fun <reified T : ViewBinding> ViewGroup.viewBinding(
    crossinline inflate: (LayoutInflater, ViewGroup, Boolean) -> T,
): T {
    return inflate(LayoutInflater.from(context), this, false)
}