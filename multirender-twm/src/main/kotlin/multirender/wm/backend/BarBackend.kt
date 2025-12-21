package multirender.wm.backend

import multirender.wm.util.Alignment

interface BarBackend {
    /**
     * Renders a bard from 0,0 that spans either the entire width or height of the screen (depending on the alignment)
     * @param width width of the bar relative to the alignment (height if the alignment is horizontal)
     */
    fun render(alignment: Alignment, width: Float)
}