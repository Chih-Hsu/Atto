package com.chihwhsu.atto.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Result
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.remote.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(val repository: AttoRepository) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun uploadUser(user: User) {

        _status.value = LoadStatus.LOADING

        coroutineScope.launch {
            when(val result = repository.uploadUser(user)){

                is Result.Success ->{
                    _error.value = null
                    _status.value = LoadStatus.DONE
                    _user.value = user
                }

                is Result.Fail ->{
                    _error.value = result.error
                    _status.value = LoadStatus.ERROR
                }

                is Result.Error->{
                    _error.value = result.exception.toString()
                    _status.value = LoadStatus.ERROR
                }

                else -> {
                    _error.value = "Error"
                    _status.value = LoadStatus.ERROR
                }
            }
        }
    }
}