package com.chihwhsu.atto.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Result
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.remote.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AttoRepository) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    var dockList = repository.getSpecificLabelApps(DOCK)

    val timerList = repository.getAllTimer()

    fun checkUsageTimer(context: Context) {

        timerList.value?.let { timers ->
            coroutineScope.launch(Dispatchers.Default) {

                for (timer in timers) {

                    val app = repository.getApp(timer.packageName)

                    app?.let {
                        val usageTime = it.getUsageTimeFromStart(context, timer.startTime)
                        if (usageTime >= timer.targetTime) {
                            repository.lockApp(it.packageName)
                            repository.deleteTimer(timer.id)
                        } else {
                            repository.updateTimer(timer.targetTime - usageTime)
                        }
                    }
                }
            }
        }
    }


    fun updateApp() {
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateAppData()
        }
    }

    fun uploadData(context: Context, email: String){

        _status.value = LoadStatus.LOADING

        coroutineScope.launch(Dispatchers.Default) {

            val localAppList = repository.getAllAppNotLiveData()

            localAppList?.let { appList ->
                when(val result = repository.uploadData(context,appList, email) ){

                    is Result.Success -> {
                        _status.value = LoadStatus.DONE
                        _error.value = null
                    }

                    is Result.Fail -> {
                        _status.value = LoadStatus.ERROR
                        _error.value = result.error
                    }

                    is Result.Error -> {
                        _status.value = LoadStatus.ERROR
                        _error.value = result.exception.toString()
                    }

                    else -> {
                        _status.value = LoadStatus.ERROR

                    }
                }
            }
        }
    }

//    fun uploadData(context: Context) {
//        coroutineScope.launch(Dispatchers.Default) {
//
//            val auth = FirebaseAuth.getInstance()
//            val dataBase = FirebaseFirestore.getInstance()
//
//            val remoteAppList = mutableListOf<App>()
//            val localAppList = repository.getAllAppNotLiveData()
//
//            dataBase.collection("user")
//                .whereEqualTo("email", auth.currentUser?.email)
//                .get()
//                .addOnSuccessListener {
//
//                    val user = if (!it.isEmpty) {
//                        it.documents.first().toObject(User::class.java)
//                    } else {
//                        null
//                    }
//
//                    user?.let { currentUser ->
//
//                        // To Avoid newPhone data overlay remote data,
//                        // when user click sync button, the deviceId will update
//                        if (currentUser.deviceId == Settings.Secure.getString(
//                                context.contentResolver,
//                                Settings.Secure.ANDROID_ID
//                            )
//                        ) {
//
//                            // if remote have data but local not, then delete remote data
//                            dataBase.collection("user")
//                                .document(currentUser.email!!)
//                                .collection("App")
//                                .get()
//                                .addOnSuccessListener { apps ->
//
//                                    if (!apps.isEmpty) {
//                                        remoteAppList.addAll(apps.toObjects(App::class.java))
//
//                                        for (app in remoteAppList) {
//
//                                            if (localAppList?.filter { it.appLabel == app.appLabel }
//                                                    .isNullOrEmpty()) {
//                                                dataBase.collection("user")
//                                                    .document(currentUser.email)
//                                                    .collection("App")
//                                                    .document(app.appLabel)
//                                                    .delete()
//                                            }
//                                        }
//                                    }
//                                }
//
//                            localAppList?.let { list ->
//
//                                for (app in list) {
//                                    // Upload App Data
//                                    val newApp = App(
//                                        app.appLabel,
//                                        app.packageName,
//                                        "gs://atto-eaae3.appspot.com/${currentUser.email}/${app.appLabel}.png",
//                                        app.label,
//                                        app.isEnable,
//                                        app.theme,
//                                        app.installed,
//                                        app.sort
//                                    )
//                                    dataBase.collection("user")
//                                        .document(currentUser.email)
//                                        .collection("App")
//                                        .document(app.appLabel)
//                                        .set(newApp)
//
//                                    // Upload AppIconImage
//                                    uploadImage(user, app)
//                                }
//                            }
//                        }
//                    }
//                }
//        }
//    }

//    private fun uploadImage(user: User, app: App) {
//
//        coroutineScope.launch(Dispatchers.IO) {
//
//            val storage = FirebaseStorage.getInstance().reference
//            val imageRef = storage.child("${user.email}/${app.appLabel}.png")
//            val file = Uri.fromFile(File(app.iconPath))
//            imageRef.putFile(file)
//        }
//    }

    companion object{
        private const val DOCK = "dock"
    }
}