package com.chihwhsu.atto.data

sealed class AppListItem {

    abstract val id: Long

    data class LabelItem(val title: String, val time: Long) : AppListItem() {
        override val id: Long = -1
    }
    data class AppItem(val app: App) : AppListItem() {
        override val id: Long = -1
    }
}
