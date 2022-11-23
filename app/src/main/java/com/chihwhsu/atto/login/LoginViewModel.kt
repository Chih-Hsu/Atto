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

//
//        val auth = FirebaseAuth.getInstance()
//
//        _user.value = user
////        _navigateToSync.value = true
//
//        // Check user data before upload
//        dataBase.collection("user")
//            .whereEqualTo("email", auth.currentUser?.email)
//            .get()
//            .addOnSuccessListener {
//
//                val remoteUser =
//                    if (!it.isEmpty) it.documents.first().toObject(User::class.java) else null
//
//                user.email?.let {
//
//                    if (remoteUser != null) {
//
//                        if (remoteUser.deviceId != user.deviceId) {
//
//                            // Do not update deviceId , if device is different
//                            val newUser = User(
//                                user.id,
//                                user.email,
//                                user.name,
//                                user.image,
//                                remoteUser.deviceId
//                            )
//
//                            dataBase.collection("user")
//                                .document(user.email)
//                                .set(newUser)
//
//                        } else {
//                            // if device is the same ,
//                            dataBase.collection("user")
//                                .document(user.email)
//                                .set(user)
//
//                        }
//                        // if remoteData not exist, just upload data
//                    } else {
//                        dataBase.collection("user")
//                            .document(user.email)
//                            .set(user)
//
//                    }
//                }
//                // if remoteData not exist, just upload data
//            }.addOnFailureListener {
//
//                user.email?.let {
//                    dataBase.collection("user")
//                        .document(user.email)
//                        .set(user)
//                }
//            }
    }
}