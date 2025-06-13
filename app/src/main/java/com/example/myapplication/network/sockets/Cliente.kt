package com.example.myapplication.network.sockets

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Cliente(dir: String) : Runnable {
  private var direccionIP = dir
  private var socket: Socket? = null
  private var dis: BufferedReader? = null
  private var dos: PrintWriter? = null

  override fun run() {
    try {
      println("Comprobando que el cliente inicio...")
      socket = Socket(direccionIP, 5200)
      dis = BufferedReader(InputStreamReader(socket!!.getInputStream()))
      dos = PrintWriter(socket!!.getOutputStream(), true)
      while (true) {
        enviarMensaje()
        recibirMensaje()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun enviarMensaje() {
    try {
      dos?.println("Vamono d esta mielda menoooool")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun recibirMensaje() {
    try {
      var mensajeRecibido: String
      while (socket?.isConnected == true) {
        mensajeRecibido = dis?.readLine().toString()
        println("El mensaje recibido fue: $mensajeRecibido")
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
