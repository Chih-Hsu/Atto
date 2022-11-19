package com.chihwhsu.atto.sync_page


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException

class SyncViewModel(private val repository: AttoRepository) : ViewModel() {


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val dataBase = FirebaseFirestore.getInstance()


    private suspend fun getAppList(): List<App>? {
        return repository.getAllAppNotLiveData()
    }


    fun getData(user: User,context:Context) {

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

                            storeRemoteIconInLocal(context,user,newApp)

                            // Check which app is not installed
                            if (appList?.filter { it.appLabel == newApp.appLabel }
                                    .isNullOrEmpty()) {
                                newApp.installed = false
                            }

                            newApp.iconPath = context.filesDir.absolutePath +"/"+"${newApp.appLabel}.png"

                            repository.insert(newApp)

                        }
                    }
                }
        }
    }

   private suspend fun storeRemoteIconInLocal(context: Context,user: User,app: App){
        val storage = FirebaseStorage.getInstance().reference
        val imageRef = storage.child("${user.email}/${app.appLabel}.png")

        imageRef.getFile(Uri.parse(context.filesDir.absolutePath +"/"+app.appLabel+ ".png")).await()
    }

    private fun updateDeviceId(user: User,context: Context){
        dataBase.collection("user")
            .document(user.email!!)
            .update("deviceId", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
    }




}
