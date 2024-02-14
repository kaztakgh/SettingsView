package io.github.kaztakgh.settingsview

/**
 * アイテムの状態が変更されたときの処理
 *
 * SettingsViewAdapterで使用する
 *
 * 定義はActivity/Fragmentで行う
 */
interface OnSeekBarStateChangeListener {
    /**
     * シークバーの値を変更した後の処理
     *
     * @param adapter [SettingsViewAdapter]
     * @param progress シークバーのプログレス
     */
    fun onItemValueChange(
        adapter: SettingsViewAdapter,
        progress: Int
    )
}