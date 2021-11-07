package com.example.mycalendar2

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.mycalendar2.databinding.ActivityMainBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val calendar = Calendar.getInstance()
    lateinit var calendarView: TableLayout

    val calendarBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(calendarBinding.root)
        calendarView = calendarBinding.mainLayoutCalendar

        calendarBinding.previousBtn.setOnClickListener(this)
        calendarBinding.nextBtn.setOnClickListener(this)

        createCalendarView(Calendar.getInstance())
    }

    //Dialog가 추가될때마다 새로 View를 갱신해주어야하므로 달력 생성 함수를 개별적으로 분리
    private fun createCalendarView(calendar: Calendar) {

        //상단의 년도와 월자 값을 연결
        calendarBinding.mainYear.text = calendar.get(Calendar.YEAR).toString()
        calendarBinding.mainMonth.text = (calendar.get(Calendar.MONTH) + 1).toString()

        var startDay: Calendar = calendar.clone() as Calendar
        startDay.set(Calendar.WEEK_OF_MONTH, 1)
        startDay.set(Calendar.DAY_OF_WEEK, 1)


        var endDay: Calendar = calendar.clone() as Calendar
        var dateCnt: Calendar = startDay.clone() as Calendar

        endDay.add(Calendar.MONTH, 1)
        endDay.set(Calendar.DATE, 1)

        endDay.add(Calendar.DATE, -1)
        endDay.set(Calendar.DAY_OF_WEEK, 7)

        var isRestartMonth = false
        while (!isRestartMonth) {
            val weekCalendar = TableRow(this)
            for(i in 0..6) {
                val setCalendar = SetCalendar(this)
                setCalendar.setDate(dateCnt.clone() as Calendar)
                setCalendar.onDayClickListener = calendarListener
                setCalendar.isCurrentMonth(dateCnt.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                weekCalendar.addView(setCalendar)

                //마지막 일자가 계속 넘겨져온 현재 날짜와 같다면 새로운 달의 시작을 판별
                if(dateCnt.time == endDay.time) {
                    isRestartMonth = true
                    break
                }
                dateCnt.add(Calendar.DATE, 1)
            }
            calendarBinding.mainLayoutCalendar.addView(weekCalendar)
        }
    }

    private var calendarListener = object : SetCalendar.OnDayClickListener {
        override fun onDayClick(v: SetCalendar, date: Calendar) {
            val dialog = AlertDialog.Builder(this@MainActivity)
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_memo_custom, null)
            val dialogDate = dialogView.findViewById<TextView>(R.id.dialogDate)
            val dialogMemo = dialogView.findViewById<EditText>(R.id.eventEditText)

            val dateFormat: DateFormat = SimpleDateFormat("EEE-d-MMM")
            val pickDayName = dateFormat.format(date.time).split("-")[0]
            val pickDay = dateFormat.format(date.time).split("-")[1]
            val monthPickDay = dateFormat.format(date.time).split("-")[2]

            dialogDate.text = "$pickDayName, $pickDay $monthPickDay"

            dialog.setView(dialogView)
                .setPositiveButton("메모 등록") { _: DialogInterface?, _: Int ->
                    v.calendarBinding.root.addView(createEventMemo(dialogMemo.text.toString()))
                }.show()

//            dialog 확인 등록 버튼 가운데로 옮기려 했지만 구현 실패 -> How to bring a getButton??
//            val positiveBtn = dialog.context.getColor(AlertDialog.BUTTON_POSITIVE)

//            val dialog = CalendarDialog(this@MainActivity)
//            dialog.showDialog()
//            dialog.setOnClickListener(object : CalendarDialog.OnDialogClickListener {
//                override fun onClicked(memo: String) {
//                    v.calendarBinding.root.addView(createMemoView())
//                }
//            })
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.nextBtn -> {
                calendar.add(Calendar.MONTH, 1)
            }
            R.id.previousBtn -> {
                calendar.add(Calendar.MONTH, -1)
            }
        }

        calendarView.removeViews(1, calendarView.childCount - 1)
        createCalendarView(calendar)
    }

    fun createEventMemo(str : String): TextView {
        val eventView = TextView(this)
        eventView.text = str
        eventView.textSize = 15f
        eventView.setTextColor(Color.WHITE)
        eventView.setBackgroundColor(Color.parseColor("#de7478"))

        return eventView
    }

}