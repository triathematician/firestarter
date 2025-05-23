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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Number parse utilities
 * 
 * @author elisha
 */
public class Numbers {

    private static final Logger LOG = Logger.getLogger(Numbers.class.getName());
    
    // utility class
    private Numbers() {
    }

    /** 
     * Attempt to decode text in an array as integers.
     * @param arr array of text
     * @return decoded integers
     */
    public static int[] decodeAsIntegers(String... arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            try {
                res[i] = Integer.decode(arr[i]);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Not an integer", ex);
                res[i] = 0;
            }
        }
        return res;
    }

    /** 
     * Attempt to decode text in an array as doubles.
     * @param arr array of text
     * @return decoded doubles
     */
    public static double[] decodeAsDoubles(String... arr) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            try {
                res[i] = Double.parseDouble(arr[i]);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Not a double", ex);
                res[i] = 0;
            }
        }
        return res;
    }
    
}
