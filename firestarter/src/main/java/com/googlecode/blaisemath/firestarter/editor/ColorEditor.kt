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
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Level
import java.util.logging.Logger
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
 * Provides multiple controls for editing a color.
 *
 * @author Elisha Peterson
 */
class ColorEditor : MPanelEditorSupport() {
    private var color = Color.black
    private var rgbaValue: JTextField? = null
    private var colorChooserCombo: ChooserComboButton? = null
    public override fun initCustomizer() {
        panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        rgbaValue = JTextField()
        rgbaValue.setBorder(null)
        rgbaValue.setPreferredSize(RGB_DIM)
        rgbaValue.setMaximumSize(RGB_DIM)
        rgbaValue.setMinimumSize(RGB_DIM)
        rgbaValue.setAlignmentX(Component.CENTER_ALIGNMENT)
        rgbaValue.setAlignmentY(Component.CENTER_ALIGNMENT)
        panel.add(rgbaValue)
        panel.add(Box.createRigidArea(Dimension(5, 0)))
        colorChooserCombo = ChooserComboButton()
        colorChooserCombo.setPreferredSize(CC_DIM)
        colorChooserCombo.setMaximumSize(CC_DIM)
        colorChooserCombo.setMinimumSize(CC_DIM)
        colorChooserCombo.setAlignmentX(Component.CENTER_ALIGNMENT)
        colorChooserCombo.setAlignmentY(Component.CENTER_ALIGNMENT)
        panel.add(colorChooserCombo)
        panel.add(Box.createRigidArea(Dimension(5, 0)))
        val table = UIManager.getDefaults()
        table[COLOR_ICON] = LookAndFeel.makeIcon(javaClass, "resources/ColorIcon.gif")
        table[COLOR_PRESSED_ICON] = LookAndFeel.makeIcon(javaClass, "resources/ColorPressedIcon.gif")
        val colorIcon = UIManager.getIcon(COLOR_ICON)
        val colorPressedIcon = UIManager.getIcon(COLOR_PRESSED_ICON)
        val colorChooserButton = JButton(colorIcon)
        colorChooserButton.pressedIcon = colorPressedIcon
        colorChooserButton.toolTipText = "press to bring up color chooser"
        colorChooserButton.margin = CC_INSETS
        colorChooserButton.isBorderPainted = false
        colorChooserButton.isContentAreaFilled = false
        colorChooserButton.alignmentX = Component.CENTER_ALIGNMENT
        colorChooserButton.alignmentY = Component.CENTER_ALIGNMENT
        panel.add(colorChooserButton)
        rgbaValue.addActionListener(ActionListener { e: ActionEvent? ->
            try {
                setNewValue(getColor(rgbaValue.getText()))
                initEditorValue()
            } catch (ex: IllegalArgumentException) {
                LOG.log(Level.FINE, "Not a color: " + rgbaValue.getText(), ex)
                JOptionPane.showMessageDialog(panel.parent, ex.toString())
            }
        })
        colorChooserButton.addActionListener { e: ActionEvent? ->
            color = JColorChooser.showDialog(panel.parent, "Color Chooser", color)
            if (color != null) {
                setNewValue(color)
                initEditorValue()
            }
        }
    }

    override fun isPaintable(): Boolean {
        return true
    }

    override fun paintValue(g: Graphics?, rect: Rectangle?) {
        val g2 = g as Graphics2D?
        g2.setColor(Color.white)
        g2.fill(rect)
        if (color.alpha != 255) {
            g2.setColor(Color(color.red, color.green, color.blue))
            g2.fill(Rectangle(rect.x, rect.y, rect.width, rect.height / 2))
            g2.setColor(color)
            g2.fill(Rectangle(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2))
        } else {
            g2.setColor(color)
            g2.fill(rect)
        }
        val outline = Color.black
        val bs = if (color.alpha != 255) BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, floatArrayOf(2f), 0.0f) else BasicStroke()
        g2.setColor(outline)
        g2.setStroke(bs)
        g2.draw(Rectangle(rect.x, rect.y, rect.width - 1, rect.height - 1))
    }

    override fun getJavaInitializationString(): String? {
        return "new java.awt.Color($asText)"
    }

    override fun getAsText(): String? {
        return getAsText(value as Color)
    }

    fun getAsText(c: Color?): String? {
        val result = c.getRed().toString() + "," + c.getGreen() + "," + c.getBlue()
        return if (c.getAlpha() == 255) {
            result
        } else {
            result + "," + c.getAlpha()
        }
    }

    override fun setAsText(s: String?) {
        value = getColor(s)
    }

    public override fun initEditorValue() {
        val c = newValue as Color
        if (c == null) {
            if (panel != null) {
                rgbaValue.setText("")
                colorChooserCombo.setBackground(panel.background)
            }
            return
        }
        color = c
        if (panel != null) {
            rgbaValue.setText(getAsText(c))
            colorChooserCombo.setBackground(c)
        }
    }

    /** combo-like rect button  */
    private inner class ChooserComboButton internal constructor() : JButton("") {
        val popup: ChooserComboPopup?
        public override fun paintComponent(g: Graphics?) {
            paintValue(g, Rectangle(0, 0, width, height))
        }

        init {
            popup = ChooserComboPopup(this@ColorEditor)
            addMouseListener(object : MouseAdapter() {
                override fun mouseReleased(e: MouseEvent?) {
                    popup.show(e.getComponent(), 0, 0)
                }
            })
            popup.addMouseListener(object : MouseAdapter() {
                override fun mouseReleased(e: MouseEvent?) {
                    setNewValue(background)
                    initEditorValue()
                }
            })
        }
    }

    companion object {
        private val LOG = Logger.getLogger(ColorEditor::class.java.name)
        private val COLOR_ICON: String? = "beaninfo.ColorIcon"
        private val COLOR_PRESSED_ICON: String? = "beaninfo.ColorPressedIcon"
        private val SQ_DIM: Dimension? = Dimension(15, 15)
        private val RGB_DIM: Dimension? = Dimension(100, 15)
        private val CC_DIM: Dimension? = Dimension(16, 16)
        private val CC_INSETS: Insets? = Insets(0, 0, 0, 0)
        fun getColor(s: String?): Color? {
            val spl: Array<String?> = s.split(",".toRegex()).toTypedArray()
            if (spl.size < 3 || spl.size > 4) {
                Logger.getLogger(ColorEditor::class.java.name).log(Level.WARNING,
                        "Invalid color {0}", s)
                return null
            }
            return try {
                val r = Math.max(0, Math.min(255, spl[0].toInt()))
                val g = Math.max(0, Math.min(255, spl[1].toInt()))
                val b = Math.max(0, Math.min(255, spl[2].toInt()))
                val a = if (spl.size == 4) Math.max(0, Math.min(255, spl[3].toInt())) else 255
                Color(r, g, b, a)
            } catch (x: NumberFormatException) {
                Logger.getLogger(ColorEditor::class.java.name).log(Level.WARNING,
                        "Invalid color $s", x)
                null
            }
        }
    }

    init {
        setNewValue(Color(0, 0, 0))
    }
}