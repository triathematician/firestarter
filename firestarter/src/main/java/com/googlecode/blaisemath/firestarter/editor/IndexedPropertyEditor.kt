package com.googlecode.blaisemath.firestarter.editor

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
import java.beans.PropertyEditor

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
 * Provides string-based editing of indexed properties, as long as the
 * underlying data type supports the [.getAsText] and [.setAsText]
 * methods. Commas are used for splitting, so the strings must not use commas.
 *
 * @author Elisha Peterson
 */
class IndexedPropertyEditor : MPropertyEditorSupport() {
    /** The editor that handles individual components of the array.  */
    private var baseEditor: PropertyEditor? = null

    /** @return component type of the underlying array.
     */
    fun getComponentType(): Class<*>? {
        val array = value as Array<Any?>
        return array.javaClass.componentType
    }

    override fun initEditorValue() {
        val array = value as Array<Any?>
        baseEditor = EditorRegistration.getRegisteredEditor(getComponentType())
        if (array.size > 0) {
            baseEditor.setValue(array[0])
        }
    }

    override fun getAsText(): String? {
        val array = value as Array<Any?>
        val result = StringBuilder()
        for (i in array.indices) {
            baseEditor.setValue(array[i])
            result.append(if (i == 0) "" else ", ").append(baseEditor.getAsText())
        }
        return result.toString()
    }

    override fun setAsText(text: String?) {
        setAsText(*text.split(",".toRegex()).toTypedArray())
    }

    private fun setAsText(vararg splits: String?) {
        val result = java.lang.reflect.Array.newInstance(getComponentType(), splits.size) as Array<Any?>
        for (i in result.indices) {
            baseEditor.setAsText(splits[i])
            result[i] = baseEditor.getValue()
        }
        value = result
    }

    init {
        value = arrayOfNulls<Any?>(0)
    }
}