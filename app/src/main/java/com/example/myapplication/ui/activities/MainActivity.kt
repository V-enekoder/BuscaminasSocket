package com.example.myapplication.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.gridlayout.widget.GridLayout
import com.example.myapplication.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myapplication.game.core.Tablero

class MainActivity : AppCompatActivity() {

    private val NUM_ROWS = 8
    private val NUM_COLS = 8
    private val NUM_MINES = 2
    private val CELL_SIZE_DP = 40 // Tamaño de cada celda en DP

    private lateinit var matrixGridLayout: GridLayout
    private lateinit var actionSpinner: Spinner
    private lateinit var rowEditText: EditText
    private lateinit var columnEditText: EditText
    private lateinit var sendMoveButton: Button

    private lateinit var tableroLogico: Tablero

    private lateinit var cellViews: Array<Array<TextView>>

    private var juegoActivo = true // Para saber si el juego ha terminado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Para el diseño Edge-to-Edge
        setContentView(R.layout.activity_main) // Carga el XML


        val mainContainer = findViewById<View>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarVistas()
        iniciarNuevoJuego()
        setupSpinner()
        setupButtonListener()
    }

    private fun inicializarVistas() {
        matrixGridLayout = findViewById(R.id.matrixGridLayout)
        actionSpinner = findViewById(R.id.actionSpinner)
        rowEditText = findViewById(R.id.rowEditText)
        columnEditText = findViewById(R.id.columnEditText)
        sendMoveButton = findViewById(R.id.sendMoveButton)
    }

    private fun iniciarNuevoJuego() {
        // 1. Crear la instancia del MODELO
        tableroLogico = Tablero(NUM_ROWS, NUM_COLS, NUM_MINES)
        juegoActivo = true

        // 2. Crear la VISTA inicial
        setupGameGrid() // Crea los TextViews
        actualizarVistaTablero() // Dibuja el estado inicial del tablero (todo oculto)
    }

    private fun setupGameGrid() {
        matrixGridLayout.removeAllViews() // Limpiar el tablero si se reinicia el juego
        matrixGridLayout.rowCount = NUM_ROWS
        matrixGridLayout.columnCount = NUM_COLS
        cellViews = Array(NUM_ROWS) { Array(NUM_COLS) { TextView(this) } }
        val cellSizePx = (CELL_SIZE_DP * resources.displayMetrics.density).toInt()

        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                val cellView = TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = cellSizePx
                        height = cellSizePx
                        rowSpec = GridLayout.spec(row, 1f)
                        columnSpec = GridLayout.spec(col, 1f)
                        setMargins(2, 2, 2, 2)
                    }
                    gravity = Gravity.CENTER
                    textSize = 18f
                    setOnClickListener {
                        rowEditText.setText(row.toString())
                        columnEditText.setText(col.toString())
                    }
                }
                cellViews[row][col] = cellView
                matrixGridLayout.addView(cellView)
            }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.move_actions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            actionSpinner.adapter = adapter
        }
    }

    private fun setupButtonListener() {
        sendMoveButton.setOnClickListener {
            if (!juegoActivo) {
                Toast.makeText(this, "El juego ha terminado. Inicia uno nuevo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val row = rowEditText.text.toString().toIntOrNull()
            val col = columnEditText.text.toString().toIntOrNull()

            if (row == null || col == null || row !in 0 until NUM_ROWS || col !in 0 until NUM_COLS) {
                Toast.makeText(this, "Coordenadas inválidas.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Le dice al MODELO qué hacer ---
            val resultadoJugada: Int = when (actionSpinner.selectedItemPosition) {
                0 -> tableroLogico.abrirCasilla(row, col)
                1 -> tableroLogico.marcarCasilla(row, col)
                2 -> tableroLogico.desmarcarCasilla(row, col)
                else -> 0
            }
            // --- Pide a la VISTA que se actualice ---

            actualizarVistaTablero()
            if(resultadoJugada == -1){
                juegoActivo = false
                revelarTableroCompleto()
            }
            // --- Comprueba el resultado del juego desde el MODELO ---
            verificarEstadoDelJuego()
        }
    }

    // --- VISTA: Función clave para sincronizar la UI con el estado del Modelo ---
    private fun actualizarVistaTablero() {
        for (r in 0 until NUM_ROWS) {
            for (c in 0 until NUM_COLS) {
                val casillaLogica = tableroLogico.getCasilla(r, c)!!
                val cellView = cellViews[r][c]

                cellView.text = "" // Limpiar texto anterior
                cellView.setBackgroundColor(Color.DKGRAY) // Color por defecto de casilla oculta

                if (casillaLogica.isMarcada()) {
                    cellView.text = "🚩" // Emoji de bandera
                    cellView.setBackgroundColor(Color.CYAN)
                } else if (casillaLogica.isAbierta()) {
                    // La casilla está abierta, mostrar su contenido
                    cellView.setBackgroundColor(Color.LTGRAY)
                    if (casillaLogica.isMina()) {
                        //cellView.text = "M"
                        cellView.text = "💣" // Emoji de bomba
                        cellView.setBackgroundColor(Color.RED)
                    } else if (casillaLogica.getMinasAlrededor() > 0) {
                        cellView.text = casillaLogica.getMinasAlrededor().toString()
                    } else {
                        // Casilla vacía y abierta, no mostrar nada
                        cellView.text = ""
                    }
                }
            }
        }
    }

    private fun mostarMinas() {
        for (r in 0 until NUM_ROWS) {
            for (c in 0 until NUM_COLS) {
                val casillaLogica = tableroLogico.getCasilla(r, c)!!
                val cellView = cellViews[r][c]

                cellView.text = "" // Limpiar texto anterior
                cellView.setBackgroundColor(Color.DKGRAY) // Color por defecto de casilla oculta

                if (casillaLogica.isMina()) {
                    //cellView.text = "M"
                    cellView.text = "💣" // Emoji de bomba
                    cellView.setBackgroundColor(Color.RED)
                }
            }
        }
    }

    private fun verificarEstadoDelJuego() {
        val resultado = tableroLogico.verificarResultado()

        if (resultado != 3) { // 3 es "Partida en progreso"
            juegoActivo = false
            sendMoveButton.isEnabled = false // Desactivar el botón

            val mensaje = when (resultado) {
                0 -> "¡Boom! Has perdido."
                1, 2 -> "¡Felicidades! ¡Has ganado!"
                else -> ""
            }
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

            // Opcional: revelar todo el tablero al terminar
            revelarTableroCompleto()
        }
    }

    private fun revelarTableroCompleto() {
        for (r in 0 until NUM_ROWS) {
            for (c in 0 until NUM_COLS) {
                tableroLogico.getCasilla(r, c)?.abrir()
            }
        }
        actualizarVistaTablero() // Vuelve a dibujar el tablero con todo revelado
    }
}
