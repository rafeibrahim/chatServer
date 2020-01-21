/*
 - author: rafei
 - date: 19/09/2019
 */
fun main(){
    //creates new instance of ChatServer and calls its serve method
    val chatServer = ChatServer()
    chatServer.serve()
    //code for testing purpose
   /* val chatConnector = ChatConnector(System.`in`, System.out)
    chatConnector.run()*/
}