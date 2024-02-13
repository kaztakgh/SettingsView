package io.github.kaztakgh.settingsview

import android.view.View
import android.widget.Spinner

/**
 * 状態をスピナーで指定するアイテムのビューの表示に使用する
 *
 * @constructor
 * スピナーを使用するアイテムのレイアウトを呼び出す
 *
 * @param view viewのレイアウトリソース
 */
internal class SpinnerChoiceItemViewHolder(view: View)
    : NormalItemViewHolder(view) {
    private val spinner: Spinner = view.findViewById(R.id.spnOptions)
    init {
        // テキストに該当する部分は表示しない
        tvText.visibility = View.GONE
    }

    /**
     * 選択可能であるか
     */
    override var enabled: Boolean
        get() {
            return this.spinner.isEnabled
        }
        set(value) {
            // 選択状態
            val alpha: Float = if (value) 1.0f else 0.5f
            tvTitle.alpha = alpha

            // 選択不可能である場合はスピナーの操作をすることが出来ないようにする
            spinner.isEnabled = value
            spinner.isClickable = value
        }

    /**
     * スピナー要素の取得
     * 他の要素に影響を与える機能を持つため、clickListenerをAdapterで記述する
     *
     * @return Spinner
     */
    fun getOptionsSpinner(): Spinner = spinner
}