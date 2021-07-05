package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics
import org.junit.BeforeClassimport
import java.awt.geom.Point2Dimport

java.awt.*import java.awt.geom.Point2Dimport

java.awt.geom.Rectangle2D
/*
 * #%L
 * Firestarter
 * --
 * Copyright (C) 2009 - 2019 Elisha Peterson
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
 */   class PointBean {
    private var dim: Dimension? = Dimension(20, 500)
    private var insets: Insets? = Insets(2, 34, 4, -10)
    private var rect: Rectangle? = Rectangle(5, -3, 30, 50)
    private var rect2: Rectangle2D.Double? = Rectangle2D.Double(5, -3, 30, 50)
    private var p2d: Point2D.Double? = Point2D.Double(3.0, 5.0)
    private var p2: Point2D? = Point2D.Double(3.0, 5.0)
    private var point: Point? = Point(4, 5)
    fun getDim(): Dimension? {
        return dim
    }

    fun setDim(dim: Dimension?) {
        this.dim = dim
    }

    fun getInsets(): Insets? {
        return insets
    }

    fun setInsets(insets: Insets?) {
        this.insets = insets
    }

    fun getRect(): Rectangle? {
        return rect
    }

    fun setRect(rect: Rectangle?) {
        this.rect = rect
    }

    fun getRect2(): Rectangle2D.Double? {
        return rect2
    }

    fun setRect2(rect2: Rectangle2D.Double?) {
        this.rect2 = rect2
    }

    fun getPoint(): Point? {
        return point
    }

    fun setPoint(point: Point?) {
        this.point = point
    }

    fun getP2d(): Point2D.Double? {
        return p2d
    }

    fun setP2d(p2d: Point2D.Double?) {
        this.p2d = p2d
    }

    fun getP2(): Point2D? {
        return p2
    }

    fun setP2(p2: Point2D?) {
        this.p2 = p2
    }
}