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
import com.googlecode.blaisemath.firestarter.swing.FilteredListModel
import com.googlecode.blaisemath.ui.PropertyActionPanel
import junit.framework.TestCase
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Before
import org.junit.BeforeClass
import java.beans.IndexedPropertyDescriptor
import java.beans.PropertyDescriptor
import java.util.*
import java.util.function.Predicate
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
 * Uses bean information about an object, gathered by introspection, to provide
 * editable attributes of that object.
 * A filter may be supplied to limit the properties made available by the model.
 *
 * @author Elisha Peterson
 */
class BeanPropertyModel(
        /** Object of this class.  */
        private val bean: Any?) : PropertyModelSupport() {
    /** Filtered items  */
    private val filteredProperties: FilteredListModel<PropertyDescriptor?>?

    /**
     * Get the bean object represented by this class.
     * @return the underlying object.
     */
    fun getBean(): Any? {
        return bean
    }

    fun getFilter(): Predicate<PropertyDescriptor?>? {
        return filteredProperties.getFilter()
    }

    fun setFilter(filter: Predicate<PropertyDescriptor?>?) {
        filteredProperties.setFilter(filter)
    }

    override fun getSize(): Int {
        return filteredProperties.getSize()
    }

    override fun getElementAt(row: Int): String? {
        return getPropertyDescriptor(row).getDisplayName()
    }

    fun getPropertyDescriptor(row: Int): PropertyDescriptor? {
        return filteredProperties.getElementAt(row)
    }

    override fun getPropertyType(row: Int): Class<*>? {
        val pd = getPropertyDescriptor(row)
        return if (pd is IndexedPropertyDescriptor) Array<Any>::class.java else pd.getPropertyType()
    }

    override fun isWritable(row: Int): Boolean {
        val pd = getPropertyDescriptor(row)
        return pd.getWriteMethod() != null
    }

    override fun getPropertyValue(row: Int): Any? {
        val pd = getPropertyDescriptor(row)
        return Reflection.tryInvokeRead(bean, pd)
    }

    override fun setPropertyValue(row: Int, value: Any?) {
        val pd = getPropertyDescriptor(row)
        val cur = Reflection.tryInvokeRead(bean, pd)
        if (Reflection.tryInvokeWrite(bean, pd, value)) {
            pcs.firePropertyChange(getElementAt(row), cur, value)
        }
    }

    /**
     * Constructs for specified bean.
     * @param bean the underlying object.
     */
    init {
        filteredProperties = FilteredListModel()
        filteredProperties.setFilter(BeanPropertyFilter.STANDARD)
        filteredProperties.addListDataListener(object : ListDataListener {
            override fun intervalAdded(e: ListDataEvent?) {
                fireIntervalAdded(this@BeanPropertyModel, e.getIndex0(), e.getIndex1())
            }

            override fun intervalRemoved(e: ListDataEvent?) {
                fireIntervalRemoved(this@BeanPropertyModel, e.getIndex0(), e.getIndex1())
            }

            override fun contentsChanged(e: ListDataEvent?) {
                fireContentsChanged(this@BeanPropertyModel, e.getIndex0(), e.getIndex1())
            }
        })
        val info = Reflection.beanInfo(bean.javaClass)
        filteredProperties.setUnfilteredItems(Arrays.asList(*info.propertyDescriptors))
    }
}