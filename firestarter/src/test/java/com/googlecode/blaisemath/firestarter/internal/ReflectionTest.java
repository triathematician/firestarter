package com.googlecode.blaisemath.firestarter.internal;

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

import com.googlecode.blaisemath.firestarter.property.IndexedBean;
import org.junit.Test;

import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;

import static org.junit.Assert.*;

public class ReflectionTest {

    @Test
    public void testIndexedPropertyDescriptor() {
        System.out.println("indexedPropertyDescriptor");
        IndexedPropertyDescriptor result = Reflection.indexedPropertyDescriptor(IndexedBean.class, "strings");
        assertEquals("strings", result.getDisplayName());
    }

    @Test
    public void testTryInvokeNew() {
        System.out.println("tryInvokeNew");
        Object result = Reflection.tryInvokeNew(Point.class);
        assertEquals(new Point(), result);
        
        assertEquals((Integer) 0, Reflection.tryInvokeNew(Integer.class));
        assertNull(Reflection.tryInvokeNew(int.class));
    }

    @Test
    public void testTryInvokeRead() {
        System.out.println("tryInvokeRead");
        BeanInfo info = Reflection.beanInfo(Point.class);
        assertEquals("x", info.getPropertyDescriptors()[2].getDisplayName());
        assertEquals(2.0, Reflection.tryInvokeRead(new Point(2,3), info.getPropertyDescriptors()[2]));
        assertNull(Reflection.tryInvokeRead("not a point", info.getPropertyDescriptors()[1]));
    }

    @Test
    public void testTryInvokeWrite() {
        System.out.println("tryInvokeWrite");
        BeanInfo info = Reflection.beanInfo(Point.class);
        assertEquals("x", info.getPropertyDescriptors()[2].getDisplayName());
        assertFalse(Reflection.tryInvokeWrite(new Point(2, 3), info.getPropertyDescriptors()[2], 2));
        Point p = new Point(2,3);
        Reflection.tryInvokeWrite(p, info.getPropertyDescriptors()[1], new Point(1,1));
        assertEquals(new Point(1,1), p);
    }

    @Test
    public void testTryInvokeIndexedRead() {
        System.out.println("tryInvokeIndexedRead");
        IndexedPropertyDescriptor result = Reflection.indexedPropertyDescriptor(IndexedBean.class, "strings");
        IndexedBean bean = new IndexedBean();
        assertEquals("hello", Reflection.tryInvokeIndexedRead(bean, result, 0));
    }

    @Test
    public void testTryInvokeIndexedWrite() {
        System.out.println("tryInvokeIndexedWrite");
    }
    
}
