package com.example.mycalendar2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.mycalendar2.databinding.ItemSetcalendarBinding
import java.util.*

open class SetCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener{

    var calendarBinding: ItemSetcalendarBinding = ItemSetcalendarBinding.inflate(LayoutInflater.from(context), this, true)

    lateinit var todayDate:Calendar
    var onDayClickListener:OnDayClickListener? = null

    init {
        calendarBinding.root.setOnClickListener(this)
    }

    fun setDate(date:Calendar){
        todayDate = date

        calendarBinding.dayviewTvDay.text = date.get(Calendar.DATE).toString()
        WeekendColor()
    }

    fun WeekendColor(){
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 1 ){
            calendarBinding.dayviewTvDay.setTextColor(Color.RED)
        }
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 7 ){
            calendarBinding.dayviewTvDay.setTextColor(Color.BLUE)
        }
    }

    fun isCurrentMonth(isCurrentMonth:Boolean){
        if(!isCurrentMonth){
            calendarBinding.dayviewTvDay.setTextColor(Color.LTGRAY)
            onDayClickListener = null
        }
    }


    override fun onClick(v: View?) {
        onDayClickListener?.onDayClick(this, todayDate)
    }

    interface OnDayClickListener {
        fun onDayClick(v:SetCalendar, date: Calendar)
    }
}