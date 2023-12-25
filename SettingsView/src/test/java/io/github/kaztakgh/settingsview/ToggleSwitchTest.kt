package io.github.kaztakgh.settingsview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ToggleSwitchTest {
    /**
     * 識別子に空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyKeyword() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ToggleSwitch(
                keyword = "",
                title = "test1",
                iconId = null,
                enabled = true,
                checked = false,
                textOnTrue = "ON",
                textOnFalse = "OFF"
            )
        }
        assertEquals("識別子は必須です", exception.message)
    }

    /**
     * タイトルに空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyTitle() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ToggleSwitch(
                keyword = "test1",
                title = "",
                iconId = null,
                enabled = true,
                checked = false,
                textOnTrue = "ON",
                textOnFalse = "OFF"
            )
        }
        assertEquals("タイトルは必須です", exception.message)
    }

    // isParcelableのtestは設定項目が複雑で分かり次第の試験追加になる
}