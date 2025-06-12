package com.example.myapplication.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.network.ConnectionListener
import com.example.myapplication.network.GameClient
import com.example.myapplication.network.GameConnection

class ClientActivity : AppCompatActivity(), ConnectionListener {
  private lateinit var gameConnection: GameConnection
  // Declaramos las vistas para poder acceder a ellas
  private lateinit var etIpAddress: EditText
  private lateinit var etPort: EditText
  private lateinit var btnConnect: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_client)

    // 1. Inicializamos las vistas usando findViewById
    etIpAddress = findViewById(R.id.etIpAddress)
    etPort = findViewById(R.id.etPort)
    btnConnect = findViewById(R.id.btnConnect)

    // 2. Configuramos el listener para el botón de conectar
    btnConnect.setOnClickListener { handleConnectButtonClick() }

    gameConnection = GameClient(ip, port)
    gameConnection.setConnectionListener(this)
    gameConnection.start()
  }

  private fun handleConnectButtonClick() {
    // 3. Obtenemos el texto de los EditText
    val ipAddress = etIpAddress.text.toString().trim()
    val portStr = etPort.text.toString().trim()

    // 4. Validamos que los campos no estén vacíos
    if (ipAddress.isEmpty()) {
      Toast.makeText(this, "Por favor, introduce una dirección IP", Toast.LENGTH_SHORT).show()
      etIpAddress.error = "Campo requerido" // Muestra un error en el EditText
      return
    }

    if (portStr.isEmpty()) {
      Toast.makeText(this, "Por favor, introduce un puerto", Toast.LENGTH_SHORT).show()
      etPort.error = "Campo requerido"
      return
    }

    // Convertimos el puerto a un número entero
    val port = portStr.toIntOrNull()
    if (port == null || port !in 1..65535) {
      Toast.makeText(this, "El puerto debe ser un número entre 1 y 65535", Toast.LENGTH_LONG).show()
      etPort.error = "Puerto inválido"
      return
    }

    val message = "Intentando conectar a IP: $ipAddress en el puerto: $port"
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}
