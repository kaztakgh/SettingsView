package io.github.kaztakgh.settingsview_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // fragmentとの紐づけ
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        val settingsScreenFragment = SettingsScreenFragment()
        if (manager.findFragmentByTag("SettingsScreenFragment") == null) {
            transaction.add(settingsScreenFragment, "SettingsScreenFragment")
            transaction.commit()
        }
    }
}
