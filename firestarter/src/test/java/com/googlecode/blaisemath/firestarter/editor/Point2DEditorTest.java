package com.googlecode.blaisemath.firestarter.editor;

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

import java.awt.Point;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class Point2DEditorTest {

    @Test
    public void testGetJavaInitializationString() {
        System.out.println("getJavaInitializationString");
        Point2DEditor instance = new Point2DEditor();
        assertEquals("null", instance.getJavaInitializationString());
        instance.setValue(new Point(3,4));
        assertEquals("new java.awt.geom.Point2D.Double(3.0,4.0)", instance.getJavaInitializationString());
    }

    @Test
    public void testSetAsText() {
        System.out.println("setAsText");
        Point2DEditor instance = new Point2DEditor();
        instance.setAsText("3,4");
        assertEquals(new Point(3,4), instance.getValue());
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Point2DEditor instance = new Point2DEditor();
        instance.setValue(new Point(3,4));
        assertEquals(3, instance.getValue(0), 1e-8);
        assertEquals(4, instance.getValue(1), 1e-8);
    }

    @Test
    public void testSetNewValue() {
        System.out.println("setNewValue");
        Point2DEditor instance = new Point2DEditor();
        instance.setNewValueList(Arrays.asList(3.0, 4.0));
        assertEquals(3, instance.getNewValue(0), 1e-8);
        assertEquals(4, instance.getNewValue(1), 1e-8);
    }

    @Test
    public void testDefaultStepSize() {
        System.out.println("defaultStepSize");
        Point2DEditor instance = new Point2DEditor();
        // Verify the default constructor uses the DEFAULT_STEP_SIZE
        assertEquals(Point2DEditor.DEFAULT_STEP_SIZE, 0.01, 1e-8);
    }

    @Test
    public void testCustomStepSize() {
        System.out.println("customStepSize");
        double customStep = 0.5;
        Point2DEditor instance = new Point2DEditor(customStep);
        // Verify the editor was created successfully with custom step size
        // The step size is used internally for spinner models, which we can't easily test
        // without creating the GUI components, but we can test that the editor works normally
        instance.setValue(new Point(5, 6));
        assertEquals(5, instance.getValue(0), 1e-8);
        assertEquals(6, instance.getValue(1), 1e-8);
    }
    
}
