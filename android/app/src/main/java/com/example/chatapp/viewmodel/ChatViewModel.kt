package com.example.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Chat
import com.example.chatapp.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> = _chatList

    private val _newChat = MutableLiveData<Chat>()
    val newChat: LiveData<Chat> = _newChat

    init {
        repository.onNewChat.observeForever { chat ->
            _newChat.value = chat
            addChatToList(chat)
        }
    }

    fun sendMessage(username: String, message: String) {
        val chat = Chat(username = username, text = message)
        viewModelScope.launch {
            repository.sendChat(chat)
        }
    }

    private fun addChatToList(chat: Chat) {
        val currentList = _chatList.value ?: emptyList()
        _chatList.value = currentList + chat
    }

    fun setUserName(username: String) {
        repository.setUserName(username)
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectSocket()
    }
}