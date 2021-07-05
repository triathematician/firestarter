package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.editor.EditorRegistrationTest
import com.googlecode.blaisemath.firestarter.internal.Reflection
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
import java.beans.IndexedPropertyDescriptor
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
*/ /**
 * Supports editing of individual items in an indexed property of a bean object.
 * Provides methods for adding a new item to the property, and for removing an
 * arbitrary set of items.
 *
 * @author elisha
 */
class BeanIndexedPropertyModel(
        /** The parent object  */
        private val parent: Any?,
        /** Descriptor  */
        private val ipd: IndexedPropertyDescriptor?) : PropertyModelSupport() {
    /** Size of the array property  */
    private var size: Int
    override fun getSize(): Int {
        return size
    }

    override fun getElementAt(row: Int): String? {
        return "" + row
    }

    override fun getPropertyType(row: Int): Class<*>? {
        return ipd.getIndexedPropertyType()
    }

    override fun getPropertyValue(pos: Int): Any? {
        return Reflection.tryInvokeIndexedRead(parent, ipd, pos)
    }

    fun getPropertyValueArray(): Array<Any?>? {
        return Reflection.tryInvokeRead(parent, ipd) as Array<Any?>
    }

    override fun isWritable(row: Int): Boolean {
        return ipd.getIndexedWriteMethod() != null
    }

    override fun setPropertyValue(pos: Int, value: Any?) {
        Reflection.tryInvokeIndexedWrite(parent, ipd, pos, value)
    }

    /**
     * Create and add a new value to the list.
     */
    fun addNewValue() {
        val newObject = Reflection.tryInvokeNew(ipd.getIndexedPropertyType())
                ?: return
        val values = getPropertyValueArray() ?: return
        val newArr = Arrays.copyOf(values, values.size + 1)
        newArr[values.size] = newObject
        if (Reflection.tryInvokeWrite(parent, ipd, newArr)) {
            size = newArr.size
            fireIntervalAdded(this, values.size, values.size)
        }
    }

    /**
     * Removes items at the specified indices from the indexed property
     * @param rows rows to remove
     */
    fun removeValues(rows: IntArray?) {
        if (rows.size == 0) {
            return
        }
        val values = Reflection.tryInvokeRead(parent, ipd) as Array<Any?>
                ?: return
        val listValues: MutableList<Any?> = ArrayList(Arrays.asList(*values))
        for (i in rows.indices.reversed()) {
            listValues.removeAt(rows.get(i))
        }
        val newArr: Array<Any?> = listValues.toArray<Any?>(java.lang.reflect.Array.newInstance(ipd.getIndexedPropertyType(), 0) as Array<Any?>)
        if (Reflection.tryInvokeWrite(parent, ipd, newArr)) {
            size = newArr.size
            fireContentsChanged(this, 0, newArr.size - 1)
        }
    }

    /**
     * Constructs for specified bean and property
     * @param parent the object owning the indexed property
     * @param ipd the indexed property for this model instance
     */
    init {
        val arr = getPropertyValueArray()
        size = arr?.size ?: 0
    }
}