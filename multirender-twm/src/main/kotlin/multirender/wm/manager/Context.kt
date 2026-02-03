package multirender.wm.manager

import multirender.wm.config.Config

data class Context(val backend: Backend, val config: Config, val mouseX: Float = 0f, val mouseY: Float = 0f)
