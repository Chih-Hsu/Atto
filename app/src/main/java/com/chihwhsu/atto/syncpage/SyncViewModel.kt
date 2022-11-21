package com.chihwhsu.atto.syncpage


import android.content.Context
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SyncViewModel(private val repository: AttoRepository) : ViewModel() {


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val dataBase = FirebaseFirestore.getInstance()

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        getUser()
    }


    private suspend fun getAppList(): List<App>? {
        return repository.getAllAppNotLiveData()
    }

    private fun getUser() {

        UserManager.userEmail?.let {

            dataBase.collection("user")
                .document(it)
                .get()
                .addOnSuccessListener {
                    val currentUser =  it.toObject(User::class.java)
                    currentUser?.let {
                        _user.value = currentUser
                    }

                }


        }

    }


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

    private fun updateDeviceId(user: User, context: Context) {
        dataBase.collection("user")
            .document(user.email!!)
            .update(
                "deviceId",
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            )
    }


}
