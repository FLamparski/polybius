package polybius.android.kextensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import retrofit2.Retrofit

/*
 * File contains functions for working with Java-idiomatic methods
 */

inline fun <reified T> Retrofit.create(): T = this.create(T::class.java)

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, onChanged: (T?) -> Unit) {
    this.observe(lifecycleOwner, Observer { onChanged(it) })
}