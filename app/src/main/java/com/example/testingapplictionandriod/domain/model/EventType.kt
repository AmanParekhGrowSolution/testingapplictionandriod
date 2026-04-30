package com.example.testingapplictionandriod.domain.model

import androidx.annotation.StringRes
import com.example.testingapplictionandriod.R

enum class EventType(@StringRes val labelRes: Int) {
    WORK(R.string.event_type_work),
    PERSONAL(R.string.event_type_personal),
    HEALTH(R.string.event_type_health),
    SOCIAL(R.string.event_type_social),
    OTHER(R.string.event_type_other)
}
