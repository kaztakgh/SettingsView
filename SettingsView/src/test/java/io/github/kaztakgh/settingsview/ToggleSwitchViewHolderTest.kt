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
class ToggleSwitchViewHolderTest {
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
                val itemsArray = ArrayList<SettingItems>()
                val toggleSwitchItem1 = ToggleSwitch(
                    keyword = "toggle_switch_1",
                    title = "Test1",
                    textOnTrue = "TRUE",
                    textOnFalse = "FALSE",
                    iconId = io.github.kaztakgh.settingsview.test.R.drawable.baseline_android_24
                )
                val toggleSwitchItem2 = ToggleSwitch(
                    keyword = "toggle_switch_2",
                    title = "Test2",
                    textOnTrue = "TRUE",
                    textOnFalse = "FALSE",
                    checked = true
                )
                val toggleSwitchItem3 = ToggleSwitch(
                    keyword = "toggle_switch_3",
                    title = "Test3",
                    textOnTrue = "TRUE",
                    textOnFalse = "FALSE",
                    enabled = false
                )
                itemsArray.add(toggleSwitchItem1)
                itemsArray.add(toggleSwitchItem2)
                itemsArray.add(toggleSwitchItem3)
                val adapter = SettingsViewAdapter(itemsArray)
                this@ToggleSwitchViewHolderTest.settingsView.adapter = adapter
                (this@ToggleSwitchViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()

                // 表示内容を確認する
                val viewHolders = mutableListOf<ToggleSwitchItemViewHolder>()
                for (range in 0 ..this@ToggleSwitchViewHolderTest.settingsView.adapter?.itemCount!!) {
                    viewHolders.add(
                        range,
                        this@ToggleSwitchViewHolderTest.settingsView.findViewHolderForAdapterPosition(range) as ToggleSwitchItemViewHolder
                    )
                }
                Assert.assertEquals(toggleSwitchItem1.title, viewHolders[0].title)
                Assert.assertEquals(toggleSwitchItem1.textOnFalse, viewHolders[0].text)
                Assert.assertEquals(View.VISIBLE, viewHolders[0].ivIcon.visibility)
                Assert.assertEquals(false, viewHolders[0].getSwitchComponent().isChecked)
                Assert.assertEquals(true, viewHolders[0].getSwitchComponent().isEnabled)
                Assert.assertEquals(toggleSwitchItem2.textOnTrue, viewHolders[1].text)
                Assert.assertEquals(View.GONE, viewHolders[1].ivIcon.visibility)
                Assert.assertEquals(true, viewHolders[1].getSwitchComponent().isChecked)
                Assert.assertEquals(false, viewHolders[2].getSwitchComponent().isEnabled)

                // 絵画完了時の処理終了
                this@ToggleSwitchViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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

    @Test
    fun changeSwitchStateOnClickViewHolder() {
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
                val itemsArray = ArrayList<SettingItems>()
                val toggleSwitchItem4 = ToggleSwitch(
                    keyword = "toggle_switch_4",
                    title = "Test4",
                    textOnTrue = "TRUE",
                    textOnFalse = "FALSE"
                )
                itemsArray.add(toggleSwitchItem4)
                val adapter = SettingsViewAdapter(itemsArray)
                this@ToggleSwitchViewHolderTest.settingsView.adapter = adapter
                (this@ToggleSwitchViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()

                // 表示内容を確認する
                val viewHolders = mutableListOf<ToggleSwitchItemViewHolder>()
                viewHolders.add(
                    0,
                    this@ToggleSwitchViewHolderTest.settingsView.findViewHolderForAdapterPosition(0) as ToggleSwitchItemViewHolder
                )
                viewHolders[0].itemView.performClick()
                Assert.assertEquals(true, viewHolders[0].getSwitchComponent().isChecked)
                viewHolders[0].itemView.performClick()
                Assert.assertEquals(false, viewHolders[0].getSwitchComponent().isChecked)

                // 絵画完了時の処理終了
                this@ToggleSwitchViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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

    @Test
    fun changeSwitchStateOnClickToggleSwitch() {
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
                val itemsArray = ArrayList<SettingItems>()
                val toggleSwitchItem5 = ToggleSwitch(
                    keyword = "toggle_switch_5",
                    title = "Test5",
                    textOnTrue = "TRUE",
                    textOnFalse = "FALSE"
                )
                itemsArray.add(toggleSwitchItem5)
                val adapter = SettingsViewAdapter(itemsArray)
                this@ToggleSwitchViewHolderTest.settingsView.adapter = adapter
                (this@ToggleSwitchViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()

                // 表示内容を確認する
                val viewHolders = mutableListOf<ToggleSwitchItemViewHolder>()
                viewHolders.add(
                    0,
                    this@ToggleSwitchViewHolderTest.settingsView.findViewHolderForAdapterPosition(0) as ToggleSwitchItemViewHolder
                )
                val switch = viewHolders[0].getSwitchComponent()
                switch.performClick()
                Assert.assertEquals(toggleSwitchItem5.textOnTrue, viewHolders[0].text)
                switch.performClick()
                Assert.assertEquals(toggleSwitchItem5.textOnFalse, viewHolders[0].text)

                // 絵画完了時の処理終了
                this@ToggleSwitchViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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