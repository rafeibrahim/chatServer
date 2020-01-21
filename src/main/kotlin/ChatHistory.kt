/*
 - author: rafei
 - date: 19/09/2019
 - ChatHistory singleton for storing all chat messages
 */
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
object ChatHistory: ChatObservable {
    //chatDataBase list for storing Chat Message objects.
    private val chatDataBase = mutableListOf<ChatMessage>()
    //Set for storing ChatObserver objects registered with ChatHistory
    private val chatObservers = mutableSetOf<ChatObserver>()
    //implemented for ChatObservable interface and updates all chat observers registered with it.
    override fun updateChatObservers(chatMessageObject: ChatMessage) {
        //updates all objects registered as obsevers with ChatHistory
        chatObservers.forEach{ it.updateChat(chatMessageObject)}
    }

    override fun toString(): String {
        var returnString :String = "chatHistory: \n\r"
        var counter = 1
        for(chatMessage in chatDataBase){
            returnString += ("$counter: $chatMessage\n\r")
            counter++
        }
        return returnString
    }

    fun chatDataBaseAsJsonString(): String{
        //returns chatDataBase list to Json string
        return Json.stringify(ChatMessage.serializer().list, chatDataBase)
    }

    fun insert(message: ChatMessage){
        //adds new chat message to chatDataBase list
        chatDataBase.add(message)
        //calls method to update all chat observers
        this.updateChatObservers(message)
    }

    //method to register chatObserver object with ChatHistory
    override fun registerChatObserver(chatObserverObject: ChatObserver){
        chatObservers.add(chatObserverObject)
    }

    //method to deregister chatObserver object from ChatHistory
    override fun deregisterChatObserver(chatObserverObject: ChatObserver){
        chatObservers.remove(chatObserverObject)
    }
}


