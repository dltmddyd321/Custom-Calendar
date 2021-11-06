package com.example.mycalendar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import com.example.mycalendar2.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val calendar = Calendar.getInstance()
    lateinit var calendarView: TableLayout
    var isRestartMonth = false


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        calendarView = binding.mainLayoutCalendar
        binding.mainBtnPrevious.setOnClickListener(this)
        binding.mainBtnNext.setOnClickListener(this)
        binding.mainTvYear.text = calendar.get(Calendar.YEAR).toString()
        binding.mainTvMonth.text = (calendar.get(Calendar.MONTH) + 1).toString()

        var startDay: Calendar = calendar.clone() as Calendar
        var endDay: Calendar = calendar.clone() as Calendar
        var dateCnt: Calendar = startDay.clone() as Calendar

        startDay.set(Calendar.WEEK_OF_MONTH, 1)
        startDay.set(Calendar.DAY_OF_WEEK, 1)

        endDay.add(Calendar.MONTH, 1)
        endDay.set(Calendar.DATE, 1)

        endDay.add(Calendar.MONTH, -1)
        endDay.set(Calendar.DATE, 7)

        while (!isRestartMonth) {
            val weekDays = TableRow(this)
            for(i in 0..6) {
                val setCalendar = SetCalendar(this)
                setCalendar.setDate(dateCnt.clone() as Calendar)
                setCalendar.onDayClickListener = calendarListener
                setCalendar.isNowMonth(dateCnt.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                weekDays.addView(setCalendar)

            }

        }
    }

    private var calendarListener = object : SetCalendar.OnDayClickListener {
        override fun onDayClick(v: SetCalendar, date: Calendar) {
            //DIALOG
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.main_btn_next -> {
                calendar.add(Calendar.MONTH, 1)
            }

            R.id.main_btn_previous -> {
                calendar.add(Calendar.MONTH, -1)
            }
        }

        calendarView.removeViews(1, calendarView.childCount - 1)
    }
}