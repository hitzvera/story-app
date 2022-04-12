package com.hitzvera.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hitzvera.storyapp.model.DetailUser

class DetailViewModel: ViewModel() {

    private var _dataItemUser = MutableLiveData<DetailUser>()
    val dataItem: LiveData<DetailUser> = _dataItemUser

    fun setDataItem(photoUrl: String, name: String, description: String) {
        val data = DetailUser(photoUrl, name, description)
        _dataItemUser.postValue(data)
    }

}