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
*/   class Point2DEditorTest {
    @Test
    fun testGetJavaInitializationString() {
        println("getJavaInitializationString")
        val instance = Point2DEditor()
        Assert.assertEquals("null", instance.javaInitializationString)
        instance.value = Point(3, 4)
        Assert.assertEquals("new java.awt.geom.Point2D.Double(3.0,4.0)", instance.javaInitializationString)
    }

    @Test
    fun testSetAsText() {
        println("setAsText")
        val instance = Point2DEditor()
        instance.asText = "3,4"
        Assert.assertEquals(Point(3, 4), instance.value)
    }

    @Test
    fun testGetValue() {
        println("getValue")
        val instance = Point2DEditor()
        instance.value = Point(3, 4)
        Assert.assertEquals(3.0, instance.getValue(0), 1e-8)
        Assert.assertEquals(4.0, instance.getValue(1), 1e-8)
    }

    @Test
    fun testSetNewValue() {
        println("setNewValue")
        val instance = Point2DEditor()
        instance.setNewValueList(Arrays.asList(3.0, 4.0))
        Assert.assertEquals(3.0, instance.getNewValue(0), 1e-8)
        Assert.assertEquals(4.0, instance.getNewValue(1), 1e-8)
    }
}