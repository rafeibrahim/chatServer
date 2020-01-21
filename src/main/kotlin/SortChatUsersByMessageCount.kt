/*
 - author: rafei
 - date: 19/09/2019
 - class for comparing ChatUsers object on the basis of messages sent. It sorts ChatUsers list in descending order
 */
class SortChatUsersByMessageCount: Comparator<ChatUser> {
    override fun compare(o1: ChatUser, o2: ChatUser): Int {
        return o2.getMessageCount() - o1.getMessageCount()
    }
}