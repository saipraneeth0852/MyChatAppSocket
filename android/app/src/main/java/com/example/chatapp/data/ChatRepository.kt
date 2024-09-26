package com.example.chatapp.data

import androidx.lifecycle.LiveData
import com.example.chatapp.model.Chat

class ChatRepository(private val socketHandler: SocketHandler) {

    val onNewChat: LiveData<Chat> = socketHandler.onNewChat

    private var userName: String = ""

    fun sendChat(chat: Chat) {
        val chatWithSelfFlag = chat.copy(isSelf = chat.username == userName)
        socketHandler.emitChat(chatWithSelfFlag)
    }

    fun setUserName(username: String) {
        userName = username
    }

    fun disconnectSocket() {
        socketHandler.disconnectSocket()
    }
}