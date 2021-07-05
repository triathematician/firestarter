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
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import javax.swing.border.LineBorder
import javax.swing.colorchooser.AbstractColorChooserPanel

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
 * Modified from the standard color swatch chooser.
 *
 * @author Elisha Peterson (updated)
 */
class SwatchChooserPanel(private val chooser: ColorEditor?, private val popup: ChooserComboPopup?) : AbstractColorChooserPanel() {
    private var swatchPanel: SwatchPanel? = null
    private var recentSwatchPanel: RecentSwatchPanel? = null
    private var mainSwatchListener: MouseListener? = null
    override fun getDisplayName(): String? {
        return UIManager.getString("ColorChooser.swatchesNameText")
    }

    override fun getSmallDisplayIcon(): Icon? {
        return null
    }

    override fun getLargeDisplayIcon(): Icon? {
        return null
    }

    public override fun buildChooser() {
        val superHolder = JPanel()
        superHolder.layout = BoxLayout(superHolder, BoxLayout.Y_AXIS)
        swatchPanel = MainSwatchPanel()
        swatchPanel.getAccessibleContext().accessibleName = displayName
        recentSwatchPanel = RecentSwatchPanel()
        recentSwatchPanel.getAccessibleContext().accessibleName = RECENT_STR
        mainSwatchListener = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                val color = swatchPanel.getColorForLocation(e.getX(), e.getY())
                chooser.setNewValue(color)
                chooser.initEditorValue()
                recentSwatchPanel.setMostRecentColor(color)
                popup.setVisible(false)
            }
        }
        swatchPanel.addMouseListener(mainSwatchListener)
        recentSwatchPanel.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                val color = recentSwatchPanel.getColorForLocation(e.getX(), e.getY())
                chooser.setNewValue(color)
                chooser.initEditorValue()
                popup.setVisible(false)
            }
        })
        val mainHolder = JPanel(BorderLayout())
        val border: Border = CompoundBorder(LineBorder(Color.black),
                LineBorder(Color.white))
        mainHolder.border = border
        mainHolder.add(swatchPanel, BorderLayout.CENTER)
        val recentHolder = JPanel(BorderLayout())
        recentHolder.border = border
        recentHolder.add(recentSwatchPanel, BorderLayout.CENTER)
        superHolder.add(recentHolder)
        superHolder.add(Box.createRigidArea(Dimension(0, 3)))
        superHolder.add(mainHolder)
        add(superHolder)
    }

    override fun uninstallChooserPanel(enclosingChooser: JColorChooser?) {
        super.uninstallChooserPanel(enclosingChooser)
        swatchPanel.removeMouseListener(mainSwatchListener)
        swatchPanel = null
        mainSwatchListener = null
        removeAll()
    }

    override fun updateChooser() {
        // no need to do anything here
    }

    private open class SwatchPanel internal constructor() : JPanel() {
        var colors: Array<Color?>?
        val swatchSize: Dimension? = Dimension(12, 12)
        var numSwatches: Dimension? = null
        var gap: Dimension? = null
        open fun initValues() {
            // hook method for sub-classes
        }

        public override fun paintComponent(g: Graphics?) {
            g.setColor(background)
            g.fillRect(0, 0, width, height)
            for (row in 0 until numSwatches.height) {
                for (column in 0 until numSwatches.width) {
                    g.setColor(getColorForCell(column, row))
                    val x = column * (swatchSize.width + gap.width)
                    val y = row * (swatchSize.height + gap.height)
                    g.fillRect(x, y, swatchSize.width, swatchSize.height)
                    g.setColor(Color.black)
                    g.drawLine(x + swatchSize.width - 1, y, x + swatchSize.width - 1, y + swatchSize.height - 1)
                    g.drawLine(x, y + swatchSize.height - 1, x + swatchSize.width - 1, y + swatchSize.width - 1)
                }
            }
        }

        override fun getPreferredSize(): Dimension? {
            val x = numSwatches.width * (swatchSize.width + gap.width) - 1
            val y = numSwatches.height * (swatchSize.height + gap.height) - 1
            return Dimension(x, y)
        }

        open fun initColors() {
            // hook method for sub-classes
        }

        override fun getToolTipText(e: MouseEvent?): String? {
            val color = getColorForLocation(e.getX(), e.getY())
            return color.getRed().toString() + ", " + color.getGreen() + ", " + color.getBlue()
        }

        fun getColorForLocation(x: Int, y: Int): Color? {
            val column = x / (swatchSize.width + gap.width)
            val row = y / (swatchSize.height + gap.height)
            return getColorForCell(column, row)
        }

        private fun getColorForCell(column: Int, row: Int): Color? {
            return colors.get(row * numSwatches.width + column)
        }

        /** Sets up the panel  */
        init {
            initValues()
            initColors()
            toolTipText = ""
            isOpaque = true
            isFocusable = false
        }
    }

    private class RecentSwatchPanel : SwatchPanel() {
        protected override fun initValues() {
            numSwatches = Dimension(16, 1)
            gap = Dimension(1, 1)
        }

        protected override fun initColors() {
            val defaultRecentColor = UIManager.getColor("ColorChooser.swatchesDefaultRecentColor")
            val numColors = numSwatches.width * numSwatches.height
            colors = arrayOfNulls<Color?>(numColors)
            for (i in 0 until numColors) {
                colors.get(i) = defaultRecentColor
            }
        }

        fun setMostRecentColor(c: Color?) {
            System.arraycopy(colors, 0, colors, 1, colors.size - 1)
            colors.get(0) = c
            repaint()
        }
    }

    private class MainSwatchPanel : SwatchPanel() {
        protected override fun initValues() {
            numSwatches = Dimension(16, 7)
            gap = Dimension(1, 1)
        }

        protected override fun initColors() {
            colors = arrayOfNulls<Color?>(numSwatches.width * numSwatches.height)
            var i = 0
            for (j in 0..15) {
                colors.get(i++) = color(255, 255, 255, j, 16)
            }
            i = addColorSequence(colors, 255, 0, 0, i)
            i = addColorSequence(colors, 0, 255, 0, i)
            i = addColorSequence(colors, 0, 0, 255, i)
            i = addColorSequence(colors, 255, 0, 255, i)
            i = addColorSequence(colors, 0, 255, 255, i)
            addColorSequence(colors, 255, 255, 0, i)
        }
    }

    companion object {
        private val RECENT_STR = UIManager.getString("ColorChooser.swatchesRecentText")
        private fun addColorSequence(arr: Array<Color?>?, r: Int, g: Int, b: Int, i0: Int): Int {
            var i = i0
            for (j in 0..7) {
                arr.get(i++) = color(r, g, b, j, 8)
            }
            for (j in 7 downTo 0) {
                arr.get(i++) = color2(255 - r, 255 - g, 255 - b, j, 8)
            }
            return i
        }

        private fun color(r: Int, g: Int, b: Int, i: Int, n: Int): Color? {
            return Color((r * i / (n - 1.0)) as Int, (g * i / (n - 1.0)) as Int, (b * i / (n - 1.0)) as Int)
        }

        private fun color2(r: Int, g: Int, b: Int, i: Int, n: Int): Color? {
            return Color(255 - (r * i / (n - 1.0)) as Int, 255 - (g * i / (n - 1.0)) as Int, 255 - (b * i / (n - 1.0)) as Int)
        }
    }
}