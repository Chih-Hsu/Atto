package com.chihwhsu.atto.appdetail

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import kotlinx.coroutines.*


class AppDetailViewModel(private val databaseDao: AttoDatabaseDao, private val argument: App) :
    ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _app = MutableLiveData<App>().apply {
        value = argument
    }
    val app: LiveData<App> get() = _app

    private var _dataPerHourList = MutableLiveData<List<Float>>()
    val dataPerHourList: LiveData<List<Float>> get() = _dataPerHourList

    private var _barSet = MutableLiveData<List<Pair<String, Float>>>()
    val barSet: LiveData<List<Pair<String, Float>>> get() = _barSet

    private var _navigateUp = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val navigateUp: LiveData<Boolean> get() = _navigateUp

    init {
    }

    fun setPerHourList(list: List<Float>) {
        _dataPerHourList.value = list
    }

    fun createBarSet() {

        val list = dataPerHourList.value

        list?.let {
            val perHourBarSet = listOf(
                "00" to list[0],
                "." to list[1],
                "." to list[2],
                "." to list[3],
                "04" to list[4],
                "." to list[5],
                "." to list[6],
                "." to list[7],
                "08" to list[8],
                "." to list[9],
                "." to list[10],
                "." to list[11],
                "12" to list[12],
                "." to list[13],
                "." to list[14],
                "." to list[15],
                "16" to list[16],
                "." to list[17],
                "." to list[18],
                "." to list[19],
                "20" to list[20],
                "." to list[21],
                "." to list[22],
                "." to list[23],
                "24" to list[23],
            )
            _barSet.value = perHourBarSet
        }
    }

    fun updateTheme(theme: Theme) {

        coroutineScope.launch(Dispatchers.IO) {
            app.value?.let {
                if (theme.index != it.theme) {
                    databaseDao.updateTheme(it.appLabel, theme.index)
                }
                withContext(Dispatchers.Main) {
                    _navigateUp.value = true
                }
            }
        }
    }

    fun doneNavigation() {
        _navigateUp.value = false
    }

    fun getNetUsage(context: Context, currentApp: App) {

        val packageManager = context.packageManager

        val info = packageManager.getApplicationInfo(currentApp.packageName, 0)

        val uid = info.uid

        val networkStatsManager =
            context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val telephoneManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

//        val subscribeId = telephoneManager.


        val summaryBucket = NetworkStats.Bucket()
        val state = networkStatsManager.queryDetailsForUid(
            ConnectivityManager.TYPE_MOBILE,
            null,
            0,
            System.currentTimeMillis(),
            uid
        )

        var rxTraffic = 0L
        var txTraffic = 0L

        state.getNextBucket(summaryBucket)


            rxTraffic += summaryBucket.getRxBytes()
            txTraffic += summaryBucket.getTxBytes()


        Log.d("static", "rx = $rxTraffic  tx = $txTraffic")

    }
}