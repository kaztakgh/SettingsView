/**
 * @file NumericalSelector.kt
 */
package io.github.kaztakgh.settingsview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * SeekBarで数値を指定するアイテムの作成
 *
 * @property keyword タグ(識別子、省略不可)
 * @property title タイトル(省略不可)
 * @property iconId 左側に表示するアイコン(省略時null)
 * @property enabled 選択可能であるか(省略時true)
 * @property state バーの初期値(省略時0)
 * @property max バーの最大値(省略時100)
 * @property min バーの最小値(省略時0)
 * @property divine バーの分割値(省略時1)
 * @property unit 単位(省略時無し)
 * @property paramsArray 数値の配列。このプロパティを指定した場合、バーの値の指定方法が配列に準拠する。
 */
@Parcelize
class NumericalSelector (
    override val keyword: String,
    override val title: String,
    val iconId: Int? = null,
    var enabled: Boolean = true,
    var state: Int = 0,
    val max: Int = 100,
    val min: Int = 0,
    val divine: Int = 1,
    val unit: String = "",
    val paramsArray: IntArray? = null
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
        // 対象範囲外の数値は指定不可
        if (state < min || state > max) {
            throw IllegalArgumentException("範囲外の値を設定することはできません")
        }
        if (paramsArray != null && !paramsArray.contains(state)) {
            throw IndexOutOfBoundsException("入力した初期値は選択できません")
        }
    }

    /**
     * @constructor シークバーで連続する数値を選択する場合に利用する
     *
     * @property keyword タグ(識別子、省略不可)
     * @property title タイトル(省略不可)
     * @property iconId 左側に表示するアイコン(省略時null)
     * @property enabled 選択可能であるか(省略時true)
     * @property state バーの初期値(省略時0)
     * @property max バーの最大値(省略時100)
     * @property min バーの最小値(省略時0)
     * @property divine バーの分割値(省略時1)
     * @property unit 単位(省略時無し)
     */
    constructor(
        keyword: String,
        title: String,
        iconId: Int?,
        enabled: Boolean,
        state: Int,
        max: Int,
        min: Int,
        divine: Int,
        unit: String
    ) : this(keyword, title, iconId, enabled, state, max, min, divine, unit, null)

    /**
     * @constructor 配列によって決められた値からシークバーで選択する場合に利用する
     *
     * @property keyword タグ(識別子、省略不可)
     * @property title タイトル(省略不可)
     * @property iconId 左側に表示するアイコン(省略時null)
     * @property enabled 選択可能であるか(省略時true)
     * @property state バーの初期値(省略時0)
     * @property unit 単位(省略時無し)
     * @property paramsArray 数値の配列。このプロパティを指定した場合、バーの値の指定方法が配列に準拠する。
     */
    constructor(
        keyword: String,
        title: String,
        iconId: Int?,
        enabled: Boolean,
        state: Int,
        unit: String,
        paramsArray: IntArray?
    ) : this(keyword, title, iconId, enabled, state, 100, 0, 1, unit, paramsArray)
}