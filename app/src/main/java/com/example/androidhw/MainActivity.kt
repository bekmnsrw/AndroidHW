package com.example.androidhw

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import androidx.core.net.toUri
import com.example.androidhw.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            btn1.setOnClickListener {
                showMap("geo:${etLongitude.text},${etLatitude.text}".toUri())
            }

            btn2.setOnClickListener {
                composeEmail(
                    etEmailAdresses.text.toString(),
                    etEmailTitle.text.toString(),
                    etEmailText.text.toString()
                )
            }

            btn3.setOnClickListener {
                addEvent(
                    etEventTitle.text.toString(),
                    etEventLocation.text.toString(),
                    getDateInMilliseconds(etEventBegin.text.toString()).toString().toLong(),
                    getDateInMilliseconds(etEventEnd.text.toString()).toString().toLong()
                )
            }
        }
    }

    private fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation

            putExtra(Intent.EXTRA_TEXT, geoLocation)
        }
        val chooserIntent = Intent.createChooser(intent, "Show with")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

    private fun composeEmail(email: String, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email?subject=$subject&body=$text")

            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(intent, "Send with")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

    private fun addEvent(title: String, location: String, begin: Long, end: Long) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI

            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
        }
        val chooserIntent = Intent.createChooser(intent, "Add with")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

    @SuppressLint("NewApi")
    private fun getDateInMilliseconds(date: String): Long? {
        val dateInMilliseconds = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
        return dateInMilliseconds.atZone(ZoneOffset.systemDefault())?.toInstant()?.toEpochMilli()
    }
}