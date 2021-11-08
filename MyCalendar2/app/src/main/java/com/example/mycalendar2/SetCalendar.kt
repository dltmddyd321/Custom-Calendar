package com.example.mycalendar2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.mycalendar2.databinding.ItemSetcalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//@JvmOverloads 어노테이션을 통해 view 생성에 대한 정의를 간략하게 표현 가능
open class SetCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    //item_setCalendar 통한 binding 선언
    var calendarBinding: ItemSetcalendarBinding =
        ItemSetcalendarBinding.inflate(LayoutInflater.from(context), this, true)

    //현재에 대한 월, 일자 값을 받아오기 위한 변수
    @SuppressLint("NewApi")
    var now = LocalDate.now()

    @SuppressLint("NewApi")
    var today = now.format(DateTimeFormatter.ofPattern("dd")).toInt()

    @SuppressLint("NewApi")
    var thisMonth = now.format(DateTimeFormatter.ofPattern("MM")).toInt()

    //View의 날짜 설정을 표현하기 위한 Calendar
    lateinit var todayDate: Calendar

    //특정 날짜 클릭 시 효과 적용을 위한 클릭 Listener 선언
    var onDayClickListener: OnDayClickListener? = null

    override fun onClick(v: View?) {
        onDayClickListener?.onDayClick(this, todayDate)
    }

    //특정 날짜 클릭에 대한 Interface
    interface OnDayClickListener {
        fun onDayClick(v: SetCalendar, date: Calendar)
    }

    //매개변수 및 반환 값이 없는 함수로서 ClickListener 생성자 실행
    init {
        calendarBinding.root.setOnClickListener(this)
    }

    //달력에 그려지는 itemView - Text에 일자 값을 할당하는 함수
    fun setDate(date: Calendar) {
        todayDate = date
        calendarBinding.dateNumber.text = date.get(Calendar.DATE).toString()
        dateColor(date)
    }

    /*
    특정 날짜에 대한 표현법을 지정한 것으로, 처음 1번(일요일)에 해당하는 숫자에는 빨간색,
    마지막 7번(토요일)에 해당하는 숫자에는 파랑색을 지정.
    오늘 날짜 표시는 먼저 실제 오늘의 월과 달력의 월이 일치하는지 검사 후, 일치한다면
    다시 실제 오늘의 일과 달력의 일이 일치하는지 검사하여 최종적으로 모두 일치한다면
    초록색으로 날짜 Text 색상이 지정되도록 구현
     */
    fun dateColor(date: Calendar) {
        if (todayDate.get(Calendar.DAY_OF_WEEK) == 1) {
            calendarBinding.dateNumber.setTextColor(Color.RED)
        }
        if (todayDate.get(Calendar.DAY_OF_WEEK) == 7) {
            calendarBinding.dateNumber.setTextColor(Color.BLUE)
        }
        if ((todayDate.get(Calendar.MONTH) + 1) == thisMonth) {
            if (date.get(Calendar.DATE) == today) {
                calendarBinding.dateNumber.setTextColor(Color.GREEN)
            }
        }
    }

    //현재 월에 해당하는 일자가 아닌 경우 라이트 그레이로 흐리게 Text 표현
    fun isCurrentMonth(isCurrentMonth: Boolean) {
        if (!isCurrentMonth) {
            calendarBinding.dateNumber.setTextColor(Color.LTGRAY)
            onDayClickListener = null
        }
    }
}