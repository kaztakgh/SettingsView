/**
 * @file NormalItemViewHolder.kt
 */
package io.github.kaztakgh.settingsview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * layout_normalを使用する設定項目の表示内容を決定する
 *
 * @constructor 通常のアイテムのレイアウトを呼び出す
 *
 * @param view viewのレイアウトリソース
 * @property title タイトル
 * @property text テキスト
 * @property enabled 利用可能な状態であるか
 */
open class NormalItemViewHolder(view: View)
    : RecyclerView.ViewHolder(view) {
    /**
     * アイテムをクリックしたときのリスナー
     */
    interface ItemClickListener {
        /**
         * アイテムをクリックしたときの処理
         *
         * @param view レイアウトビュー
         * @param position アダプター内のアイテムの位置
         */
        fun onItemClick(view: View, position: Int)
    }
    open val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    open val tvText: TextView = view.findViewById(R.id.tvText)
    open val ivIcon: ImageView = view.findViewById(R.id.ivIcon)

    /**
     * タイトル
     */
    var title : String
        get() = tvTitle.text.toString()
        set(value) {
            // 空文字列は設定しない
            if (value.isEmpty() || value.isBlank()) return
            tvTitle.text = value
        }

    /**
     * テキスト
     */
    var text : String
        get() = tvText.text.toString()
        set(value) {
            tvText.text = value
        }

    /**
     * 選択可能状態
     */
    open var enabled: Boolean = true
        set(value) {
            field = value
            // 選択可能であるかどうかを透明度で示す
            // 選択可能な場合は全てのパーツを不透明、選択不可能な場合は全てのパーツを半透明に変更する
            val alpha: Float = if (value) 1.0f else 0.5f
            tvTitle.alpha = alpha
            tvText.alpha = alpha
            if (ivIcon.visibility == View.VISIBLE)
                ivIcon.alpha = alpha
        }

    /**
     * resourcesを利用してアイコンを表示する
     *
     * @param id 画像ID or null
     */
    fun setIconFromDrawableId(id: Int?) = if (id != null) {
        ivIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, id, null))
    }
    else {
        ivIcon.visibility = View.GONE
    }

    /**
     * アイテムをクリックしたときのリスナー
     */
    open var itemClickListener: ItemClickListener? = null
}