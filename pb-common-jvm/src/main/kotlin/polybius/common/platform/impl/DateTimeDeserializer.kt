package polybius.common.platform.impl

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import polybius.common.platform.DateTime

class DateTimeDeserializer : StdDeserializer<DateTime>(DateTime::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): DateTime {
        val str = p!!.codec.readValue(p, String::class.java).toString()
        return DateTime(str)
    }
}