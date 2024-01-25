package io.github.kaztakgh.settingsview

import android.view.View
import android.view.ViewTreeObserver
import io.github.kaztakgh.settingsview.test.TestActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SpinnerChoiceViewHolderTest {
    private lateinit var activity: TestActivity
    private lateinit var settingsView: SettingsView
    @Before
    fun setup() {
        // testフォルダでRobolectricを使用している場合、testフォルダ内にresourcesとAndroidManifest.xmlがあると動作する(?)
        // activityをRESUMEの状態にする
        activity = Robolectric.buildActivity(TestActivity::class.java).create().start().resume().get()
        activity.setContentView(io.github.kaztakgh.settingsview.test.R.layout.layout_test)
        this.settingsView = activity.findViewById(io.github.kaztakgh.settingsview.test.R.id.settingsView)
    }

    /**
     * 表示内容を確認する
     */
    @Test
    fun displayViewHolder() {
        // 絵画が完了したときにしかテストできないため、ViewTreeObserver内部にテストを記述
        val viewTreeObserver: ViewTreeObserver = this.settingsView.viewTreeObserver
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener,
            ViewTreeObserver.OnDrawListener {
            /**
             * Callback method to be invoked when the view tree is about to be drawn. At this point, all
             * views in the tree have been measured and given a frame. Clients can use this to adjust
             * their scroll bounds or even to request a new layout before drawing occurs.
             *
             * @return Return true to proceed with the current drawing pass, or false to cancel.
             *
             * @see android.view.View.onMeasure
             *
             * @see android.view.View.onLayout
             *
             * @see android.view.View.onDraw
             */
            override fun onPreDraw(): Boolean {
                val optionsArray = arrayOf("test1", "test2", "test3")
                val itemsArray = ArrayList<SettingItems>()
                val spinnerChoiceItem1 = SpinnerChoice(
                    keyword = "spinner_choice_1",
                    title = "Test1",
                    iconId = io.github.kaztakgh.settingsview.test.R.drawable.baseline_android_24,
                    options = optionsArray,
                    select = 1
                )
                val spinnerChoiceItem2 = SpinnerChoice(
                    keyword = "spinner_choice_2",
                    title = "Test2",
                    enabled = false,
                    options = optionsArray
                )
                itemsArray.add(spinnerChoiceItem1)
                itemsArray.add(spinnerChoiceItem2)
                val adapter = SettingsViewAdapter(itemsArray)
                this@SpinnerChoiceViewHolderTest.settingsView.adapter = adapter
                (this@SpinnerChoiceViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                // 表示内容を確認する
                val viewHolders = mutableListOf<SpinnerChoiceItemViewHolder>()
                for (range in 0 ..this@SpinnerChoiceViewHolderTest.settingsView.adapter?.itemCount!!) {
                    viewHolders.add(
                        range,
                        this@SpinnerChoiceViewHolderTest.settingsView.findViewHolderForAdapterPosition(range) as SpinnerChoiceItemViewHolder
                    )
                }
                Assert.assertEquals(spinnerChoiceItem1.title, viewHolders[0].title)
                Assert.assertEquals(View.VISIBLE, viewHolders[0].ivIcon.visibility)
                Assert.assertEquals(true, viewHolders[0].getOptionsSpinner().isEnabled)
                Assert.assertEquals("test2", viewHolders[0].getOptionsSpinner().selectedItem.toString())
                Assert.assertEquals(View.GONE, viewHolders[1].ivIcon.visibility)
                Assert.assertEquals(false, viewHolders[1].getOptionsSpinner().isEnabled)
                Assert.assertEquals("test3", viewHolders[1].getOptionsSpinner().selectedItem.toString())

                // 絵画完了時の処理終了
                this@SpinnerChoiceViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
                return true
            }

            /**
             *
             * Callback method to be invoked when the view tree is about to be drawn. At this point,
             * views cannot be modified in any way.
             *
             *
             * Unlike with [OnPreDrawListener], this method cannot be used to cancel the
             * current drawing pass.
             *
             *
             * An [OnDrawListener] listener **cannot be added or removed**
             * from this method.
             *
             * @see android.view.View.onMeasure
             *
             * @see android.view.View.onLayout
             *
             * @see android.view.View.onDraw
             */
            override fun onDraw() {
            }
        })
    }

    /**
     * スピナーの選択肢を変更した際に、内部のデータも変更されることを確認
     */
    @Test
    fun changeSelectFromDropdown() {
        // 絵画が完了したときにしかテストできないため、ViewTreeObserver内部にテストを記述
        val viewTreeObserver: ViewTreeObserver = this.settingsView.viewTreeObserver
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener,
            ViewTreeObserver.OnDrawListener {
            /**
             * Callback method to be invoked when the view tree is about to be drawn. At this point, all
             * views in the tree have been measured and given a frame. Clients can use this to adjust
             * their scroll bounds or even to request a new layout before drawing occurs.
             *
             * @return Return true to proceed with the current drawing pass, or false to cancel.
             *
             * @see android.view.View.onMeasure
             *
             * @see android.view.View.onLayout
             *
             * @see android.view.View.onDraw
             */
            override fun onPreDraw(): Boolean {
                val optionsArray = arrayOf("test1", "test2", "test3")
                val itemsArray = ArrayList<SettingItems>()
                val spinnerChoiceItem3 = SpinnerChoice(
                    keyword = "spinner_choice_3",
                    title = "Test3",
                    options = optionsArray
                )
                itemsArray.add(spinnerChoiceItem3)
                val adapter = SettingsViewAdapter(itemsArray)
                this@SpinnerChoiceViewHolderTest.settingsView.adapter = adapter
                (this@SpinnerChoiceViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                // 選択内容を変更する
                val viewHolders = mutableListOf<SpinnerChoiceItemViewHolder>()
                viewHolders.add(
                    0,
                    this@SpinnerChoiceViewHolderTest.settingsView.findViewHolderForAdapterPosition(0) as SpinnerChoiceItemViewHolder
                )
//                viewHolders[0].getOptionsSpinner().performClick()
                // クリックして選択する方法が不明のため、setSelectionで選択肢を変更
                viewHolders[0].getOptionsSpinner().setSelection(1)
                Assert.assertEquals(
                    1,
                    (this@SpinnerChoiceViewHolderTest.settingsView.getViewAdapter()!!.settingItemsList[0] as SpinnerChoice).select
                )

                // 絵画完了時の処理終了
                this@SpinnerChoiceViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
                return true
            }

            /**
             *
             * Callback method to be invoked when the view tree is about to be drawn. At this point,
             * views cannot be modified in any way.
             *
             *
             * Unlike with [OnPreDrawListener], this method cannot be used to cancel the
             * current drawing pass.
             *
             *
             * An [OnDrawListener] listener **cannot be added or removed**
             * from this method.
             *
             * @see android.view.View.onMeasure
             *
             * @see android.view.View.onLayout
             *
             * @see android.view.View.onDraw
             */
            override fun onDraw() {
            }
        })
    }
}