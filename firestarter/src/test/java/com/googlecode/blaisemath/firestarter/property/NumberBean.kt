package com.googlecode.blaisemath.firestarter.property

import com.google.common.base.MoreObjects
import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics

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
*/   class NumberBean {
    private var byte1: Byte? = -5
    private var long1: Long? = 199823844L
    private var double1 = 2.0
    private var int1 = 4
    private var float1 = 1f
    private var short1: Short = 3
    fun getNByte(): Byte? {
        return byte1
    }

    fun setNByte(NByte: Byte?) {
        byte1 = NByte
    }

    fun getNLong(): Long? {
        return long1
    }

    fun setNLong(NLong: Long?) {
        long1 = NLong
    }

    fun getNDouble(): Double {
        return double1
    }

    fun setNDouble(n: Double) {
        double1 = n
    }

    fun getNFloat(): Float {
        return float1
    }

    fun setNFloat(n: Float) {
        float1 = n
    }

    fun getNShort(): Short {
        return short1
    }

    fun setNShort(n: Short) {
        short1 = n
    }

    fun getNInteger(): Int {
        return int1
    }

    fun setNInteger(n: Int) {
        int1 = n
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("byte1", byte1)
                .add("long1", long1)
                .add("double1", double1)
                .add("int1", int1)
                .add("float1", float1)
                .add("short1", short1.toInt())
                .toString()
    }
}