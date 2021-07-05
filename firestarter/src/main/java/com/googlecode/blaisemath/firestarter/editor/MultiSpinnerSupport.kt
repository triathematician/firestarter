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
import java.awt.Dimension
import java.awt.GridLayout
import java.util.*
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

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
 * Base class for editors that use multiple coordinates.
 *
 * @param <N> numeric type for the spinners
 *
 * @author Elisha Peterson
</N> */
abstract class MultiSpinnerSupport<N : Number?>(iVal: Any?, vararg tips: String?) : MPanelEditorSupport() {
    protected val spinners: Array<JSpinner?>?
    private val tips: Array<String?>?
    public override fun initCustomizer() {
        val n = spinners.size
        panel = JPanel()
        panel.layout = GridLayout(1, n)
        for (i in 0 until n) {
            spinners.get(i) = JSpinner()
            spinners.get(i).setToolTipText(tips.get(i))
            spinners.get(i).setBorder(null)
            panel.add(spinners.get(i))
        }
        val cl = ChangeListener { e: ChangeEvent? ->
            val values: MutableList<N?> = ArrayList()
            for (spinner in spinners) {
                values.add(spinner.getValue() as N)
            }
            setNewValueList(values)
        }
        for (spinner in spinners) {
            spinner.addChangeListener(cl)
        }
        panel.revalidate()
        panel.repaint()
    }

    override fun initEditorValue() {
        if (panel != null) {
            initSpinnerModels()
            for (spinner in spinners) {
                val spDim = spinner.getPreferredSize()
                spinner.setPreferredSize(Dimension(Math.min(spDim.width, MAX_SPINNER_WIDTH), spDim.height))
            }
        }
    }

    /** Used by subclass to initialize its spinner models  */
    protected abstract fun initSpinnerModels()
    override fun getAsText(): String? {
        val result = StringBuilder(getValue(0).toString())
        for (i in 1 until spinners.size) {
            result.append(",").append(getValue(i))
        }
        return result.toString()
    }

    /**
     * Use a comma-delimited technique for setting as text.
     * @param s text
     */
    override fun setAsText(s: String?) {
        val splits: Array<String?> = s.split(",".toRegex()).toTypedArray()
        require(splits.size == spinners.size)
        setAsText(*splits)
    }

    abstract fun setAsText(vararg strings: String?)
    fun getValue(i: Int): N? {
        return getValue(value, i)
    }

    fun getNewValue(i: Int): N? {
        return getValue(newValue, i)
    }

    /**
     * Retrieve the indexed value for the given object.
     * @param bean the bean under consideration
     * @param i the position of the desired property
     * @return property at the given position of the bean.
     */
    protected abstract fun getValue(bean: Any?, i: Int): N?

    /**
     * Set the object with a list of values.
     * @param values the values
     */
    abstract fun setNewValueList(values: MutableList<N?>?)

    companion object {
        private const val MAX_SPINNER_WIDTH = 70
    }

    init {
        newValue = iVal
        spinners = arrayOfNulls<JSpinner?>(tips.size)
        this.tips = tips
    }
}