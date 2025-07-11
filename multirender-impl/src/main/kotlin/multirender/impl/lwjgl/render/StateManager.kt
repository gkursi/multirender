package xyz.qweru.multirender.impl.lwjgl.render

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13
import xyz.qweru.multirender.api.Provider
import java.util.HashSet

object StateManager {
    private val all: HashSet<State> = HashSet()

    private var msaa: Boolean = false;
    private var lineSmooth: Boolean = false;
    private var polygonSmooth: Boolean = false;
    private var wireframe: Boolean = false;

    private var blend: Boolean = false;
    private var blSourceFactor: Int = GL_SRC_ALPHA
    private var blDestFactor: Int = GL_ONE_MINUS_SRC_ALPHA

    fun applyState(vararg states: State) {
        if (!Provider.API.isOnRenderThread()) {
            Provider.API.recordRenderCall { applyState(*states) }
            return
        }

        println("Applying state: ${states.joinToString()}")
        val toDisable: HashSet<State> = HashSet(all)
        toDisable.removeAll(states.toSet());

        states.forEach(this::handleStateEnable)
        toDisable.forEach(this::handleStateDisable)
    }

    fun applyAdditionalState(state: State) {
        handleStateEnable(state);
    }

    private fun handleStateEnable(state: State) {
        when (state) {
            State.MSAA -> {
                if (!msaa) {
                    glEnable(GL13.GL_MULTISAMPLE)
                    msaa = true
                }
            }

            State.LINE_SMOOTH -> {
                if (!lineSmooth) {
                    glEnable(GL_LINE_SMOOTH)
                    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)
                    lineSmooth = true
                }
            }

            State.POLYGON_SMOOTH -> {
                if (!polygonSmooth) {
                    glEnable(GL_POLYGON_SMOOTH)
                    glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)
                    polygonSmooth = true
                }
            }

            State.WIREFRAME -> {
                if (!wireframe) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                    wireframe = true
                }
            }

            State.BLEND -> {
                if (!blend) {
                    glEnable(GL_BLEND)
                    glBlendFunc(blSourceFactor, blDestFactor)
                    blend = true
                }
            }
        }
    }

    private fun handleStateDisable(state: State) {
        when (state) {
            State.MSAA -> {
                msaa = false
                glDisable(GL13.GL_MULTISAMPLE)
            }
            State.LINE_SMOOTH -> {
                lineSmooth = false
                glDisable(GL_LINE_SMOOTH)
            }
            State.POLYGON_SMOOTH -> {
                polygonSmooth = false
                glDisable(GL_POLYGON_SMOOTH)
            }
            State.WIREFRAME -> {
                wireframe = false
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }
            State.BLEND -> {
                blend = false
                glDisable(GL_BLEND)
            }
        }
    }

    fun blendFunc(sourceFactor: Int, destFactor: Int) {
        blSourceFactor = sourceFactor
        blDestFactor = destFactor
        glBlendFunc(sourceFactor, destFactor);
    }

    init {
        State.entries.forEach(all::add)
    }

    enum class State {
        MSAA,
        LINE_SMOOTH,
        POLYGON_SMOOTH,
        WIREFRAME,
        BLEND
    }
}