package com.example.myapplication

import kotlin.random.Random

class Tablero(
    private val filas: Int,
    private val columnas: Int,
    private val numeroMinas: Int
) {
    private var jugadas: Int = 0
    private var juegoTerminado: Boolean = false
    private var victoria: Boolean = false
    private val tablero: Array<Array<Casilla>>

    init {
        if (numeroMinas > filas * columnas) {
            throw IllegalArgumentException("El número de minas no puede exceder el total de casillas.")
        }
        if (numeroMinas < 0 || filas <= 0 || columnas <= 0) {
            throw IllegalArgumentException("Dimensiones y número de minas deben ser positivos.")
        }

        // 1. Crear el tablero vacío con Casillas
        tablero = Array(filas) { r ->
            Array(columnas) { c ->
                Casilla(r, c)
            }
        }
        asignarMinas()
        actualizarMinasAlrededor()
    }

    private fun asignarMinas() {
        var minasGeneradas = 0
        while (minasGeneradas < numeroMinas) {
            val i = Random.nextInt(filas)
            val j = Random.nextInt(columnas)

            if (!tablero[i][j].isMina()) {
                tablero[i][j].setMina(true)
                minasGeneradas++
            }
        }
    }

    private fun actualizarMinasAlrededor() {
        for (r in 0 until filas) {
            for (c in 0 until columnas) {
                if (tablero[r][c].isMina()) {
                    aumentarContadorAdyacentes(r, c)
                }
            }
        }
    }

    private fun aumentarContadorAdyacentes(filaMina: Int, columnaMina: Int) {
        for (i in (filaMina - 1)..(filaMina + 1)) {
            for (j in (columnaMina - 1)..(columnaMina + 1)) {
                if (estaDentroDeLimites(i, j)
                    && (i != filaMina || j != columnaMina) && !tablero[i][j].isMina()) {
                    tablero[i][j].incrementarMinasAlrededor()
                }
            }
        }
    }

    private fun estaDentroDeLimites(fila: Int, columna: Int): Boolean {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas
    }

    fun descubrirCasilla(fila: Int, columna: Int): Boolean {
        if (juegoTerminado || !estaDentroDeLimites(fila, columna)) {
            return !juegoTerminado
        }

        val casilla = tablero[fila][columna]

        if (casilla.isAbierta() || casilla.isMarcada()) {
            return true //
        }
        jugadas++
        casilla.abrir()


        if (casilla.isMina()) { // O casilla.isMina()
            juegoTerminado = true
            victoria = false
            // Opcional: abrir todas las minas al perder
            // revelarTodasLasMinas()
            return false
        }

        if (casilla.getMinasAlrededor() == 0) {
            abrirAlrededorRecursivo(casilla)
        }

        if (comprobarVictoria()) {
            juegoTerminado = true
            victoria = true
        }

        return !juegoTerminado || victoria
    }

    private fun abrirAlrededorRecursivo(casillaOriginal: Casilla) {
        if (casillaOriginal.getMinasAlrededor() == 0) {
            val casillasAdyacentes = obtenerCasillasAdyacentesParaAbrir(casillaOriginal)
            for (adyacente in casillasAdyacentes) {
                if (!adyacente.isAbierta() && !adyacente.isMarcada()) {
                    adyacente.abrir()
                    if (adyacente.getMinasAlrededor()== 0) {
                        abrirAlrededorRecursivo(adyacente)
                    }
                }
            }
        }
    }
    private fun obtenerCasillasAdyacentesParaAbrir(casillaBase: Casilla): List<Casilla> {
        val alrededor = mutableListOf<Casilla>()
        val filaBase = casillaBase.getX()
        val columnaBase = casillaBase.getY()

        for (i in (filaBase - 1)..(filaBase + 1)) {
            for (j in (columnaBase - 1)..(columnaBase + 1)) {
                if (estaDentroDeLimites(i, j) && (i != filaBase || j != columnaBase)) {
                    val casillaAdyacente = tablero[i][j]
                    if (!casillaAdyacente.isMina() ) {
                        alrededor.add(casillaAdyacente)
                    }
                }
            }
        }
        return alrededor
    }

    fun marcarCasilla(fila: Int, columna: Int) {
        if (juegoTerminado || !estaDentroDeLimites(fila, columna)) {
            return
        }
        val casilla = tablero[fila][columna]
        if (!casilla.isAbierta()) {
            casilla.marcar()
        }
    }

    private fun comprobarVictoria(): Boolean {
        for (r in 0 until filas) {
            for (c in 0 until columnas) {
                val casilla = tablero[r][c]
                if (!casilla.isMina() && !casilla.isAbierta()) {
                    return false
                }
            }
        }
        return true
    }


    fun getFilas(): Int = filas
    fun getColumnas(): Int = columnas
    fun getMinas(): Int = numeroMinas

    fun getJugadas(): Int = jugadas

    fun getTablero(): Array<Array<Casilla>>{
        return tablero
    }

    fun setJugadas(nuevasJugadas: Int) {
        if (nuevasJugadas >= 0) { // Ejemplo de validación
            this.jugadas = nuevasJugadas
        }
    }


    fun getCasilla(fila: Int, columna: Int): Casilla? {
        if (fila in 0 until filas && columna in 0 until columnas) {
            return tablero[fila][columna]
        }
        return null
    }
}
