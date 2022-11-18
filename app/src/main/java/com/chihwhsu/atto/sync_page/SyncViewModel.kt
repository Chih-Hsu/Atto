package com.chihwhsu.atto.sync_page


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class SyncViewModel(private val repository: AttoRepository) : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val dataBase = FirebaseFirestore.getInstance()


//    fun setUser(user:User) {
//        _user.value = user
//    }

    suspend fun getAppList(): List<App>? {
        return repository.getAllAppNotLiveData()
    }

//    fun uploadData(user:User){
//        coroutineScope.launch(Dispatchers.Default) {
//
//            val appList = getAppList()
//
//            appList?.let {
//
//                for (app in it) {
//                    dataBase.collection("user")
//                        .document(user.email!!)
//                        .collection("App")
//                        .document(app.appLabel)
//                        .set(app)
//
//                }
//            }
//        }
//    }

    fun getData(user: User) {


        dataBase.collection("user")
            .document(user.email!!)
            .collection("App")
            .get()
            .addOnSuccessListener {
                for (item in it) {
                    coroutineScope.launch(Dispatchers.Default) {
                        val newApp = item.toObject(App::class.java)
                        repository.insert(newApp)
                        Log.d("getFirebase", "$newApp")
                    }
                }
            }
    }


}