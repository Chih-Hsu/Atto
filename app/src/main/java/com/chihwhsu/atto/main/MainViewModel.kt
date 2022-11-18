package com.chihwhsu.atto.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    fun uploadData() {
        coroutineScope.launch(Dispatchers.Default) {

            val dataBase = FirebaseFirestore.getInstance()

            var user: User? = null
            val remoteAppList = mutableListOf<App>()
            val localAppList = repository.getAllAppNotLiveData()


            dataBase.collection("user")
                .whereEqualTo("idToken", UserManager.userToken)
                .get()
                .addOnSuccessListener {

                       user = it.documents.first().toObject(User::class.java)



                    user?.email?.let { email ->

                        dataBase.collection("user")
                            .document(email)
                            .collection("App")
                            .get()
                            .addOnSuccessListener { apps ->

                                remoteAppList.addAll(apps.toObjects(App::class.java))

                                for (app in remoteAppList) {
                                    if (localAppList?.filter { it.appLabel == app.appLabel }.isNullOrEmpty()) {
                                        dataBase.collection("user")
                                            .document(email)
                                            .collection("App")
                                            .document(app.appLabel)
                                            .delete()
                                    }



                                    localAppList?.let {
                                        for (app in it) {
                                            dataBase.collection("user")
                                                .document(email)
                                                .collection("App")
                                                .document(app.appLabel)
                                                .set(app)
                                        }
                                    }


                                }

                            }




















                   }
//                    user = it.toObjects(User::class.java).first()



            }
        }
    }
}