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
import org.junit.BeforeClassimport
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import java.util.Timer
import javax.swing.*

java.awt.*import java.awt.event.*
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
 * Editor for fonts.
 */
class FontEditor : MPanelEditorSupport() {
    private var familyNameCombo: JComboBox<String?>? = null
    private var fontSizeCombo: JComboBox<String?>? = null
    private var styleCombo: JComboBox<Int?>? = null
    private var editing = false
    public override fun initCustomizer() {
        panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        initializeComboBoxes()
        val p = JPanel(GridBagLayout())
        p.background = panel.background
        val gbc = GridBagConstraints()
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        gbc.insets = Insets(0, 1, 0, 1)
        p.add(familyNameCombo, gbc)
        gbc.weightx = .3
        p.add(fontSizeCombo, gbc)
        gbc.weightx = .5
        p.add(styleCombo, gbc)
        panel.add(p)
    }

    /**
     * Creates the ComboBoxes. The fons and point sizes must be initialized.
     */
    private fun initializeComboBoxes() {
        if (fontsLoaded) {
            familyNameCombo = JComboBox<String?>(FONTS.toArray<Any?>(Array<Any?> { _Dummy_.__Array__() }))
        } else {
            familyNameCombo = JComboBox(arrayOf<String?>("<html><i>Loading fonts...</i>"))
            familyNameCombo.setEnabled(false)
        }
        familyNameCombo.setMinimumSize(Dimension(60, 0))
        familyNameCombo.setAlignmentX(Component.CENTER_ALIGNMENT)
        familyNameCombo.setAlignmentY(Component.CENTER_ALIGNMENT)
        fontSizeCombo = JComboBox()
        for (ptSize in PT_SIZES) {
            fontSizeCombo.addItem("" + ptSize)
        }
        fontSizeCombo.setMinimumSize(Dimension(40, 0))
        fontSizeCombo.setAlignmentX(Component.CENTER_ALIGNMENT)
        fontSizeCombo.setAlignmentY(Component.CENTER_ALIGNMENT)
        styleCombo = JComboBox(arrayOf<Int?>(Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC))
        styleCombo.setRenderer(StyleComboRenderer())
        styleCombo.setMinimumSize(Dimension(50, 0))
        styleCombo.setAlignmentX(Component.CENTER_ALIGNMENT)
        styleCombo.setAlignmentY(Component.CENTER_ALIGNMENT)
        val comboListener = ActionListener { e: ActionEvent? -> handleComboChange() }
        familyNameCombo.addActionListener(comboListener)
        fontSizeCombo.addActionListener(comboListener)
        styleCombo.addActionListener(comboListener)
        loadFonts()
    }

    private fun handleComboChange() {
        if (editing) {
            return
        }
        val family = if (familyNameCombo.getSelectedIndex() == -1) if (newValue == null) "Dialog" else (newValue as Font).family else familyNameCombo.getSelectedItem() as String
        val size = if (fontSizeCombo.getSelectedIndex() == -1) 12 else PT_SIZES.get(fontSizeCombo.getSelectedIndex())
        val style = if (styleCombo.getSelectedIndex() == -1) Font.PLAIN else styleCombo.getSelectedItem() as Int
        setNewValue(Font(family, style, size))
        initEditorValue()
    }

    /** Loads font names in a background thread so GUI isn't blocked  */
    @Synchronized
    private fun loadFonts() {
        if (fontsLoaded || loadStarted) {
            return
        }
        loadStarted = true
        val t = Timer()
        t.schedule(object : TimerTask() {
            override fun run() {
                loadFontsInBackground()
                t.cancel()
            }
        }, 0)
    }

    private fun loadFontsInBackground() {
        val ff = GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames
        synchronized(FONTS) {
            FONTS.addAll(Arrays.asList(*ff))
            if (newValue != null) {
                SwingUtilities.invokeLater {
                    familyNameCombo.setModel(DefaultComboBoxModel<String?>(FONTS.toArray<Any?>(Array<Any?> { _Dummy_.__Array__() })))
                    familyNameCombo.setEnabled(true)
                    familyNameCombo.setSelectedItem((newValue as Font).family)
                }
            }
            fontsLoaded = true
            panel.revalidate()
            panel.repaint()
        }
    }

    override fun isPaintable(): Boolean {
        return true
    }

    override fun paintValue(g: Graphics?, rect: Rectangle?) {
        val oldFont = g.getFont()
        g.setFont(value as Font)
        val fm = g.getFontMetrics()
        val pad = (rect.height - fm.ascent) / 2
        g.drawString("Abcde", 0, rect.height - pad)
        g.setFont(oldFont)
    }

    override fun getJavaInitializationString(): String? {
        val font = value as Font
        return "new java.awt.Font(\"" + font.family + "\", " +
                font.style + ", " + font.size + ")"
    }

    override fun initEditorValue() {
        if (panel == null) {
            return
        }
        editing = true
        if (newValue == null) {
            familyNameCombo.setSelectedIndex(-1)
            fontSizeCombo.setSelectedIndex(-1)
            styleCombo.setSelectedIndex(-1)
        } else {
            val font = newValue as Font
            familyNameCombo.setSelectedItem(font.family)
            styleCombo.setSelectedItem(font.style)
            for (i in PT_SIZES.indices) {
                if (font.size <= PT_SIZES.get(i)) {
                    fontSizeCombo.setSelectedIndex(i)
                    break
                }
            }
            panel.revalidate()
            panel.repaint()
        }
        editing = false
    }

    private class StyleComboRenderer : DefaultListCellRenderer() {
        override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component? {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
            val iVal = value as Int? ?: return this
            text = when (iVal) {
                Font.PLAIN -> "Plain"
                Font.BOLD -> "<html><font style=\"color:black\"><b>Bold</b></font>"
                Font.ITALIC -> "<html><font style=\"color:black\"><i>Italic</i></font>"
                Font.BOLD + Font.ITALIC -> "<html><font style=\"color:black\"><b><i>Bold Italic</i></b></font>"
                else -> throw IllegalStateException()
            }
            return this
        }
    }

    companion object {
        /** List of font sizes to show in dropdown  */
        private val PT_SIZES: IntArray? = intArrayOf(3, 5, 8, 9, 10, 11, 12, 14, 18, 24, 36, 48, 72, 96, 108, 120)

        /** Static list of fonts. Will be loaded only once.  */
        private val FONTS: MutableList<String?>? = ArrayList()
        private var loadStarted = false
        private var fontsLoaded = false
    }
}