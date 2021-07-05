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
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

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
 * Components for editing numbers, using spinners.
 *
 * @author Elisha Peterson
 */
abstract class NumberEditor : MPropertyEditorSupport() {
    protected val spinner: JSpinner?
    override fun supportsCustomEditor(): Boolean {
        return spinner != null
    }

    override fun getCustomEditor(): Component? {
        return spinner
    }

    override fun getJavaInitializationString(): String? {
        val value = value
        return value?.toString() ?: "null"
    }

    class ByteEditor : NumberEditor() {
        override fun getJavaInitializationString(): String? {
            val value = value
            return if (value != null) "((byte)$value)" else "null"
        }

        override fun setAsText(text: String?) {
            value = if (text == null) null else java.lang.Byte.decode(text)
        }

        override fun initEditorValue() {
            val value = (newValue as Number).toByte()
            // cast to Number required for proper spinner setup
            spinner.setModel(SpinnerNumberModel(value as Number, Byte.MIN_VALUE, Byte.MAX_VALUE, 1))
        }

        init {
            newValue = 0 as Byte
            initEditorValue()
        }
    }

    class ShortEditor : NumberEditor() {
        override fun getJavaInitializationString(): String? {
            val value = value
            return if (value != null) "((short)$value)" else "null"
        }

        override fun setAsText(text: String?) {
            value = if (text == null) null else java.lang.Short.decode(text)
        }

        override fun initEditorValue() {
            val `val` = (newValue as Number).toShort()
            // cast to Number required for proper spinner setup
            spinner.setModel(SpinnerNumberModel(`val` as Number, Short.MIN_VALUE, Short.MAX_VALUE, 1))
        }

        init {
            newValue = 0 as Short
            initEditorValue()
        }
    }

    class IntegerEditor : NumberEditor() {
        override fun setAsText(text: String?) {
            value = if (text == null) null else Integer.decode(text)
        }

        override fun initEditorValue() {
            spinner.setModel(SpinnerNumberModel((newValue as Number).toInt(), Int.MIN_VALUE, Int.MAX_VALUE, 1))
        }

        init {
            newValue = 0
            initEditorValue()
        }
    }

    class LongEditor : NumberEditor() {
        override fun getJavaInitializationString(): String? {
            val value = value
            return if (value != null) value.toString() + "L" else "null"
        }

        override fun setAsText(text: String?) {
            value = if (text == null) null else java.lang.Long.decode(text)
        }

        override fun initEditorValue() {
            val `val` = (newValue as Number).toLong()
            // cast to Number required for proper spinner setup
            spinner.setModel(SpinnerNumberModel(`val` as Number, Long.MIN_VALUE, Long.MAX_VALUE, 1))
            val pref = spinner.getPreferredSize()
            spinner.setPreferredSize(Dimension(
                    Math.min(MAX_SPINNER_WIDTH, pref.width), pref.height))
        }

        init {
            newValue = 0L
            initEditorValue()
        }
    }

    class FloatEditor : NumberEditor() {
        override fun getJavaInitializationString(): String? {
            val value = value
            return if (value != null) value.toString() + "F" else "null"
        }

        override fun setAsText(text: String?) {
            value = if (text == null) null else java.lang.Float.valueOf(text)
        }

        override fun initEditorValue() {
            val value = (newValue as Number).toFloat()
            // cast to Number required for proper spinner setup
            spinner.setModel(SpinnerNumberModel(value as Number, -Float.MAX_VALUE, Float.MAX_VALUE, 0.1f))
            spinner.setPreferredSize(Dimension(MAX_SPINNER_WIDTH, spinner.getPreferredSize().height))
        }

        init {
            newValue = 0f
            initEditorValue()
        }
    }

    class DoubleEditor : NumberEditor() {
        override fun setAsText(text: String?) {
            value = if (text == null) null else java.lang.Double.valueOf(text)
        }

        override fun initEditorValue() {
            spinner.setModel(SpinnerNumberModel((newValue as Number).toDouble(), -Double.MAX_VALUE, Double.MAX_VALUE, 0.01))
            spinner.setPreferredSize(Dimension(MAX_SPINNER_WIDTH, spinner.getPreferredSize().height))
        }

        init {
            newValue = 0.0
            initEditorValue()
        }
    }

    companion object {
        private const val MAX_SPINNER_WIDTH = 100
    }

    init {
        spinner = JSpinner()
        spinner.addChangeListener(ChangeListener { e: ChangeEvent? -> setNewValue(spinner.value) })
    }
}