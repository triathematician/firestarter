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
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.swing.AbstractListModel

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
 * Maintains an array of [PropertyDescriptor]s, as well
 * as a vector representing a filtered version of this array.
 *
 * @param <O> the type of the elements of this model
 *
 * @author Elisha Peterson
</O> */
class FilteredListModel<O> : AbstractListModel<O?>() {
    protected var unfilteredItems: MutableList<O?>? = ArrayList()
    private val filterItems: MutableList<O?>? = ArrayList()

    /** Stores the present filter value.  */
    protected var filter: Predicate<O?>? = null

    //region PROPERTIES
    fun getUnfilteredItems(): MutableList<O?>? {
        return Collections.unmodifiableList(unfilteredItems)
    }

    /**
     * Set the unfiltered items
     * @param items properties
     */
    fun setUnfilteredItems(items: MutableList<O?>?) {
        unfilteredItems = ArrayList(items)
        refilter()
    }

    /**
     * Get current filter.
     * @return current filter value.
     */
    fun getFilter(): Predicate<O?>? {
        return filter
    }

    /**
     * Set current filter
     * @param filter the new filter value.
     */
    fun setFilter(filter: Predicate<O?>?) {
        if (this.filter !== filter) {
            this.filter = filter
            refilter()
        }
    }

    //endregion
    override fun getSize(): Int {
        return filterItems.size
    }

    override fun getElementAt(index: Int): O? {
        return if (index < filterItems.size) filterItems.get(index) else null
    }

    /** Re-filters the list of properties based on the current criteria.  */
    protected fun refilter() {
        if (filter != null) {
            val unsorted: MutableSet<O?>? = unfilteredItems.stream()
                    .filter(filter).collect(Collectors.toCollection { LinkedHashSet() })
            filterItems.clear()
            filterItems.addAll(unsorted)
        } else {
            filterItems.clear()
            filterItems.addAll(unfilteredItems)
        }
        fireContentsChanged(this, 0, size + 1)
    }
}