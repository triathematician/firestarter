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
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeListener
import javax.swing.*
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

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
 * Table used for editing a collection of key-value properties.
 *
 * @author Elisha Peterson
 */
open class PropertySheet : JPanel {
    /** Default width of first column  */
    protected var defaultNameColWidth = 70

    /** The table displayed.  */
    protected var table: JTable? = null

    /** Flag for showing/hiding extra panels  */
    protected var toolsVisible = TOOLBAR_VISIBLE_DEFAULT

    /** The panel with the filter box.  */
    protected var toolPanel: JPanel? = null

    /** The combo box for filtering.  */
    protected var filterCombo: JComboBox<BeanPropertyFilter?>? = null

    /** The underlying table model.  */
    protected var model: PropertySheetModel? = null

    /** Initialize sheet with an empty model.  */
    constructor() {
        initComponents(PropertyModel.Empty())
    }

    /**
     * Initialize sheet with specified model.
     * @param pm property model for editing
     */
    constructor(pm: PropertyModel?) {
        initComponents(pm)
    }

    //region INITIALIZATION
    protected fun initComponents(model: PropertyModel?) {
        initTable(model)
        initToolbar()
        layout = BorderLayout()
        add(table, BorderLayout.CENTER)
        if (toolsVisible) {
            add(toolPanel, BorderLayout.NORTH)
        }
    }

    /**
     * Initialize table, setting up components, column sizes, etc.
     * @param pm property model
     */
    protected fun initTable(pm: PropertyModel?) {
        table = JTable()
        val fg = UIManager.getColor("Label.foreground")
        val bg = UIManager.getColor("Label.background")
        table.setGridColor(Color(
                (fg.red + 5 * bg.red) / 6,
                (fg.green + 5 * bg.green) / 6,
                (fg.blue + 5 * bg.blue) / 6))
        model = PropertySheetModel(PropertyEditorModel(pm))
        model.addTableModelListener(TableModelListener { e: TableModelEvent? -> handleTableChange() })
        table.setModel(model)
        table.getTableHeader().reorderingAllowed = false
        updateRowHeights()
        val column = table.getColumnModel().getColumn(1)
        column.cellRenderer = ValueColEditor()
        column.cellEditor = ValueColEditor()

        // set up column sizes
        for (col in 0..1) {
            var prefWidth = MIN_CELL_WIDTH
            for (row in 0 until model.getRowCount()) {
                val `val` = model.getValueAt(row, col)
                val comp = table.getCellRenderer(row, col)
                        .getTableCellRendererComponent(table, `val`, false, false, row, col)
                val wid = if (comp is DefaultPropertyComponent) PREF_BUTTON_WIDTH else comp.preferredSize.width
                prefWidth = Math.max(prefWidth, wid)
            }
            prefWidth = Math.min(prefWidth, MAX_CELL_WIDTH)
            table.getColumnModel().getColumn(col).preferredWidth = prefWidth
            if (col == 0) {
                table.getColumnModel().getColumn(col).minWidth = Math.min(prefWidth, MIN_CELL_WIDTH)
            }
        }
    }

    protected open fun initToolbar() {
        // set up filter
        filterCombo = JComboBox(DefaultComboBoxModel(BeanPropertyFilter.values()))
        val font = filterCombo.getFont().deriveFont(filterCombo.getFont().size as Float - 2)
        filterCombo.setFont(font)
        filterCombo.setSelectedItem(BeanPropertyFilter.STANDARD)
        filterCombo.addActionListener(ActionListener { e: ActionEvent? ->
            if (getPropertyModel() is BeanPropertyModel) {
                (getPropertyModel() as BeanPropertyModel?).setFilter(
                        filterCombo.getSelectedItem() as BeanPropertyFilter)
            }
        })
        val filterLabel = JLabel("Filter: ")
        filterLabel.font = font.deriveFont(Font.ITALIC)
        filterLabel.isOpaque = false
        toolPanel = JPanel()
        toolPanel.setLayout(BoxLayout(toolPanel, BoxLayout.LINE_AXIS))
        toolPanel.setBackground(table.getGridColor())
        toolPanel.add(Box.createGlue())
        toolPanel.add(filterLabel)
        toolPanel.add(filterCombo)
    }
    //endregion
    /**
     * Get the core property model used by the property sheet.
     * @return property model
     */
    fun getPropertyModel(): PropertyModel? {
        return model.getPropertyModel()
    }

    /**
     * Return toolbar status.
     * @return true if toolbar is visible
     */
    fun isToolbarVisible(): Boolean {
        return toolsVisible
    }

    /**
     * Sets toolbar visibility
     * @param val true if visible
     */
    fun setToolbarVisible(`val`: Boolean) {
        if (`val` != toolsVisible) {
            toolsVisible = `val`
            if (`val`) {
                add(toolPanel, BorderLayout.NORTH)
            } else {
                remove(toolPanel)
            }
            validate()
        }
    }

    //region EVENT HANDLING
    fun removeBeanChangeListener(listener: PropertyChangeListener?) {
        getPropertyModel().removePropertyChangeListener(listener)
    }

    fun removeBeanChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
        getPropertyModel().removePropertyChangeListener(propertyName, listener)
    }

    fun addBeanChangeListener(listener: PropertyChangeListener?) {
        getPropertyModel().addPropertyChangeListener(listener)
    }

    fun addBeanChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
        getPropertyModel().addPropertyChangeListener(propertyName, listener)
    }

    /**
     * Update row heights when the underlying data changes.
     */
    protected open fun handleTableChange() {
        table.getSelectionModel().clearSelection()
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing()
        }
        updateRowHeights()
        firePropertyChange("size", null, null)
        repaint()
    }

    /** Updates the size of the table.  */
    fun updateRowHeights() {
        var comp: Component?
        for (i in 0 until model.getRowCount()) {
            comp = model.getPropertyEditorModel().getElementAt(i)
            if (comp != null) {
                table.setRowHeight(i, Math.max(comp.preferredSize.height, MIN_CELL_HEIGHT))
            }
        }
        table.setPreferredScrollableViewportSize(Dimension(
                preferredSize.width,
                preferredSize.height + if (table.getTableHeader() != null) table.getTableHeader().height else 0))
    }
    //endregion
    //region INNER CLASSES
    /** Provides support for editing properties.  */
    internal inner class ValueColEditor : AbstractCellEditor(), TableCellEditor, TableCellRenderer {
        /** Current row being edited.  */
        var row = -1
        override fun getCellEditorValue(): Any? {
            return if (row == -1) null else getPropertyModel().getPropertyValue(row)
        }

        override fun getTableCellEditorComponent(table: JTable?, value: Any?, isSelected: Boolean, row: Int, column: Int): Component? {
            this.row = row
            return model.getPropertyEditorModel().getElementAt(row)
        }

        override fun getTableCellRendererComponent(table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component? {
            return model.getPropertyEditorModel().getElementAt(row)
        }
    } //endregion

    companion object {
        /** This static variable determines whether the filter panel is on or off by default.  */
        var TOOLBAR_VISIBLE_DEFAULT = false

        /** Determines minimum height of cells in the table.  */
        private const val MIN_CELL_HEIGHT = 20

        /** Minimum cell width  */
        private const val MIN_CELL_WIDTH = 40

        /** Maximum width of a cell  */
        private const val MAX_CELL_WIDTH = 400

        /** Preferred width for custom editor buttons.  */
        private const val PREF_BUTTON_WIDTH = 100

        /**
         * Create a property sheet that uses the supplied bean object for editing components.
         * @param bean a bean object
         * @return new property sheet for editing the bean's properties
         */
        fun forBean(bean: Any?): PropertySheet? {
            val res = PropertySheet()
            res.initComponents(BeanPropertyModel(bean))
            return res
        }

        /**
         * Create a property sheet with a custom model
         * @param model property model
         * @return property sheet
         */
        fun forModel(model: PropertyModel?): PropertySheet? {
            return PropertySheet(model)
        }
    }
}