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
import java.awt.BorderLayout
import java.awt.Window
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.beans.IndexedPropertyDescriptor
import java.util.function.Predicate
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JScrollPane

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
 * Shows a property sheet for a specified bean within a dialog box.
 * @author Elisha Peterson
 */
class PropertySheetDialog : JDialog {
    private constructor(win: Window?, modal: ModalityType?, bean: Any?, filter: Predicate<String?>?) : super(win, bean.toString(), modal) {
        val propertySheet: PropertySheet = PropertySheet.Companion.forBean(bean)
        if (filter != null) {
            val pm = propertySheet.propertyModel
            (pm as BeanPropertyModel).filter = BeanPropertyFilter.Companion.byName(filter)
        }
        initComponents(propertySheet)
    }

    private constructor(win: Window?, modal: ModalityType?, bean: Any?, ipd: IndexedPropertyDescriptor?) : super(win, "Indexed property [" + ipd.getDisplayName() + "] of " + bean.toString(), modal) {
        initComponents(IndexedPropertySheet.Companion.forIndexedProperty(bean, ipd))
    }

    private constructor(win: Window?, modal: ModalityType?, bean: Any?, model: PropertyModel?) : super(win, "Editing " + bean.toString(), modal) {
        initComponents(PropertySheet(model))
    }

    //endregion
    private fun initComponents(propertySheet: PropertySheet?) {
        add(JScrollPane(propertySheet,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ), BorderLayout.CENTER)
        val okButton = JButton("Close")
        okButton.addActionListener { e: ActionEvent? ->
            isVisible = false
            dispose()
        }
        add(okButton, BorderLayout.SOUTH)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        pack()
    }

    companion object {
        //region FACTORY METHODS
        private fun modality(modal: Boolean): ModalityType? {
            return if (modal) ModalityType.APPLICATION_MODAL else ModalityType.MODELESS
        }
        /**
         * Creates new form PropertySheetDialog
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param onClose function to call when the dialog is closed (null ok)
         */
        /**
         * Creates new form PropertySheetDialog
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         */
        @JvmOverloads
        fun show(parent: Window?, modal: Boolean, bean: Any?, onClose: Runnable? = null as Runnable?) {
            val dialog = PropertySheetDialog(parent, modality(modal), bean, null as Predicate<String?>?)
            configureAndShow(dialog, onClose)
        }
        /**
         * Creates new form PropertySheetDialog
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param propertyFilter filters properties by name
         * @param onClose function to call when the dialog is closed (null ok)
         */
        /**
         * Creates new form PropertySheetDialog
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param propertyFilter filters properties by name
         */
        @JvmOverloads
        fun show(parent: Window?, modal: Boolean, bean: Any?, propertyFilter: Predicate<String?>?, onClose: Runnable? = null) {
            val dialog = PropertySheetDialog(parent, modality(modal), bean, propertyFilter)
            configureAndShow(dialog, onClose)
        }
        /**
         * Creates new form PropertySheetDialog with an indexed property.
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param ipd an indexed property descriptor
         * @param onClose function to call when the dialog is closed (null ok)
         */
        /**
         * Creates new form PropertySheetDialog with an indexed property.
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param ipd an indexed property descriptor
         */
        @JvmOverloads
        fun show(parent: Window?, modal: Boolean, bean: Any?, ipd: IndexedPropertyDescriptor?, onClose: Runnable? = null) {
            val dialog = PropertySheetDialog(parent, modality(modal), bean, ipd)
            configureAndShow(dialog, onClose)
        }
        /**
         * Creates new form PropertySheetDialog with an indexed property.
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param model model for the property sheet
         * @param onClose function to call when the dialog is closed (null ok)
         */
        /**
         * Creates new form PropertySheetDialog with an indexed property.
         * @param parent the parent frame
         * @param modal whether the dialog box is modal
         * @param bean object to populate the box
         * @param model model for the property sheet
         */
        @JvmOverloads
        fun show(parent: Window?, modal: Boolean, bean: Any?, model: PropertyModel?, onClose: Runnable? = null) {
            val dialog = PropertySheetDialog(parent, modality(modal), bean, model)
            configureAndShow(dialog, onClose)
        }

        private fun configureAndShow(dialog: PropertySheetDialog?, onClose: Runnable?) {
            if (onClose != null) {
                dialog.addWindowListener(object : WindowAdapter() {
                    override fun windowClosed(e: WindowEvent?) {
                        onClose.run()
                    }
                })
            }
            dialog.setVisible(true)
        }
    }
}