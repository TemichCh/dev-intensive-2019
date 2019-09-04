package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false


) {
    fun unreadableMessageCount(): Int {
        //TODO implement
        return 0
    }

    private fun lastMessageDate(): Date? {
        //TODO implement
        return null
    }

    private fun lastMessageShort(): String {
        //TODO implement
        return "Сообщений еще нет"
    }

    private fun isSingle(): Boolean = members.size == 1
    fun toChatItem(): ChatItem {
       return if (isSingle()){
            val user= members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName,user.lastName)?:"??",
                "${user.firstName?:""} ${user.lastName ?:""}",
                lastMessageShort(),
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline

            )
        }else        {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort(),
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false
            )



        }

    }

}



