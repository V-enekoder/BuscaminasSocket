package com.example.myapplication.network.sockets

import android.content.Context
import android.content.Intent
import com.example.myapplication.ui.activities.ConfiguracionTablero
import com.example.myapplication.ui.activities.GameActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Cliente(dir: String, private val turno: Int) : Runnable {
  private var direccionIP = dir
  private var socket: Socket? = null
  private var dis: BufferedReader? = null
  private var dos: PrintWriter? = null
  private var context: Context? = null
  private var moveListener: OnMoveReceivedListener? = null

  override fun run() {
    try {
      socket = Socket(direccionIP, 5200)
      dis = BufferedReader(InputStreamReader(socket!!.getInputStream()))
      dos = PrintWriter(socket!!.getOutputStream(), true)
      while (true) {
        recibirMensaje()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun descifrarMensaje(msj: String) {
    val partes = msj.split(" ")
    val type = partes[0]

    when (type) {
      "GAME_CONFIG" -> {
        val config = interpretarConfiguracion(msj)
        if (config != null) {
          // Lanzar GameActivity con esa config
          val intent = Intent(context, GameActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
          intent.putExtra("GAME_CONFIG", config)
          context?.startActivity(intent)
        }
      }
      "MOVE" -> {
        try {
          val turnoJugador = partes[1].toInt() // Turno del que jugó
          val action = partes[2] // "REVEAL", "FLAG", etc.
          val coords = partes[3].split("_")
          val row = coords[0].toInt()
          val col = coords[1].toInt()

          // Notifica a GameActivity, ¡ahora con el turno!
          moveListener?.onMoveReceived(turnoJugador, action, row, col)
        } catch (e: Exception) {
          println("Error al interpretar mensaje MOVE: $msj")
          e.printStackTrace()
        }
      }
    // ... otros posibles tipos de mensajes ...
    }
  }

  fun setMoveListener(listener: OnMoveReceivedListener?) {
    this.moveListener = listener
  }

  fun interpretarConfiguracion(msj: String): ConfiguracionTablero? {
    return try {
      val divide = msj.removePrefix("GAME_CONFIG ").split("_")
      val filas = divide[0].toInt()
      val columnas = divide[1].toInt()
      val minas = divide[2].toInt()
      ConfiguracionTablero(filas, columnas, minas)
    } catch (e: Exception) {
      null
    }
  }

  fun enviarMensaje(msj: String) {
    try {
      dos?.println(msj)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun recibirMensaje() {
    try {
      var mensajeRecibido: String
      while (socket?.isConnected == true) {
        mensajeRecibido = dis?.readLine().toString()
        // println("El mensaje recibido fue: $mensajeRecibido")
        descifrarMensaje(mensajeRecibido)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun setContext(contexto: Context) {
    context = contexto
  }

  fun getTurno(): Int {
    return turno
  }
}
