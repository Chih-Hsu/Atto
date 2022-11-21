package com.chihwhsu.atto.main

import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.io.File

class MainViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    var dockList = repository.getSpecificLabelApps("dock")
    val timerList = repository.getAllTimer()

    init {

    }

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
            AttoApplication.instance.attoRepository.updateAppData()
        }
    }

    fun uploadData(context: Context) {
        coroutineScope.launch(Dispatchers.Default) {

            val dataBase = FirebaseFirestore.getInstance()

            val remoteAppList = mutableListOf<App>()
            val localAppList = repository.getAllAppNotLiveData()


            dataBase.collection("user")
                .whereEqualTo("email", UserManager.userEmail)
                .get()
                .addOnSuccessListener {

                    val user = if (!it.isEmpty) {
                        it.documents.first().toObject(User::class.java)
                    } else {
                        null
                    }




                    user?.let { currentUser ->

                        // To Avoid newPhone data overlay remote data,
                        // when user click sync button, the deviceId will update
                        if (currentUser.deviceId == Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)){

                        // if remote have data but local not, then delete remote data
                        dataBase.collection("user")
                            .document(currentUser.email!!)
                            .collection("App")
                            .get()
                            .addOnSuccessListener { apps ->

                                if (!apps.isEmpty) {
                                    remoteAppList.addAll(apps.toObjects(App::class.java))

                                    for (app in remoteAppList) {

                                        if (localAppList?.filter { it.appLabel == app.appLabel }
                                                .isNullOrEmpty()) {
                                            dataBase.collection("user")
                                                .document(currentUser.email)
                                                .collection("App")
                                                .document(app.appLabel)
                                                .delete()
                                        }
                                    }
                                }
                            }

                        localAppList?.let { list ->

                            for (app in list) {
                                // Upload App Data
                                val newApp = App(
                                    app.appLabel,
                                    app.packageName,
                                    "gs://atto-eaae3.appspot.com/${currentUser.email}/${app.appLabel}.png",
                                    app.label,
                                    app.isEnable,
                                    app.theme,
                                    app.installed,
                                    app.sort
                                )
                                dataBase.collection("user")
                                    .document(currentUser.email)
                                    .collection("App")
                                    .document(app.appLabel)
                                    .set(newApp)

                                // Upload AppIconImage
                                uploadImage(user, app)

                            }
                        }


                    }
                    }
                }
        }
    }

    private fun uploadImage(user: User, app: App) {
        coroutineScope.launch(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance().reference
            val imageRef = storage.child("${user.email}/${app.appLabel}.png")
            val file = Uri.fromFile(File(app.iconPath))
            imageRef.putFile(file)
        }

    }


}