package com.googlecode.blaisemath.firestarter.property

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
import java.beans.PropertyChangeListener
import javax.swing.ListModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

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
 * Provides several editors for an object, called the "bean". These editors are
 * referred to by their "row". Methods are also provided for modifying the properties,
 * and listeners are available to send notifications when the properties change.
 * As a [ListModel], this class provides names for properties being displayed,
 * and generates [ListDataEvent]s when that list changes.
 *
 * @author Elisha
 */
interface PropertyModel : ListModel<String?> {
    /**
     * Returns the Java type info for the property at the given row.
     * @param row the row
     * @return property type
     */
    open fun getPropertyType(row: Int): Class<*>?

    /**
     * Whether write is enabled for the given row
     * @param row the row
     * @return true if row is writable
     */
    open fun isWritable(row: Int): Boolean

    /**
     * Returns value at given position.
     * @param row the row
     * @return value at the row, null if there is none
     */
    open fun getPropertyValue(row: Int): Any?

    /**
     * Sets property in given row.
     * @param row property row
     * @param value value for the row
     */
    open fun setPropertyValue(row: Int, value: Any?)
    open fun addPropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?)
    open fun addPropertyChangeListener(listener: PropertyChangeListener?)
    open fun removePropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?)
    open fun removePropertyChangeListener(listener: PropertyChangeListener?)

    /** Blank instance of a property model  */
    class Empty : PropertyModel {
        override fun getSize(): Int {
            return 0
        }

        override fun getElementAt(index: Int): String? {
            throw UnsupportedOperationException()
        }

        override fun getPropertyType(row: Int): Class<*>? {
            throw UnsupportedOperationException()
        }

        override fun isWritable(row: Int): Boolean {
            throw UnsupportedOperationException()
        }

        override fun getPropertyValue(row: Int): Any? {
            throw UnsupportedOperationException()
        }

        override fun setPropertyValue(row: Int, value: Any?) {
            throw UnsupportedOperationException()
        }

        override fun addPropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
            // ignore
        }

        override fun addPropertyChangeListener(listener: PropertyChangeListener?) {
            // ignore
        }

        override fun removePropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
            // ignore
        }

        override fun removePropertyChangeListener(listener: PropertyChangeListener?) {
            // ignore
        }

        override fun addListDataListener(l: ListDataListener?) {
            // ignore
        }

        override fun removeListDataListener(l: ListDataListener?) {
            // ignore
        }
    }
}