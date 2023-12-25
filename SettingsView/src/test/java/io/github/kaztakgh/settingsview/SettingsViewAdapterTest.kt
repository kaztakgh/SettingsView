package io.github.kaztakgh.settingsview

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsViewAdapterTest {
    /**
     * Adapterで取得可能な要素数が入力したListと一致することを確認
     */
    @Test
    fun returnCorrectItemsCount() {
        val inputItems = ArrayList<SettingItems>()
        inputItems.add(ToggleSwitch(title = "Test1", keyword = "Test1"))
        inputItems.add(ToggleSwitch(title = "Test2", keyword = "Test2"))
        val adapter = SettingsViewAdapter(inputItems)
        assertEquals(inputItems.count(), adapter.itemCount)
    }

    /**
     * keywordが重複する要素がある場合はエラー扱いにすることを確認
     */
    @Test
    fun inputDuplicateKeyword() {
        val exception = Assert.assertThrows(IllegalArgumentException::class.java) {
            val inputItems = ArrayList<SettingItems>()
            inputItems.add(ToggleSwitch(title = "Test1", keyword = "Test1"))
            inputItems.add(ToggleSwitch(title = "Test1", keyword = "Test1"))
            SettingsViewAdapter(inputItems)
        }
        assertEquals("keywordは要素間で重複しないように設定してください", exception.message)
    }
}