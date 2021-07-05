package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.BeforeClassimport

java.awt.*
/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2019 Elisha Peterson
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */internal class CustomBean {
    private var color = Color.BLACK
    private var color2 = Color.YELLOW
    private var boo: Boolean? = true
    private val boo2 = true
    private var string: String? = "string"
    private var font: Font? = Font("", Font.PLAIN, 10)
    private var nb: NumberBean? = NumberBean()
    fun getColor(): Color? {
        return color
    }

    fun setColor(color: Color?) {
        this.color = color
    }

    fun getColor2(): Color? {
        return color2
    }

    fun setColor2(color: Color?) {
        color2 = color
    }

    fun getBoo(): Boolean? {
        return boo
    }

    fun setBoo(boo: Boolean?) {
        this.boo = boo
    }

    fun getBoo2(): Boolean {
        return boo
    }

    fun setBoo2(boo: Boolean) {
        this.boo = boo
    }

    fun getFont(): Font? {
        return font
    }

    fun setFont(font: Font?) {
        this.font = font
    }

    fun getString(): String? {
        return string
    }

    fun setString(string: String?) {
        this.string = string
    }

    fun getBean(): NumberBean? {
        return nb
    }

    fun setBean(nb: NumberBean?) {
        this.nb = nb
    }
}