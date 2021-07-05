package com.googlecode.blaisemath.firestarter.editor

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Assert
import org.junit.Test
import java.awt.Rectangle
import java.awt.geom.Rectangle2D

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
*/   class RectangularShapeEditorTest {
    @Test
    fun testGetJavaInitializationString() {
        println("getJavaInitializationString")
        val instance = RectangularShapeEditor()
        Assert.assertEquals("???", instance.javaInitializationString)
        instance.value = Rectangle(3, 4, 5, 6)
        Assert.assertEquals("???", instance.javaInitializationString)
    }

    @Test
    fun testSetAsText() {
        println("setAsText")
        val instance = RectangularShapeEditor()
        instance.asText = "3,4,5,6"
        Assert.assertEquals(Rectangle2D.Double(3, 4, 5, 6), instance.value)
    }

    @Test
    fun testGetValue() {
        println("getValue")
        val instance = RectangularShapeEditor()
        instance.value = Rectangle2D.Double(3, 4, 5, 6)
        Assert.assertEquals(3.0, instance.getValue(0), 1e-8)
        Assert.assertEquals(4.0, instance.getValue(1), 1e-8)
        Assert.assertEquals(5.0, instance.getValue(2), 1e-8)
        Assert.assertEquals(6.0, instance.getValue(3), 1e-8)
    }

    @Test
    fun testSetNewValue() {
        println("setNewValue")
        val instance = RectangularShapeEditor()
        instance.setNewValue(Rectangle2D.Double(3, 4, 5, 6))
        Assert.assertEquals(3.0, instance.getNewValue(0), 1e-8)
        Assert.assertEquals(4.0, instance.getNewValue(1), 1e-8)
        Assert.assertEquals(5.0, instance.getNewValue(2), 1e-8)
        Assert.assertEquals(6.0, instance.getNewValue(3), 1e-8)
    }
}