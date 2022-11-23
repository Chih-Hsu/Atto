package com.chihwhsu.atto.data.database.remote

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.*
import com.chihwhsu.atto.data.database.AttoDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AttoRemoteDataSource : AttoDataSource {

    const val STORAGE_PATH = "gs://atto-eaae3.appspot.com"


    override suspend fun insert(app: App) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(appLockTimer: AppLockTimer) {
        TODO("Not yet implemented")
    }

    override fun insert(widget: Widget) {
        TODO("Not yet implemented")
    }

    override suspend fun update(app: App) {
        TODO("Not yet implemented")
    }

    override fun updateLabel(appName: String, label: String?) {
        TODO("Not yet implemented")
    }

    override fun updateSort(appName: String, sort: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTheme(appName: String, theme: Int?) {
        TODO("Not yet implemented")
    }

    override fun updateAppInstalled(appName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun lockApp(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unLockAllApp() {
        TODO("Not yet implemented")
    }

    override fun unLockSpecificLabelApp(label: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun getApp(packageName: String): App? {
        TODO("Not yet implemented")
    }

    override fun getAllApps(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getNoLabelApps(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getSpecificLabelApps(label: String): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getAllAppsWithoutDock(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getLabelList(): LiveData<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAppData() {
        TODO("Not yet implemented")
    }

    override fun getAllAppNotLiveData(): List<App>? {
        TODO("Not yet implemented")
    }

    override fun deleteSpecificLabel(label: String) {
        TODO("Not yet implemented")
    }

    override fun updateIconPath(appName: String, path: String) {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(id: Int): Event {
        TODO("Not yet implemented")
    }

    override fun getTypeEvent(type: Int): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun delayEvent5Minutes(id: Int) {
        TODO("Not yet implemented")
    }

    override fun lockAllApp() {
        TODO("Not yet implemented")
    }

    override fun lockSpecificLabelApp(label: String) {
        TODO("Not yet implemented")
    }

    override fun isPomodoroIsExist(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTimer(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTimer() {
        TODO("Not yet implemented")
    }

    override suspend fun updateTimer(remainTime: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getTimer(packageName: String): AppLockTimer? {
        TODO("Not yet implemented")
    }

    override fun getAllTimer(): LiveData<List<AppLockTimer>> {
        TODO("Not yet implemented")
    }

    override fun getAllWidget(): LiveData<List<Widget>> {
        TODO("Not yet implemented")
    }

    override fun deleteWidget(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(email: String): Result<User> = suspendCoroutine { continuation ->

//        val auth = FirebaseAuth.getInstance()
        val dataBase = FirebaseFirestore.getInstance()

        var currentUser: User?

        dataBase.collection("user")
            .document(email)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    currentUser = task.result.toObject(User::class.java)

                    if (currentUser != null) {
                        continuation.resume(Result.Success(currentUser!!))
                    } else {
                        continuation.resume(Result.Fail("No User"))
                    }

                } else {

                    task.exception?.let { e ->

                        continuation.resume(Result.Error(e))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("Get User Fail"))
                }

            }
    }

    override suspend fun syncRemoteData(
        context: Context,
        user: User,
        appList: List<App>
    ): Result<List<App>> = suspendCoroutine { continuation ->

//        val auth = FirebaseAuth.getInstance()
        val dataBase = FirebaseFirestore.getInstance()

        updateDeviceId(user, context)

        dataBase.collection("user")
            .document(user.email!!)
            .collection("App")
            .get()
            .addOnCompleteListener { task ->


                val newAppList = mutableListOf<App>()

                for (item in task.result) {

                    val newApp = item.toObject(App::class.java)

                    // Check which app is not installed
                    if (appList.filter { it.appLabel == newApp.appLabel }
                            .isEmpty()) {
                        newApp.installed = false
                    }

                    newApp.iconPath =
                        context.filesDir.absolutePath + "/" + "${newApp.appLabel}.png"

                    newAppList.add(newApp)

                }

                if (newAppList.isNotEmpty()) {
                    continuation.resume(Result.Success(newAppList))
                } else {
                    continuation.resume(Result.Fail("No App List"))
                }
            }
    }

    @SuppressLint("HardwareIds")
    override fun uploadData(context: Context, localAppList: List<App>) {

        val auth = FirebaseAuth.getInstance()
        val dataBase = FirebaseFirestore.getInstance()

        val remoteAppList = mutableListOf<App>()
//        val localAppList = repository.getAllAppNotLiveData()

        dataBase.collection("user")
            .whereEqualTo("email", auth.currentUser?.email)
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
                    if (currentUser.deviceId == Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    ) {

                        // if remote have data but local not, then delete remote data
                        dataBase.collection("user")
                            .document(currentUser.email!!)
                            .collection("App")
                            .get()
                            .addOnSuccessListener { apps ->

                                if (!apps.isEmpty) {
                                    remoteAppList.addAll(apps.toObjects(App::class.java))

                                    for (app in remoteAppList) {

                                        if (localAppList.filter { it.appLabel == app.appLabel }
                                                .isEmpty()) {
                                            dataBase.collection("user")
                                                .document(currentUser.email)
                                                .collection("App")
                                                .document(app.appLabel)
                                                .delete()
                                        }
                                    }
                                }
                            }

                        for (app in localAppList) {
                            // Upload App Data
                            val newApp = App(
                                app.appLabel,
                                app.packageName,
                                "$STORAGE_PATH/${currentUser.email}/${app.appLabel}.png",
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

    override suspend fun uploadUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->

        val dataBase = FirebaseFirestore.getInstance()

        // Check user data before upload
        dataBase.collection("user")
            .whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener {

                val remoteUser =
                    if (!it.isEmpty) it.documents.first().toObject(User::class.java) else null

                if (remoteUser != null) {

                    if (remoteUser.deviceId != user.deviceId) {

                        // Do not update deviceId , if device is different
                        val newUser = User(
                            user.id,
                            user.email,
                            user.name,
                            user.image,
                            remoteUser.deviceId
                        )

                        user.email?.let {

                            dataBase.collection("user")
                                .document(user.email)
                                .set(newUser)

                            continuation.resume(Result.Success(true))
                        }


                    } else {
                        // if device is the same ,
                        user.email?.let {

                            dataBase.collection("user")
                                .document(user.email)
                                .set(user)

                            continuation.resume(Result.Success(true))
                        }


                    }
                    // if remoteData not exist, just upload data
                } else {
                    user.email?.let {
                        dataBase.collection("user")
                            .document(user.email)
                            .set(user)

                        continuation.resume(Result.Success(true))
                    }
                }
                // if remoteData not exist, just upload data
            }.addOnFailureListener {
                user.email?.let {
                    dataBase.collection("user")
                        .document(user.email)
                        .set(user)

                    continuation.resume(Result.Success(true))
                }
            }

    }

    private fun uploadImage(user: User, app: App) {

        val storage = FirebaseStorage.getInstance().reference
        val imageRef = storage.child("${user.email}/${app.appLabel}.png")
        val file = Uri.fromFile(File(app.iconPath))
        imageRef.putFile(file)

    }


    @SuppressLint("HardwareIds")
    private fun updateDeviceId(user: User, context: Context) {
        val dataBase = FirebaseFirestore.getInstance()
        dataBase.collection("user")
            .document(user.email!!)
            .update(
                "deviceId",
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            )
    }

}
