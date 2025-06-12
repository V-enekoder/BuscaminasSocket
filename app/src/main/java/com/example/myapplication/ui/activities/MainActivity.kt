package com.example.myapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

// Se elige si se quiere ser cliente o servidor

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Busca cada vista (widget) por su ID
    val btnServer: Button = findViewById(R.id.btnServer)
    val btnClient: Button = findViewById(R.id.btnClient)

    // Listener para el botón de Servidor
    btnServer.setOnClickListener {
      Toast.makeText(this, "Iniciando modo Servidor...", Toast.LENGTH_SHORT).show()

      // Crea un Intent para ir a ServerActivity
      val intent = Intent(this, ServerActivity::class.java)
      startActivity(intent)
    }

    // Listener para el botón de Cliente
    btnClient.setOnClickListener {
      Toast.makeText(this, "Iniciando modo Cliente...", Toast.LENGTH_SHORT).show()

      // Crea un Intent para ir a ClientActivity
      val intent = Intent(this, ClientActivity::class.java)
      startActivity(intent)
    }
  }
}
