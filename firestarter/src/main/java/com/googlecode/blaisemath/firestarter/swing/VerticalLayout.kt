package com.googlecode.blaisemath.firestarter.swing

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
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.Box

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
 * Controls layout vertically. The width of components is fixed by the parent's
 * size, but the height is determined by the internal components.
 */
class VerticalLayout : GridBagLayout() {
    private val dc: GridBagConstraints?
    private val vSpacer: Component?
    override fun addLayoutComponent(comp: Component?, constraints: Any?) {
        super.addLayoutComponent(comp, dc)
        dc.insets = Insets(if (dc.gridy == 0) Y_MARGIN else Y_SPACING,
                X_MARGIN, 0, X_MARGIN)
        dc.gridy++
        dc.weighty = 1.0
        super.setConstraints(vSpacer, dc)
        dc.weighty = 0.0
    }

    fun getVerticalSpacer(): Component? {
        return vSpacer
    }

    companion object {
        private const val X_MARGIN = 4
        private const val Y_MARGIN = 4
        private const val Y_SPACING = 4
    }

    init {
        dc = GridBagConstraints()
        dc.fill = GridBagConstraints.HORIZONTAL
        dc.weightx = 1.0
        dc.gridx = 0
        dc.gridy = 0
        dc.ipadx = 0
        dc.ipady = 0
        vSpacer = Box.createVerticalGlue()
    }
}