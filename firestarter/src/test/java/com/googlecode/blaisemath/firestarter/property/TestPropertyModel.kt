/*
 * Copyright 2015 elisha.
 *
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
 */
package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
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
 *
 * @author elisha
 */
class TestPropertyModel(types: MutableMap<String?, Class<*>?>?, map: MutableMap<String?, Any?>?) : PropertyModelSupport() {
    /** Properties to edit  */
    private val props: MutableList<String?>?

    /** Types of the properties  */
    private val types: MutableMap<String?, Class<*>?>?

    /** Map being edited  */
    private val map: MutableMap<String?, Any?>?
    override fun getSize(): Int {
        return props.size
    }

    override fun getElementAt(index: Int): String? {
        return props.get(index)
    }

    override fun getPropertyType(row: Int): Class<*>? {
        return types.get(props.get(row))
    }

    override fun isWritable(row: Int): Boolean {
        return true
    }

    override fun getPropertyValue(row: Int): Any? {
        return map.get(props.get(row))
    }

    override fun setPropertyValue(row: Int, value: Any?) {
        map[props.get(row)] = value
    }

    init {
        props = ArrayList(types.keys)
        this.types = types
        this.map = map
    }
}