/**
 * @file SettingsViewAdapter.kt
 */
package io.github.kaztakgh.settingsview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Viewに表示する内容を記述する
 *
 * @property settingItemsList Viewに表示させるSettingItemsのArrayList
 * @property switchCheckedChangeListener ToggleSwitchでクリックしたときの追加処理。Activity/Fragmentで指定。
 */
class SettingsViewAdapter(
    var settingItemsList: ArrayList<SettingItems>
) : RecyclerView.Adapter<ViewHolder>(){
    companion object {
        /**
         * 通常(タイトルとテキスト)
         */
        const val VIEW_TYPE_NORMAL: Int = 0
        /**
         * スイッチによる選択
         */
        const val VIEW_TYPE_SWITCH: Int = 1
        /**
         * スピナーによる選択
         */
        const val VIEW_TYPE_SPINNER: Int = 2
        /**
         * シークバーでの数値選択
         */
        const val VIEW_TYPE_SEEKBAR: Int = 3
        /**
         * 文字入力
         */
        const val VIEW_TYPE_INPUT_TEXT: Int = 4
        /**
         * ヘッダー
         */
        const val VIEW_TYPE_HEADER: Int = 5
    }

    /**
     * parentViewのContext
     */
    private lateinit var context: Context

    /**
     * 紐づけたSettingsView
     */
    private var parentView: SettingsView? = null

    /**
     * スイッチの設定項目で、切り替え時における追加処理
     */
    var switchCheckedChangeListener : OnSwitchCheckedChangeListener? = null

    init {
        // keywordが重複した場合はエラーを出力
        if (!this.isDifferentAllKeyword(this.settingItemsList)) {
            throw IllegalArgumentException("keywordは要素間で重複しないように設定してください")
        }
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     *
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     * @see .onDetachedFromRecyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.parentView = recyclerView as SettingsView
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see .onAttachedToRecyclerView
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.parentView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val layoutInflater = LayoutInflater.from(this.context)
        // DataBindingの箇所が利用しづらい状況
        when (viewType) {
            VIEW_TYPE_SWITCH -> {
                val view = layoutInflater.inflate(
                    R.layout.layout_switch,
                    parent,
                    false
                )
                val holder = ToggleSwitchItemViewHolder(view)
                view.setOnClickListener { v ->
                    this.parentView.let {
                        holder.itemClickListener?.onItemClick(v, it!!.getChildAdapterPosition(v))
                    }
                }
                return holder
            }
            VIEW_TYPE_SPINNER -> {
                val view = layoutInflater.inflate(
                    R.layout.layout_spinner,
                    parent,
                    false
                )
                val holder = SpinnerChoiceItemViewHolder(view)
                view.setOnClickListener { v ->
                    this.parentView.let {
                        holder.itemClickListener?.onItemClick(v, it!!.getChildAdapterPosition(v))
                    }
                }
                return holder
            }
            else -> {
                val view = layoutInflater.inflate(
                    R.layout.layout_normal,
                    parent,
                    false
                )
                val holder = NormalItemViewHolder(view)
                view.setOnClickListener { v ->
                    this.parentView.let {
                        holder.itemClickListener?.onItemClick(v, it!!.getChildAdapterPosition(v))
                    }
                }
                return holder
            }
        }
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int): Int {
        return when (this.settingItemsList[position]) {
            is ToggleSwitch -> VIEW_TYPE_SWITCH
            is SpinnerChoice -> VIEW_TYPE_SPINNER
            else -> VIEW_TYPE_NORMAL
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return this.settingItemsList.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getBindingAdapterPosition] which
     * will have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = this.settingItemsList[position]) {
            is ToggleSwitch -> {
                val viewHolder = holder as ToggleSwitchItemViewHolder
                val updateItem: ToggleSwitch = item
                this.bindSwitchItemViewHolder(viewHolder, updateItem)
            }
            is SpinnerChoice -> {
                val viewHolder = holder as SpinnerChoiceItemViewHolder
                val updateItem: SpinnerChoice = item
                this.bindSpinnerItemViewHolder(viewHolder, updateItem)
            }
            else -> {}
        }
    }

    /**
     * 識別子の重複チェック
     *
     * @param list 表示に使用するリスト
     * @return true : 重複なし, false : 重複あり
     */
    private fun isDifferentAllKeyword(list: ArrayList<SettingItems>) : Boolean {
        val distinctKeywordArray: List<String> = list.map { it.keyword }.distinct()
        return distinctKeywordArray.size == list.size
    }

    /**
     * Switchの表示内容を決定する
     *
     * @param holder [ToggleSwitchItemViewHolder]
     * @param item リスト内にあるToggleSwitchのデータ
     */
    private fun bindSwitchItemViewHolder(holder: ToggleSwitchItemViewHolder, item: ToggleSwitch) {
        // 表示内容の指定
        holder.title = item.title
        holder.textOnTrue = item.textOnTrue
        holder.textOnFalse = item.textOnFalse
        holder.enabled = item.enabled
        holder.checked = item.checked
        holder.setIconFromDrawableId(item.iconId)
        val position: Int = holder.bindingAdapterPosition

        // 要素をクリックしたときにスイッチを切り替えられるようにする
        holder.itemClickListener = object : NormalItemViewHolder.ItemClickListener {
            /**
             * アイテムをクリックしたときの処理
             *
             * @param view レイアウトビュー
             * @param position アダプター内のアイテムの位置
             */
            override fun onItemClick(view: View, position: Int) {
                // スイッチの状態を変更する
                this@SettingsViewAdapter.changeSwitchCheck(holder, item, position)
            }
        }
        // スイッチを直接クリックした場合、スイッチに合わせて状態を変更する
        holder.getSwitchComponent().setOnClickListener {
            this.changeSwitchCheck(holder, item, position)
        }
    }

    /**
     * スイッチの状態が変化したときの処理
     *
     * @param holder [ToggleSwitchItemViewHolder]
     * @param item リスト内にあるToggleSwitchのデータ
     * @param position アダプター内のリストの順序
     */
    private fun changeSwitchCheck(holder: ToggleSwitchItemViewHolder, item: ToggleSwitch, position: Int) {
        val listItem: ToggleSwitch = this.settingItemsList[position] as ToggleSwitch
        // スイッチの操作ができない状態の場合はここで終了
        // returnをここで指定しているのは、2通り(ViewHolder内部、スイッチ)の操作で変更できてしまうため
        if (!listItem.enabled) return

        // スイッチの状態の変更
        val checked: Boolean = !item.checked
        holder.checked = checked
        listItem.checked = checked
        // 変更後の処理がある場合
        if (this.switchCheckedChangeListener != null) {
            this.switchCheckedChangeListener!!.onCheckedChange(position, checked)
        }
    }

    /**
     * SpinnerItemの表示内容をitemから指定する
     *
     * @param holder [SpinnerChoiceItemViewHolder]
     * @param item 表示対象のSpinnerChoice
     */
    private fun bindSpinnerItemViewHolder(holder: SpinnerChoiceItemViewHolder, item: SpinnerChoice) {
        // 表示内容の指定
        holder.title = item.title
        holder.enabled = item.enabled
        // 選択肢の設定
        val optionsSpinner: Spinner = holder.getOptionsSpinner()
        setSpinnerOptions(optionsSpinner, item.options)
        optionsSpinner.setSelection(item.select)
        optionsSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            /**
             * 何も選択しなかった場合
             * もしくはSpinnerの外側をタップした場合
             *
             * @param parent The AdapterView that now contains no selected item.
             */
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            /**
             * Spinnerで表示しているアイテムを選択した場合の処理
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position アダプターの選択肢の位置
             * @param id 選択した列のID
             */
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // アイテムの選択を変更
                (this@SettingsViewAdapter.settingItemsList[holder.bindingAdapterPosition] as SpinnerChoice).select = position
                // 変更後の処理がある場合
//                if (item.selectChangeListener != null && item.focusable) {
//                    item.selectChangeListener!!.onItemSelectChanged(this@SettingItemsAdapter, position)
//                }
                // 初回操作時
                when {
                    !item.focusable -> item.focusable = true
                }
            }
        }    }

    /**
     * スピナーの選択肢のセット
     *
     * @param spinner セット対象のスピナー
     * @param options 選択肢
     */
    private fun setSpinnerOptions(spinner: Spinner, options: Array<String>) {
        val spinnerAdapter = ArrayAdapter(
            this.context,
            android.R.layout.simple_spinner_item,
            options
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }
}