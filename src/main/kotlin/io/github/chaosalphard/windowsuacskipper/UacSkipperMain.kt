package io.github.chaosalphard.windowsuacskipper

import io.github.chaosalphard.windowsuacskipper.controller.MainController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.kordamp.bootstrapfx.BootstrapFX

/**
 * @author wan
 * @version 1.0.0
 * Date 2022/12/06 03:10
 */

fun main() {
    Application.launch(UacSkipperMain::class.java)
}

class UacSkipperMain : Application() {
    override fun start(stage: Stage) {
        val loader = FXMLLoader(UacSkipperMain::class.java.getResource("fxml/UacSkipperMain.fxml"))
        val root = loader.load<Parent>()
        val mainController = loader.getController<MainController>()
        mainController.initStage(stage)

        val scene = Scene(root)
        scene.stylesheets.add(BootstrapFX.bootstrapFXStylesheet())

        stage.title = "Windows UAC Skipper"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}
