package polybius.android.kextensions

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/*
 * Utility functions
 */
inline fun <reified T: ViewModel> getViewModel(ctx: FragmentActivity) =
        ViewModelProviders.of(ctx).get(T::class.java)
inline fun <reified T: ViewModel> getViewModel(ctx: Fragment) =
        ViewModelProviders.of(ctx).get(T::class.java)

fun tag(o: Any) = o::class.simpleName