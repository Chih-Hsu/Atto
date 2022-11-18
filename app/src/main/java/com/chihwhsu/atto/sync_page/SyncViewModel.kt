package com.chihwhsu.atto.sync_page


import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.database.AttoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SyncViewModel(private val repository: AttoRepository) : ViewModel() {


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val dataBase = FirebaseFirestore.getInstance()


    private suspend fun getAppList(): List<App>? {
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

        coroutineScope.launch(Dispatchers.Default) {

            val appList = getAppList()

            dataBase.collection("user")
                .document(user.email!!)
                .collection("App")
                .get()
                .addOnSuccessListener { list ->
                    for (item in list) {
                        coroutineScope.launch(Dispatchers.Default) {
                            val newApp = item.toObject(App::class.java)

                            // Check which app is not installed
                            if (appList?.filter { it.appLabel == newApp.appLabel }
                                    .isNullOrEmpty()) {
                                newApp.installed = false
                            }
                            repository.insert(newApp)

                        }
                    }
                }
        }
    }


}