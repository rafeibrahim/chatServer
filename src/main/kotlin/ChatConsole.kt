/*
 - author: rafei
 - date: 19/09/2019
 - class chatConsole registers as obsever to chathistory and prints new chat message to console every time new message is added to Chat History
 */
class ChatConsole: ChatObserver {
    //implements ChatObserver method updateChat to add a new message in its list and print already saved messages.
    override fun updateChat(newChatMessage: ChatMessage) {
        println(newChatMessage)
    }
}