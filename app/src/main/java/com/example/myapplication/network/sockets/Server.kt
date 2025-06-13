package com.example.myapplication


import com.example.myapplication.network.sockets.ClientHandler
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(private val listener: ClientHandler.ClienteConectadoListener) : Runnable {
  private val port: Int = 5200
  private var serverSocket: ServerSocket? = null

  override fun run() {
    serverSocket = ServerSocket(port)
    println("Esperando por clientes...")
    try {
      while (true) {
        val socket: Socket = serverSocket!!.accept()
        println("Cliente conectado: ${socket.inetAddress.hostAddress}")
        val cliente: ClientHandler = ClientHandler(socket)

        listener.onClientCountChanged(ClientHandler.clientes.size)

        thread { ClientHandler(socket).run() }
          println("Cantidad de clientes conectados: ${ClientHandler.clientes.size}")
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun cerrarServidor() {
    try {
      serverSocket?.close()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun getPort(): Int {
    return port
  }
}
