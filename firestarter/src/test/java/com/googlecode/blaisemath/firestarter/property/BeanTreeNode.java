package com.googlecode.blaisemath.firestarter.property;

/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2021 Elisha Peterson
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
 */

import com.googlecode.blaisemath.firestarter.editor.EditorRegistration;
import com.googlecode.blaisemath.firestarter.internal.Reflection;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Adds the sub-properties of a bean object (that do
 * not have registered editors) as children of the tree.
 *
 * @author Elisha Peterson
 */
final class BeanTreeNode extends DefaultMutableTreeNode {

    private static final Logger LOG = Logger.getLogger(BeanTreeNode.class.getName());

    /** Object of this class. */
    Object bean;
    /** The info of the bean. */
    BeanInfo info;
    /** Property descriptors. */
    List<PropertyDescriptor> descriptors;

    public BeanTreeNode(Object bean) {
        super(bean);
        if (bean != null) {
            this.bean = bean;
            info = Reflection.beanInfo(bean.getClass());
            System.out.println("adding node: " + bean);
            descriptors = Arrays.asList(info.getPropertyDescriptors());
            addSubNodes();
        }
    }

    /** Creates and adds child nodes, which are the "non-terminal" properties of the bean. */
    void addSubNodes() {
        descriptors.stream().filter(BeanPropertyFilter.STANDARD).forEach(descriptor -> {
            if (descriptor instanceof IndexedPropertyDescriptor) {
                try {
                    // add all array elements
                    Object[] elements = (Object[]) descriptor.getReadMethod().invoke(bean);
                    for (Object e : elements) {
                        add(new BeanTreeNode(e));
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.log(Level.SEVERE, "Read failed", ex);
                }
            } else if (!BeanPropertyFilter.STANDARD.test(descriptor)) {
                // add only elements not supporting custom editors
                PropertyEditor editor = EditorRegistration.getEditor(bean, descriptor);
                if (!editor.supportsCustomEditor()) {
                    add(new BeanTreeNode(editor.getValue()));
                }
            }
        });
    }
}
