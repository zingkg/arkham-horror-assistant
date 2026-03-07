package com.zingkg.arkhamhorrorassistant

import java.io.InputStreamReader

object Common {
  fun readResourceFile(file: String): List<String> {
    val reader = InputStreamReader(javaClass.classLoader?.getResourceAsStream(file))
    val lines = reader.readLines()
    reader.close()
    return lines
  }
}