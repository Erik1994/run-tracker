package com.example.wear.run.presentation

import com.example.presentation.ui.UiText
import com.example.wear.run.domain.ExerciseError

fun ExerciseError.toUiText(): UiText? {
    return when (this) {
        ExerciseError.ONGOING_OWN_EXERCISE,
        ExerciseError.ONGOING_OTHER_EXERCISE -> UiText.StringResource(R.string.error_ongoing_exercise)
        ExerciseError.EXERCISE_ALREADY_ENDED -> UiText.StringResource(R.string.error_exercise_already_ended)
        ExerciseError.UNKNOWN -> UiText.StringResource(com.example.presentation.ui.R.string.error_unknown)
        ExerciseError.TRACKING_NOT_SUPPORTED -> null
    }
}