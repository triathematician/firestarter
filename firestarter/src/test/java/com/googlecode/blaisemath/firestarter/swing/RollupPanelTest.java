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

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * Test for RollupPanel refresh behavior.
 * 
 * @author Elisha Peterson
 */
public class RollupPanelTest {
    
    @Test
    public void testAddComponent() {
        System.out.println("addComponent");
        RollupPanel instance = new RollupPanel();
        
        // Initially should have 1 component (the vertical spacer)
        assertEquals(1, instance.getComponentCount());
        
        // Add a component
        JLabel label = new JLabel("Test");
        instance.add(label, "Test Label");
        
        // Should now have 2 components (spacer + the MPanel wrapper)
        assertEquals(2, instance.getComponentCount());
        
        // The second component should be wrapped in an MPanel
        Component secondComp = instance.getComponent(1);
        assertTrue(secondComp instanceof MPanel);
        MPanel mPanel = (MPanel) secondComp;
        assertEquals(label, mPanel.getPrimaryComponent());
        assertEquals("Test Label", mPanel.getTitle());
    }
    
    @Test
    public void testAddMPanel() {
        System.out.println("addMPanel");
        RollupPanel instance = new RollupPanel();
        
        // Add an MPanel directly
        MPanel mPanel = new MPanel("Direct", new JLabel("Direct content"));
        instance.add(mPanel);
        
        // Should have 2 components (spacer + the MPanel)
        assertEquals(2, instance.getComponentCount());
        
        // The second component should be the MPanel we added
        assertEquals(mPanel, instance.getComponent(1));
    }
    
    @Test
    public void testRemoveComponent() {
        System.out.println("removeComponent");
        RollupPanel instance = new RollupPanel();
        
        JLabel label = new JLabel("Test");
        instance.add(label, "Test Label");
        assertEquals(2, instance.getComponentCount());
        
        // Remove the component
        instance.remove(label);
        
        // Should be back to just the spacer
        assertEquals(1, instance.getComponentCount());
    }
    
    @Test
    public void testRemoveMPanel() {
        System.out.println("removeMPanel");
        RollupPanel instance = new RollupPanel();
        
        MPanel mPanel = new MPanel("Direct", new JLabel("Direct content"));
        instance.add(mPanel);
        assertEquals(2, instance.getComponentCount());
        
        // Remove the MPanel
        instance.remove(mPanel);
        
        // Should be back to just the spacer
        assertEquals(1, instance.getComponentCount());
    }
    
    @Test
    public void testRefreshBehaviorOnAdd() {
        System.out.println("testRefreshBehaviorOnAdd");
        
        // Create a test RollupPanel that tracks revalidate/repaint calls
        TestableRollupPanel instance = new TestableRollupPanel();
        
        // Reset counters
        instance.resetCounters();
        
        // Add a component
        instance.add(new JLabel("Test"), "Test");
        
        // Verify that revalidate and repaint were called
        assertTrue("revalidate() should be called after adding component", instance.revalidateCallCount > 0);
        assertTrue("repaint() should be called after adding component", instance.repaintCallCount > 0);
    }
    
    @Test
    public void testRefreshBehaviorOnRemove() {
        System.out.println("testRefreshBehaviorOnRemove");
        
        // Create a test RollupPanel that tracks revalidate/repaint calls  
        TestableRollupPanel instance = new TestableRollupPanel();
        
        JLabel label = new JLabel("Test");
        instance.add(label, "Test");
        
        // Reset counters after adding
        instance.resetCounters();
        
        // Remove the component
        instance.remove(label);
        
        // Verify that revalidate and repaint were called
        assertTrue("revalidate() should be called after removing component", instance.revalidateCallCount > 0);
        assertTrue("repaint() should be called after removing component", instance.repaintCallCount > 0);
    }
    
    @Test
    public void testRefreshBehaviorOnRemoveByIndex() {
        System.out.println("testRefreshBehaviorOnRemoveByIndex");
        
        TestableRollupPanel instance = new TestableRollupPanel();
        instance.add(new JLabel("Test"), "Test");
        
        // Reset counters after adding
        instance.resetCounters();
        
        // Remove by index (component at index 1, since 0 is the spacer)
        instance.remove(1);
        
        // Verify that revalidate and repaint were called
        assertTrue("revalidate() should be called after removing by index", instance.revalidateCallCount > 0);
        assertTrue("repaint() should be called after removing by index", instance.repaintCallCount > 0);
    }
    
    @Test
    public void testRefreshBehaviorOnRemoveAll() {
        System.out.println("testRefreshBehaviorOnRemoveAll");
        
        TestableRollupPanel instance = new TestableRollupPanel();
        instance.add(new JLabel("Test1"), "Test1");
        instance.add(new JLabel("Test2"), "Test2");
        
        // Reset counters after adding
        instance.resetCounters();
        
        // Remove all components
        instance.removeAll();
        
        // Should be back to just the spacer
        assertEquals(1, instance.getComponentCount());
        
        // Verify that revalidate and repaint were called
        assertTrue("revalidate() should be called after removeAll", instance.revalidateCallCount > 0);
        assertTrue("repaint() should be called after removeAll", instance.repaintCallCount > 0);
    }
    
    /**
     * Test class that extends RollupPanel to track revalidate/repaint calls
     */
    private static class TestableRollupPanel extends RollupPanel {
        int revalidateCallCount = 0;
        int repaintCallCount = 0;
        
        @Override
        public void revalidate() {
            revalidateCallCount++;
            super.revalidate();
        }
        
        @Override
        public void repaint() {
            repaintCallCount++;
            super.repaint();
        }
        
        void resetCounters() {
            revalidateCallCount = 0;
            repaintCallCount = 0;
        }
    }
}