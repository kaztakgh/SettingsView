/**
 * @file NumericalSelectorItemViewHolder.kt
 */
package io.github.kaztakgh.settingsview

import android.os.Build
import android.view.View
import android.widget.SeekBar

/**
 * 状態を数値バーで指定するアイテムのビューの表示に使用する
 *
 * @constructor
 * シークバーを使用するアイテムのレイアウトを呼び出す
 *
 * @param view viewのレイアウトリソース
 */
class NumericalSelectorItemViewHolder(view: View)
    : NormalItemViewHolder(view) {
    private val sbState: SeekBar = view.findViewById(R.id.sbState)

    /**
     * 選択可能状態
     */
    override var enabled: Boolean = true
        get() = super.enabled
        set(value) {
            field = value
            // 選択可能であるかどうかを透明度で示す
            // 選択可能な場合は全てのパーツを不透明、選択不可能な場合は全てのパーツを半透明に変更する
            val alpha: Float = if (value) 1.0f else 0.5f
            tvTitle.alpha = alpha
            tvText.alpha = alpha
            if (ivIcon.visibility == View.VISIBLE)
                ivIcon.alpha = alpha

            // 選択不可能の場合はシークバーの操作を行えないようにする
            sbState.isEnabled = value
        }

    /**
     * バーのプログレス
     *
     * 初期値の設定が必要
     */
    var state: Int
        get() = sbState.progress
        set(value) {
            sbState.progress = value
        }

    /**
     * バーの最大値
     *
     * 初期値の設定が必要
     */
    var max: Int
        get() = sbState.max
        set(value) {
            sbState.max = value
        }

    /**
     * バーの最小値
     *
     * Oreoよりも前のバージョンでは値を変更できない
     */
    var min: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) sbState.min else 0
        set(value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sbState.min = value
            }
        }

    /**
     * 数値バーの取得
     *
     * 他の要素に影響を与える機能を持つため、変更中の処理をAdapterで記述する
     */
    fun getSeekBar(): SeekBar = sbState
}