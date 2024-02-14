package io.github.kaztakgh.settingsview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class NumericalSelectorTest {
    /**
     * 識別子に空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyKeyword() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            NumericalSelector(
                keyword = "",
                title = "Test1",
                iconId = null,
                enabled = true,
                state = 50,
                max = 100,
                min = 0,
                divine = 1,
                unit = "%"
            )
        }
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    /**
     * タイトルに空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyTitle() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            NumericalSelector(
                keyword = "Test1",
                title = "",
                iconId = null,
                enabled = true,
                state = 50,
                max = 100,
                min = 0,
                divine = 1,
                unit = "%"
            )
        }
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    /**
     * 最大値よりも大きいstateを入力した場合、エラーになることを確認
     */
    @Test
    fun inputOverMaximumState() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            NumericalSelector(
                keyword = "Test2",
                title = "Test2",
                iconId = null,
                enabled = true,
                state = 51,
                max = 50,
                min = 0
            )
        }
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    /**
     * 最小値よりも小さいstateを入力した場合、エラーになることを確認
     */
    @Test
    fun inputUnderMinimumState() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            NumericalSelector(
                keyword = "Test3",
                title = "Test3",
                iconId = null,
                enabled = true,
                state = 0,
                max = 50,
                min = 1
            )
        }
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    /**
     * 配列にない値をstateに入力した場合、エラーになることを確認
     */
    @Test
    fun inputOutOfValuesRange() {
        val valuesArray = IntArray(5) { (it + 1) * 10 }
        val exception = assertThrows(IndexOutOfBoundsException::class.java) {
            NumericalSelector(
                keyword = "Test3",
                title = "Test3",
                iconId = null,
                enabled = true,
                state = 0,
                unit = "m",
                paramsArray = valuesArray
            )
        }
        assertEquals(true, exception.message!!.isNotEmpty())
    }
}