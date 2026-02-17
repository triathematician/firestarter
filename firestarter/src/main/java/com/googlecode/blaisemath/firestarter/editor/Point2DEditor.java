package com.googlecode.blaisemath.firestarter.editor;

/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2026 Elisha Peterson
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

import com.googlecode.blaisemath.firestarter.internal.Numbers;

import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.SpinnerNumberModel;

/**
 * Edits a single Point2D with event handling support.
 *
 * @author Elisha Peterson
 */
public class Point2DEditor extends MultiSpinnerSupport<Double> {

    @SuppressWarnings("CanBeFinal")
    public static double DEFAULT_STEP_SIZE = 0.01;

    private final double stepSize;

    public Point2DEditor() {
        this(DEFAULT_STEP_SIZE);
    }

    /**
     * Creates a Point2DEditor with a custom step size for the spinners.
     * 
     * @param stepSize the increment value for the spinner controls
     */
    public Point2DEditor(double stepSize) {
        super(new Point2D.Double(), "x", "y");
        this.stepSize = stepSize;
    }
    
    @Override
    public String getJavaInitializationString() {
        Object value = getValue();
        return value != null ? "new java.awt.geom.Point2D.Double(" + 
                getAsText() + ")" : "null";
    }

    @Override
    public void setAsText(String... s) {
        double[] arr = Numbers.decodeAsDoubles(s);
        setValue(new Point2D.Double(arr[0], arr[1]));
    }

    @Override
    protected void initSpinnerModels() {
        spinners[0].setModel(new SpinnerNumberModel((Number) getNewValue(0), -Double.MAX_VALUE, Double.MAX_VALUE, stepSize));
        spinners[1].setModel(new SpinnerNumberModel((Number) getNewValue(1), -Double.MAX_VALUE, Double.MAX_VALUE, stepSize));
    }
    
    @Override
    public Double getValue(Object bean, int i) {
        if (bean == null) {
            return 0.0;
        }
        switch (i) {
            case 0:
                return ((Point2D) bean).getX();
            case 1:
                return ((Point2D) bean).getY();
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    void setNewValueList(List<Double> values) {
        setNewValue(new Point2D.Double(values.get(0), values.get(1)));
    }
}
