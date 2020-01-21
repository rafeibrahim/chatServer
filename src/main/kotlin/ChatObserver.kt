/*
 - author: rafei
 - date: 19/09/2019
 - ChatObserver interface implemented as part of Observer pattern. To be implemented by all classes which need to be updated for new messages.
 */
interface ChatObserver {
    fun updateChat(newChatMessage: ChatMessage)
}