package polybius.common.platform

expect class DateTime(isoString: String) {
    override fun toString(): String
    fun toUnix(): Long

    companion object {
        fun now(): DateTime
    }
}