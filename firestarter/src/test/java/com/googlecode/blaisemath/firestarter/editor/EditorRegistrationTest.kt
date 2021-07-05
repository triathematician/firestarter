/*
 * Copyright 2014 Elisha.
 *
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
 */
package com.googlecode.blaisemath.firestarter.editor

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.Assert
import org.junit.Test
import java.awt.*
import java.awt.geom.*
import java.beans.PropertyEditorManager

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
 *
 * @author Elisha
 */
class EditorRegistrationTest {
    /**
     * Test of registerEditors method, of class EditorRegistration.
     */
    @Test
    fun testRegisterEditors() {
        println("registerEditors")
        EditorRegistration.registerEditors()

        // basic editors
        checkEditor(Boolean::class.javaPrimitiveType, BooleanEditor::class.java)
        checkEditor(Boolean::class.java, BooleanEditor::class.java)
        checkEditor(String::class.java, StringEditor::class.java)
        checkEditor(Enum::class.java, EnumEditor::class.java)

        // number editors
        checkEditor(Byte::class.java, NumberEditor.ByteEditor::class.java)
        checkEditor(Byte::class.javaPrimitiveType, NumberEditor.ByteEditor::class.java)
        checkEditor(Short::class.java, NumberEditor.ShortEditor::class.java)
        checkEditor(Short::class.javaPrimitiveType, NumberEditor.ShortEditor::class.java)
        checkEditor(Int::class.java, NumberEditor.IntegerEditor::class.java)
        checkEditor(Int::class.javaPrimitiveType, NumberEditor.IntegerEditor::class.java)
        checkEditor(Long::class.java, NumberEditor.LongEditor::class.java)
        checkEditor(Long::class.javaPrimitiveType, NumberEditor.LongEditor::class.java)
        checkEditor(Float::class.java, NumberEditor.FloatEditor::class.java)
        checkEditor(Float::class.javaPrimitiveType, NumberEditor.FloatEditor::class.java)
        checkEditor(Double::class.java, NumberEditor.DoubleEditor::class.java)
        checkEditor(Double::class.javaPrimitiveType, NumberEditor.DoubleEditor::class.java)

        // array editors
        checkEditor(Array<String>::class.java, IndexedPropertyEditor::class.java)

        // point editors
        checkEditor(Point::class.java, PointEditor::class.java)
        checkEditor(Dimension::class.java, DimensionEditor::class.java)
        checkEditor(Rectangle::class.java, RectangleEditor::class.java)
        checkEditor(Insets::class.java, InsetsEditor::class.java)
        checkEditor(Point2D.Double::class.java, Point2DEditor::class.java)
        checkEditor(Line2D.Double::class.java, Line2DEditor::class.java)
        checkEditor(Ellipse2D.Double::class.java, RectangularShapeEditor::class.java)
        checkEditor(Rectangle2D.Double::class.java, RectangularShapeEditor::class.java)
        checkEditor(RectangularShape::class.java, RectangularShapeEditor::class.java)

        // complex editors
        checkEditor(Color::class.java, ColorEditor::class.java)
        checkEditor(Font::class.java, FontEditor::class.java)
    }

    companion object {
        private fun checkEditor(objectType: Class<*>?, editorType: Class<*>?) {
            val pe = PropertyEditorManager.findEditor(objectType)
            Assert.assertNotNull(pe)
            Assert.assertEquals(editorType, pe.javaClass)
        }
    }
}