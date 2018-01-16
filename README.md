```

                    _|            _|        _|
_|_|_|      _|_|    _|  _|    _|  _|_|_|        _|    _|    _|_|_|
_|    _|  _|    _|  _|  _|    _|  _|    _|  _|  _|    _|  _|_|
_|    _|  _|    _|  _|  _|    _|  _|    _|  _|  _|    _|      _|_|
_|_|_|      _|_|    _|    _|_|_|  _|_|_|    _|    _|_|_|  _|_|_|
_|                            _|
_|                        _|_|
```

Overengineered TV remote/multi-service play queue manager in
Kotlin with multiplatform

# Using Android:
The Android module (pb-android) does not sync in any IDE except Android Studio 3.0
for now. That's why the Gradle project structure is such a mess. For now, you will
need to use Android Studio to work on the Android application, and IntelliJ to
work on the rest. The Android project acts as a root project that includes the
common and common-jvm projects. The Android project cannot be included by the root
project by default because it breaks IntelliJ sync. The Android project can be
included if invoking gradlew from the shell, but it needs ANDROID_HOME environment
variable. The Android project is, therefore, what's known to most people as
a scumbag. Thanks, Google.