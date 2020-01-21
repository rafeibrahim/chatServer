/*
 - author: rafei
 - date: 19/09/2019
 - class top chatter prints top four chatters to the console everytime top 4 chatters change
 */
import kotlin.math.min
class TopChatter : ChatObserver {
    //savedlist of top 4 chatters
    private val savedListOfTop4Chatters = mutableListOf<ChatUser>()
    //updated list of top 4 chatters. it is refilled everytime a new message is saved in ChatHistory
    private val updatedListOfTop4Chatters = mutableListOf<ChatUser>()

    //method which is called every time a new message is added to ChatHistory
    override fun updateChat(newChatMessage: ChatMessage) {
        //creates an updated list of top 4 chatters
       this.createUpdatedListOfTop4Chatters()
        //if loop to check if top4 chatters have changed
        if(top4ChattersChangeDetector()){
            //if the change is detected it clears saved list of top 4 chatters
            this.savedListOfTop4Chatters.clear()
            //it will add all elements of updated top 4 chatters list to saved top 4 chatters list
            this.savedListOfTop4Chatters.addAll(this.updatedListOfTop4Chatters)
            //print nicely formatted string of top 4 chatters
            println(this.toString())
        }
    }

    //method to create updated list of top 4 chatters
    fun createUpdatedListOfTop4Chatters() {
        //take a list of ChatUsers sorted by number of messages sent
        val chatUserList = ChatUsersRepository.topChatters()
        //set upper limit for loop to run so that it can add only top 4 chatters to the updatedListOfTop4Chatters
        var lastIndex = min(chatUserList.size, 4)
        //removes all elements from updatedListOfTopChatters
        updatedListOfTop4Chatters.clear()
        //this loop runs until last index and adds top 4 chatters to updatedLisOfTopChatters
        for (x in 0 until lastIndex) {
            updatedListOfTop4Chatters.add(chatUserList[x])
        }
    }

    //returns true if top4chatterslist has changed otherwise false
    fun top4ChattersChangeDetector(): Boolean{
        if(this.savedListOfTop4Chatters.equals(this.updatedListOfTop4Chatters)){
            return false
        }
        return true
    }

    //returns a nicely formatted string of top 4 chatters
    override fun toString(): String{
        var returnString = "New Top Four Chatters: \n\r"
        var count = 1
        this.savedListOfTop4Chatters.forEach{
            returnString += count.toString() + ": " + it + " : " + it.getMessageCount() + "\n\r"
            count++
        }
        return returnString
    }
}
