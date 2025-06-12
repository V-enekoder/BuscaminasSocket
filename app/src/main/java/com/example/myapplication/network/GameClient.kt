package com.example.myapplication.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Socket

class GameClient(private val ip: String, private val port: Int) : GameConnection {
  private var listener: ConnectionListener? = null
  private var socket: Socket? = null
  private val scope = CoroutineScope(Dispatchers.IO)

  override fun setConnectionListener(listener: ConnectionListener) {
    this.listener = listener
  }

  override fun start() {
    scope.launch {
      try {
        // Intenta conectarse al servidor (operación bloqueante)
        socket = Socket(ip, port)
        println("Conectado al servidor en $ip:$port")

        withContext(Dispatchers.Main) { listener?.onConnected("Servidor") }

        // Inicia un bucle para escuchar mensajes del servidor
        listenForMessages()
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) { listener?.onConnectionLost() }
      }
    }
  }

  private fun listenForMessages() {
    // ... Lógica para leer datos del socket.inputStream en un bucle ...
  }

  override fun sendMove(move: String) {
    // ...
  }

  override fun close() {
    // ...
  }
}
