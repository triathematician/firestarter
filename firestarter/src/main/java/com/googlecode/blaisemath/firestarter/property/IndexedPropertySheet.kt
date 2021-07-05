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
import com.googlecode.blaisemath.ui.PropertyActionPanel
import junit.framework.TestCase
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Before
import org.junit.BeforeClass
import java.awt.Insets
import java.awt.event.ActionEvent
import java.beans.IndexedPropertyDescriptor
import java.util.*
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
 * Table-formatted list of individual properties within an array, useful for editing
 * indexed properties. Uses [BeanIndexedPropertyModel] for the underlying model.
 *
 * @author Elisha Peterson
 */
class IndexedPropertySheet : PropertySheet() {
    /** The model for the indexed property  */
    private var beanModel: BeanIndexedPropertyModel? = null
    override fun initToolbar() {
        toolsVisible = true
        // set up tool panel
        val aa: AbstractAction = object : AbstractAction("+") {
            override fun actionPerformed(e: ActionEvent?) {
                beanModel.addNewValue()
                handleTableChange()
            }
        }
        aa.putValue(Action.SHORT_DESCRIPTION, "Add a new element to the end of the list.")
        val addB = JButton(aa)
        addB.margin = Insets(2, 4, 2, 4)
        val remove: AbstractAction = object : AbstractAction("-") {
            override fun actionPerformed(e: ActionEvent?) {
                val cellEditor: CellEditor? = table.cellEditor
                cellEditor?.stopCellEditing()
                beanModel.removeValues(table.selectedRows)
                handleTableChange()
            }
        }
        remove.putValue(Action.SHORT_DESCRIPTION, "Remove the selected element from the end of the list.")
        val delB = JButton(remove)
        delB.margin = Insets(2, 4, 2, 4)
        val font = addB.font.deriveFont(addB.font.size as Float - 2)
        addB.font = font
        delB.font = font
        toolPanel = JPanel()
        toolPanel.layout = BoxLayout(toolPanel, BoxLayout.LINE_AXIS)
        toolPanel.background = table.gridColor
        toolPanel.add(Box.createGlue())
        toolPanel.add(addB)
        toolPanel.add(delB)
    }

    override fun handleTableChange() {
        updateRowHeights()
        firePropertyChange("size", null, null)
        repaint()
    }

    companion object {
        /**
         * Create a property sheet that uses the supplied model for editing components.
         * @param bean the object
         * @param propName name of an indexed property
         * @return new property sheet
         */
        fun forIndexedProperty(bean: Any?, propName: String?): PropertySheet? {
            Objects.requireNonNull(bean)
            return forIndexedProperty(bean, Reflection.indexedPropertyDescriptor(bean.javaClass, propName))
        }

        /**
         * Create a property sheet that uses the supplied model for editing components.
         * @param bean the object
         * @param ipd indexed property
         * @return new property sheet
         */
        fun forIndexedProperty(bean: Any?, ipd: IndexedPropertyDescriptor?): PropertySheet? {
            Objects.requireNonNull(bean)
            Objects.requireNonNull(ipd)
            val res = IndexedPropertySheet()
            res.beanModel = BeanIndexedPropertyModel(bean, ipd)
            res.model.headers = arrayOf<String?>("Index", "Value")
            res.defaultNameColWidth = 35
            res.initComponents(res.beanModel)
            return res
        }
    }
}