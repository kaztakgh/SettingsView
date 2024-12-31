package io.github.kaztakgh.settingsview_sample

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

import io.github.kaztakgh.settingsview.OnSwitchCheckedChangeListener
import io.github.kaztakgh.settingsview.PermissionManager
import io.github.kaztakgh.settingsview.SettingItems
import io.github.kaztakgh.settingsview.SettingsView
import io.github.kaztakgh.settingsview.SettingsViewAdapter
import io.github.kaztakgh.settingsview.StorageFileSelect
import io.github.kaztakgh.settingsview.ToggleSwitch

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsScreenFragment : Fragment() {
    private lateinit var settingsView : SettingsView
    private val permissionManager = PermissionManager()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SettingsScreenFragment.
         */
        fun newInstance() : SettingsScreenFragment = SettingsScreenFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_screen, container, false)
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // パーミッションが許可された場合の処理
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // 拒否された場合の処理
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        permissionManager.register(this)
        this.settingsView = view.findViewById(R.id.settingsList)
        val settingsList: ArrayList<SettingItems> = createSettingsList()
        val settingsViewAdapter = SettingsViewAdapter(settingsList)
        settingsViewAdapter.switchCheckedChangeListener = object : OnSwitchCheckedChangeListener {
            override fun onCheckedChange(position: Int, checked: Boolean) {
                Toast.makeText(context, "checked: $checked, position: $position", Toast.LENGTH_SHORT).show()
            }
        }
        this.settingsView.adapter = settingsViewAdapter
    }

    /**
     * Called when the view previously created by [.onCreateView] has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after [.onStop] and before [.onDestroy].  It is called
     * *regardless* of whether [.onCreateView] returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        this.settingsView.adapter = null
        super.onDestroyView()
    }

    /**
     * 設定項目の挿入
     */
    private fun createSettingsList() : ArrayList<SettingItems> {
        val settingsList: ArrayList<SettingItems> = ArrayList()
        val switchItem = ToggleSwitch(
            title = "Switch",
            keyword = "switch",
            iconId = R.drawable.baseline_check_box_24,
            textOnTrue = "TRUE",
            textOnFalse = "FALSE"
        )
        val fileSelectItem = StorageFileSelect(
            title = "FileSelect",
            keyword = "file_select",
            mimeType = "image/*",
            onItemClick = { _ ->
                selectAllFileTiramisuVersion("image/*")
            }
        )
        settingsList.add(switchItem)
        settingsList.add(fileSelectItem)
        return settingsList
    }

    private fun selectAllFileTiramisuVersion(mimeType: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
        }
        this.localFileSelectorLauncher.launch(intent)
    }

    private val localFileSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 選択しなかったなどの場合はここで終了
        if (result.resultCode != RESULT_OK) {
            return@registerForActivityResult
        }
        try {
            // 選択した内容の表示
            // 内容によって変わってくるので、別関数にしている
            // pathがdocument://スキームになっている
            result.data?.data?.also {
                Toast.makeText(context, it.path, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 例外の調査が必要
            e.printStackTrace()
        }
    }
}