package com.example.myapplication

fun main() {
    val ui = Visual()
    val miTablero = Tablero(filas = 8, columnas = 8, numeroMinas = 10)

    while(true){
        ui.mostrarTablero(miTablero)

        var (fila,columna) = ui.ingresarJugada()

        var casilla = miTablero.getCasilla(fila,columna)

        if(!casilla!!.isAbierta() && casilla.isDisponible()){ //Abrir una casilla disponible
            miTablero.descubrirCasilla(fila, columna)
        }
        //Abrir una casilla no disponible
        //Marcar una casilla disponible
        //Marcar una casilla no disponible

    }
}


//Gana el que tenga mas puntos, si hay empate, gana el que mas banderas tenga. una bandera por turno las banderas correctas suman puntos
//Debe hhaber 3 niveles y personalizado