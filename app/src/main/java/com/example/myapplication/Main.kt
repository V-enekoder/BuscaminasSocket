package com.example.myapplication

/*
* model:

PlayerAction.kt: Data class serializable para representar una acción que se envía por la red (ej. data class PlayerAction(val type: ActionType, val x: Int, val y: Int) donde ActionType es un enum como REVEAL, FLAG).
*
* */



/*fun main() {
    val ui = Visual()

    val opcion = ui.menu()

    when(opcion){
        1 ->{

            val (filas,columnas,minas) = ui.configurarPartida()

            val miTablero = Tablero(filas,columnas,minas)
            
            while(true){
                ui.clearScreen()
                ui.mostrarTablero(miTablero)
                val accion = ui.seleccionarAccion()

                if(accion == 4){
                    break
                }
                val (fila,columna) = ui.ingresarJugada(miTablero.getFilas(),miTablero.getColumnas())

                val casilla = miTablero.getCasilla(fila, columna)!!

                when(accion){
                    1->{
                        if(!casilla.isAbierta() && casilla.isDisponible()){
                            miTablero.abrirCasilla(fila, columna)
                        }
                    }
                    2->{
                        if(!casilla.isMarcada() && casilla.isDisponible()){
                            miTablero.marcarCasilla(fila,columna)
                        }
                    }
                    3->{
                        if(casilla.isMarcada()){
                            miTablero.desmarcarCasilla(fila,columna)
                        }
                    }
                    else->{
                        println("Error: Acción desconocida")
                    }
                }
            }
        }
    }
    ui.clearScreen()
    print("Juego terminado")
}*/


//Gana el que tenga mas puntos, si hay empate, gana el que mas banderas tenga. una bandera por turno las banderas correctas suman puntos
//Debe hhaber 3 niveles y personalizado



/*
* ├── network
│   ├── client
│   │   └── GameClient.kt            // Lógica del cliente socket
│   ├── server
│   │   └── GameServer.kt            // Lógica del servidor socket
│   ├── common                       // Clases comunes para cliente y servidor
│   │   ├── SocketManager.kt         // Podría ser una interfaz o clase base
│   │   ├── MessageHandler.kt        // Para procesar mensajes entrantes
│   │   └── NetworkMessage.kt        // DTOs para la comunicación (serializables)
│   └── ConnectionState.kt         // Enum para el estado de la conexión (CONNECTING, CONNECTED, FAILED, DISCONNECTED)
* */