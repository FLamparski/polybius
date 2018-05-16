package polybius.android.repo

import org.koin.dsl.module.applicationContext

val repositoryModule = applicationContext {
    bean { InMemoryStateRepository() }
}