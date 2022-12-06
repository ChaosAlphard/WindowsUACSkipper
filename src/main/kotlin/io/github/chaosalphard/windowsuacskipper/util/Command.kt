package io.github.chaosalphard.windowsuacskipper.util

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.BufferedReader
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author wan
 * @version 1.0.0
 * Date 2022/12/05 23:33
 */
class CommandUtil {
    companion object {

        @JvmStatic
        private suspend fun runCommand(
            commandList: List<String>,
            workingDirectory: File = File("."),
            redirectErrorStream: Boolean = true,
            timeoutSecond: Int = 10,
            timeUnit: TimeUnit = TimeUnit.SECONDS
        ): BufferedReader {
            return coroutineScope {
                return@coroutineScope async {
                    return@async runCatching {
                        ProcessBuilder(commandList)
                            .directory(workingDirectory)
                            .redirectErrorStream(redirectErrorStream)
                            .start()
                            .also {
                                it.waitFor(timeoutSecond.toLong(), timeUnit)
                            }
                            .inputReader()
                    }.onFailure {
                        it.printStackTrace()
                    }.getOrThrow()
                }.await()
            }
        }

        suspend fun runAsStringResult(
            commandList: List<String>,
            workingDirectory: File = File("."),
            redirectErrorStream: Boolean = true,
            timeoutSecond: Int = 10,
            timeUnit: TimeUnit = TimeUnit.SECONDS
        ): String {
            return runCommand(commandList, workingDirectory, redirectErrorStream, timeoutSecond, timeUnit).readText()
        }

        suspend fun runAsStringListResult(
            commandList: List<String>,
            workingDirectory: File = File("."),
            redirectErrorStream: Boolean = true,
            timeoutSecond: Int = 10,
            timeUnit: TimeUnit = TimeUnit.SECONDS
        ): List<String> {
            return runCommand(commandList, workingDirectory, redirectErrorStream, timeoutSecond, timeUnit).readLines()
        }
    }
}
