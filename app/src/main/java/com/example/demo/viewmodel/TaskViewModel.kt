package com.example.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

    var taskLiveData: MutableLiveData<Any> = MutableLiveData<Any>()

}
