package com.googlecode.blaisemath.firestarter.editor

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Assert
import org.junit.Test
import java.awt.Point
import java.util.*

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
*/   class PointEditorTest {
    @Test
    fun testGetJavaInitializationString() {
        println("getJavaInitializationString")
        val instance = PointEditor()
        Assert.assertEquals("null", instance.javaInitializationString)
        instance.value = Point(3, 4)
        Assert.assertEquals("new java.awt.Point(3,4)", instance.javaInitializationString)
    }

    @Test
    fun testSetAsText() {
        println("setAsText")
        val instance = PointEditor()
        instance.asText = "3,4"
        Assert.assertEquals(Point(3, 4), instance.value)
    }

    @Test
    fun testGetValue() {
        println("getValue")
        val instance = PointEditor()
        instance.value = Point(3, 4)
        Assert.assertEquals(3, instance.getValue(0) as Int.toLong())
        Assert.assertEquals(4, instance.getValue(1) as Int.toLong())
    }

    @Test
    fun testSetNewValueList() {
        println("setNewValueList")
        val instance = PointEditor()
        instance.setNewValueList(Arrays.asList(3, 4))
        Assert.assertEquals(3, instance.getNewValue(0) as Int.toLong())
        Assert.assertEquals(4, instance.getNewValue(1) as Int.toLong())
    }
}