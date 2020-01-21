/*
 - author: rafei
 - date: 19/09/2019
 - class defines structure of chat message
 */
import kotlinx.serialization.Serializable
@Serializable
class ChatMessage(private val command: String, private val message: String, private val dateAndTime: String, private val username: String) {

    override fun toString(): String {
        return command + ": " + username + ": " + this.message + " (" + this.dateAndTime + ")"
    }

    fun getCommand(): String{
        return this.command
    }

    fun getMessage(): String{
        return this.message
    }

    fun getUsername(): String{
        return this.username
    }
}