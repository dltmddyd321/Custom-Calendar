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

open class SetCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener{

    var calendarBinding: ItemSetcalendarBinding = ItemSetcalendarBinding.inflate(LayoutInflater.from(context), this, true)

    @SuppressLint("NewApi")
    var now = LocalDate.now()

    @SuppressLint("NewApi")
    var today = now.format(DateTimeFormatter.ofPattern("dd")).toInt()
    @SuppressLint("NewApi")
    var thisMonth = now.format(DateTimeFormatter.ofPattern("MM")).toInt()

    lateinit var todayDate:Calendar
    var onDayClickListener:OnDayClickListener? = null

    init {
        calendarBinding.root.setOnClickListener(this)
    }

    fun setDate(date:Calendar){
        todayDate = date

        calendarBinding.dateNumber.text = date.get(Calendar.DATE).toString()

        dateColor(date)
    }

    fun dateColor(date : Calendar){
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 1 ){
            calendarBinding.dateNumber.setTextColor(Color.RED)
        }
        if(todayDate.get(Calendar.DAY_OF_WEEK) == 7 ){
            calendarBinding.dateNumber.setTextColor(Color.BLUE)
        }
        if((todayDate.get(Calendar.MONTH)+1) == thisMonth) {
            if(date.get(Calendar.DATE) == today + 1) {
                calendarBinding.dateNumber.setTextColor(Color.GREEN)
            }
        }
    }

    fun isCurrentMonth(isCurrentMonth:Boolean){
        if(!isCurrentMonth){
            calendarBinding.dateNumber.setTextColor(Color.LTGRAY)
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