package io.github.kaztakgh.settingsview

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Permissionが必要な処理について、結果の処理を任せるためのクラス
 *
 * ActivityまたはFragmentでの`onCreate`または`onViewCreated`で呼び出す
 *
 * @exception IllegalStateException registerを呼び出さなかった場合に発生
 */
class PermissionManager {
    private var launcher: ActivityResultLauncher<String>? = null

    /**
     * Activityへの登録
     *
     * @param activity 利用するActivity
     */
    fun register(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // コールバック処理
            callback?.invoke(isGranted)
        }
    }

    /**
     * Fragmentへの登録
     *
     * @param fragment 利用するFragment
     */
    fun register(fragment: Fragment) {
        launcher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // コールバック処理
            callback?.invoke(isGranted)
        }
    }

    private var callback: ((Boolean) -> Unit)? = null

    /**
     * 特定のパーミッションの許可を求め、その結果を記述する
     *
     * @param permission 許可対象のパーミッション
     */
    fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        this.callback = callback
        launcher?.launch(permission)
    }
}