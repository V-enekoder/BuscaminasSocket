package com.example.myapplication

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Cliente (dir: String): Runnable{
    private var direccionIP = dir
    private var socket: Socket? = null
    private var dis: BufferedReader? = null
    private var dos: PrintWriter? = null

    override fun run() {
        try{
            socket = Socket(direccionIP, 5200)
            dis = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            dos = PrintWriter(socket!!.getOutputStream(), true)

            while(true){
                enviarMensaje()
                recibirMensaje()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun enviarMensaje(){
        try {
            dos?.println("Vamono d esta mielda menoooool")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun recibirMensaje(){
        try{
            var mensajeRecibido: String
            while(socket?.isConnected == true){
                mensajeRecibido = dis?.readLine().toString()
                println("$mensajeRecibido")
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}