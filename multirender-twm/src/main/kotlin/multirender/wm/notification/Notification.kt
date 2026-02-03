package multirender.wm.notification

data class Notification(
    val title: String,
    val description: String? = null,
    val time: Long = System.nanoTime(),
    val type: Type = Type.INFO,
    val group: Group = Group.NONE
) {
    enum class Type {
        INFO, WARN, ERROR, SUCCESS, FAIL, ENABLE, DISABLE
    }

    fun hasExpired(): Boolean =
        System.nanoTime() - time > 1000 // todo config

    @JvmInline
    value class Group(val string: String) {
        companion object {
            val NONE = Group("None")
        }
    }
}