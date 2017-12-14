package polybius.common.platform.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import polybius.common.platform.DateTime

class DateTimeSerializer : StdSerializer<DateTime>(DateTime::class.java) {
    override fun serialize(value: DateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value != null) {
            gen!!.writeString(value.toString())
        } else {
            gen!!.writeNull()
        }
    }
}