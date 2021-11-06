package com.example.mycalendar2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.mycalendar2.databinding.ItemDayviewBinding
import java.util.*

//View 그리기 위한 클래스 상속을 위해 open class 선언
open class SetCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    lateinit var todayDate : Calendar
    var onDayClickListener: OnDayClickListener ? =null

    //캘린더 디자인 그릴 때 사용하는 DayViewBinding
    //attachToParent True -> 앱 활성화 시 바로 출력
    var calendarBinding: ItemDayviewBinding = ItemDayviewBinding.inflate(LayoutInflater.from(context), this, false)

    //달력 클릭 시 메모 추가를 위한 리스너 작용
    init {
        calendarBinding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onDayClickListener?.onDateClick(this, todayDate)
    }

    interface OnDayClickListener {
        fun onDateClick(v:SetCalendar, date: Calendar)
    }

    //기본 달력 일자 배치 함수
    fun setDate(date: Calendar) {
        todayDate = date
        calendarBinding.dayviewTvDay.text = date.get(Calendar.DATE).toString()
        weekendColor()
    }

    //토요일 일요일 색상 변경
    fun weekendColor(){
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 1 ){
            calendarBinding.dayviewTvDay.setTextColor(Color.RED)
        }
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 7 ){
            calendarBinding.dayviewTvDay.setTextColor(Color.BLUE)
        }
    }

    //실제 시간의 달과 일치하지 않으면 텍스트 색상 변경
    fun isCurrentMonth(isCurrentMonth:Boolean){
        if(!isCurrentMonth){
            calendarBinding.dayviewTvDay.setTextColor(Color.LTGRAY)
            onDayClickListener = null
        }
    }
}