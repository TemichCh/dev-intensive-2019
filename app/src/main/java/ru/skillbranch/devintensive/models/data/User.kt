package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false
) {
    fun toUserItem(): UserItem {
        val lastActivity = when {
            lastVisit == null -> "Еще ни разу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
            id,
            "${firstName.orEmpty()} ${lastName.orEmpty()}",
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivity,
            false,
            isOnline
        )
    }

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    //var introBit: String = getIntro()

    constructor(id: String) : this(id, "John", "Doe")


    companion object Factory {
        private var lastId: Int = -1

        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }


    }

    data class Builder(
        var b_id: String = "",
        var b_firstName: String? = null,
        var b_lastName: String? = null,
        var b_avatar: String? = null,
        var b_rating: Int = 0,
        var b_respect: Int = 0,
        var b_lastVisit: Date? = null,
        var b_isOnline: Boolean = false
    ) {
        fun id(id: String) = apply { this.b_id = id }
        fun firstName(firstName: String?) = apply { this.b_firstName = firstName }
        fun lastName(lastName: String?) = apply { this.b_lastName = lastName }
        fun avatar(avatar: String?) = apply { this.b_avatar = avatar }
        fun rating(rating: Int) = apply { this.b_rating = rating }
        fun respect(respect: Int) = apply { this.b_respect = respect }
        fun lastVisit(lastVisit: Date?) = apply { this.b_lastVisit = lastVisit }
        fun isOnline(isOnline: Boolean) = apply { this.b_isOnline = isOnline }
        fun build() = User(
            b_id,
            b_firstName,
            b_lastName,
            b_avatar,
            b_rating,
            b_respect,
            b_lastVisit,
            b_isOnline
        )
    }


}