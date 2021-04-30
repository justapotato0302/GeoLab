package com.example.geolab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class GameVIewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _currentQuestionCount = MutableLiveData(1)
    val currentQuestionCount: LiveData<Int>
        get() = _currentQuestionCount


    fun reinitializeData() {
        _currentQuestionCount.value = 1
        _score.value = 0
    }

    fun nextQuestion(): Boolean {
        return _currentQuestionCount.value!! <= MAX_NO_OF_QUESTION
    }

    fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun increaseCurrentQuestionCount() {
        _currentQuestionCount.value = (_currentQuestionCount.value)?.plus(QUESTION_COUNT)
    }


}