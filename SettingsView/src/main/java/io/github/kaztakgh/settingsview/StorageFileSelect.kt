package io.github.kaztakgh.settingsview

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import kotlinx.parcelize.Parcelize

/**
 * ファイルを指定するアイテムの作成
 *
 * このアイテムのみ呼び出し元でのPermissionの確認が必要
 *
 * また、ファイル選択時の動作は呼び出し元で定義が必要
 *
 * @property keyword View上の設定項目の識別子。省略不可。
 * @property title タイトル。省略不可。
 * @property iconId drawableにある画像ID。使用しない場合は省略する。
 * @property enabled 選択可能であるか。デフォルトはtrue。
 * @property uri ファイルの場所を示すURI。デフォルトはnull。
 * @property mimeType ファイルの種類。デフォルトはすべて。
 * @property displayFullPath 一覧画面でファイルパスをすべて表示するか。デフォルトはfalse。
 * @property onItemClick ファイル選択時の動作を定義。adapterの更新もここで記述することになる。
 */
@Parcelize
data class StorageFileSelect(
    override val keyword: String,
    override val title: String,
    val iconId: Int? = null,
    var enabled: Boolean = true,
    var uri: Uri? = null,
    val mimeType: String = "*/*",
    val displayFullPath: Boolean = false,
    val onItemClick: (Uri) -> Unit
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
    }

    /**
     * デフォルトのファイル選択画面を呼び出すためのIntent
     *
     * 呼び出し元のstartActivityForResultで使用する
     *
     * 使用方法
     * 
     * ```
     * val Intent = item.getOpenDocumentIntent()
     * this.localFileSelectorLauncher.launch(intent)
     * ```
     *
     * @return ファイル選択のIntent
     */
    fun getOpenDocumentIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
        }
    }

    /**
     * Uriからファイルパスを取得する
     *
     * @param context データ
     * @return ファイルパス
     */
    internal fun getContentPathStringFromUri(context: Context): String {
        if (uri == null) return ""

        // 変数初期化
        var path = ""
        val cr = context.contentResolver
        // この方法のみで対応可能ではない模様(Androidデフォルトのファイルを使用していない場合など)
        // 選択時に使用するアプリによって方法が変わるのも含めて対処するべき？
        var projection: Array<String>? = null
        val id: String? = DocumentsContract.getDocumentId(uri)
        val mimeTypeResult = cr.getType(uri!!)
        val selection = "_id=?"
        var selectionArgs = arrayOfNulls<String>(0)
        if (id != null) {
            selectionArgs =
                arrayOf(id.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
        }
        // API Level29でDATAが非推奨に変更
        if (mimeTypeResult != null) {
            projection = when {
                mimeTypeResult.startsWith("audio") -> arrayOf(MediaStore.Audio.Media.DATA)
                mimeTypeResult.startsWith("image") -> arrayOf(MediaStore.Images.Media.DATA)
                mimeTypeResult.startsWith("video") -> arrayOf(MediaStore.Video.Media.DATA)
                else -> arrayOf(MediaStore.MediaColumns.DATA)
            }
        }
        val crsCursor = cr.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            null
        )
        // ファイル名の取得
        if (crsCursor != null) {
            crsCursor.moveToFirst()
            path = crsCursor.getString(0)
            crsCursor.close()
        }

        // 文字列の整形
        if (!displayFullPath) {
            val pathArray = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            path = pathArray[pathArray.size - 1]
        }

        return path
    }

    /**
     * Uriからファイルパスを取得する
     *
     * 取得結果はフルパス表示不可
     *
     * ApiLevel:29以上
     *
     * @param context データ
     * @return ファイルパス
     */
    @TargetApi(Build.VERSION_CODES.Q)
    internal fun getContentFilePathDescriptor(context: Context): String {
        // 変数初期化
        var path = ""
        val cr = context.contentResolver
        // 表示するファイル名の形式の選択
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val crsCursor = cr.query(
            uri!!,
            projection,
            null,
            null,
            null
        )
        // ファイル名の取得
        if (crsCursor != null) {
            crsCursor.moveToFirst()
            path = crsCursor.getString(0)
            crsCursor.close()
        }

        return path
    }
}