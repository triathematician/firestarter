package com.googlecode.blaisemath.firestarter.property

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
import java.beans.PropertyDescriptor
import java.util.function.Predicate

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
 * Encodes possible filters for bean patterns.
 */
enum class BeanPropertyFilter : Predicate<PropertyDescriptor?> {
    /**
     * Matches non-expert, non-hidden patterns with read and write methods
     */
    STANDARD {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.getWriteMethod() != null && pd.getReadMethod() != null && !pd.isExpert() && !pd.isHidden()
        }

        override fun toString(): String? {
            return "standard properties (read and write)"
        }
    },

    /**
     * Matches non-expert, non-hidden patterns with read or write methods
     */
    STANDARD_READ_OR_WRITE {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return (pd.getWriteMethod() != null || pd.getReadMethod() != null) && !pd.isExpert() && !pd.isHidden()
        }

        override fun toString(): String? {
            return "standard properties (read or write)"
        }
    },

    /**
     * Matches preferred patterns
     */
    PREFERRED {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.isPreferred()
        }

        override fun toString(): String? {
            return "preferred properties"
        }
    },

    /**
     * Matches expert patterns
     */
    EXPERT {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.isExpert()
        }

        override fun toString(): String? {
            return "expert properties"
        }
    },

    /**
     * Matches all patterns.
     */
    ALL {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return true
        }

        override fun toString(): String? {
            return "all properties"
        }
    },

    /**
     * Matches all patterns without a write method
     */
    READ_ONLY {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.getWriteMethod() == null
        }

        override fun toString(): String? {
            return "read-only properties"
        }
    },

    /**
     * Matches bound patterns with a write method
     */
    BOUND {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.isBound() && pd.getWriteMethod() != null
        }

        override fun toString(): String? {
            return "bound properties"
        }
    },

    /**
     * Matches constrained patterns with a write method
     */
    CONSTRAINED {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.isConstrained() && pd.getWriteMethod() != null
        }

        override fun toString(): String? {
            return "constrained properties"
        }
    },

    /**
     * Matches hidden patterns
     */
    HIDDEN {
        override fun test(pd: PropertyDescriptor?): Boolean {
            return pd.isHidden()
        }

        override fun toString(): String? {
            return "hidden properties"
        }
    };

    companion object {
        /**
         * Converts string filter to a property filter, using the property name.
         * @param propertyFilter filter
         * @return predicate
         */
        fun byName(propertyFilter: Predicate<String?>?): Predicate<PropertyDescriptor?>? {
            return Predicate { input: PropertyDescriptor? -> propertyFilter == null || propertyFilter.test(input.getName()) }
        }
    }
}