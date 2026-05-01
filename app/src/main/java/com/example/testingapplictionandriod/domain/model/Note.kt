package com.example.testingapplictionandriod.domain.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String = "",
    val tag: String = "",
    val colorIndex: Int = 0,
    val createdYear: Int,
    val createdMonth: Int,
    val createdDay: Int
)
