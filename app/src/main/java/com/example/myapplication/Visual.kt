package com.example.myapplication

class Visual {

    fun ingresarJugada(): Pair<Int, Int> {
        var fila: Int?      // Puede ser null inicialmente o si la entrada no es un número
        var columna: Int?

        do {
            print("Ingresa la fila (0-7): ")
            val entradaFila = readLine()
            fila = entradaFila?.toIntOrNull()

            print("Ingresa la Columna (0-7): ")
            val entradaColumna = readLine()
            columna = entradaColumna?.toIntOrNull()
            if (fila == null || columna == null) {
                println("Entrada inválida. Debes ingresar números para fila y columna.")
            }

        } while (
            fila == null || fila < 0 || fila > 7 ||
            columna == null || columna < 0 || columna > 7
        )
        return Pair(fila, columna)
    }

    fun mostrarTablero(tablero: Tablero) {
        print("   ")
        for (c in 0 until tablero.getColumnas()) {
            if (c < 10) {
                print(" $c ")
            } else {
                print("$c ")
            }
        }
        println()

        print("  +-")
        for (c in 0 until tablero.getColumnas()) {
            print("---")
        }
        println("-+")

        for (r in 0 until tablero.getFilas()) {
            // Imprimir encabezado de fila
            if (r < 10) {
                print(" $r |")
            } else {
                print("$r |")
            }

            for (c in 0 until tablero.getColumnas()) {
                val casilla = tablero.getCasilla(r, c)

                val representacion = if (casilla != null) {
                    when {
                        casilla.isMarcada() -> " M "
                        !casilla.isAbierta() -> " ■ "
                        casilla.isMina() -> " * "
                        else -> {
                            val minasStr = casilla.getMinasAlrededor().toString()
                            if (minasStr.length == 1 && casilla.getMinasAlrededor() != 0) " $minasStr "
                            else if (casilla.getMinasAlrededor() == 0 && casilla.isAbierta()) "   "
                            else " $minasStr"
                        }
                    }
                } else {
                    " ? "
                }
                print(representacion)
            }
            println("|")
        }

        print("  +-")
        for (c in 0 until tablero.getColumnas()) {
            print("---")
        }
        println("-+")
    }
}