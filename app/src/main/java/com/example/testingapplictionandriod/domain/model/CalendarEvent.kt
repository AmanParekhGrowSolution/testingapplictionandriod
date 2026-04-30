package com.example.testingapplictionandriod.domain.model

data class CalendarEvent(
    val id: String,
    val title: String,
    val description: String = "",
    val type: EventType,
    val year: Int,
    val month: Int,
    val day: Int,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int
)
