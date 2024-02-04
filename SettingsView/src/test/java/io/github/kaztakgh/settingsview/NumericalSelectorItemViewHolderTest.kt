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
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)

class NumericalSelectorItemViewHolderTest {
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
     *
     * SdkVersionが26(Oreo)以上の場合
     */
    @Test
    @Config(sdk = [26])
    fun displayViewHolderVersionOreo() {
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
                val numericalSelector1 = NumericalSelector(
                    keyword = "numerical_selector_1",
                    title = "Test1",
                    iconId = io.github.kaztakgh.settingsview.test.R.drawable.baseline_android_24,
                    state = 50,
                    max = 80,
                    min = 20,
                    divine = 5,
                    unit = "px"
                )
                val numericalSelector2 = NumericalSelector(
                    keyword = "numerical_selector_2",
                    title = "Test2",
                    enabled = false,
                    state = 50,
                    max = 80,
                    min = 20,
                    divine = 5,
                    unit = "px"
                )
                itemsArray.add(numericalSelector1)
                itemsArray.add(numericalSelector2)
                val adapter = SettingsViewAdapter(itemsArray)
                this@NumericalSelectorItemViewHolderTest.settingsView.adapter = adapter
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                // 表示内容を確認する
                val viewHolders = mutableListOf<NumericalSelectorItemViewHolder>()
                for (range in 0 ..this@NumericalSelectorItemViewHolderTest.settingsView.adapter?.itemCount!!) {
                    viewHolders.add(
                        range,
                        this@NumericalSelectorItemViewHolderTest.settingsView.findViewHolderForAdapterPosition(range) as NumericalSelectorItemViewHolder
                    )
                }
                Assert.assertEquals(numericalSelector1.title, viewHolders[0].title)
                Assert.assertEquals(View.VISIBLE, viewHolders[0].ivIcon.visibility)
                Assert.assertEquals(true, viewHolders[0].getSeekBar().isEnabled)
                Assert.assertEquals(numericalSelector1.max, viewHolders[0].getSeekBar().max)
                Assert.assertEquals(numericalSelector1.min, viewHolders[0].getSeekBar().min)
                Assert.assertEquals(numericalSelector1.state, viewHolders[0].getSeekBar().progress)
                Assert.assertEquals(numericalSelector1.state.toString() + numericalSelector1.unit, viewHolders[0].text)
                Assert.assertEquals(View.GONE, viewHolders[1].ivIcon.visibility)
                Assert.assertEquals(false, viewHolders[1].getSeekBar().isEnabled)

                // 値を変更したときの表示内容の変化を見る
                viewHolders[0].getSeekBar().progress = 60
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                Assert.assertEquals("60px", viewHolders[0].text)

                // 絵画完了時の処理終了
                this@NumericalSelectorItemViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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
     * 表示内容を確認する
     *
     * SdkVersionが25(Nougat)までの場合
     */
    @Test
    @Config(sdk = [24])
    fun displayViewHolderVersionNougat() {
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
                val numericalSelector1 = NumericalSelector(
                    keyword = "numerical_selector_3",
                    title = "Test3",
                    iconId = io.github.kaztakgh.settingsview.test.R.drawable.baseline_android_24,
                    state = 50,
                    max = 80,
                    min = 20,
                    divine = 5,
                    unit = "px"
                )
                val numericalSelector2 = NumericalSelector(
                    keyword = "numerical_selector_4",
                    title = "Test4",
                    enabled = false,
                    state = 50,
                    max = 80,
                    min = 20,
                    divine = 5,
                    unit = "px"
                )
                itemsArray.add(numericalSelector1)
                itemsArray.add(numericalSelector2)
                val adapter = SettingsViewAdapter(itemsArray)
                this@NumericalSelectorItemViewHolderTest.settingsView.adapter = adapter
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                // 表示内容を確認する
                val viewHolders = mutableListOf<NumericalSelectorItemViewHolder>()
                for (range in 0 ..this@NumericalSelectorItemViewHolderTest.settingsView.adapter?.itemCount!!) {
                    viewHolders.add(
                        range,
                        this@NumericalSelectorItemViewHolderTest.settingsView.findViewHolderForAdapterPosition(range) as NumericalSelectorItemViewHolder
                    )
                }
                Assert.assertEquals(numericalSelector1.title, viewHolders[0].title)
                Assert.assertEquals(View.VISIBLE, viewHolders[0].ivIcon.visibility)
                Assert.assertEquals(true, viewHolders[0].getSeekBar().isEnabled)
                Assert.assertEquals(
                    (numericalSelector1.max - numericalSelector1.min) / numericalSelector1.divine,
                    viewHolders[0].getSeekBar().max
                )
                Assert.assertEquals(0, viewHolders[0].getSeekBar().min)
                Assert.assertEquals(
                    (numericalSelector1.state - numericalSelector1.min) / numericalSelector1.divine,
                    viewHolders[0].getSeekBar().progress
                )
                Assert.assertEquals(numericalSelector1.state.toString() + numericalSelector1.unit, viewHolders[0].text)
                Assert.assertEquals(View.GONE, viewHolders[1].ivIcon.visibility)
                Assert.assertEquals(false, viewHolders[1].getSeekBar().isEnabled)

                // 値を変更したときの表示内容の変化を見る
                viewHolders[0].getSeekBar().progress = (60 - numericalSelector1.min) / numericalSelector1.divine
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                Assert.assertEquals("60px", viewHolders[0].text)

                // 絵画完了時の処理終了
                this@NumericalSelectorItemViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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
     * 数値の配列で指定した場合の表示の確認
     */
    @Test
    fun displayViewHolderInputParamsArray() {
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
                val paramsArray = IntArray(13) { it * 5 + 20 }
                val numericalSelector1 = NumericalSelector(
                    keyword = "numerical_selector_5",
                    title = "Test5",
                    state = 50,
                    unit = "px",
                    paramsArray = paramsArray
                )
                itemsArray.add(numericalSelector1)
                val adapter = SettingsViewAdapter(itemsArray)
                this@NumericalSelectorItemViewHolderTest.settingsView.adapter = adapter
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                // 表示内容を確認する
                val viewHolders = mutableListOf<NumericalSelectorItemViewHolder>()
                for (range in 0 ..this@NumericalSelectorItemViewHolderTest.settingsView.adapter?.itemCount!!) {
                    viewHolders.add(
                        range,
                        this@NumericalSelectorItemViewHolderTest.settingsView.findViewHolderForAdapterPosition(range) as NumericalSelectorItemViewHolder
                    )
                }
                Assert.assertEquals(paramsArray.size - 1, viewHolders[0].getSeekBar().max)
                Assert.assertEquals(0, viewHolders[0].getSeekBar().min)
                Assert.assertEquals(paramsArray.indexOf(numericalSelector1.state), viewHolders[0].getSeekBar().progress)
                Assert.assertEquals(numericalSelector1.state.toString() + numericalSelector1.unit, viewHolders[0].text)

                // 値を変更したときの表示内容の変化を見る
                viewHolders[0].getSeekBar().progress = paramsArray.indexOf(60)
                (this@NumericalSelectorItemViewHolderTest.settingsView.adapter as SettingsViewAdapter).notifyDataSetChanged()
                Assert.assertEquals("60px", viewHolders[0].text)

                // 絵画完了時の処理終了
                this@NumericalSelectorItemViewHolderTest.settingsView.viewTreeObserver.removeOnDrawListener(this)
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