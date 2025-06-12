package com.example.myapplication.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket

class GameServer(private val port: Int) : GameConnection {
  private var listener: ConnectionListener? = null
  private var serverSocket: ServerSocket? = null
  private var clientSocket: Socket? = null
  private val scope = CoroutineScope(Dispatchers.IO) // Coroutine scope para operaciones de red

  override fun setConnectionListener(listener: ConnectionListener) {
    this.listener = listener
  }

  override fun start() {
    scope.launch {
      try {
        serverSocket = ServerSocket(port)
        println("Servidor escuchando en el puerto $port")
        // Espera a que un cliente se conecte (operación bloqueante)
        clientSocket = serverSocket!!.accept()
        println("Cliente conectado: ${clientSocket!!.inetAddress.hostAddress}")

        withContext(Dispatchers.Main) {
          listener?.onConnected("Cliente") // Notifica a la UI
        }

        // Inicia un bucle para escuchar mensajes del cliente
        listenForMessages()
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) { listener?.onConnectionLost() }
      }
    }
  }

  private fun listenForMessages() {
    // ... Lógica para leer datos del clientSocket.inputStream en un bucle ...
  }

  override fun sendMove(move: String) {
    scope.launch {
      try {
        clientSocket?.getOutputStream()?.write(move.toByteArray())
      } catch (e: Exception) {
        // ...
      }
    }
  }

  override fun close() {
    // ... Cierra los sockets y el serverSocket ...
  }
}
