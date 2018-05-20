package polybius.android.service

import org.koin.dsl.module.applicationContext
import polybius.android.fake.FakeDataGenerator

val serviceModule = applicationContext {
    bean { FakeDataGenerator(get()) }
}