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
import java.awt.Insets
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
 * Edits insets, with 4 spinners.
 *
 * @author Elisha Peterson
 */
class InsetsEditor : MultiSpinnerSupport<Int?>(Insets(0, 0, 0, 0), "top", "left", "bottom", "right") {
    override fun getJavaInitializationString(): String? {
        val value = value
        return if (value != null) "new java.awt.Insets($asText)" else "null"
    }

    override fun setAsText(vararg s: String?) {
        val arr = Numbers.decodeAsIntegers(*s)
        value = Insets(arr[0], arr[1], arr[2], arr[3])
    }

    override fun initSpinnerModels() {
        spinners[0].model = SpinnerNumberModel(Math.max(0, getNewValue(0)), 0, Int.MAX_VALUE, 1)
        spinners[1].model = SpinnerNumberModel(Math.max(0, getNewValue(1)), 0, Int.MAX_VALUE, 1)
        spinners[2].model = SpinnerNumberModel(Math.max(0, getNewValue(2)), 0, Int.MAX_VALUE, 1)
        spinners[3].model = SpinnerNumberModel(Math.max(0, getNewValue(3)), 0, Int.MAX_VALUE, 1)
    }

    public override fun getValue(bean: Any?, i: Int): Int? {
        return when (i) {
            0 -> (bean as Insets?).top
            1 -> (bean as Insets?).left
            2 -> (bean as Insets?).bottom
            3 -> (bean as Insets?).right
            else -> throw ArrayIndexOutOfBoundsException()
        }
    }

    public override fun setNewValueList(values: MutableList<Int?>?) {
        setNewValue(Insets(values.get(0), values.get(1), values.get(2), values.get(3)))
    }
}