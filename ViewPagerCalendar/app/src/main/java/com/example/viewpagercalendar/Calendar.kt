package com.example.viewpagercalendar

import java.util.*
import java.util.Calendar

class Calendar {

    //달력 상수 선언
    companion object {
        //달력 한 줄마다 표시될 일 수
        const val DAYS_OF_WEEK = 7

        //달력 한 달 기준 주의 수
        var LOW_OF_CALENDAR = 5
    }



    val calendar = Calendar.getInstance()

    var preMonthSetting = 0
    var currentMonthMaxSetting = 0
    var nextMonthStart = 0

    //달력 날짜 값들을 담을 배열
    var calendarData = arrayListOf<Int>()

    //클래스 인스턴스화 시 가장 먼저 Date 기본 설정
    init {
        calendar.time = Date()
    }

    //JAVA의 Void와 유사한 Unit : Return 값을 명시할 필요 X
    fun initBaseCalendar(refreshCallback: (Calendar) -> Unit) {
        makeMonthDate(refreshCallback)
    }

    private fun makeMonthDate(refreshCallback: (Calendar) -> Unit) {

        //값이 없긴하지만, 배열 한번 초기화
        calendarData.clear()

        //오늘 1일 지정
        calendar.set(Calendar.DATE, 1)

        //월의 마지막 날짜
        currentMonthMaxSetting = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        preMonthSetting = calendar.get(Calendar.DAY_OF_WEEK) -1

        makePrevMonth(calendar.clone() as Calendar)
        makeCurrentMonth(calendar)

        nextMonthStart = LOW_OF_CALENDAR * DAYS_OF_WEEK - (preMonthSetting + currentMonthMaxSetting)
        makeNextMonthHead()

        refreshCallback(calendar)
    }

    private fun makePrevMonth(calendar: Calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate - preMonthSetting

        //이전 달의 끝 부분도 현재 달력에 표시하기 위한 반복문
        for(i in 1..preMonthSetting) {
            calendarData.add(++maxOffsetDate)
        }
    }

    private fun makeCurrentMonth(calendar: Calendar) {
        //현재 월의 날짜 만큼 순회하여 그 크기만큼 배열에 값 추가
        for(i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            calendarData.add(i)
        }
    }

    private fun makeNextMonthHead() {
        var date = 1

        for(i in 1..nextMonthStart) {
            calendarData.add(date++)
        }
    }

    fun changeToPrevMonth(refreshCallback: (Calendar) -> Unit) {
        if(calendar.get(Calendar.MONTH) == 0) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) -1)
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1)
        }
        makeMonthDate(refreshCallback)
    }

    fun changeToNextMonth(refreshCallback: (Calendar) -> Unit) {
        //12월 다음 월은 년 수 변경
        if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
            calendar.set(Calendar.MONTH, 0)
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
        }
        makeMonthDate(refreshCallback)
    }

    fun discriminateFirstDate():Int {
        if(calendar.get(Calendar.DATE) == Calendar.FRIDAY || calendar.get(Calendar.DATE) == Calendar.THURSDAY) {
            LOW_OF_CALENDAR = 6
        } else LOW_OF_CALENDAR = 5

        return LOW_OF_CALENDAR
    }

}