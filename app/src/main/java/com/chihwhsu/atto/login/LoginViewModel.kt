package com.chihwhsu.atto.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {



    private var _navigateToSync = MutableLiveData<Boolean>()
    val navigateToSync: LiveData<Boolean> get() = _navigateToSync

    private var _user = MutableLiveData<User>()
    val user : LiveData<User> get() = _user

    private val dataBase = FirebaseFirestore.getInstance()


    fun uploadUser(user: User) {

        val auth = FirebaseAuth.getInstance()

        _user.value = user
//        _navigateToSync.value = true

        // Check user data before upload
        dataBase.collection("user")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnSuccessListener {

                val remoteUser =
                    if (!it.isEmpty) it.documents.first().toObject(User::class.java) else null

                if (remoteUser != null) {

                    if (remoteUser.deviceId != user.deviceId) {
                        Log.d("test", "remoteUser.deviceId != user.deviceId")

                        // Do not update deviceId , if device is different
                        val newUser = User(
                            user.id,
                            user.email,
                            user.name,
                            user.image,
                            remoteUser.deviceId
                        )

                        dataBase.collection("user")
                            .document(user.email!!)
                            .set(newUser)

                    } else {
                        // if device is the same ,
                        dataBase.collection("user")
                            .document(user.email!!)
                            .set(user)

                        Log.d("test", "remoteUser.deviceId == user.deviceId")
                    }

                    // if remoteData not exist, just upload data
                } else {
                    Log.d("test", "no remoteUser")
                    dataBase.collection("user")
                        .document(user.email!!)
                        .set(user)

                }
                // if remoteData not exist, just upload data
            }.addOnFailureListener {

                dataBase.collection("user")
                    .document(user.email!!)
                    .set(user)
            }


    }
}