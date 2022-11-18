package com.chihwhsu.atto.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.User
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user : LiveData<User> get() = _user

    private val dataBase = FirebaseFirestore.getInstance()

    fun uploadUser(currentUser: User){

        _user.value = currentUser

        // upload to firebase
        currentUser.email?.let {
            dataBase.collection("user")
                .document(it)
                .set(currentUser)
        }

    }

}