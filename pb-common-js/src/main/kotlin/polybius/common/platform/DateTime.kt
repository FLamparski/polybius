package polybius.common.platform

external fun moment(str: String): dynamic = definedExternally

actual class DateTime {
    private val date: dynamic

    actual override fun toString(): String = date.format()

    actual fun toUnix(): Long = date.unix()

    actual constructor(isoString: String) {
        date = moment(isoString)
    }
}