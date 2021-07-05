package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfigimport
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
import org.junit.BeforeClassimport
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Methodimport
import javax.swing.*

com.googlecode.blaisemath.firestarter.editor.MPanelEditorSupport
import java.lang.InterruptedException
import java.util.HashMap
import java.util.function.ToDoubleFunction
import java.util.Deque
import java.util.TreeSet
import java.util.stream.IntStream
import java.util.TreeMap
import java.util.function.BiFunction
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import com.googlecode.blaisemath.ui.PropertyActionPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants
import java.util.function.IntFunction
import org.junit.BeforeClassimport

java.awt.*import java.awt.event.*
import java.lang.reflect.Methodimport

java.util.logging.Levelimport java.util.logging.Logger
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
 * assigned editor, but have an associated "getInstance" method, with argument taking
 * an enum value, such that each enum returns a value of the object. When the enum value is
 * selected in a ComboBox, the underlying value is updated.
 *
 * @author Elisha Peterson
 */
class EnumObjectEditor : MPanelEditorSupport() {
    /** Method used to retrieve new instances.  */
    private var instanceMethod: Method? = null

    /** Options for selecting different object types  */
    private var combo: JComboBox<Any?>? = null

    /** String representing the custom object selected in the combo box model  */
    private var custom: String? = null
    override fun initCustomizer() {
        panel = JPanel()
        panel.layout = BorderLayout()
        combo = JComboBox()
        combo.setEditable(false)
        combo.setBackground(panel.background)
        combo.addItemListener(ItemListener { e: ItemEvent? -> handleSelectionChange(e) })
        panel.add(combo, BorderLayout.CENTER)
        val button = JButton("...")
        button.margin = Insets(3, 3, 3, 2)
        button.addActionListener { e: ActionEvent? ->
            val win = SwingUtilities.getWindowAncestor(panel)
            show(win, false, newValue)
        }
        panel.add(button, BorderLayout.EAST)
    }

    fun getEnumClass(): Class<*>? {
        return instanceMethod.getParameterTypes()[0]
    }

    override fun initEditorValue() {
        val mm = newValue.javaClass.methods
        for (m in mm) {
            if (validMethod(m, newValue.javaClass)) {
                instanceMethod = m
                break
            }
        }
        if (panel != null) {
            val c1: Array<Any?>? = getEnumClass().getEnumConstants()
            val model = DefaultComboBoxModel(c1)
            custom = "Custom $value"
            model.addElement(custom)
            model.selectedItem = custom
            combo.setModel(model)
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

    private fun handleSelectionChange(e: ItemEvent?) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getItem() === custom) {
                setNewValue(super.getValue())
            } else {
                try {
                    setNewValue(instanceMethod.invoke(null, e.getItem()))
                } catch (ex: IllegalAccessException) {
                    LOG.log(Level.SEVERE, null, ex)
                } catch (ex: InvocationTargetException) {
                    LOG.log(Level.SEVERE, null, ex)
                }
            }
        }
    }

    companion object {
        private val LOG = Logger.getLogger(EnumObjectEditor::class.java.name)
        fun validMethod(m: Method?, type: Class<*>?): Boolean {
            return ("getInstance" == m.getName() && m.getReturnType().isAssignableFrom(type)
                    && m.getParameterTypes().size == 1 && m.getParameterTypes()[0].isEnum)
        }
    }

    init {
        initCustomizer()
    }
}