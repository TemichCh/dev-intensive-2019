package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.TimeUnit
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.toUserView
import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_instance() {
        val user2 = User("2", "John", "Cena")
        print("$user2")
    }

    @Test

    fun test_fuctory() {
//        val user = User.makeUser("John Cena")
//        val user3 = User.makeUser("John Silverhand")
        val user = User.makeUser("John Wick") //
        val user2 = user.copy("2", lastName = "Cena", lastVisit = Date())

        print("$user \n$user2")
    }

    @Test
    fun test_decomposition() {
        val user = User.makeUser("John Wick")

        fun getUserIfo() = user

        val (id, firstName, lastName) = getUserIfo()

        println("$id , $firstName, $lastName")
        println("${user.component1()} , ${user.component2()}, ${user.component3()}")
    }

    @Test
    fun test_copy() {
        val user = User.makeUser("John Wick")
        val user2 = user.copy(lastVisit = Date())
        val user3 = user.copy(lastVisit = Date().add(-2, TimeUnit.SECOND))
        val user4 = user.copy(lastVisit = Date().add(2, TimeUnit.HOUR))

        println(
            """
            ${user.lastVisit?.format()}
            ${user2.lastVisit?.format()}
            ${user3.lastVisit?.format()}
            ${user4.lastVisit?.format()}
        """.trimIndent()
        )

        /* if (user.equals(user2))
             println(" equals data and hash ${user.hashCode()} $user \n ${user2.hashCode()} $user2")
         else
             println("not equals data and hash ${user.hashCode()} $user \n ${user2.hashCode()} $user2")

         if (user === user2)
             println(" equals address ${System.identityHashCode(user)}  ${System.identityHashCode(user2)}")
         else
             println("not equals address ${System.identityHashCode(user)}  ${System.identityHashCode(user2)}")
 */

    }


    @Test
    fun test_dataq_maping() {
        val user = User.makeUser("Черников Артем")
        val newUser = user.copy(lastVisit = Date().add(-4, TimeUnit.MINUTE))
        println(newUser)
        val userView = newUser.toUserView()
        userView.printMe()
    }


    @Test
    fun test_initials() {
        println(Utils.toInitials("john", "doe")) //JD
        println(Utils.toInitials("John", null)) //J
        println(Utils.toInitials(null, null)) //null
        println(Utils.toInitials(" ", "")) //null
    }


    @Test
    fun test_transliterations() {
        println(Utils.transliteration("Женя стереотипов")) //
        println(Utils.transliteration("Amazing Петр", "_")) //

    }

    @Test
    fun test_abstract_factory() {
        val user = User.makeUser("Черников Артем")
        val txtMessage = BaseMessage.makeMessage(user, Chat("0"), payload = "any text message", type = "text")
        val imgMessage = BaseMessage.makeMessage(user, Chat("0"), payload = "any image url", type = "image")
/*
        when (txtMessage) {
            is TextMessage -> println("this is text mressage")
            is ImageMessage -> println("this is image message")
        }*/

        println(txtMessage.formatMessage())
        println(imgMessage.formatMessage())
    }
}
