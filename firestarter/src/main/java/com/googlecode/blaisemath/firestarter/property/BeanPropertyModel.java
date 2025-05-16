package com.googlecode.blaisemath.firestarter.property;

/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2025 Elisha Peterson
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

import com.googlecode.blaisemath.firestarter.swing.FilteredListModel;
import com.googlecode.blaisemath.firestarter.internal.Reflection;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.function.Predicate;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Uses bean information about an object, gathered by introspection, to provide
 * editable attributes of that object.
 * A filter may be supplied to limit the properties made available by the model.
 *
 * @author Elisha Peterson
 */
public final class BeanPropertyModel extends PropertyModelSupport {

    /** Object of this class. */
    private final Object bean;
    /** Filtered items */
    private final FilteredListModel<PropertyDescriptor> filteredProperties;

    /**
     * Constructs for specified bean.
     * @param bean the underlying object.
     */
    public BeanPropertyModel(Object bean) {
        this.bean = bean;

        filteredProperties = new FilteredListModel<>();
        filteredProperties.setFilter(BeanPropertyFilter.STANDARD);
        filteredProperties.addListDataListener(new ListDataListener(){
            @Override
            public void intervalAdded(ListDataEvent e) {
                fireIntervalAdded(BeanPropertyModel.this, e.getIndex0(), e.getIndex1());
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fireIntervalRemoved(BeanPropertyModel.this, e.getIndex0(), e.getIndex1());
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                fireContentsChanged(BeanPropertyModel.this, e.getIndex0(), e.getIndex1());
            }
        });
        
        BeanInfo info = Reflection.beanInfo(bean.getClass());
        filteredProperties.setUnfilteredItems(Arrays.asList(info.getPropertyDescriptors()));
    }
    
    /** 
     * Get the bean object represented by this class.
     * @return the underlying object.
     */
    public Object getBean() {
        return bean;
    }

    public Predicate<PropertyDescriptor> getFilter() {
        return filteredProperties.getFilter();
    }

    public void setFilter(Predicate<PropertyDescriptor> filter) {
        filteredProperties.setFilter(filter);
    }

    @Override
    public int getSize() {
        return filteredProperties.getSize();
    }

    @Override
    public String getElementAt(int row) {
        return getPropertyDescriptor(row).getDisplayName();
    }
    
    public PropertyDescriptor getPropertyDescriptor(int row) {
        return filteredProperties.getElementAt(row);
    }

    @Override
    public Class<?> getPropertyType(int row) {
        PropertyDescriptor pd = getPropertyDescriptor(row);
        return pd instanceof IndexedPropertyDescriptor
                ? Object[].class : pd.getPropertyType();
    }

    @Override
    public boolean isWritable(int row) {
        PropertyDescriptor pd = getPropertyDescriptor(row);
        return pd.getWriteMethod() != null;
    }

    @Override
    public Object getPropertyValue(int row) {
        PropertyDescriptor pd = getPropertyDescriptor(row);
        return Reflection.tryInvokeRead(bean, pd);
    }

    @Override
    public void setPropertyValue(int row, Object value) {
        PropertyDescriptor pd = getPropertyDescriptor(row);
        Object cur = Reflection.tryInvokeRead(bean, pd);
        if (Reflection.tryInvokeWrite(bean, pd, value)) {
            pcs.firePropertyChange(getElementAt(row), cur, value);
        }
    }

}
