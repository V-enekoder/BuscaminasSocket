package com.example.myapplication

import com.example.myapplication.game.core.Casilla
import com.example.myapplication.game.core.Tablero

data class ConfiguracionTablero(
    val filas: Int,
    val columnas: Int,
    val minas: Int
)

class Visual {
    fun menu(): Int {
        val baseIndent = 40 // Adjust for desired centering
        printAtCol(baseIndent + 5, "-----------------------")
        printAtCol(baseIndent + 5, "| B U S C A M I N A S |")
        printAtCol(baseIndent + 5, "-----------------------")
        println()
        printAtCol(baseIndent, "1)  Nueva Partida.")
        printAtCol(baseIndent + 2, "2)  Salir del juego.")
        println()

        return validarNumero(1, 2, "Seleccione una opcion: ", baseIndent + 4)
    }

    fun validarNumero(minimo: Int, maximo: Int, prompt: String = "Seleccione una opcion: ", indent: Int = 40): Int {
        var opcionStr: String?
        var opcionInt: Int?
        do {
            print(" ".repeat(indent) + prompt)
            opcionStr = readLine()
            if (opcionStr != null && esNumero(opcionStr)) {
                opcionInt = opcionStr.toIntOrNull()
                if (opcionInt != null && opcionInt >= minimo && opcionInt <= maximo) {
                    return opcionInt
                } else {
                    printAtCol(indent -5, "Opción inválida. Intente de nuevo.")
                    opcionInt = null // Reset to loop again
                }
            } else {
                printAtCol(indent -5, "Entrada no numérica. Intente de nuevo.")
                opcionInt = null // Reset to loop again
            }
        } while (opcionInt == null) // Loop will exit only when a valid number in range is returned
        return -1 // Should not be reached due to the return inside the loop
    }

    private fun esNumero(s: String): Boolean {
        if (s.isEmpty()) return false
        return s.all { it.isDigit() }
    }

    fun clearScreen() {
        repeat(50) { println() }
    }

    fun configurarPartida(): ConfiguracionTablero {
        clearScreen()
        val baseIndent = 30
        println("\n\n")
        printAtCol(baseIndent, "Selecciona la dificultad de juego ")
        printAtCol(baseIndent, "-----------------------------------")
        println()
        printAtCol(baseIndent - 7, "1) Fácil. (4x4 con 4 minas).")
        printAtCol(baseIndent - 2, "2) Intermedio. (6x6 con 10 minas).")
        printAtCol(baseIndent - 5, "3) Díficil.(8x8 con 12 minas).")
        printAtCol(baseIndent - 5, "4) Personalizado.(8x8 con 12 minas).")
        println()

        val opcion = validarNumero(1, 4, "Seleccione una dificultad: ", baseIndent)

        clearScreen()
        val config = when (opcion) {
            1 -> ConfiguracionTablero(4, 4, 4)         // Fácil
            2 -> ConfiguracionTablero(6, 6, 10)        // Intermedio
            3 -> ConfiguracionTablero(8, 8, 12)        // Difícil
            4 -> configurarPartidaPersonalizada()
            else -> {
                System.err.println("Error: Opción de dificultad inválida '$opcion' recibida. Usando configuración fácil por defecto.")
                ConfiguracionTablero(4, 4, 4)
            }
        }
        return config
    }

    fun configurarPartidaPersonalizada(): ConfiguracionTablero {
        clearScreen()
        val baseIndent = 20 // Ajusta según necesites para la alineación
        printAtCol(baseIndent, "--- Configuración Personalizada ---")
        println()

        var filas: Int? = null
        var columnas: Int? = null
        var minas: Int? = null

        // Pedir Filas
        do {
            printAtCol(baseIndent - 5, "Número de Filas (ej: 8): ")
            val entradaFilas = readLine()
            filas = entradaFilas?.toIntOrNull()

            if (filas == null || filas <= 0) {
                printAtCol(baseIndent - 5, "Entrada inválida. Las filas deben ser un número positivo.")
                filas = null // Para que el bucle continúe
            }
        } while (filas == null)

        // Pedir Columnas
        do {
            printAtCol(baseIndent - 5, "Número de Columnas (ej: 8): ")
            val entradaColumnas = readLine()
            columnas = entradaColumnas?.toIntOrNull()

            if (columnas == null || columnas <= 0) {
                printAtCol(baseIndent - 5, "Entrada inválida. Las columnas deben ser un número positivo.")
                columnas = null // Para que el bucle continúe
            }
        } while (columnas == null)

        // Pedir Minas (con validación dependiente de filas y columnas)
        val totalCasillas = filas * columnas // filas y columnas ya no son null aquí
        do {
            printAtCol(baseIndent - 5, "Número de Minas (ej: 10): ")
            val entradaMinas = readLine()
            minas = entradaMinas?.toIntOrNull()

            if (minas == null || minas <= 0) {
                printAtCol(baseIndent - 5, "Entrada inválida. Las minas deben ser un número positivo.")
                minas = null // Para que el bucle continúe
            } else if (minas >= totalCasillas) {
                printAtCol(baseIndent - 5, "Error: El número de minas ($minas) no puede ser igual o mayor al total de casillas ($totalCasillas).")
                printAtCol(baseIndent - 5, "Por favor, ingrese un número menor de minas.")
                minas = null // Para que el bucle continúe
            }
        } while (minas == null)

        clearScreen()
        printAtCol(baseIndent, "Configuración personalizada creada:")
        printAtCol(baseIndent, "Filas: $filas, Columnas: $columnas, Minas: $minas")
        println()
        // systemPause("Presiona Enter para comenzar...") // Si tienes una función systemPause

        // filas, columnas, y minas ya no son null en este punto debido a los bucles do-while
        return ConfiguracionTablero(filas, columnas, minas)
    }

    fun ingresarJugada(filas: Int, columnas: Int): Pair<Int, Int> {
        var fila: Int?
        var columna: Int?

        do {
            print("Ingresa la fila (0-${filas - 1}): ")
            val entradaFila = readLine()
            fila = entradaFila?.toIntOrNull()

            print("Ingresa la Columna (0-${columnas - 1}): ")
            val entradaColumna = readLine()
            columna = entradaColumna?.toIntOrNull()
            if (fila == null || columna == null) {
                println("Entrada inválida. Debes ingresar números para fila y columna.")
            }

        } while (
            fila == null || fila < 0 || fila > filas ||
            columna == null || columna < 0 || columna > columnas
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

    private fun systemPause(message: String = "Presiona Enter para continuar...") {
        println(message)
        readLine()
    }

    private fun String.padLeft(length: Int, padChar: Char = ' '): String {
        return this.padStart(length, padChar)
    }
    private fun printAtCol(col: Int, text: String) {
        println(" ".repeat(col) + text)
    }

    fun seleccionarAccion(): Int {
        val baseIndent = 40
        println()
        printAtCol(baseIndent, "--Opciones de juego--")
        println()
        printAtCol(baseIndent - 2, "1) Abrir casilla.")
        printAtCol(baseIndent, "2) Colocar bandera.")
        printAtCol(baseIndent - 1, "3) Quitar bandera.")
        printAtCol(baseIndent - 7, "4) Rendirse.")
        return validarNumero(1, 4, "Seleccione una acción: ", baseIndent)
    }

    fun abrirCasilla(casilla: Casilla) {
        println("Abrirá la casilla (${casilla.getX()},${casilla.getY()}).")
    }

    fun marcarCasilla(casilla: Casilla) {
        println("Marcará la casilla (${casilla.getX()},${casilla.getY()}).")
    }

    fun desmarcarCasilla(casilla: Casilla) {
        println("Desmarcará la casilla (${casilla.getX()},${casilla.getY()}).")
    }

    fun mensajeWinOrLose(tablero: Tablero) {
        val baseIndent = 40
        when (tablero.verificarResultado()) {
            0 -> {
                println("BOOOOOM. Ha explotado una mina\n")
                mostrarMinas(tablero)
                printAtCol(baseIndent, "--Juego finalizado--")
                printAtCol(baseIndent, "¡Quizá la próxima!")
                systemPause()
            }
            1 -> {
                println("Ha marcado todas las minas. Felicitaciones.")
                systemPause()
            }
            2 -> {
                println("Ha abierto todas las casillas con éxito. Felicitaciones.")
                systemPause()
            }
            3 -> { // Surrender option or specific meaning
                println("Vuelve pronto a terminar tu partida. No te rindas.")
                systemPause()
            }
            4 -> { // Another condition, maybe a different type of loss or early exit
                printAtCol(baseIndent, "Será para la próxima")
                printAtCol(baseIndent, "No te rindas.")
                systemPause()
            }
            else -> {
                println("Resultado desconocido del juego.")
                systemPause()
            }
        }
    }

    fun mostrarMinas(t: Tablero) {
        println("\n\nUbicacion de las Minas:")
        print("   ") // Align with mostrarTablero
        for (col in 0 until t.getColumnas()) {
            print(" ${if (col < 10) " $col" else col}") // Match formatting
        }
        println()
        print("  +-")
        for (col in 0 until t.getColumnas()) {
            print("---")
        }
        println("-+")

        for (fila in 0 until t.getFilas()) {
            print(if (fila < 10) " $fila |" else "$fila |") // Row number
            for (columna in 0 until t.getColumnas()) {
                val casilla = t.getCasilla(fila, columna)
                val displayChar = if (casilla?.isMina() == true) " * " else " . "
                print(displayChar)
            }
            println("|")
        }
        print("  +-")
        for (c in 0 until t.getColumnas()) {
            print("---")
        }
        println("-+")
        println()
    }

    // Your existing methods from the problem description
    /*fun ingresarJugada(): Pair<Int, Int> {
        var fila: Int?
        var columna: Int?

        do {
            print("Ingresa la fila (0-${Tablero(8,8).getFilas()-1}): ") // Assuming an 8x8 board for this prompt
            val entradaFila = readLine()
            fila = entradaFila?.toIntOrNull()

            print("Ingresa la Columna (0-${Tablero(8,8).getColumnas()-1}): ")
            val entradaColumna = readLine()
            columna = entradaColumna?.toIntOrNull()

            if (fila == null || columna == null) {
                println("Entrada inválida. Debes ingresar números para fila y columna.")
            } else if (fila < 0 || fila >= Tablero(8,8).getFilas() || columna < 0 || columna >= Tablero(8,8).getColumnas()){
                println("Valores fuera de rango. Intente de nuevo.")
                fila = null // Force re-loop
                columna = null // Force re-loop
            }


        } while (fila == null || columna == null) // Loop if null, range check is now inside
        return Pair(fila, columna) // Non-null asserted due to loop condition
    }*/
}