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
import java.beans.PropertyEditorSupport

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
 * Extends the basic property editor to
 * improve handling of events. Maintains a temporary `newValue` object that
 * tracks changes by the editor. These changes will pass events to any interested
 * listeners if adjusted through the [.setNewValue] method rather than directly.
 * An interested bean can then check the "newValue" to see what has changed.
 *
 *
 * The [.cancelEditAction] and [.stopEditAction] methods will
 * either reset the editor to the initial state, or make a permanent change to the
 * initial value. In both cases, an event may be fired to indicate that the external
 * value has changed.
 *
 *
 * Registered listeners hear when the new value is changed somehow. If interested
 * in updating constantly, they should look to the [.getNewValue] method.
 * If only interested in the final value, they should update only when the [.getValue]
 * method returns a different value.
 *
 *
 * External changes, e.g. to the underlying bean, should be handled by invoking the
 * [.setValue] method here. This calls the [.initEditorValue]
 * method to set up the editor based on some external value.
 *
 * @author Elisha Peterson
 * @see java.beans.PropertyEditor
 */
abstract class MPropertyEditorSupport : PropertyEditorSupport() {
    /** Maintains the updated value of the object.  */
    protected var newValue: Any? = null

    /** Flag to prevent extraneous events  */
    private var updating = false

    /**
     * Contains revised/new value of the property. This may be called by
     * external classes when they hear that the value has changed.
     *
     * @return initial value of the editor.
     */
    fun getNewValue(): Any? {
        return newValue
    }

    /**
     * Internally adjusts the new value of the editor. This is intended to be
     * called within event handling methods from customizer components.
     *
     * @param newValue the new value for the object.
     */
    fun setNewValue(newValue: Any?) {
        this.newValue = newValue
        firePropertyChange()
    }

    /**
     * Sets both the `newValue` property and the `value` property to the
     * specified value.
     *
     * This is typically called when the editor is initially created and its value
     * is changed to reflect an underlying bean property. It should not be called
     * within any editor class itself. Instead, the editor class should update the
     * `newValue` field with any changes made.
     *
     * @param value the value of the property being edited
     */
    override fun setValue(value: Any?) {
        if (getNewValue() !== value) {
            setNewValue(value)
        }
        if (getValue() !== value) {
            super.setValue(value)
        }
        initEditorValue()
    }

    override fun firePropertyChange() {
        if (updating) {
            return
        }
        updating = true
        super.firePropertyChange()
        updating = false
    }

    /**
     * This is called whenever an external class changes the value of this editor.
     * It is intended to be overridden by subclasses and to propagate changes to
     * any custom component.
     */
    protected abstract fun initEditorValue()

    /**
     * Called to indicate that the editor is finished editing.
     */
    protected fun cancelEditAction() {
        if (newValue !== value) {
            newValue = value
            initEditorValue()
            firePropertyChange()
        }
    }

    /**
     * Called to indicate that the editor is finished editing.
     */
    protected fun stopEditAction() {
        value = newValue
    }
}