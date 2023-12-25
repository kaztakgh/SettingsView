/**
 * @file SettingItems.kt
 */
package io.github.kaztakgh.settingsview

sealed interface SettingItems {
    /**
     * View上の設定項目の識別子
     */
    val keyword: String

    /**
     * タイトル
     */
    val title: String
}