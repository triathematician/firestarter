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
import java.awt.geom.Point2D
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
 * Edits a single Point2D with event handling support.
 *
 * @author Elisha Peterson
 */
class Point2DEditor : MultiSpinnerSupport<Double?>(Point2D.Double(), "x", "y") {
    override fun getJavaInitializationString(): String? {
        val value = value
        return if (value != null) "new java.awt.geom.Point2D.Double(" +
                asText + ")" else "null"
    }

    override fun setAsText(vararg s: String?) {
        val arr = Numbers.decodeAsDoubles(*s)
        value = Point2D.Double(arr[0], arr[1])
    }

    override fun initSpinnerModels() {
        spinners[0].model = SpinnerNumberModel(getNewValue(0) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, DEFAULT_STEP_SIZE)
        spinners[1].model = SpinnerNumberModel(getNewValue(1) as Number?, -Double.MAX_VALUE, Double.MAX_VALUE, DEFAULT_STEP_SIZE)
    }

    public override fun getValue(bean: Any?, i: Int): Double? {
        return if (bean == null) {
            0.0
        } else when (i) {
            0 -> (bean as Point2D?).getX()
            1 -> (bean as Point2D?).getY()
            else -> throw ArrayIndexOutOfBoundsException()
        }
    }

    public override fun setNewValueList(values: MutableList<Double?>?) {
        setNewValue(Point2D.Double(values.get(0), values.get(1)))
    }

    companion object {
        var DEFAULT_STEP_SIZE = 0.01
    }
}