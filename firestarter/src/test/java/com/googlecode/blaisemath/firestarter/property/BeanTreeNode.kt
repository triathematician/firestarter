package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.firestarter.editor.EditorRegistration
import com.googlecode.blaisemath.firestarter.internal.Reflection
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import java.beans.BeanInfo
import java.beans.IndexedPropertyDescriptor
import java.beans.PropertyDescriptor
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.tree.DefaultMutableTreeNode

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
 * Adds the sub-properties of a bean object (that do
 * not have registered editors) as children of the tree.
 *
 * @author Elisha Peterson
 */
internal class BeanTreeNode(bean: Any?) : DefaultMutableTreeNode(bean) {
    /** Object of this class.  */
    var bean: Any? = null

    /** The info of the bean.  */
    var info: BeanInfo? = null

    /** Property descriptors.  */
    var descriptors: MutableList<PropertyDescriptor?>? = null

    /** Creates and adds child nodes, which are the "non-terminal" properties of the bean.  */
    fun addSubNodes() {
        descriptors.stream().filter(BeanPropertyFilter.STANDARD).forEach { descriptor: PropertyDescriptor? ->
            if (descriptor is IndexedPropertyDescriptor) {
                try {
                    // add all array elements
                    val elements = descriptor.getReadMethod().invoke(bean) as Array<Any?>
                    for (e in elements) {
                        add(BeanTreeNode(e))
                    }
                } catch (ex: IllegalAccessException) {
                    LOG.log(Level.SEVERE, "Read failed", ex)
                } catch (ex: IllegalArgumentException) {
                    LOG.log(Level.SEVERE, "Read failed", ex)
                } catch (ex: InvocationTargetException) {
                    LOG.log(Level.SEVERE, "Read failed", ex)
                }
            } else if (!BeanPropertyFilter.STANDARD.test(descriptor)) {
                // add only elements not supporting custom editors
                val editor = EditorRegistration.getEditor(bean, descriptor)
                if (!editor.supportsCustomEditor()) {
                    add(BeanTreeNode(editor.value))
                }
            }
        }
    }

    companion object {
        private val LOG = Logger.getLogger(BeanTreeNode::class.java.name)
    }

    init {
        if (bean != null) {
            this.bean = bean
            info = Reflection.beanInfo(bean.javaClass)
            println("adding node: $bean")
            descriptors = Arrays.asList(*info.getPropertyDescriptors())
            addSubNodes()
        }
    }
}