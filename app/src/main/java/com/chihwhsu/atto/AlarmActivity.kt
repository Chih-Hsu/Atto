package com.chihwhsu.atto

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ActivityAlarmBinding
import com.chihwhsu.atto.ext.getTimeFromStartOfDay
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.ext.toFormat
import com.chihwhsu.atto.ext.toMinuteSecondFormat
import com.chihwhsu.atto.util.SECOND

class AlarmActivity : AppCompatActivity() {

    private var currentRingtone: Ringtone? = null
    private var vibrator: Vibrator? = null
    private var ringTone: String? = null
    private var flag: Int? = null

    private val viewModel by viewModels<AlarmActivityViewModel> { getVmFactory() }
    private lateinit var binding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set NavigationBar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val intent = this.intent
        viewModel.setIntent(intent)
        viewModel.currentIntent.observe(
            this
        ) {
            val id = intent.getIntExtra(ID, 0)
            viewModel.getEvent(id)
        }

        viewModel.event.observe(
            this
        ) { event ->

            ringTone = intent.getStringExtra(RINGTONE)
            flag = intent.getIntExtra(FLAG, 0)

            when (event.type) {

                // Pomodoro Work
                Event.POMODORO_WORK_TYPE -> {
                    setUI(
                        showAlarmAnimation = false,
                        showBreakAnimation = false,
                        showWorkAnimation = true,
                        event = event
                    )

                    event.lockAppLabel?.let {
                        viewModel.lockApp(it)
                    }

                    createNotificationChannel()
                    runCountDownTimer(event)
                }

                // Pomodoro break
                Event.POMODORO_BREAK_TYPE -> {
                    setUI(
                        showAlarmAnimation = false,
                        showBreakAnimation = true,
                        showWorkAnimation = false,
                        event = event
                    )
                    binding.textCountDown.text =
                        getTimeFromStartOfDay(System.currentTimeMillis()).toFormat()

                    event.lockAppLabel?.let {
                        viewModel.unLockApp(it)
                    }

                    runCountDownTimer(event)
                }

                else -> {  // Alarm
                    setUI(
                        showAlarmAnimation = true,
                        showBreakAnimation = false,
                        showWorkAnimation = false,
                        event = event
                    )
                    binding.textCountDown.text =
                        getTimeFromStartOfDay(System.currentTimeMillis()).toFormat()

                    setVibratorAndRingTone(flag!!, ringTone!!)
                }
            }
        }


        binding.materialButton.setOnClickListener {
            if (flag == ONLY_RINGTONE || flag == VIBRATION_AND_RINGTONE) {
                currentRingtone?.stop()
            }
            if (flag == ONLY_VIBRATION || flag == VIBRATION_AND_RINGTONE) {
                vibrator?.cancel()
            }
            val newIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun setUI(
        event: Event,
        showWorkAnimation: Boolean,
        showAlarmAnimation: Boolean,
        showBreakAnimation: Boolean
    ) {

        val text = when (event.type) {
            Event.ALARM_TYPE -> getString(R.string.wake_up_text)
            Event.POMODORO_WORK_TYPE -> getString(R.string.work_text)
            Event.POMODORO_BREAK_TYPE -> getString(R.string.take_a_break_text)
            else -> ""
        }

        binding.animationWork.visibility = animationVisibility(showWorkAnimation)
        binding.animationAlarm.visibility = animationVisibility(showAlarmAnimation)
        binding.animationBreak.visibility = animationVisibility(showBreakAnimation)
        binding.textTimeToWork.text = getString(R.string.time_to_activity_text, text)
    }

    private fun animationVisibility(isShow: Boolean): Int {
        return if (isShow) View.VISIBLE else View.GONE
    }

    private fun runCountDownTimer(
        event: Event
    ) {
        // when this countdown close , break countdown will start,
        // so the startActivity will close break countdown in the same time
        // the purpose of minus 1000 is to stagger two timer
        event.startTime?.let {

            val countDownTimer = object :
                CountDownTimer(event.alarmTime - event.startTime - SECOND, SECOND) {

                override fun onTick(millisUntilFinished: Long) {

                    binding.textCountDown.text = millisUntilFinished.toMinuteSecondFormat()

                    val notificationIntent =
                        Intent(this@AlarmActivity, AlarmActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this@AlarmActivity,
                        REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE
                    )

                    val title =
                        if (event.type == Event.POMODORO_WORK_TYPE) {
                            getString(R.string.time_to_work_alarm)
                        } else {
                            getString(
                                R.string.time_to_break_text
                            )
                        }

                    val notification: Notification =
                        NotificationCompat.Builder(this@AlarmActivity, CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(
                                getString(
                                    R.string.remain_time,
                                    millisUntilFinished.toMinuteSecondFormat()
                                )
                            )
                            .setSmallIcon(R.drawable.atto_icon)
                            .setContentIntent(pendingIntent)
                            .build()

                    val notificationManager =
                        NotificationManagerCompat.from(this@AlarmActivity)

                    notificationManager.notify(NOTIFICATION_ID, notification)
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

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


    companion object {
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "foreground_service"
        private const val NOTIFICATION_ID = 1
        private const val ID = "id"
        private const val RINGTONE = "ringTone"
        private const val FLAG = "flag"
        private const val REQUEST_CODE = 0
        private const val VIBRATION_AND_RINGTONE = 2
        private const val ONLY_RINGTONE = 1
        private const val ONLY_VIBRATION = 0
    }
}
