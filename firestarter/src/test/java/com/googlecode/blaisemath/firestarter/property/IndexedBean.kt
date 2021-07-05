package com.googlecode.blaisemath.firestarter.property

import com.googlecode.blaisemath.app.ApplicationMenuConfig
import com.googlecode.blaisemath.ui.PropertyActionPanel
import org.apache.commons.math.stat.descriptive.SummaryStatistics

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
*/ /**
 *
 *
 * `IndexedBean` ...
 *
 *
 * @author Elisha Peterson
 */
class IndexedBean {
    private var string: Array<String?>? = arrayOf("hello", "me", "you", "everyone")
    fun getStrings(): Array<String?>? {
        return string
    }

    fun setStrings(string: Array<String?>?) {
        this.string = string
    }

    fun getStrings(index: Int): String? {
        return string.get(index)
    }

    fun setStrings(index: Int, newString: String?) {
        string.get(index) = newString
    }

    private var ports: Array<Int?>? = arrayOf(80, 443)
    fun getPorts(): Array<Int?>? {
        return ports
    }

    fun setPorts(ports: Array<Int?>?) {
        this.ports = ports
    }

    fun getPorts(index: Int): Int? {
        return ports.get(index)
    }

    fun setPorts(index: Int, port: Int?) {
        ports.get(index) = port
    }

    private var nb: Array<NumberBean?>? = arrayOf(NumberBean(), NumberBean())
    fun getNbs(): Array<NumberBean?>? {
        return nb
    }

    fun setNbs(nb: Array<NumberBean?>?) {
        this.nb = nb
    }

    fun getNbs(index: Int): NumberBean? {
        return nb.get(index)
    }

    fun setNbs(index: Int, nb: NumberBean?) {
        this.nb.get(index) = nb
    }

    private var myEnum: TestEnum? = TestEnum.YO
    fun getMyEnum(): TestEnum? {
        return myEnum
    }

    fun setMyEnum(myEnum: TestEnum?) {
        this.myEnum = myEnum
    }

    enum class TestEnum(val i: Int, val s: String?) {
        HI(1, "hi"), YO(2, "yo"), DUDE(28, "dude");
    }

    class Indexed2 {
        var inBean: IndexedBean?

        /**
         * Get the value of inBean
         *
         * @return the value of inBean
         */
        fun getInBean(): IndexedBean? {
            return inBean
        }

        /**
         * Set the value of inBean
         *
         * @param inBean new value of inBean
         */
        fun setInBean(inBean: IndexedBean?) {
            this.inBean = inBean
        }

        init {
            inBean = IndexedBean()
        }
    }

    companion object {
        /** Test method to retrieve an instance of this class subject to the provided enum value.  */
        fun getInstance(value: TestEnum?): IndexedBean? {
            val result = IndexedBean()
            result.string.get(0) = value.s
            result.string.get(1) = Integer.toString(value.i)
            return result
        }
    }
}