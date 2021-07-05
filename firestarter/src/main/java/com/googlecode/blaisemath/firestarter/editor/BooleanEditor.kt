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
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.*

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
 * Edits boolean values using a checkbox.
 *
 * @author Elisha Peterson
 */
class BooleanEditor : MPanelEditorSupport() {
    private var checkbox: JCheckBox? = null
    private var label: JLabel? = null
    public override fun initCustomizer() {
        panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        checkbox = JCheckBox()
        checkbox.setBackground(panel.background)
        checkbox.setAlignmentX(Component.CENTER_ALIGNMENT)
        checkbox.setAlignmentY(Component.CENTER_ALIGNMENT)
        panel.add(checkbox)
        panel.add(Box.createRigidArea(Dimension(5, 0)))
        label = JLabel("")
        label.setAlignmentX(Component.CENTER_ALIGNMENT)
        label.setAlignmentY(Component.CENTER_ALIGNMENT)
        panel.add(label)

        // initialize listening
        checkbox.addItemListener(ItemListener { evt: ItemEvent? ->
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                setNewValue(java.lang.Boolean.TRUE)
                label.setText(getValidName(true))
            } else {
                setNewValue(java.lang.Boolean.FALSE)
                label.setText(getValidName(false))
            }
        })
    }

    override fun initEditorValue() {
        if (panel != null) {
            checkbox.setSelected(java.lang.Boolean.TRUE == getNewValue())
            label.setText(asText)
            panel.repaint()
        }
    }

    override fun getJavaInitializationString(): String? {
        val value = value
        return value?.toString() ?: "null"
    }

    override fun getAsText(): String? {
        val value = value
        return if (value is Boolean) getValidName(value as Boolean) else null
    }

    override fun setAsText(text: String?) {
        value = if (text == null) {
            null
        } else if (isValidName(true, text)) {
            java.lang.Boolean.TRUE
        } else if (isValidName(false, text)) {
            java.lang.Boolean.FALSE
        } else {
            throw IllegalArgumentException(text)
        }
    }

    override fun getTags(): Array<String?>? {
        return arrayOf(getValidName(true), getValidName(false))
    }

    private fun getValidName(value: Boolean): String? {
        return if (value) "True" else "False"
    }

    private fun isValidName(value: Boolean, text: String?): Boolean {
        return getValidName(value).equals(text, ignoreCase = true)
    }

    init {
        value = false
        newValue = false
    }
}