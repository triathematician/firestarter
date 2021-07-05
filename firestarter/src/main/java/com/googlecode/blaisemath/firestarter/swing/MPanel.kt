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
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.awt.geom.Line2D
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.*
import javax.swing.*

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
 * Adds a title bar onto another component, with text and a show/hide options.
 * The component may be minimized or maximized depending upon the user's actions.
 * The title bar defaults to a simple label, but the user may also alter that
 * component directly for custom displays.
 *
 * @author Elisha Peterson
 */
class MPanel(
        /** The label displaying the title  */
        private val titleLabel: JLabel?,
        /** Component within the panel  */
        private var component: Component?) : JPanel() {
    /** The title bar  */
    private val titleBar: JPanel?

    /** The button for minimizing  */
    private val toggle: JToggleButton?

    /** Listen for changes to component size  */
    private val componentSizeListener: PropertyChangeListener?

    constructor(component: Component?) : this(component.toString(), component) {}

    @JvmOverloads
    constructor(title: String? = "Title", component: Component? = JLabel("Component")) : this(JLabel(title), component) {
    }

    private fun isMinimized(): Boolean {
        return toggle.isSelected()
    }

    private fun updateSize() {
        val height: Int
        val minWidth: Int
        val prefWidth: Int
        if (isMinimized()) {
            if (Arrays.asList(*components).contains(component)) {
                remove(component)
            }
            height = titleBar.getPreferredSize().height + 4
            minWidth = 20
            prefWidth = 20
        } else {
            if (!Arrays.asList(*components).contains(component)) {
                add(component)
            }
            height = component.getPreferredSize().height + titleBar.getPreferredSize().height + 4
            minWidth = component.getMinimumSize().width
            prefWidth = component.getPreferredSize().width
        }
        minimumSize = Dimension(minWidth + 4, height)
        maximumSize = Dimension(prefWidth + 4, height)
        preferredSize = Dimension(component.getPreferredSize().width + 4, height)
        revalidate()
        repaint()
    }
    //region PROPERTIES
    /**
     * Get title string
     * @return title
     */
    fun getTitle(): String? {
        return titleLabel.getText()
    }

    /**
     * Sets title string
     * @param title title of component
     */
    fun setTitle(title: String?) {
        titleLabel.setText(title)
    }

    /**
     * Get component in this panel
     * @return the component active in this panel
     */
    fun getPrimaryComponent(): Component? {
        return component
    }

    /**
     * Sets main component
     * @param c component
     */
    fun setPrimaryComponent(c: Component?) {
        if (component != null) {
            component.removePropertyChangeListener(componentSizeListener)
            remove(component)
        }
        component = c
        if (component is JComponent) {
            (component as JComponent?).setBorder(null)
        }
        component.addPropertyChangeListener(componentSizeListener)
        add(component, BorderLayout.CENTER)
        updateSize()
    }

    //endregion
    //region INNER CLASSES
    private abstract class SquareIcon internal constructor() : Icon {
        val sz: Int
        override fun getIconWidth(): Int {
            return sz
        }

        override fun getIconHeight(): Int {
            return sz
        }

        init {
            sz = ICON_SIZE
        }
    }

    private class ExpandIcon internal constructor(private val min: Boolean, private val color: Color?) : SquareIcon() {
        override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
            val g2 = g as Graphics2D?
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setColor(color)
            g2.setStroke(BasicStroke(1f))
            val hsz = .5 * sz
            if (min) {
                g2.draw(Line2D.Double(x + .3 * sz, y + 2, x + .7 * sz, y + hsz))
                g2.draw(Line2D.Double(x + .3 * sz, y + sz - 2, x + .7 * sz, y + hsz))
            } else {
                g2.draw(Line2D.Double(x + 2, y + .3 * sz, x + hsz, y + .7 * sz))
                g2.draw(Line2D.Double(x + sz - 2, y + .3 * sz, x + hsz, y + .7 * sz))
            }
        }
    }

    private class PressIcon internal constructor(private val color: Color?) : SquareIcon() {
        override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
            val g2 = g as Graphics2D?
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setColor(color)
            g2.setStroke(BasicStroke(1f))
            g2.draw(Line2D.Double(x + .65 * sz, y + .25 * sz, x + .65 * sz, y + .65 * sz))
            g2.draw(Line2D.Double(x + .25 * sz, y + .65 * sz, x + .65 * sz, y + .65 * sz))
        }
    } //endregion

    companion object {
        private val ICON_SIZE = UIManager.getFont("Label.font").size
    }

    init {
        val titleHt = titleLabel.getPreferredSize().height
        toggle = JToggleButton()
        toggle.border = null
        toggle.isBorderPainted = false
        toggle.margin = Insets(0, 0, 0, 0)
        toggle.isContentAreaFilled = false
        toggle.isFocusPainted = false
        val fg = UIManager.getColor("Label.foreground")
        val bg = UIManager.getColor("Label.background")
        val bg2 = Color(
                (fg.red + 3 * bg.red) / 4,
                (fg.green + 3 * bg.green) / 4,
                (fg.blue + 3 * bg.blue) / 4)
        val fg2 = Color(
                (fg.red * 5 + bg.red) / 6,
                (fg.green * 5 + bg.green) / 6,
                (fg.blue * 5 + bg.blue) / 6)
        titleLabel.setForeground(fg)
        toggle.icon = ExpandIcon(false, bg)
        toggle.rolloverIcon = ExpandIcon(false, fg2)
        toggle.selectedIcon = ExpandIcon(true, bg)
        toggle.rolloverSelectedIcon = ExpandIcon(true, fg2)
        toggle.pressedIcon = PressIcon(fg2)
        toggle.addItemListener(ItemListener { e: ItemEvent? -> updateSize() })
        titleBar = JPanel()
        titleBar.minimumSize = Dimension(component.getMinimumSize().width + 2, titleHt)
        titleBar.maximumSize = Dimension(component.getMaximumSize().width + 2, titleHt)
        titleBar.preferredSize = Dimension(component.getPreferredSize().width + 2, titleHt)
        titleBar.background = bg2
        titleBar.layout = BorderLayout()
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0))
        titleBar.add(toggle, BorderLayout.WEST)
        titleBar.add(titleLabel, BorderLayout.CENTER)
        border = BorderFactory.createLineBorder(titleBar.background, 2)
        layout = BorderLayout()
        add(titleBar, BorderLayout.NORTH)
        componentSizeListener = PropertyChangeListener { evt: PropertyChangeEvent? ->
            val prop = evt.getPropertyName()
            if ("size" == prop || "minimumSize" == prop || "preferredSize" == prop || "maximumSize" == prop) {
                updateSize()
            }
        }
        setPrimaryComponent(component)
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                updateSize()
            }
        })
        updateSize()
    }
}