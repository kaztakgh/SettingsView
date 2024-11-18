package io.github.kaztakgh.settingsview

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * ファイルを指定するアイテムの作成
 *
 * Permissionの確認が必要になってくる
 *
 * @property keyword View上の設定項目の識別子。省略不可。
 * @property title タイトル。省略不可。
 * @property iconId drawableにある画像ID。使用しない場合は省略する。
 * @property enabled 選択可能であるか。デフォルトはtrue。
 * @property uri ファイルの場所を示すURI。デフォルトはnull。
 * @property mimeType ファイルの種類。デフォルトはすべて。
 * @property requestCode リクエストコード。デフォルト値は1。
 * @property displayFullPath 一覧画面でファイルパスをすべて表示するか。デフォルトはfalse。
 */
@Parcelize
data class StorageFileSelect(
    override val keyword: String,
    override val title: String,
    val iconId: Int? = null,
    var enabled: Boolean = true,
    var uri: Uri? = null,
    val mimeType: String = "*/*",
    val requestCode: Int = 1,
    val displayFullPath: Boolean = false
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
     * パーミッション情報の取得
     *
     * @param context データ
     */
    internal fun requestPermission(context: Context) {
        // 既に許可を得ている場合、ファイル選択のウィンドウを開く
        if (checkPermission(context)) {
            this.openDocument(context)
        }
        else {
            // ダイアログ出力許可がある場合
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, READ_EXTERNAL_STORAGE)) {
                // 許可を求めるパーミッションダイアログを出力する
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    requestCode
                )
            }
            // ダイアログ出力許可がない場合
            else
            // 操作できないという警告文を出力する
                Toast.makeText(context, "デバイス上のファイルを操作できません", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * WRITE_EXTERNAL_STORAGEのパーミッション確認
     *
     * @param context データ
     * @return boolean READ_EXTERNAL_STORAGEのパーミッションの戻り値がPERMISSION_GRANTEDであるか
     */
    private fun checkPermission(context: Context) : Boolean {
        return ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun confirmAccessPermissions(context: Context) {
        // 画像、映像、音楽ファイルのいずれかのアクセス権限がない場合はダイアログを起動
        var isGranted = true
        run breaking@ {
            this.requestTiramisuPermissionsSet.forEach { permission ->
                val accepted : Int = ContextCompat.checkSelfPermission(context, permission)
                if (accepted != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                    return@breaking
                }
            }
        }
    }

    /**
     * ファイル選択画面の出力
     *
     * @param context データ
     */
    private fun openDocument(context: Context) {
        // Intentの生成
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        // uriが設定されている場合
        if (uri != null) {
            // uriとMIMEタイプの設定
            intent.setDataAndType(uri, mimeType)
        }
        else {
            // MIMEタイプの設定
            intent.type = mimeType
        }
        // ActivityもしくはFragmentで処理してもらう
        (context as Activity).startActivityForResult(intent, requestCode)
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

    /**
     * Tiramisu以降で確認する必要のあるパーミッションの種類
     *
     * 1つでも許可が無い場合は要確認
     */
    @IgnoredOnParcel
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestTiramisuPermissionsSet = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
}