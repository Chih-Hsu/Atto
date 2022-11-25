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

    private const val STORAGE_PATH = "gs://atto-eaae3.appspot.com"


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

    override fun insert(timeZone: AttoTimeZone) {
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
                    if (appList.none { it.appLabel == newApp.appLabel }) {
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
    override suspend fun uploadData(context: Context, localAppList: List<App>): Result<Boolean> =
        suspendCoroutine { continuation ->

            val auth = FirebaseAuth.getInstance()
            val dataBase = FirebaseFirestore.getInstance()
            val remoteAppList = mutableListOf<App>()

            dataBase.collection("user")
                .whereEqualTo("email", auth.currentUser?.email)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val user = if (!task.result.isEmpty) {
                            task.result.documents.first().toObject(User::class.java)
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
                                syncRemoteAndLocalData(
                                    dataBase,
                                    currentUser,
                                    remoteAppList,
                                    localAppList
                                )
                                uploadLocalToRemote(localAppList, currentUser, dataBase)
                            }
                        }
                    } else {
                        task.exception?.let { e ->
                            continuation.resume(Result.Error(e))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("Upload Data Fail"))
                    }
                }
        }

    private fun uploadLocalToRemote(
        localAppList: List<App>,
        currentUser: User,
        dataBase: FirebaseFirestore
    ) {

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

            currentUser.email?.let {
                dataBase.collection("user")
                    .document()
                    .collection("App")
                    .document(app.appLabel)
                    .set(newApp)

                // Upload AppIconImage
                uploadImage(currentUser, app)
            }

        }
    }

    private fun syncRemoteAndLocalData(
        dataBase: FirebaseFirestore,
        currentUser: User,
        remoteAppList: MutableList<App>,
        localAppList: List<App>
    ) {
        currentUser.email?.let { email ->
            dataBase.collection("user")
                .document(email)
                .collection("App")
                .get()
                .addOnSuccessListener { apps ->

                    if (!apps.isEmpty) {
                        remoteAppList.addAll(apps.toObjects(App::class.java))

                        for (app in remoteAppList) {

                            if (localAppList.none { it.appLabel == app.appLabel }) {
                                dataBase.collection("user")
                                    .document(email)
                                    .collection("App")
                                    .document(app.appLabel)
                                    .delete()
                            }
                        }
                    }
                }
        }
    }

    override suspend fun uploadUser(user: User): Result<Boolean> =
        suspendCoroutine { continuation ->

            val dataBase = FirebaseFirestore.getInstance()

            // Check user data before upload
            dataBase.collection("user")
                .whereEqualTo("email", user.email)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val remoteUser =
                            if (!task.result.isEmpty) {
                                task.result.documents.first()
                                    .toObject(User::class.java)
                            } else null

                        // Do not update deviceId , if device is different
                        val newUser = User(
                            user.id,
                            user.email,
                            user.name,
                            user.image,
                            if (remoteUser != null || remoteUser?.deviceId == user.deviceId) user.deviceId else remoteUser?.deviceId
                        )

                        user.email?.let { email ->
                            dataBase.collection("user")
                                .document(email)
                                .set(newUser)

                            continuation.resume(Result.Success(true))
                        }

                    } else {

                        task.exception?.let { e ->
                            continuation.resume(Result.Error(e))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("Upload User Fail"))
                    }
                }
        }

    override fun getAllTimeZone():LiveData<List<AttoTimeZone>> {
        TODO("Not yet implemented")
    }

    override fun deleteTimeZone(id: Long) {
        TODO("Not yet implemented")
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
