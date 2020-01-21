/*
 - author: rafei
 - date: 19/09/2019
 - Singleton class for storing ChatUser objects
 */
import kotlinx.serialization.json.Json
import kotlinx.serialization.set
import java.util.*
import kotlin.collections.HashSet
object ChatUsersRepository {
    private val activeUserNameData = HashSet<ChatUser>()
    //inserts new ChatUser to chatUserRepository
    fun insertUser(user: ChatUser): Boolean{
       return this.activeUserNameData.add(user)
    }

    //removes new ChatUser from chatUserRepository
    fun removeUser(user: ChatUser): Boolean{
        return this.activeUserNameData.remove(user)
    }

    //returns activeChatUserData list in the form of Json string
    fun activeChatUserDataJSonString(): String{
        return Json.stringify(ChatUser.serializer().set, activeUserNameData)
    }

    //checks if ChatUser already exists
    fun userExists(user: ChatUser): Boolean{
        return this.activeUserNameData.contains(user)
    }

    //returns nicely formatted string of activeChatUsers
    override fun toString(): String {
        var returnString = ""
        var count = 1
        this.activeUserNameData.forEach{
            returnString += count.toString() + ": " + it + " : " + it.getMessageCount() + "\n\r"
            count++
        }
        return returnString
    }

    //return sorted list of active chat users (by number of sent messages)
    fun topChatters(): MutableList<ChatUser>{
        //changing hashset to list data structure
        val listOfActiveChatUsers = this.activeUserNameData.toMutableList()
        //for testing println("for testing: " + listOfActiveChatUsers.toString())
        //sorting ChatUsers in descending order based on messages sent
        Collections.sort(listOfActiveChatUsers, SortChatUsersByMessageCount())
        // for testing println("after sorting: " + listOfActiveChatUsers.toString())
        return listOfActiveChatUsers
    }
}