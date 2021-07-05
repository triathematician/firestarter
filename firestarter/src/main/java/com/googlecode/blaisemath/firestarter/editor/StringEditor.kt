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
import java.util.*
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

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
 * Editor for strings.
 *
 * @author Elisha Peterson
 */
class StringEditor : MPropertyEditorSupport() {
    private var field: JTextField? = null
    private var updating = false
    override fun supportsCustomEditor(): Boolean {
        return true
    }

    override fun getCustomEditor(): Component? {
        if (field == null) {
            field = JTextField()
            initEditorValue()
            field.getDocument().addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) {
                    setNewAsText(field.getText())
                }

                override fun removeUpdate(e: DocumentEvent?) {
                    setNewAsText(field.getText())
                }

                override fun changedUpdate(e: DocumentEvent?) {
                    setNewAsText(field.getText())
                }
            })
        }
        return field
    }

    override fun initEditorValue() {
        if (field != null && !updating) {
            field.setText(asText)
        }
    }

    override fun getJavaInitializationString(): String? {
        val value = value ?: return "null"
        val str = value.toString()
        val length = str.length
        val sb = StringBuilder(length + 2)
        sb.append('"')
        for (i in 0 until length) {
            val ch = str[i]
            val iSpecial = Arrays.asList(*SPECIAL_CHARS).indexOf(ch)
            if (iSpecial != -1) {
                sb.append(SPECIAL_CHARS_REPLACE.get(iSpecial))
            } else if (ch < ' ' || ch > '~') {
                sb.append("\\u")
                val hex = Integer.toHexString(ch.toInt())
                sb.append("0".repeat(4 - hex.length))
                sb.append(hex)
            } else {
                sb.append(ch)
            }
        }
        sb.append('"')
        return sb.toString()
    }

    override fun setAsText(text: String?) {
        value = text
    }

    private fun setNewAsText(text: String?) {
        updating = true
        setNewValue(text)
        updating = false
    }

    companion object {
        private val SPECIAL_CHARS: Array<Char?>? = arrayOf('\b', '\t', '\n', '\f', '\r', '\"', '\\')
        private val SPECIAL_CHARS_REPLACE: Array<String?>? = arrayOf("\\b", "\\t", "\\n", "\\f", "\\r", "\\\"", "\\\\")
    }
}