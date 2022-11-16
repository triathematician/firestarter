package com.googlecode.blaisemath.firestarter.property;

/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2022 Elisha Peterson
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

import com.google.common.base.MoreObjects;

public class NumberBean {

    private Byte byte1 = -5;
    private Long long1 = 199823844L;
    private double double1 = 2.0;
    private int int1 = 4;
    private float float1 = 1f;
    private short short1 = 3;

    public Byte getNByte() {
        return byte1;
    }

    public void setNByte(Byte NByte) {
        this.byte1 = NByte;
    }

    public Long getNLong() {
        return long1;
    }

    public void setNLong(Long NLong) {
        this.long1 = NLong;
    }

    public double getNDouble() {
        return double1;
    }

    public void setNDouble(double n) {
        this.double1 = n;
    }

    public float getNFloat() {
        return float1;
    }

    public void setNFloat(float n) {
        this.float1 = n;
    }

    public short getNShort() {
        return short1;
    }

    public void setNShort(short n) {
        this.short1 = n;
    }

    public int getNInteger() {
        return int1;
    }

    public void setNInteger(int n) {
        this.int1 = n;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("byte1", byte1)
                .add("long1", long1)
                .add("double1", double1)
                .add("int1", int1)
                .add("float1", float1)
                .add("short1", short1)
                .toString();
    }
}
