package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.editor.EditorRegistration
import com.googlecode.blaisemath.firestarter.editor.EditorRegistrationTest
import com.googlecode.blaisemath.firestarter.editor.MPropertyEditorSupport
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
import com.googlecode.blaisemath.firestarter.swing.FilteredListModel
import com.googlecode.blaisemath.ui.PropertyActionPanel
import junit.framework.TestCase
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Before
import org.junit.BeforeClass
import java.awt.Component
import java.beans.*
import java.util.*
import javax.swing.JComponent
import javax.swing.ListModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

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
 * Provides lists of [PropertyEditor]s and corresponding components used
 * for editing the properties of a [PropertyModel].
 *
 * @author Elisha Peterson
 */
class PropertyEditorModel(
        /** Model providing which properties are being displayed/editable  */
        private val model: PropertyModel?) : ListModel<Component?> {
    /** List of property editors.  */
    private var editors: Array<PropertyEditor?>?

    /** List of component editors.  */
    private val components: FilteredListModel<Component?>?

    /** Listens for changes to editors  */
    private val editorListener: PropertyChangeListener?

    /** Propagates changes from the underlying property model  */
    private val pcs: PropertyChangeSupport? = PropertyChangeSupport(this)

    /** Constructs visual editor components & set up listening.  */
    private fun initEditors() {
        val size = model.getSize()
        editors = arrayOfNulls<PropertyEditor?>(size)
        val comps: MutableList<Component?> = ArrayList()
        for (i in 0 until size) {
            val type = model.getPropertyType(i)
                    ?: throw IllegalStateException(model.getElementAt(i).toString() + " has null type for model " + model)
            val `val` = model.getPropertyValue(i)
            editors.get(i) = EditorRegistration.getRegisteredEditor(`val`, model.getPropertyType(i))
            if (editors.get(i) != null) {
                editors.get(i).setValue(model.getPropertyValue(i))
                editors.get(i).addPropertyChangeListener(editorListener)
            }
            val ci = if (editors.get(i) != null && editors.get(i).supportsCustomEditor()) editors.get(i).getCustomEditor() else DefaultPropertyComponent(model, i)
            ci.isEnabled = ci is DefaultPropertyComponent || model.isWritable(i)
            if (ci is JComponent) {
                (ci as JComponent).border = null
            }
            comps.add(ci)
        }
        components.setUnfilteredItems(comps)
    }

    /** Manages a change from one of the editors  */
    private fun handleEditorChange(evt: PropertyChangeEvent?) {
        val source = evt.getSource()
        for (i in 0 until model.getSize()) {
            if (editors.get(i) is PropertyEditorSupport
                    && source === (editors.get(i) as PropertyEditorSupport?).getSource()) {
                val newValue = (source as MPropertyEditorSupport).newValue
                model.setPropertyValue(i, newValue)
                editors.get(i).setValue(newValue)
            } else if (source === editors.get(i)) {
                val newValue = editors.get(i).getValue()
                model.setPropertyValue(i, newValue)
            }
        }
    }

    fun getPropertyModel(): PropertyModel? {
        return model
    }

    override fun getSize(): Int {
        return components.getSize()
    }

    override fun getElementAt(index: Int): Component? {
        return components.getElementAt(index)
    }

    fun getPropertyEditor(row: Int): PropertyEditor? {
        return editors.get(row)
    }

    //region ListDataListener METHODS
    override fun addListDataListener(l: ListDataListener?) {
        components.addListDataListener(l)
    }

    override fun removeListDataListener(l: ListDataListener?) {
        components.removeListDataListener(l)
    }

    //endregion
    //region PropertyChangeSupport METHODS
    fun addPropertyChangeListener(listener: PropertyChangeListener?) {
        pcs.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener?) {
        pcs.removePropertyChangeListener(listener)
    }

    fun addPropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
        pcs.addPropertyChangeListener(propertyName, listener)
    }

    fun removePropertyChangeListener(propertyName: String?, listener: PropertyChangeListener?) {
        pcs.removePropertyChangeListener(propertyName, listener)
    } //endregion

    init {
        model.addPropertyChangeListener(PropertyChangeListener { evt: PropertyChangeEvent? ->
            val name = evt.getPropertyName()!!
            for (i in 0 until model.getSize()) {
                if (name == model.getElementAt(i)) {
                    editors.get(i).setValue(evt.getNewValue())
                }
            }
            pcs.firePropertyChange(evt)
        })
        model.addListDataListener(object : ListDataListener {
            override fun intervalAdded(e: ListDataEvent?) {
                initEditors()
            }

            override fun intervalRemoved(e: ListDataEvent?) {
                initEditors()
            }

            override fun contentsChanged(e: ListDataEvent?) {
                initEditors()
            }
        })
        editorListener = PropertyChangeListener { evt: PropertyChangeEvent? -> handleEditorChange(evt) }
        components = FilteredListModel()
        initEditors()
    }
}