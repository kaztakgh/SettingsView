/**
 * @file ToggleSwitch.kt
 */
package io.github.kaztakgh.settingsview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * スイッチで内容のON/OFFを切り替える項目
 *
 * @property keyword View上の設定項目の識別子。省略不可。
 * @property title タイトル。省略不可。
 * @property iconId drawableにある画像ID。使用しない場合は省略する。
 * @property enabled 選択可能であるか。デフォルトはtrue。
 * @property checked スイッチの状態。デフォルトはfalse。
 * @property textOnTrue ONの時に表示するテキスト。省略時はONと表示する。
 * @property textOnFalse OFFの時に表示するテキスト。省略時はOFFと表示する。
 */
@Parcelize
data class ToggleSwitch(
    override val keyword: String,
    override val title: String,
    val iconId: Int? = null,
    var enabled: Boolean = true,
    var checked: Boolean = false,
    val textOnTrue: String = "ON",
    val textOnFalse: String = "OFF"
) : SettingItems, Parcelable {
    init {
        // 省略不可のパラメータチェック
        // タイトル・識別子は空文字無効
        if (keyword.isEmpty() || keyword.isBlank()) {
            throw IllegalArgumentException("識別子は必須です")
        }
        if (title.isEmpty() || title.isBlank()) {
            throw IllegalArgumentException("タイトルは必須です")
        }
    }
}
