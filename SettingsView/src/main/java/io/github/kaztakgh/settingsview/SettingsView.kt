/**
 * @file SettingsView.kt
 */
package io.github.kaztakgh.settingsview

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * SettingItemsを表示するView
 *
 * RecyclerViewを利用して表示する
 *
 * adapterにSettingsViewAdapterを指定して利用する
 *
 * @constructor Context、AttributeSet(null可)
 *
 * @property items SettingItemsの配列
 * @property fragment Fragmentから表示する場合は指定すること。Activityから表示する場合は不要。
 * @property fragmentManager fragmentと同様にFragmentから表示する場合のみ指定
 */
class SettingsView : RecyclerView {
    private var items = ArrayList<SettingItems>()
    var fragment: Fragment? = null
    var fragmentManager: FragmentManager? = null

    /**
     * 表示用のアダプター
     */
//    private var displayAdapter: SettingsViewAdapter? = null

    /**
     * 画面上で見える一番上の要素の順番(0開始)
     *
     * アクティビティの復帰対策で使用
     */
    private var topItemPosition: Int = 0

    /**
     * 画面上の一番上の要素について、スクロールでの移動量
     *
     * アクティビティの復帰対策で使用
     */
    private var scrollPositionOffset: Int = 0

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        // レイアウトの設定
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = VERTICAL
        layoutManager = linearLayoutManager
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // fragmentについては後で追加する

        // 区切り線のセット
        val itemDecoration = DividerItemDecoration(context, VERTICAL)
        addItemDecoration(itemDecoration)

        // スクロール位置調節
        scrollToPosition(this.topItemPosition)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter = null
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        // 各要素の現在の状態を保存
        // 異なるクラスのアイテムを保存するため、各要素でクラスの取得とそれに対する保存を行う
//        // null-safe(?.)を利用しないと復帰不可
//        this.displayAdapter?.settingItemsList?.forEach {
//        }
        (adapter as SettingsViewAdapter).settingItemsList.forEach {
            when (it) {
                is ToggleSwitch -> {
                    val item : ToggleSwitch = it
                    bundle.putParcelable(item.keyword, item)
                }

                else -> {}
            }
        }
        this.items = (adapter as SettingsViewAdapter).settingItemsList
        val parent = super.onSaveInstanceState()
        val saved = SavedState(parent!!)
        saved.bundle = bundle

        // スクロール位置の保存
        val pos = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        saved.topItemPosition = pos
        if (adapter == null || adapter!!.itemCount == 0) {
            saved.scrollPositionOffset = 0
        } else {
            saved.scrollPositionOffset = getChildAt(0).top
        }

        return saved
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val saved = state as SavedState
        super.onRestoreInstanceState(state)
        // bundleに保存した内容をリストにコピーする
        // bundleのkeyの値を見て、リストの値の番号にあったところにオブジェクトを挿入する
        // listの中身はnullになっていないので、listに対して値を上書きする
        // (復元時はonAttachedToWindowより先に呼ばれるため)
        val tagArray = items.map { it.keyword }
        saved.bundle!!.keySet().forEach {
            val index = tagArray.indexOf(it)
            val savedItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                saved.bundle!!.getParcelable(it, SettingItems::class.java)
            } else {
                @Suppress("DEPRECATION")
                saved.bundle!!.getParcelable(it)
            }
            when (savedItem) {
                is ToggleSwitch -> {
                    val item = this.items[index] as ToggleSwitch
                    item.enabled = savedItem.enabled
                    item.checked = savedItem.checked
                    this.items[index] = item
                }

                else -> {}
            }
        }
        this.topItemPosition = saved.topItemPosition
        this.scrollPositionOffset = saved.scrollPositionOffset
        saved.bundle!!.clear()
    }

    /**
     * adapterを取得
     *
     * @return [SettingsListViewAdapter] SettingsListViewAdapter
     */
    fun getViewAdapter() = adapter as SettingsViewAdapter?

    /**
     * ビューの状態を保存する
     *
     * @property bundle adapterに記録された内容
     * @property topItemPosition 画面上で見える一番上の要素の順番(0開始)
     * @property scrollPositionOffset 画面上の一番上の要素について、スクロールでの移動量
     */
    internal class SavedState : BaseSavedState {
        // 保存する変数
        var bundle: Bundle? = null
        var topItemPosition: Int = 0
        var scrollPositionOffset: Int = 0

        constructor(source: Parcel): super(source) {
            bundle = source.readBundle(javaClass.classLoader)
            topItemPosition = source.readInt()
            scrollPositionOffset = source.readInt()
        }

        constructor(superState: Parcelable): super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBundle(bundle)
            out.writeInt(topItemPosition)
            out.writeInt(scrollPositionOffset)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}