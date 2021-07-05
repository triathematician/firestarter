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
import org.junit.BeforeClassimport
import javax.swing.JPanel
import javax.swing.Scrollable

java.awt.*
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
 * Combines several collapsible components into a single panel. Each component
 * is wrapped by an [MPanel] so that it has a displayed title bar and
 * expand/collapse buttons.
 *
 * @author Elisha Peterson
 */
class RollupPanel : JPanel(), Scrollable {
    override fun addImpl(comp: Component?, constraints: Any?, index: Int) {
        if (comp is MPanel) {
            super.addImpl(comp, constraints, index)
        } else if (comp != null && constraints is String) {
            super.addImpl(MPanel(constraints as String?, comp), constraints, index)
        } else {
            super.addImpl(MPanel(comp), constraints, index)
        }
    }

    override fun remove(comp: Component?) {
        val component = this.components
        var i = component.size
        while (--i >= 0) {
            if (component[i] === comp || (component[i] is MPanel
                            && (component[i] as MPanel).primaryComponent === comp)) {
                super.remove(i)
            }
        }
    }

    override fun getPreferredScrollableViewportSize(): Dimension? {
        return preferredSize
    }

    override fun getScrollableUnitIncrement(visibleRect: Rectangle?, orientation: Int, direction: Int): Int {
        return 20
    }

    override fun getScrollableBlockIncrement(visibleRect: Rectangle?, orientation: Int, direction: Int): Int {
        return 100
    }

    override fun getScrollableTracksViewportWidth(): Boolean {
        return true
    }

    override fun getScrollableTracksViewportHeight(): Boolean {
        return false
    }

    init {
        val layout = VerticalLayout()
        setLayout(layout)
        super.addImpl(layout.verticalSpacer, null, -1)
    }
}