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
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.table.AbstractTableModel

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
 * Model for a table of editable properties, based on a [PropertyModel]
 * and a corresponding [PropertyEditorModel].
 *
 * @author elisha
 */
class PropertySheetModel internal constructor(private val editorModel: PropertyEditorModel?) : AbstractTableModel() {
    /** Names of header columns  */
    var headers: Array<String?>? = arrayOf("Name", "Value")
    private val propModel: PropertyModel?
    fun getPropertyModel(): PropertyModel? {
        return propModel
    }

    fun getPropertyEditorModel(): PropertyEditorModel? {
        return editorModel
    }

    override fun getRowCount(): Int {
        return editorModel.getSize()
    }

    override fun getColumnCount(): Int {
        return 2
    }

    override fun getColumnName(col: Int): String? {
        return headers.get(col)
    }

    override fun getColumnClass(col: Int): Class<*>? {
        return if (col == 0) String::class.java else Any::class.java
    }

    override fun isCellEditable(row: Int, col: Int): Boolean {
        return when (col) {
            NAME_COL -> false
            VALUE_COL -> editorModel.getElementAt(row).isEnabled
            else -> throw IllegalStateException()
        }
    }

    override fun getValueAt(row: Int, col: Int): Any? {
        return when (col) {
            NAME_COL -> propModel.getElementAt(row)
            VALUE_COL -> propModel.getPropertyValue(row)
            else -> throw IllegalStateException()
        }
    }

    companion object {
        /** Column containing property names  */
        private const val NAME_COL = 0

        /** Column containing property values  */
        private const val VALUE_COL = 1
    }

    init {
        propModel = editorModel.getPropertyModel()
        editorModel.addListDataListener(object : ListDataListener {
            override fun intervalAdded(e: ListDataEvent?) {
                fireTableDataChanged()
            }

            override fun intervalRemoved(e: ListDataEvent?) {
                fireTableDataChanged()
            }

            override fun contentsChanged(e: ListDataEvent?) {
                fireTableDataChanged()
            }
        })
    }
}