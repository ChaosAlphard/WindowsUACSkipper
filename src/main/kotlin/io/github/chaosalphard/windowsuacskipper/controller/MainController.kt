package io.github.chaosalphard.windowsuacskipper.controller

import io.github.chaosalphard.windowsuacskipper.util.CommandUtil
import io.github.chaosalphard.windowsuacskipper.util.TimeUtil
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

/**
 * @author wan
 * @version 1.0.0
 * Date 2022/12/06 19:09
 */
@Suppress("unused")
class MainController : BaseController() {
    companion object {
        @JvmStatic
        private val BAT_OUT_DIR = "startup"
    }
    @FXML
    private lateinit var rootBox: VBox
    @FXML
    private lateinit var filePath: Label
    @FXML
    private lateinit var btnChooseFile: Button
    @FXML
    private lateinit var btnExecute: Button
    @FXML
    private lateinit var execResult: TextArea

    private var canExecute: Boolean = false

    private var file: File? = null

    @FXML
    private fun onChooseFileButtonClick() {
        val chooser = FileChooser()
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("可执行文件 (*.exe)", "*.exe"))
        file = chooser.showOpenDialog(super.stage)

        checkIfCanExecute(file)

        file?.let {
            filePath.text = it.absolutePath
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @FXML
    private fun onExecuteButtonClick() {
        val nonNullFile = this.file?:let {
            println("file is null!")
            return
        }
        val schTaskId = UUID.randomUUID().toString().filterNot { '-' == it }

        /* 参考文档: https://learn.microsoft.com/zh-cn/windows-server/administration/windows-commands/schtasks-create?source=recommendations */
        val argList = listOf(
            "schtasks", "/create",
            "/tn", "\"UACSkip\\${schTaskId}\"",
            "/tr", "\"${nonNullFile.absolutePath}\"",
            "/rl", "highest",
            "/sc", "once",
            // "/sd", "2000/01/01", // 不指定时默认为计算机上的当前日期
            "/st", "00:00",
            "/f"
        )
        GlobalScope.launch (Dispatchers.Default) {
            val result: List<String> = CommandUtil.runAsStringListResult(argList)
            val resStr = result.joinToString("\n")
            println(resStr)
            execResult.text = "[${TimeUtil.nowInstant()}]\n"
            execResult.appendText(resStr)

            val startUp = createBatStartUp(nonNullFile.nameWithoutExtension, schTaskId)
            execResult.appendText("\n已在${BAT_OUT_DIR}目录下创建快捷启动文件\"${startUp.name}\"")
        }
        val clg = argList.joinToString(" ","正在执行指令, 若长时间未响应可将指令复制到cmd中运行: ")
        println(clg)
        execResult.text = "[${TimeUtil.nowInstant()}]\n"
        execResult.appendText(clg)
    }

    private fun checkIfCanExecute(file: File?) {
        file?.let {
            if (!it.isFile) {
                filePath.text = "${it.name}不是一个常规文件"
                setCanExecute(false)
                return
            }
        }
        setCanExecute(file != null)
    }

    private fun setCanExecute(boolean: Boolean) {
        canExecute = boolean
        btnExecute.isDisable = !boolean
    }

    private fun createBatStartUp(filename: String, schTaskId: String): File {
        val file = File("./${BAT_OUT_DIR}/${filename}.skipuac.bat")

        runCatching {
            file.parentFile.mkdirs()
            file.delete()
            file.createNewFile()

            val writer = file.bufferedWriter(Charsets.UTF_8, 2048).use {
                it.write("chcp 65001")
                it.appendLine()
                it.appendLine("schtasks /run /tn \"UACSkip\\${schTaskId}\"")
            }
            println(file.absolutePath)
        }.onFailure {
            it.printStackTrace()
        }

        return file
    }
}
