package multirender.wm.notification

import multirender.wm.manager.Context
import java.util.concurrent.CopyOnWriteArrayList

object NotificationManager {
    val queue = CopyOnWriteArrayList<Notification>()
    val active = HashMap<Notification.Group, MutableList<Notification>>()

    var handler: NotificationHandler = object : NotificationHandler {}

    fun post(notification: Notification) {
        if (!handler.accepts(notification)) return
        queue.add(notification)
    }

    fun collect() {
        for (notification in queue) {
            handler.process(notification)
            active.computeIfAbsent(notification.group) { arrayListOf() }
                .add(notification)
        }
    }

    fun render(ctx: Context) {
        val entries = active.iterator()
        while (entries.hasNext()) {
            val entry = entries.next()
            val notifs = entry.value.iterator()
            var index = 0

            while (notifs.hasNext()) {
                val notification = notifs.next()

                if (notification.hasExpired()) {
                    notifs.remove()
                    continue
                }

                handler.render(notification, index, ctx)
                index++
            }

            if (entry.value.isEmpty()) {
                entries.remove()
            }
        }
    }
}