package com.chihwhsu.atto.syncpage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Result
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.remote.LoadStatus
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
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

    private var _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    private fun getAppList(): List<App>? {
        return repository.getAllAppNotLiveData()
    }


    fun getUser(email: String) {

        _status.value = LoadStatus.LOADING

        coroutineScope.launch {

            when (val result = repository.getUser(email)) {

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


    fun syncData(context: Context, user: User) {

        _status.value = LoadStatus.LOADING

        coroutineScope.launch(Dispatchers.IO) {

            val appList = getAppList()
            appList?.let {

                when (val result = repository.syncRemoteData(context, user, it)) {

                    is Result.Success -> {

                        for (app in result.data) {
                            runDataSync(context, user, app)
                        }

                        withContext(Dispatchers.Main) {
                            _error.value = null
                            _status.value = LoadStatus.DONE
                            _navigateToMain.value = true
                        }
                    }

                    is Result.Fail -> {
                        withContext(Dispatchers.Main) {
                            _error.value = result.error
                            _status.value = LoadStatus.ERROR
                        }
                    }

                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _error.value = "Error"
                            _status.value = LoadStatus.ERROR
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            _error.value = "Error"
                            _status.value = LoadStatus.ERROR
                        }
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

    fun doneNavigation() {
        _navigateToMain.value = false
    }
}
