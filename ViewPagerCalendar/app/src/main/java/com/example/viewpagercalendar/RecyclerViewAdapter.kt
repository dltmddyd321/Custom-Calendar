package com.example.viewpagercalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_calendar.view.*

class RecyclerViewAdapter(val mainActivity: MainActivity) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderHelper>() {

    val baseCalendar = Calendar()

    //View 호출 시 우선 설정 -> 기본 날짜 명시
    init {
        baseCalendar.initBaseCalendar {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return ViewHolderHelper(view)
    }

    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
        if(position % Calendar.DAYS_OF_WEEK == 0) {
            holder.itemView.dateNum.setTextColor(Color.parseColor("#ff1200"))
        } else
            holder.itemView.dateNum.setTextColor(Color.parseColor("#676d6e"))

        //현재 월이 아닌 이전 또는 다음달 날짜는 흐리게 처리
        if(position < baseCalendar.preMonthSetting || position >= baseCalendar.preMonthSetting + baseCalendar.currentMonthMaxSetting) {
            holder.itemView.dateNum.alpha = 0.3f
        } else {
            holder.itemView.dateNum.alpha = 1f
        }

        holder.itemView.dateNum.text = baseCalendar.calendarData[position].toString()
    }

    override fun getItemCount(): Int {
        var num = baseCalendar.discriminateFirstDate()
        return num * Calendar.DAYS_OF_WEEK
    }

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            updateView(it)
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            updateView(it)
        }
    }

    fun updateView(calendar: java.util.Calendar) {
        notifyDataSetChanged()
        mainActivity.updateCurrentMonth(calendar)
    }

    inner class ViewHolderHelper(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}