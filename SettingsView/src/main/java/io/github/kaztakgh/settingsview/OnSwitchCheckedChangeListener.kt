/**
 * @file OnSwitchCheckedChangeListener.kt
 */
package io.github.kaztakgh.settingsview

/**
 * 設定項目内のスイッチが変更されたときの処理
 *
 * ToggleSwitchの項目があるかつスイッチの切り替えの他に追加する処理がある場合に使用する
 *
 * Activity/Fragmentで動作を記述
 */
interface OnSwitchCheckedChangeListener {
    /**
     * スイッチ切り替え時の追加処理
     *
     * @param position adapterで対応する設定項目の位置
     * @param checked スイッチの状態
     */
    fun onCheckedChange(
        position: Int,
        checked: Boolean
    )
}