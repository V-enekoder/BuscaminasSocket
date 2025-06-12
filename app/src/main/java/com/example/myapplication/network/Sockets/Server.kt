package com.example.myapplication

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server : Runnable{
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

                thread{ ClientHandler(socket).run() }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cerrarServidor(){
        try {
            serverSocket?.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getPort(): Int{
        return port
    }
}