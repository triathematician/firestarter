package com.googlecode.blaisemath.firestarter.internal

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.editor.EditorRegistrationTest
import com.googlecode.blaisemath.firestarter.property.BeanTreeNode
import com.googlecode.blaisemath.firestarter.property.CustomBean
import com.googlecode.blaisemath.firestarter.property.HelloWorldTestFrame
import com.googlecode.blaisemath.firestarter.property.IndexedBean
import com.googlecode.blaisemath.firestarter.property.IndexedBean.Indexed2
import com.googlecode.blaisemath.firestarter.property.IndexedBean.TestEnum
import com.googlecode.blaisemath.firestarter.property.NumberBean
import com.googlecode.blaisemath.firestarter.property.PointBean
import com.googlecode.blaisemath.firestarter.property.PropertySheetDialogTestFrame
import com.googlecode.blaisemath.firestarter.property.ShapeBean
import com.googlecode.blaisemath.firestarter.property.TestPropertyModel
import com.googlecode.blaisemath.firestarter.property.Testing
import com.googlecode.blaisemath.ui.PropertyActionPanel
import junit.framework.TestCase
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Before
import org.junit.BeforeClass
import java.util.logging.Level
import java.util.logging.Logger

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
*/ /**
 * Number parse utilities
 *
 * @author elisha
 */
object Numbers {
    private val LOG = Logger.getLogger(Numbers::class.java.name)

    /**
     * Attempt to decode text in an array as integers.
     * @param arr array of text
     * @return decoded integers
     */
    fun decodeAsIntegers(vararg arr: String?): IntArray? {
        val res = IntArray(arr.size)
        for (i in 0 until arr.size) {
            try {
                res[i] = Integer.decode(arr[i])
            } catch (ex: NumberFormatException) {
                LOG.log(Level.WARNING, "Not an integer", ex)
                res[i] = 0
            }
        }
        return res
    }

    /**
     * Attempt to decode text in an array as doubles.
     * @param arr array of text
     * @return decoded doubles
     */
    fun decodeAsDoubles(vararg arr: String?): DoubleArray? {
        val res = DoubleArray(arr.size)
        for (i in 0 until arr.size) {
            try {
                res[i] = arr[i].toDouble()
            } catch (ex: NumberFormatException) {
                LOG.log(Level.WARNING, "Not a double", ex)
                res[i] = 0
            }
        }
        return res
    }
}