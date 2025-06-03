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

class MainActivity : AppCompatActivity() {

    private val NUM_ROWS = 8
    private val NUM_COLS = 8
    private val CELL_SIZE_DP = 40 // Tamaño de cada celda en DP

    private lateinit var matrixGridLayout: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Para el diseño Edge-to-Edge
        setContentView(R.layout.activity_main) // Carga el XML

        // Referencia al ConstraintLayout raíz para el listener de WindowInsets
        val mainContainer = findViewById<View>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar el GridLayout desde el XML
        matrixGridLayout = findViewById(R.id.matrixGridLayout)

        // Configurar el GridLayout
        matrixGridLayout.rowCount = NUM_ROWS
        matrixGridLayout.columnCount = NUM_COLS

        // Generar y mostrar la matriz
        generateAndDisplayMatrix()
    }

    private fun generateAndDisplayMatrix() {
        // Convertir DP a Píxeles para el tamaño de la celda
        val cellSizePx = (CELL_SIZE_DP * resources.displayMetrics.density).toInt()

        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                // Crear una nueva vista para la celda (usaremos un TextView simple por ahora)
                val cellView = TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = cellSizePx
                        height = cellSizePx
                        // Especificar la fila y columna para esta celda en el GridLayout
                        rowSpec = GridLayout.spec(row, 1f) // El '1f' es el peso, útil para distribución
                        columnSpec = GridLayout.spec(col, 1f)
                        setMargins(2, 2, 2, 2) // Pequeños márgenes si useDefaultMargins no es suficiente
                    }
                    text = "($row,$col)" // Mostrar coordenadas
                    textSize = 10f      // Tamaño de texto pequeño
                    gravity = Gravity.CENTER
                    setBackgroundColor(if ((row + col) % 2 == 0) Color.LTGRAY else Color.DKGRAY) // Tablero de ajedrez
                    // setBackgroundColor(Color.parseColor("#DDDDDD")) // O un color sólido

                    // Añadir un listener de clic (opcional, ejemplo)
                    setOnClickListener {
                        // Aquí puedes manejar el clic en una celda
                        // Por ejemplo, revelar una celda en el buscaminas
                        text = "X" // Cambia el texto al hacer clic
                        (it as TextView).setBackgroundColor(Color.YELLOW)
                        println("Celda clickeada: ($row, $col)")
                    }
                }
                // Añadir la celda al GridLayout
                matrixGridLayout.addView(cellView)
            }
        }
    }
}

/*class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}*/