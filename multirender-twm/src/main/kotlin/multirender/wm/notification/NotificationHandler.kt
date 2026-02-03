package multirender.wm.notification

import multirender.wm.manager.Context

interface NotificationHandler {
    /**
     * Called when a notification is posted (can be off-thread)
     * @return true if the notification has been accepted
     */
    fun accepts(notification: Notification): Boolean = true

    /**
     * Called when a notification is processed (always on the render thread)
     */
    fun process(notification: Notification) {}

    fun render(notification: Notification, index: Int, context: Context) {}
}