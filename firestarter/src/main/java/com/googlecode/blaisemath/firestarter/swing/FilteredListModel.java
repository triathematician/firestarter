package com.googlecode.blaisemath.firestarter.swing;

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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toCollection;
import javax.swing.AbstractListModel;

/**
 * Maintains an array of {@link PropertyDescriptor}s, as well
 * as a vector representing a filtered version of this array.
 * 
 * @param <O> the type of the elements of this model
 *
 * @author Elisha Peterson
 */
public class FilteredListModel<O> extends AbstractListModel<O> {

    protected List<O> unfilteredItems = new ArrayList<>();
    private final List<O> filterItems = new ArrayList<>();
    
    /** Stores the present filter value. */
    protected Predicate<O> filter = null;
    
    //region PROPERTIES

    public List<O> getUnfilteredItems() {
        return Collections.unmodifiableList(unfilteredItems);
    }
    
    /**
     * Set the unfiltered items
     * @param items properties
     */
    public void setUnfilteredItems(List<O> items) {
        this.unfilteredItems = new ArrayList<>(items);
        refilter();
    }
    
    /** 
     * Get current filter.
     * @return current filter value. 
     */
    public Predicate<O> getFilter() {
        return filter;
    }

    /** 
     * Set current filter
     * @param filter the new filter value. 
     */
    public void setFilter(Predicate<O> filter) {
        if (this.filter != filter) {
            this.filter = filter;
            refilter();
        }
    }
    
    //endregion

    @Override
    public int getSize() {
        return filterItems.size();
    }

    @Override
    public O getElementAt(int index) {
        return index < filterItems.size() ? filterItems.get(index) : null;
    }

    /** Re-filters the list of properties based on the current criteria. */
    protected final void refilter() {
        if (filter != null) {
            Set<O> unsorted = unfilteredItems.stream()
                    .filter(filter).collect(toCollection(LinkedHashSet::new));
            filterItems.clear();
            filterItems.addAll(unsorted);
        } else {
            filterItems.clear();
            filterItems.addAll(unfilteredItems);
        }
        fireContentsChanged(this, 0, getSize()+1);
    }

}
