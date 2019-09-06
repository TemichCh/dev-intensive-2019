package ru.skillbranch.devintensive.models.data

import android.util.Log
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false


) {
    fun unreadableMessageCount(): Int {
        val count = messages.filter { (it.isReaded==false) }.count() ?: 0
        Log.d("M_Chat","Непрочитанных сообщений = $count")
        return count
    }

    fun lastMessageDate(): Date? {
        val lastd = messages.maxBy { it.date }?.date
        return lastd
    }

    fun lastMessageShort(): Pair<String, String> {
        val lastm = messages.maxBy { it.date }
        var text: String
        var auth = ""
        if (lastm != null) {
            when {
                lastm is TextMessage -> text = lastm.text ?: ""
                else -> text = " ${lastm.from!!.firstName ?: "@John_Doe"} отправил фото"
            }
            auth = lastm.from!!.firstName ?: "@John_Doe"
        } else text = "Сообщений еще нет"

        return text to auth
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second

            )


        }

    }


}


enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}


