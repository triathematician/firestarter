package com.googlecode.blaisemath.firestarter.editor

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.editor.EditorRegistrationTest
import com.googlecode.blaisemath.firestarter.internal.Numbers
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
import java.awt.geom.Line2D
import javax.swing.SpinnerNumberModel

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
 * Edits a line: x1, y1, x2, y2.
 *
 * @author Elisha Peterson
 */
class Line2DEditor : MultiSpinnerSupport<Double?>(Line2D.Double(), "x1", "y1", "x2", "y2") {
    override fun getJavaInitializationString(): String? {
        val value = value
        return if (value != null) "new java.awt.geom.Line2D.Double($asText)" else "null"
    }

    override fun setAsText(vararg s: String?) {
        val arr = Numbers.decodeAsDoubles(*s)
        value = Line2D.Double(arr[0], arr[1], arr[2], arr[3])
    }

    override fun initSpinnerModels() {
        spinners[0].model = SpinnerNumberModel(getNewValue(0) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, DEFAULT_STEP_SIZE)
        spinners[1].model = SpinnerNumberModel(getNewValue(1) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, DEFAULT_STEP_SIZE)
        // permit negative width/height rectangles
        spinners[2].model = SpinnerNumberModel(getNewValue(2) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, .1)
        spinners[3].model = SpinnerNumberModel(getNewValue(3) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, .1)
    }

    public override fun getValue(bean: Any?, i: Int): Double? {
        return when (i) {
            0 -> (bean as Line2D?).getX1()
            1 -> (bean as Line2D?).getY1()
            2 -> (bean as Line2D?).getX2()
            3 -> (bean as Line2D?).getY2()
            else -> throw ArrayIndexOutOfBoundsException()
        }
    }

    public override fun setNewValueList(values: MutableList<Double?>?) {
        setNewValue(Line2D.Double(values.get(0), values.get(1), values.get(2), values.get(3)))
    }

    companion object {
        var DEFAULT_STEP_SIZE = 0.1
    }
}