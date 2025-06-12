package com.example.myapplication.network

interface GameConnection {
  // Inicia la conexión (para el cliente) o empieza a escuchar (para el servidor)
  fun start()

  // Cierra la conexión y libera los recursos
  fun close()

  // Envía un movimiento (por ejemplo, una jugada del buscaminas) al otro jugador
  fun sendMove(move: String) // Puedes usar un objeto específico en lugar de String

  // Establece un "listener" para que la Activity sea notificada de eventos
  fun setConnectionListener(listener: ConnectionListener)
}

// Interfaz para que la capa de red notifique a la Activity
interface ConnectionListener {
  fun onConnected(name: String) // Cuando la conexión se establece

  fun onMessageReceived(message: String) // Cuando llega una jugada del oponente

  fun onConnectionLost() // Cuando la conexión se pierde
}
