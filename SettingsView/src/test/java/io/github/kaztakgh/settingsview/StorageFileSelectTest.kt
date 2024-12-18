package io.github.kaztakgh.settingsview

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StorageFileSelectTest {
    /**
     * 識別子に空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyKeyword() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            StorageFileSelect(
                keyword = "",
                title = "Test1",
                iconId = null,
                enabled = true,
                uri = null,
                mimeType = "*/*",
                displayFullPath = true,
                onItemClick = { uri: Uri -> return@StorageFileSelect }
            )
        }
        // エラーメッセージの出力を確認
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    /**
     * タイトルに空文字を入力した場合、エラーになることを確認
     */
    @Test
    fun inputEmptyTitle() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            StorageFileSelect(
                keyword = "Test1",
                title = "",
                iconId = null,
                enabled = true,
                uri = null,
                mimeType = "*/*",
                displayFullPath = true,
                onItemClick = { uri: Uri -> return@StorageFileSelect }
            )
        }
        // エラーメッセージの出力を確認
        assertEquals(true, exception.message!!.isNotEmpty())
    }

    // Uri関係のテストはファイルパスの設定方法がわからないため、ここでの記述はしないようにする?
//    @Test
//    @Config(sdk = [28])
//    fun getContentPathStringFromUriVersionPie() {
//        val context = InstrumentationRegistry.getInstrumentation().context
//        // assetsフォルダにあるファイルを利用する
//        val inputStream: InputStream = context.assets.open("sample.txt")
//        val shadowContentResolver: ShadowContentResolver = Shadows.shadowOf(context.contentResolver)
//        val path = "file://android_asset/sample.txt"
//        val uri = Uri.parse(path)
//        shadowContentResolver.registerInputStream(uri, inputStream)
//        val selector = StorageFileSelect(
//            keyword = "Test2",
//            title = "Test2",
//            iconId = null,
//            enabled = true,
//            uri = uri,
//        )
//        val actual = selector.getContentPathStringFromUri(context)
//        assertEquals(path, actual)
//    }
}