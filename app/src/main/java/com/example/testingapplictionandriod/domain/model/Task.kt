package com.example.testingapplictionandriod.domain.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val notes: String = "",
    val priority: TaskPriority = TaskPriority.NONE,
    val dueYear: Int? = null,
    val dueMonth: Int? = null,
    val dueDay: Int? = null,
    val dueHour: Int? = null,
    val dueMinute: Int? = null,
    val isCompleted: Boolean = false
)

enum class TaskPriority { HIGH, MEDIUM, LOW, NONE }
