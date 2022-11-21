package com.chihwhsu.atto

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ActivityAlarmBinding
import com.chihwhsu.atto.ext.getTimeFrom00am
import com.chihwhsu.atto.ext.toFormat
import com.chihwhsu.atto.ext.toMinuteSecondFormat
import com.chihwhsu.atto.factory.AlarmActivityViewModelFactory

class AlarmActivity : AppCompatActivity() {

    private var currentRingtone: Ringtone? = null
    private var vibrator: Vibrator? = null

    private var ringTone : String? = null
    private var flag : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set NavigationBar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val viewModelFactory =
            AlarmActivityViewModelFactory(((applicationContext as AttoApplication).attoRepository))
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(AlarmActivityViewModel::class.java)

        val intent = this.intent
        viewModel.setIntent(intent)
        viewModel.currentIntent.observe(this, Observer {
            val id = intent.getIntExtra("id", 0)
            viewModel.getEvent(id)
        })


        viewModel.event.observe(this, Observer { event ->

            ringTone = intent.getStringExtra("ringTone")
            flag = intent.getIntExtra("flag", 0)

            when (event.type) {

                // Pomodoro Work
                Event.POMODORO_WORK_TYPE -> {
                    binding.animationWork.visibility = View.VISIBLE
                    binding.animationAlarm.visibility = View.GONE
                    binding.animationBreak.visibility = View.GONE
                    binding.textTimeToWork.text = "Time To \nWork !"
                    binding.materialButton.text = "Fuck Work"

                    event.lockAppLabel?.let {
                        viewModel.lockApp(it)
                    }

                    // when this countdown close , break countdown will start,
                    // so the startActivity will close break countdown in the same time
                    // the purpose of minus 1000 is to stagger two timer
                    val countDownTimer = object :
                        CountDownTimer(event.alarmTime - event.startTime!! - 1000L, 1000L) {
                        override fun onTick(millisUntilFinished: Long) {
                            binding.textCountDown.text = millisUntilFinished.toMinuteSecondFormat()
                        }

                        override fun onFinish() {
                            val newIntent = Intent(applicationContext, MainActivity::class.java)
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(newIntent)
                            finish()
                        }
                    }

                    countDownTimer.start()
                }

                // Pomodoro break
                Event.POMODORO_BREAK_TYPE -> {
                    binding.animationWork.visibility = View.GONE
                    binding.animationAlarm.visibility = View.GONE
                    binding.animationBreak.visibility = View.VISIBLE
                    binding.textCountDown.text =
                        getTimeFrom00am(System.currentTimeMillis()).toFormat()
                    binding.textTimeToWork.text = "Time To \nTake a Break !"
                    binding.materialButton.text = "Close"

                    event.lockAppLabel?.let {
                        viewModel.unLockApp(it)
                    }


                    val countDownTimer =
                        object : CountDownTimer(event.alarmTime - event.startTime!! - 1000, 1000L) {
                            override fun onTick(millisUntilFinished: Long) {
                                binding.textCountDown.text =
                                    millisUntilFinished.toMinuteSecondFormat()
                            }

                            override fun onFinish() {
                                val newIntent = Intent(applicationContext, MainActivity::class.java)
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(newIntent)
                                finish()
                            }
                        }
                    countDownTimer.start()

                }

                else -> {
                    binding.animationWork.visibility = View.GONE
                    binding.animationAlarm.visibility = View.VISIBLE
                    binding.animationBreak.visibility = View.GONE
                    binding.textCountDown.text =
                        getTimeFrom00am(System.currentTimeMillis()).toFormat()
                    binding.textTimeToWork.text = "Time To \nWake Up !"
                    binding.materialButton.text = "Close"
                    setVibratorAndRingTone(flag!!, ringTone!!)
                }
            }
        })


        binding.materialButton.setOnClickListener {
            if (flag == 1 || flag == 2) {
                currentRingtone?.stop()
            }
            if (flag == 0 || flag == 2) {
                vibrator?.cancel()
            }
            val newIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(newIntent)
        }


    }

    private fun setVibratorAndRingTone(flag: Int, ringTone: String) {
        if (flag == 1 || flag == 2) {
            currentRingtone = RingtoneManager
                .getRingtone(
                    this,
                    Uri.parse(ringTone)
                )
            currentRingtone?.play()
        }

        if (flag == 0 || flag == 2) {
            vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            // first pram is how to vibrate , second is repeatOrNot 0 is keep vibrate -1 is not repeat
            vibrator?.vibrate(longArrayOf(100, 10, 100, 600), 0)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

}