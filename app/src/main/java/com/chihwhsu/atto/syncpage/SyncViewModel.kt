package com.chihwhsu.atto.syncpage


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Result
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.remote.LoadStatus
import com.chihwhsu.atto.data.succeeded
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SyncViewModel(private val repository: AttoRepository) : ViewModel() {

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Firebase
    private val dataBase = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        getUser()
    }


    private suspend fun getAppList(): List<App>? {
        return repository.getAllAppNotLiveData()
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun getUser(){

        coroutineScope.launch {

            _status.value = LoadStatus.LOADING

            when(val result = repository.getUser()){

                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadStatus.DONE
                    _user.value = result.data
                }

                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadStatus.ERROR

                }

                is Result.Error -> {
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

//    private fun getUser() {
//
//        auth.currentUser?.email?.let {
//            dataBase.collection("user")
//                .document(it)
//                .get()
//                .addOnSuccessListener { document ->
//
//                    val currentUser = document.toObject(User::class.java)
//
//                    currentUser?.let { newUser ->
//                        _user.value = newUser
//                    }
//
//                }
//        }
//    }


    fun getData(user: User, context: Context) {

        coroutineScope.launch(Dispatchers.Default) {

            updateDeviceId(user, context)

            val appList = getAppList()

            dataBase.collection("user")
                .document(user.email!!)
                .collection("App")
                .get()
                .addOnSuccessListener { list ->
                    for (item in list) {
                        coroutineScope.launch(Dispatchers.IO) {
                            val newApp = item.toObject(App::class.java)


                            // Check which app is not installed
                            if (appList?.filter { it.appLabel == newApp.appLabel }
                                    .isNullOrEmpty()) {
                                newApp.installed = false
                            }

                            newApp.iconPath =
                                context.filesDir.absolutePath + "/" + "${newApp.appLabel}.png"

                            runDataSync(context, user, newApp)

                        }
                    }
                }
        }
    }

    private suspend fun runDataSync(context: Context, user: User, app: App) {
        syncLocalAppLabelWithRemote(context, user, app)
        syncLocalAppPicWithRemote(app)
    }

    private suspend fun syncLocalAppLabelWithRemote(context: Context, user: User, app: App) {
        val storage = FirebaseStorage.getInstance().reference
        val imageRef = storage.child("${user.email}/${app.appLabel}.png")

        imageRef.getFile(Uri.parse(context.filesDir.absolutePath + "/" + app.appLabel + ".png"))
            .await()
    }

    private suspend fun syncLocalAppPicWithRemote(newApp: App) {
        repository.insert(newApp)
    }

    @SuppressLint("HardwareIds")
    private fun updateDeviceId(user: User, context: Context) {
        dataBase.collection("user")
            .document(user.email!!)
            .update(
                "deviceId",
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            )
    }


}
