package com.googlecode.blaisemath.firestarter.internal

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.property.IndexedBean
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Assert
import org.junit.Test
import java.awt.Point

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
*/   class ReflectionTest {
    @Test
    fun testIndexedPropertyDescriptor() {
        println("indexedPropertyDescriptor")
        val result = Reflection.indexedPropertyDescriptor(IndexedBean::class.java, "strings")
        Assert.assertEquals("strings", result.displayName)
    }

    @Test
    fun testTryInvokeNew() {
        println("tryInvokeNew")
        val result: Any? = Reflection.tryInvokeNew(Point::class.java)
        Assert.assertEquals(Point(), result)
        Assert.assertEquals(0 as Int, Reflection.tryInvokeNew(Int::class.java))
        Assert.assertNull(Reflection.tryInvokeNew(Int::class.javaPrimitiveType))
    }

    @Test
    fun testTryInvokeRead() {
        println("tryInvokeRead")
        val info = Reflection.beanInfo(Point::class.java)
        Assert.assertEquals("x", info.propertyDescriptors[2].displayName)
        Assert.assertEquals(2.0, Reflection.tryInvokeRead(Point(2, 3), info.propertyDescriptors[2]))
        Assert.assertNull(Reflection.tryInvokeRead("not a point", info.propertyDescriptors[1]))
    }

    @Test
    fun testTryInvokeWrite() {
        println("tryInvokeWrite")
        val info = Reflection.beanInfo(Point::class.java)
        Assert.assertEquals("x", info.propertyDescriptors[2].displayName)
        Assert.assertFalse(Reflection.tryInvokeWrite(Point(2, 3), info.propertyDescriptors[2], 2))
        val p = Point(2, 3)
        Reflection.tryInvokeWrite(p, info.propertyDescriptors[1], Point(1, 1))
        Assert.assertEquals(Point(1, 1), p)
    }

    @Test
    fun testTryInvokeIndexedRead() {
        println("tryInvokeIndexedRead")
        val result = Reflection.indexedPropertyDescriptor(IndexedBean::class.java, "strings")
        val bean = IndexedBean()
        Assert.assertEquals("hello", Reflection.tryInvokeIndexedRead(bean, result, 0))
    }

    @Test
    fun testTryInvokeIndexedWrite() {
        println("tryInvokeIndexedWrite")
    }
}