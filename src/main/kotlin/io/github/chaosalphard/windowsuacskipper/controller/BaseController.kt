package io.github.chaosalphard.windowsuacskipper.controller

import javafx.stage.Stage

/**
 * @author wan
 * @version 1.0.0
 * Date 2022/12/06 19:16
 */
abstract class BaseController {
    protected lateinit var stage: Stage

    fun initStage(stage: Stage) {
        this.stage = stage
    }
}
