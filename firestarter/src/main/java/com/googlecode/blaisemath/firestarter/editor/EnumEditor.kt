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
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JPanel

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
 * Uses a combo box for editing enum's. Must be registered separately for each
 * enum type that uses it.
 *
 * @author Elisha Peterson
 */
class EnumEditor : MPanelEditorSupport() {
    private var combo: JComboBox<Any?>? = null
    override fun initCustomizer() {
        panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        combo = JComboBox()
        combo.setEditable(false)
        combo.setBackground(panel.background)
        combo.setAlignmentY(Component.CENTER_ALIGNMENT)
        panel.add(combo)
        combo.addItemListener(ItemListener { evt: ItemEvent? ->
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                setNewValue(evt.getItem())
            }
        })
    }

    fun getEnumClass(): Class<*>? {
        return (value as Enum<*>).getDeclaringClass()
    }

    override fun initEditorValue() {
        if (panel != null) {
            combo.setModel(DefaultComboBoxModel(getEnumClass().getEnumConstants()))
            combo.setSelectedItem(getNewValue())
            panel.repaint()
        }
    }

    override fun getJavaInitializationString(): String? {
        return getEnumClass().toString() + "." + (value as Enum<*>).name
    }

    override fun getTags(): Array<String?>? {
        val values: Array<Any?>? = getEnumClass().getEnumConstants()
        val result = arrayOfNulls<String?>(values.size)
        for (i in result.indices) {
            result[i] = values.get(i).toString()
        }
        return result
    }
}