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
import java.awt.event.ActionEvent
import java.beans.IndexedPropertyDescriptor
import java.util.*
import javax.swing.JButton
import javax.swing.SwingUtilities

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
 * Implements a customized editor for components that do not have a default
 * assigned editor. By default, this creates a dialog box displaying the bean properties
 * of the property itself. The button's text is displayed using the text of the property.
 */
internal class DefaultPropertyComponent(
        /** The property's parent object.  */
        private val parent: PropertyModel?,
        /** Index (when the descriptor is an indexed property)  */
        private val row: Int) : JButton(parent.getElementAt(row)) {
    private fun updateButtonText() {
        val value = parent.getPropertyValue(row)
        if (value != null) {
            text = if (value.javaClass.isArray) {
                val txt = Arrays.deepToString(arrayOf<Any?>(value))
                txt.substring(1, txt.length - 1)
            } else {
                value.toString()
            }
            isEnabled = true
        }
    }

    /** Called whenever the button is pressed. Calls up a dialog box with the new bean's properties.  */
    private fun updateProperty() {
        val value = parent.getPropertyValue(row)
        if (value != null) {
            val window = SwingUtilities.windowForComponent(this)
            if (parent is BeanPropertyModel
                    && (parent as BeanPropertyModel?).getPropertyDescriptor(row) is IndexedPropertyDescriptor) {
                show(window, true,
                        (parent as BeanPropertyModel?).getBean(),
                        (parent as BeanPropertyModel?).getPropertyDescriptor(row) as IndexedPropertyDescriptor)
            } else {
                show(window, true, value)
            }
            updateButtonText()
        }
    }

    init {
        addActionListener { e: ActionEvent? -> updateProperty() }
        isEnabled = false
        updateButtonText()
    }
}