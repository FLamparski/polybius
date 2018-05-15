package polybius.android.viewmodel

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

/**
 * Created by filip on 14/05/18.
 */
val viewModelModule = applicationContext {
    viewModel { PlaybackControlViewModel() }
}