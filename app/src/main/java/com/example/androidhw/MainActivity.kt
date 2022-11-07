package com.example.androidhw

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidhw.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var alarmManager: AlarmManager? =  null
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        NotificationProvider(this).createNotificationChannel()

        binding?.run {
            tvDate.setOnClickListener {
                showDatePicker()
            }
            tvTime.setOnClickListener {
                showTimePicker()
            }
            btnStart.setOnClickListener {
                setAlarm()
            }
            btnStop.setOnClickListener {
                cancelAlarm()
            }
        }
    }

    private fun setAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )

        Toast.makeText(
            this,
            "Alarm has been successfully set on ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.time)}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager?.cancel(alarmIntent)

        binding?.tvDate?.text = "Select date"
        binding?.tvTime?.text = "Select time"

        Toast.makeText(
            this,
            "Alarm has been successfully cancelled",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showTimePicker() {
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                binding?.tvTime?.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }
        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                binding?.tvDate?.text = SimpleDateFormat("dd.MM.yyyy").format(calendar.time)
            }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}