package com.example.mycalendar2

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
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
        calendarBinding.mainMonth.text = (calendar.get(Calendar.MONTH) +1).toString()

        //Calendar 객체를 복사하여 시작 날짜, 종료 날짜에 대한 상황별로 나누어 날짜 추가 및 등록
        var startDay: Calendar = calendar.clone() as Calendar
        startDay.set(Calendar.WEEK_OF_MONTH, 1)
        startDay.set(Calendar.DAY_OF_WEEK, 1)

        //마지막 일자에 대한 Calendar 복사 값
        var endDay: Calendar = calendar.clone() as Calendar

        //시작 일자에 대한 Calendar 복사 값으로 한 달이 끝났는지 여부를 판단할 요소
        var dateCnt: Calendar = startDay.clone() as Calendar

        //한달 치 날짜 추가
        endDay.add(Calendar.MONTH, 1)

        //한달 끝 하루 뒤의 날짜 (1일)
        endDay.set(Calendar.DATE, 1)

        //한달 끝 다음 일주일 단위의 주 1개 삭제
        endDay.add(Calendar.DAY_OF_WEEK, -1)

        //7일 단위 Set
        endDay.set(Calendar.DAY_OF_WEEK, 7)

        //새로운 달의 시작 유무를 알리는 상태 변수
        var isRestartMonth = false

        //새로운 달이 시작할때(True)가 될 때까지 순회
        while (!isRestartMonth) {
            //Table Row 객체를 생성하여 내부에 달력 일수를 삽입
            val weekCalendar = TableRow(this)

            //주 단위로 순회 후 View 생성
            for (i in 0..6) {
                val setCalendar = SetCalendar(this)
                setCalendar.setDate(dateCnt.clone() as Calendar)
                setCalendar.onDayClickListener = calendarListener
                setCalendar.isCurrentMonth(dateCnt.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                weekCalendar.addView(setCalendar)

                //마지막 일자가 계속 넘겨져온 현재 날짜와 같다면 새로운 달의 시작을 판별
                if (dateCnt.time == endDay.time) {
                    isRestartMonth = true
                    break
                }

                //하루씩 일자 더하기
                dateCnt.add(Calendar.DATE, 1)
            }
            //Table 형태로 그려진 View를 상위 Layout View에 삽입
            calendarBinding.mainLayoutCalendar.addView(weekCalendar)
        }
    }

    private var calendarListener = object : SetCalendar.OnDayClickListener {
        override fun onDayClick(v: SetCalendar, date: Calendar) {
            val dialog = AlertDialog.Builder(this@MainActivity)
                .create()
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_memo_custom, null)
            val dialogDate = dialogView.findViewById<TextView>(R.id.dialogDate)
            val dialogMemo = dialogView.findViewById<EditText>(R.id.eventEditText)

            //DateFormat 지정 후 날짜 값을 문자열로 받아온 뒤, 배열로 변환
            val dateFormat: DateFormat = SimpleDateFormat("EEE-d-MMM")

            //요일명
            val pickDayName = dateFormat.format(date.time).split("-")[0]

            //일
            val pickDay = dateFormat.format(date.time).split("-")[1]

            //월
            val monthPickDay = dateFormat.format(date.time).split("-")[2]

            dialogDate.text = "$pickDayName, $pickDay $monthPickDay"

            val saveBtn = dialogView.findViewById<Button>(R.id.updateButton)
            saveBtn.setOnClickListener {
                v.calendarBinding.root.addView(createEventMemo(dialogMemo.text.toString()))
                dialog.dismiss()
            }

            dialog.setView(dialogView)
            dialog.show()

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
        when (v!!.id) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createEventMemo(str: String): TextView {
        val eventView = TextView(this)
        eventView.background = resources.getDrawable(R.drawable.round_memo)
        eventView.text = str
        eventView.gravity = Gravity.CENTER
        eventView.textSize = 15f
        eventView.setTextColor(Color.WHITE)

        return eventView
    }

    fun createLongEventMemo() {

    }

}