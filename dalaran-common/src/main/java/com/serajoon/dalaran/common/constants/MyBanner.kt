package com.serajoon.dalaran.constants

import org.springframework.boot.Banner
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.boot.ansi.AnsiStyle
import org.springframework.core.env.Environment
import java.io.PrintStream


/**
 * 打印SpringBoot启动标语
 * @author hm 2019/1/15 10:56
 */
class MyBanner : Banner {
    private val author = "HANMENG"
    private val version = "(1.0)"
    var BANNER: String = """
            ____        __
           / __ \____ _/ /___ __________ _____
          / / / / __ `/ / __ `/ ___/ __ `/ __ \
         / /_/ / /_/ / / /_/ / /  / /_/ / / / /
        /_____/\__,_/_/\__,_/_/   \__,_/_/ /_/

        """.trimIndent()

    override fun printBanner(environment: Environment?, sourceClass: Class<*>?, out: PrintStream?) {
        out!!.println(BANNER)
        val version = version
        out.println(AnsiOutput.toString(AnsiColor.GREEN, AnsiStyle.BOLD, " Powered by $author :: ", AnsiColor.BLUE, AnsiStyle.BOLD, version))
        out.println()
    }

}