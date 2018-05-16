package polybius.android

import android.app.Application
import org.koin.android.ext.android.startKoin
import polybius.android.api.httpModule
import polybius.android.repo.repositoryModule
import polybius.android.viewmodel.viewModelModule

/**
 * Created by filip on 14/05/18.
 */
class PolybiusApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(httpModule, repositoryModule, viewModelModule))
    }
}