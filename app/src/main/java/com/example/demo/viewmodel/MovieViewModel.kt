package com.example.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.api.ResponseSealedClass
import com.example.demo.testing.flow.FlowTesting
import com.example.demo.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    var movieLiveData: MutableLiveData<ResponseSealedClass<out Any>> =
        MutableLiveData<ResponseSealedClass<out Any>>()

    fun getPopularMovies() {
        viewModelScope.launch {
            getMovies()
        }
    }

    private fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = RetrofitInstance.api.getPopularMovies("69d66957eebff9666ea46bd464773cf0")
                    .execute()
                movieLiveData.postValue(ResponseSealedClass.Success(resp.body()!!))
            } catch (exception: Exception) {
                movieLiveData.postValue(ResponseSealedClass.Failure<String>(exception.toString()))
            }
        }
    }
}
