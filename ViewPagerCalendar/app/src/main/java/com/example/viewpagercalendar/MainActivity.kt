package com.example.viewpagercalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var calendarRecyclerView: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarRecyclerView = RecyclerViewAdapter(this)

        rv_schedule.layoutManager = GridLayoutManager(this, Calendar.DAYS_OF_WEEK)
        rv_schedule.adapter = calendarRecyclerView
        rv_schedule.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        tv_prev_month.setOnClickListener{
            calendarRecyclerView.changeToPrevMonth()
        }

        tv_next_month.setOnClickListener{
            calendarRecyclerView.changeToNextMonth()
        }
    }

    fun updateCurrentMonth(calendar: java.util.Calendar) {
        val simpleDateFormat = SimpleDateFormat("yyyy MM", Locale.KOREA)
        tv_current_month.text = simpleDateFormat.format(calendar.time)
    }
}