package com.googlecode.blaisemath.firestarter.editor

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Assert
import org.junit.Test

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
*/   class StringEditorTest {
    @Test
    fun testSupportsCustomEditor() {
        println("supportsCustomEditor")
        val instance = StringEditor()
        Assert.assertTrue(instance.supportsCustomEditor())
    }

    @Test
    fun testGetJavaInitializationString() {
        println("getJavaInitializationString")
        val instance = StringEditor()
        instance.value = "test"
        Assert.assertEquals("\"test\"", instance.javaInitializationString)
        instance.value = "\"test\"\n"
        Assert.assertEquals("\"\\\"test\\\"\\n\"", instance.javaInitializationString)
    }

    @Test
    fun testSetAsText() {
        println("setAsText")
        val instance = StringEditor()
        instance.asText = "test"
        Assert.assertEquals("test", instance.value)
    }
}