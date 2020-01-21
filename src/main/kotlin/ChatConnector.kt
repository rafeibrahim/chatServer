/*
 - author: rafei
 - date: 19/09/2019
 - class chat connector takes input and output stream of a socket and Implements Runnable and ChatObserver interfaces. It also has command interpretation functionality
 */
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonParsingException
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.lang.IllegalStateException
import java.util.Scanner


class ChatConnector(private val inputStreamObject: InputStream, private val outputStreamObject: OutputStream) : Runnable, ChatObserver {
    //creating printWriterObject by giving Outputstream object to PrintWriter class
    private val printWriterObject = PrintWriter(outputStreamObject)
    //creating scannerObject by giving InputStream object to Scanner class.
    private val scannerObject = Scanner(inputStreamObject)
    //instance variable user for keeping track of user associated with ChatConnector (type of lateinit to avoid getting null exception)
    private lateinit var user: ChatUser

    //run method to fulfill Runnable interface and use start method of thread
    override fun run() {
        printWriterObject.println("Welcome to Chat Server\r")
        printWriterObject.flush()

        //this loop runs until user enters quit command
        while (true) {
            //call readChatMessageJsonString which reads JsonString of type ChatMessage and takes care of exception.
            val chatMessageObject = readChatMessageJsonString()
            val command = chatMessageObject.getCommand()
            val username = chatMessageObject.getUsername()

            if (command.equals("exit")) {
                if (::user.isInitialized) {
                    //de-registering this chatConnector object as ChatObserver to ChatHistory(ChatObservable)
                    ChatHistory.deregisterChatObserver(this)
                    //removes this ChatUser from Chat user repository
                    ChatUsersRepository.removeUser(this.user)
                }
                break

            } else if (command.equals("username")) {
                if (::user.isInitialized) {
                    printWriterObject.println("username already set")
                    printWriterObject.flush()
                }else {
                    val tempUser = ChatUser(username)
                    if (ChatUsersRepository.insertUser(tempUser)) {
                        //assigning reference of tempUser to user instance variable of this ChatConnector
                        this.user = tempUser
                        //must send acknowledgement message to client to inform user name regi3tration message
                        val usernameAckMessage = ChatMessage("username", "", "", this.user.toString())
                        val jsonUsernameAck = Json.stringify(ChatMessage.serializer(), usernameAckMessage)
                        printWriterObject.println(jsonUsernameAck)
                        printWriterObject.flush()
                        //registering this chatConnector object as ChatObserver to ChatHistory(ChatObservable)
                        ChatHistory.registerChatObserver(this)
                        //printWriterObject.println("username set\r")
                        //printWriterObject.flush()
                    }else{
                        printWriterObject.println("username already exists, try another username\r")
                        printWriterObject.flush()
                    }
                }

            } else if (command.equals("history")) {
                if (::user.isInitialized) {
                    //using overriden toString method of ChatHistory to send previous ChatMessages stored in list of ChatHistory
                    sendChatHistoryAsJsonString()
                } else {
                    printWriterObject.println("username must be registered first")
                    printWriterObject.flush()
                }

            } else if (command.equals("users")) {
                if (::user.isInitialized) {
                    //using overriden toString method of ChatUsers to send active ChatUsers stored in HashSet of ChatUsersRepository
                    sendOtherActiveChatUsersListAsJsonString()
                } else {
                    printWriterObject.println("username must be registered first")
                    printWriterObject.flush()
                }

            } else if (command.equals("message")) {
                if (::user.isInitialized) {
                    //adds one to message count property of ChatUser associated with this ChatConnector
                    this.user.addMessageCount()
                    //adding chatMessage object to ChatHistory Singleton
                    ChatHistory.insert(chatMessageObject)
                } else {
                    printWriterObject.println("username must be registered first")
                    printWriterObject.flush()
                }
            }
        }
    }

    fun sendChatHistoryAsJsonString() {
/*        //write Json sting from toString method of ChatHistory singleton to the outputStream
        this.printWriterObject.println(ChatHistory.chatDataBaseAsJsonString())
        //If the stream has saved any characters in a buffer, flush method of PrintWriter class is used to write them immediately to their intended destination.
        this.printWriterObject.flush()*/

        //we need a new interpretation of history command in chatHistory command.
        val chatDatabaseAsJsonString = ChatHistory.chatDataBaseAsJsonString()
        val historyCommandChatMessage = ChatMessage("history", chatDatabaseAsJsonString, "", this.user.toString())
        val historyCommandChatMessageJson = Json.stringify(ChatMessage.serializer(), historyCommandChatMessage)
        printWriterObject.println(historyCommandChatMessageJson)
        printWriterObject.flush()
        println("chatHistory sent")
    }

    fun sendOtherActiveChatUsersListAsJsonString() {
/*        //write Json string from toString method of ChatUsersRepository singleton to the outputStream
        this.printWriterObject.println(ChatUsersRepository.activeChatUserDataJSonString())
        //If the stream has saved any characters in a buffer, flush method of PrintWriter class is used to write them immediately to their intended destination.
        this.printWriterObject.flush()*/

        val usersDatabaseAsJsonString = ChatUsersRepository.activeChatUserDataJSonString()
        val usersCommandChatMessage = ChatMessage("users", usersDatabaseAsJsonString, "", this.user.toString())
        val usersCommandChatMessageJson = Json.stringify(ChatMessage.serializer(), usersCommandChatMessage)
        printWriterObject.println(usersCommandChatMessageJson)
        printWriterObject.flush()
        println("chatUsers sent")
    }

    override fun updateChat(newChatMessage: ChatMessage) {
        //creating Json String from chatMessage object
        val chatMessageAsJson = Json.stringify(ChatMessage.serializer(), newChatMessage)
        //write chatMessageJsonString to outputStream
        printWriterObject.println(chatMessageAsJson)
        ////If the stream has saved any characters in a buffer, flush method of PrintWriter class is used to write them immediately to their intended destination.
        this.printWriterObject.flush()
    }

    fun readChatMessageJsonString(): ChatMessage {
        //inputChatMessage is declared with lateinit so that it cannot has null value
        lateinit var inputChatMessage: ChatMessage
        while (true) {
            try {
                val inputJsonString = this.scannerObject.nextLine()
                inputChatMessage = Json.parse(ChatMessage.serializer(), inputJsonString)


                break
            } catch (e: JsonParsingException) {
                println("Got exception (Json format must be of Class ChatMessage): ${e.message}")
            }catch (e : IllegalStateException){
                println("Got exception (Given multiple Json strings): ${e.message}")
            }catch (e : NoSuchElementException){
                println("Got exception (Local host window closed): ${e.message}")
                //in case of this exception, inputChatMessage is created here with command exit to end this ChatConnector
                inputChatMessage  = ChatMessage("exit", "", "", "")
                break
            }
        }
        return inputChatMessage
    }
    //implemented equals and hasCode method so that ChatConnectorObjects can be compared.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChatConnector
        if (inputStreamObject != other.inputStreamObject) return false
        if (outputStreamObject != other.outputStreamObject) return false
        if (user != other.user) return false
        return true
    }

    override fun hashCode(): Int {
        var result = inputStreamObject.hashCode()
        result = 31 * result + outputStreamObject.hashCode()
        result = 31 * result + (user.hashCode() ?: 0)
        return result
    }
}