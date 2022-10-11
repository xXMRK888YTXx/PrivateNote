package com.xxmrk888ytxx.privatenote.Utils

sealed class LoadRepositoryState {
    object Loaded : LoadRepositoryState()
    object LoadNewFile : LoadRepositoryState()
}