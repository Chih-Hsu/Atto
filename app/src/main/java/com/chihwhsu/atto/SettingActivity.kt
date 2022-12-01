package com.chihwhsu.atto

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.login.LoginViewModel
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.tutorial.SettingViewModel


class SettingActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingViewModel> { getVmFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
        window.statusBarColor = Color.TRANSPARENT

        // set NavigationBar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        // Only run this function in first start
        viewModel.appNumber.observe(this, Observer {
            if (it == 0) {
                viewModel.updateApp()
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}