/*
 - author: rafei
 - date: 19/09/2019
 - class defines structure of ChatUser
 */
import kotlinx.serialization.Serializable

@Serializable
class ChatUser(private val userName: String) {
    private var messageCount = 0

    //increment message count in ChatUser
    fun addMessageCount(){
        messageCount++
    }

    fun getMessageCount(): Int{
        return this.messageCount
    }

    override fun toString(): String {
        return this.userName
    }

    //equals and hashCode methods implemented to compare ChatUser objects
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChatUser
        if (userName != other.userName) return false
        return true
    }

    override fun hashCode(): Int {
        return userName.hashCode()
    }
}