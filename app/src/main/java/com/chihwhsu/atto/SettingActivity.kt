package com.chihwhsu.atto

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.setting.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // Cancel Half Transparent
        window.decorView.systemUiVisibility =
            (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Fullscreen，do not hide status bar
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // render statusBar background
        window.statusBarColor = Color.TRANSPARENT

        // set NavigationBar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Only run this function in first start
        viewModel.appNumber.observe(
            this
        ) {
            if (it == 0) {
                viewModel.updateApp()
            }
        }
    }

}
