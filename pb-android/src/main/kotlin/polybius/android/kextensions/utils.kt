package polybius.android.kextensions

import android.content.Context
import android.content.Intent

/*
 * Utility functions
 */
inline fun <reified T: Any> createIntent(ctx: Context) = Intent(ctx, T::class.java)

fun tag(o: Any) = o::class.simpleName

fun capitalizeWords(words: Collection<String>) = words.map { it.capitalize() }