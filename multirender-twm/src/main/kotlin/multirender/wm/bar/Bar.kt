package multirender.wm.bar

import multirender.wm.manager.Context

interface Bar {
    /**
     * Renders a bard from 0,0 that spans either the entire width or height of the screen (depending on the alignment)
     */
    fun render(context: Context)
}