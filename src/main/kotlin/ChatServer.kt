/*
 - author: rafei
 - date: 19/09/2019
 - Class ChatServer takes care of opening new ServerSocket and runs loop to keep accepting incoming connections
 */
import java.net.ServerSocket

class ChatServer {
    fun serve(){
        //creating lateinitializing variable of class ServerSocket so that it is available in future code blocks and it cannot be null
        lateinit var serverSocketObject: ServerSocket

        try {
            //creating server socket for ChatServer
            serverSocketObject = ServerSocket(30005, 5)
            println("We have port " + serverSocketObject.localPort)
            val chatConsoleObject = ChatConsole()
            val topChatterObject = TopChatter()
            ChatHistory.registerChatObserver(chatConsoleObject)
            ChatHistory.registerChatObserver(topChatterObject)
        }catch (e: Exception){
            println("Got exception: ${e.message}")
        }finally {
            println("All serving done.")
        }

        //this loop keeps accepting new Connections while ChatServer is running
        while(true){
            System.out.println("accepting new clients")
            val socket = serverSocketObject.accept()
            println("new connection : " + socket.inetAddress.hostAddress + " " + socket.port)
            val chatConnector = ChatConnector(socket.getInputStream(), socket.getOutputStream())
            //creating thread to Start ChatConnector Object
            Thread(chatConnector).start()
         }
    }
}