package io.github.kaztakgh.settingsview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Spinnerで選択項目を表示
 *
 * @property keyword View上の設定項目の識別子。省略不可。
 * @property title タイトル。省略不可。
 * @property iconId drawableにある画像ID。使用しない場合は省略する。
 * @property enabled 選択可能であるか。デフォルトはtrue。
 * @property options 選択項目。文字列のみ指定可。
 * @property select 現在選択中のインデックス
 * @property focusable 選択した操作があるか。このパラメータは通常記述しない。
 */
@Parcelize
data class SpinnerChoice (
    override val keyword: String,
    override val title: String,
    val iconId: Int? = null,
    var enabled: Boolean = true,
    val options: Array<String>,
    var select: Int = 0,
    var focusable: Boolean = false
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
        // 選択項目がない場合はエラー
        if (options.isEmpty()) {
            throw IllegalArgumentException("選択肢がありません")
        }
        // 選択肢が選択項目を参照できない場合、エラー
        if (select < 0 || select >= options.size) {
            throw IndexOutOfBoundsException("参照が不可能です")
        }

        // focusableは初期状態ではfalseで固定
        focusable = false
    }
    constructor(
        keyword: String,
        title: String,
        iconId: Int?,
        enabled: Boolean,
        options: Array<String>,
        select: Int
    ) : this(keyword, title, iconId, enabled, options, select, false)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpinnerChoice

        if (keyword != other.keyword) return false
        if (title != other.title) return false
        if (iconId != other.iconId) return false
        if (enabled != other.enabled) return false
        if (!options.contentEquals(other.options)) return false
        if (focusable != other.focusable) return false
        return select == other.select

    }

    override fun hashCode(): Int {
        var result = keyword.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (iconId ?: 0)
        result = 31 * result + enabled.hashCode()
        result = 31 * result + options.contentHashCode()
        result = 31 * result + select
        result = 31 * result + focusable.hashCode()
        return result
    }
}