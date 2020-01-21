/*
 - author: rafei
 - date: 19/09/2019
 - ChatObservable interface implemented as part of Observer pattern. To be implemented by class which needs to update others.
 */
interface ChatObservable {
    fun registerChatObserver(chatObserverObject: ChatObserver)
    fun deregisterChatObserver(chatObserverObject: ChatObserver)
    fun updateChatObservers(chatMessageObject: ChatMessage)
}