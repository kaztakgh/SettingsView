package io.github.kaztakgh.settingsview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SpinnerChoiceTest {
    /**
     * 識別子に空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyKeyword() {
        val optionsArray = arrayOf("test1", "test2", "test3")
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SpinnerChoice(
                keyword = "",
                title = "Title1",
                iconId = null,
                enabled = true,
                options = optionsArray,
                select = 0
            )
        }
        assertEquals(false, exception.message.isNullOrEmpty())
    }

    /**
     * タイトルに空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyTitle() {
        val optionsArray = arrayOf("test1", "test2", "test3")
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SpinnerChoice(
                keyword = "keyword1",
                title = "",
                iconId = null,
                enabled = true,
                options = optionsArray,
                select = 0
            )
        }
        assertEquals(false, exception.message.isNullOrEmpty())
    }

    /**
     * 選択肢が存在しない場合、エラーになることを確認
     */
    @Test
    fun inputEmptySelectOptions() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SpinnerChoice(
                keyword = "keyword1",
                title = "Title1",
                iconId = null,
                enabled = true,
                options = arrayOf(),
                select = 0
            )
        }
        assertEquals(false, exception.message.isNullOrEmpty())
    }

    /**
     * 初期選択値にマイナスの値を入力するとエラーになることを確認
     */
    @Test
    fun selectNegativeIndex() {
        val optionsArray = arrayOf("test1", "test2", "test3")
        val exception = assertThrows(IndexOutOfBoundsException::class.java) {
            SpinnerChoice(
                keyword = "keyword1",
                title = "Title1",
                iconId = null,
                enabled = true,
                options = optionsArray,
                select = -1
            )
        }
        assertEquals(false, exception.message.isNullOrEmpty())
    }

    /**
     * 選択肢の数よりも大きい選択値を入力するとエラーになることを確認
     */
    @Test
    fun selectOutOfOptionsIndex() {
        val optionsArray = arrayOf("test1", "test2", "test3")
        val exception = assertThrows(IndexOutOfBoundsException::class.java) {
            SpinnerChoice(
                keyword = "keyword1",
                title = "Title1",
                iconId = null,
                enabled = true,
                options = optionsArray,
                select = 3
            )
        }
        assertEquals(false, exception.message.isNullOrEmpty())
    }
}