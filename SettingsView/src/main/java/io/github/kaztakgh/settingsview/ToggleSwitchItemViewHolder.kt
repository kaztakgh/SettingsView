/**
 * @file ToggleSwitchItemViewHolder.kt
 */
package io.github.kaztakgh.settingsview

import android.view.View
import com.google.android.material.switchmaterial.SwitchMaterial

/**
 * layout_switchを使用する設定項目の表示内容を決定する
 *
 * @constructor 通常のアイテムのレイアウトを呼び出す
 *
 * @param view viewのレイアウトリソース
 * @property title タイトル
 * @property text テキスト
 * @property enabled 利用可能な状態であるか
 * @property checked スイッチのON/OFFの状態
 * @property textOnTrue スイッチがONの時の表示テキスト
 * @property textOnFalse スイッチがOFFの時の表示テキスト
 */
class ToggleSwitchItemViewHolder(view: View)
    : NormalItemViewHolder(view) {
    companion object {
        /**
         * スイッチがONの時のテキスト(デフォルト)
         */
        const val STATE_ON_TEXT_DEFAULT = "ON"
        /**
         * スイッチがOFFの時のテキスト(デフォルト)
         */
        const val STATE_OFF_TEXT_DEFAULT = "OFF"
    }
    private val swChecked : SwitchMaterial = view.findViewById(R.id.swChecked)

    /**
     * 選択可能状態
     */
    override var enabled: Boolean
        get() = swChecked.isEnabled
        set(value) {
            super.enabled
            swChecked.isClickable = value
            swChecked.isEnabled = value
        }

    /**
     * スイッチの状態
     */
    var checked: Boolean
        get() = swChecked.isChecked
        set(value) {
            // チェック状態によるスイッチとテキストの変更
            swChecked.isChecked = value
            text = if (value) textOnTrue else textOnFalse
        }

    /**
     * ONの時のテキスト
     */
    var textOnTrue: String = STATE_ON_TEXT_DEFAULT
        set(value) {
            // 空文字ではない場合のみ登録
            if (value.isNotEmpty() && value.isNotBlank()) field = value
        }

    /**
     * OFFの時のテキスト
     */
    var textOnFalse: String = STATE_OFF_TEXT_DEFAULT
        set(value) {
            // 空文字ではない場合のみ登録
            if (value.isNotEmpty() && value.isNotBlank()) field = value
        }

    /**
     * スイッチの要素取得
     *
     * 他の要素に影響を与える機能を持つため、clickListenerをAdapterで記述する
     *
     * @return com.google.android.material.switchmaterial.SwitchMaterial
     */
    fun getSwitchComponent() : SwitchMaterial = swChecked
}