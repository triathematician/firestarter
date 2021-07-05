package com.googlecode.blaisemath.firestarter.editor

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
import java.awt.*
import java.awt.geom.*
import java.beans.*
import java.util.*

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
 * Static code for registering and accessing registered property editors.
 *
 * @author Elisha Peterson
 */
object EditorRegistration {
    /** Registers all of the editors specified within this package.  */
    fun registerEditors() {
        // basic editors
        PropertyEditorManager.registerEditor(Boolean::class.javaPrimitiveType, BooleanEditor::class.java)
        PropertyEditorManager.registerEditor(Boolean::class.java, BooleanEditor::class.java)
        PropertyEditorManager.registerEditor(String::class.java, StringEditor::class.java)
        PropertyEditorManager.registerEditor(Enum::class.java, EnumEditor::class.java)

        // number editors
        PropertyEditorManager.registerEditor(Byte::class.java, NumberEditor.ByteEditor::class.java)
        PropertyEditorManager.registerEditor(Byte::class.javaPrimitiveType, NumberEditor.ByteEditor::class.java)
        PropertyEditorManager.registerEditor(Short::class.java, NumberEditor.ShortEditor::class.java)
        PropertyEditorManager.registerEditor(Short::class.javaPrimitiveType, NumberEditor.ShortEditor::class.java)
        PropertyEditorManager.registerEditor(Int::class.java, NumberEditor.IntegerEditor::class.java)
        PropertyEditorManager.registerEditor(Int::class.javaPrimitiveType, NumberEditor.IntegerEditor::class.java)
        PropertyEditorManager.registerEditor(Long::class.java, NumberEditor.LongEditor::class.java)
        PropertyEditorManager.registerEditor(Long::class.javaPrimitiveType, NumberEditor.LongEditor::class.java)
        PropertyEditorManager.registerEditor(Float::class.java, NumberEditor.FloatEditor::class.java)
        PropertyEditorManager.registerEditor(Float::class.javaPrimitiveType, NumberEditor.FloatEditor::class.java)
        PropertyEditorManager.registerEditor(Double::class.java, NumberEditor.DoubleEditor::class.java)
        PropertyEditorManager.registerEditor(Double::class.javaPrimitiveType, NumberEditor.DoubleEditor::class.java)

        // array editors
        PropertyEditorManager.registerEditor(Array<String>::class.java, IndexedPropertyEditor::class.java)

        // point editors
        PropertyEditorManager.registerEditor(Point::class.java, PointEditor::class.java)
        PropertyEditorManager.registerEditor(Dimension::class.java, DimensionEditor::class.java)
        PropertyEditorManager.registerEditor(Rectangle::class.java, RectangleEditor::class.java)
        PropertyEditorManager.registerEditor(Insets::class.java, InsetsEditor::class.java)
        PropertyEditorManager.registerEditor(Point2D.Double::class.java, Point2DEditor::class.java)
        PropertyEditorManager.registerEditor(Line2D.Double::class.java, Line2DEditor::class.java)
        PropertyEditorManager.registerEditor(Ellipse2D.Double::class.java, RectangularShapeEditor::class.java)
        PropertyEditorManager.registerEditor(Rectangle2D.Double::class.java, RectangularShapeEditor::class.java)
        PropertyEditorManager.registerEditor(RectangularShape::class.java, RectangularShapeEditor::class.java)

        // complex editors
        PropertyEditorManager.registerEditor(Color::class.java, ColorEditor::class.java)
        PropertyEditorManager.registerEditor(Font::class.java, FontEditor::class.java)
    }

    /**
     * Returns editor type for a given class, as registered by the property manager.
     * @param cls the class type
     * @return a property editor for the provided class, or `null` if there is no available editor
     */
    fun getRegisteredEditor(cls: Class<*>?): PropertyEditor? {
        return getRegisteredEditor<Any?>(null, cls)
    }

    /**
     * Returns editor type for a given object/class, as registered by the property manager.
     * @param <T> object type
     * @param obj the object type
     * @param cls the class type
     * @return a property editor for the provided class, or `null` if there is no available editor
    </T> */
    fun <T> getRegisteredEditor(obj: T?, cls: Class<*>?): PropertyEditor? {
        Objects.requireNonNull(cls)
        var result = PropertyEditorManager.findEditor(cls)
        if (result != null) {
            return result
        }
        // look for an enum editor for enum classes
        if (cls.isEnum()) {
            result = PropertyEditorManager.findEditor(Enum::class.java)
            if (result != null) {
                return result
            }
        }
        // look for the object instance type
        if (obj != null) {
            assert(cls.isInstance(obj))
            result = PropertyEditorManager.findEditor(obj.javaClass)
            if (result != null) {
                return result
            }
        }
        return PropertyEditorManager.findEditor(Any::class.java)
    }

    /**
     * Returns a new instance of an editor for the given property, initialized with
     * the introspected value of the property in the bean.
     * @param bean the underlying class
     * @param descriptor a descriptor for a bean property
     * @return an appropriate editor for the property (not initialized)
     */
    fun getEditor(bean: Any?, descriptor: PropertyDescriptor?): PropertyEditor? {
        requireNotNull(descriptor) { "Null property descriptor!" }

        // set to type specified by the property editor, if possible
        var result: PropertyEditor? = null
        val cls = descriptor.propertyEditorClass
        if (cls != null) {
            result = Reflection.tryInvokeNew(cls) as PropertyEditor
        }

        // if no luck, set to editor registered for actual type
        if (result == null) {
            val sampleRead = Reflection.tryInvokeRead(bean, descriptor)
            if (sampleRead != null) {
                result = getRegisteredEditor(sampleRead.javaClass)
            }
        }

        // if no luck, set to editor registered for descriptor type
        if (result == null && descriptor.propertyType != null) {
            result = getRegisteredEditor(descriptor.propertyType)
        }

        // if no luck, use the default editor
        if (result == null) {
            result = PropertyEditorSupport()
        }
        updateEditorValue(bean, descriptor, result)
        addChangeListening(bean, descriptor, result)
        return result
    }

    /** Updates the value of an editor.  */
    private fun updateEditorValue(bean: Any?, descriptor: PropertyDescriptor?, editor: PropertyEditor?) {
        val value = Reflection.tryInvokeRead(bean, descriptor)
        editor.setValue(value)
    }

    /** Sets up change listening to update the bean when the value changes.  */
    private fun addChangeListening(bean: Any?, descriptor: PropertyDescriptor?, result: PropertyEditor?) {
        if (descriptor.getWriteMethod() != null) {
            result.addPropertyChangeListener(PropertyChangeListener { evt: PropertyChangeEvent? ->
                val source = evt.getSource()
                val newValue = if (source is MPropertyEditorSupport) (source as MPropertyEditorSupport).getNewValue() else if (source is PropertyEditor) (source as PropertyEditor).value else source
                Reflection.tryInvokeWrite(bean, descriptor, newValue)
            })
        }
    }

    /**
     * Returns a new instance of an editor for the given indexed property, initialized with
     * the introspected value of the property in the bean.
     *
     * @param bean the underlying class
     * @param descriptor a descriptor for a bean property
     * @param n the index to use for values
     *
     * @return an appropriate editor for the property (not initialized)
     */
    fun getIndexedEditor(bean: Any?, descriptor: IndexedPropertyDescriptor?, n: Int): PropertyEditor? {
        var result = getRegisteredEditor(descriptor.getIndexedPropertyType())
        if (result == null) {
            result = PropertyEditorSupport()
        }
        updateEditorValue(bean, descriptor, n, result)
        addChangeListening(bean, descriptor, n, result)
        return result
    }

    /** Updates the value of an editor for an indexed property.  */
    private fun updateEditorValue(bean: Any?, descriptor: IndexedPropertyDescriptor?, n: Int, editor: PropertyEditor?) {
        val value = Reflection.tryInvokeIndexedRead(bean, descriptor, n)
        editor.setValue(value)
    }

    /** Sets up change listening to update the bean when the value changes (indexed properties).  */
    private fun addChangeListening(bean: Any?, descriptor: IndexedPropertyDescriptor?, n: Int, result: PropertyEditor?) {
        if (descriptor.getIndexedWriteMethod() != null) {
            result.addPropertyChangeListener(PropertyChangeListener { evt: PropertyChangeEvent? ->
                val source = evt.getSource()
                val newValue = if (source is MPropertyEditorSupport) (source as MPropertyEditorSupport).getNewValue() else if (source is PropertyEditor) (source as PropertyEditor).value else source
                Reflection.tryInvokeIndexedWrite(bean, descriptor, n, newValue)
            })
        }
    }
}