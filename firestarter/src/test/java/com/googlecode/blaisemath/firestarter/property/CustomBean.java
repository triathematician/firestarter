package com.googlecode.blaisemath.firestarter.property;

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

import java.awt.Color;
import java.awt.Font;

class CustomBean {

    private Color color = Color.BLACK;
    private Color color2 = Color.YELLOW;
    private Boolean boo = true;
    private boolean boo2 = true;
    private String string = "string";
    private Font font = new Font("", Font.PLAIN, 10);
    private NumberBean nb = new NumberBean();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color) {
        this.color2 = color;
    }

    public Boolean getBoo() {
        return boo;
    }

    public void setBoo(Boolean boo) {
        this.boo = boo;
    }

    public boolean getBoo2() {
        return boo;
    }

    public void setBoo2(boolean boo) {
        this.boo = boo;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public NumberBean getBean() {
        return nb;
    }

    public void setBean(NumberBean nb) {
        this.nb = nb;
    }
    
}
