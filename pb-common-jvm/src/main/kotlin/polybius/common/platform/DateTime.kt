package polybius.common.platform

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlinx.serialization.Serializer
import polybius.common.platform.impl.DateTimeDeserializer
import polybius.common.platform.impl.DateTimeSerializer
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonSerialize(using = DateTimeSerializer::class)
@JsonDeserialize(using = DateTimeDeserializer::class)
actual class DateTime {
    private val date: ZonedDateTime

    actual override fun toString(): String =
            date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    actual fun toUnix() = date.toEpochSecond()

    actual constructor(isoString: String) {
        date = ZonedDateTime.parse(isoString)
    }

    private constructor(d: ZonedDateTime) {
        date = d
    }

    actual companion object {
        actual fun now() = DateTime(ZonedDateTime.now())
    }
}