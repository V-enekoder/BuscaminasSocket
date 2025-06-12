package com.example.myapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.network.GameConnection
import com.example.myapplication.network.GameServer
import java.util.Locale

class ServerActivity : AppCompatActivity(), GameConnection {
  private val DELAY_MILLISECONDS: Long = 5000 // 5 segundos
  private lateinit var gameConnection: GameConnection

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_server)

    // 1. Encontrar el TextView por su ID
    val tvIpAddress: TextView = findViewById(R.id.tvIpAddress)

    // 2. Obtener la dirección IP y mostrarla
    val ipAddress = getDeviceIpAddress()
    if (ipAddress != null) {
      tvIpAddress.text = ipAddress
    } else {
      tvIpAddress.text = "No se pudo obtener la IP. ¿Estás conectado a una red WiFi?"
    }
    var gameConnection = GameServer(8080)
    gameConnection.setConnectionListener(this) // La Activity escucha los eventos
    gameConnection.start() // Inicia el servidor en un hilo de fondo

    navigateToNextScreenAfterDelay()
  }

  /**
   * Obtiene la dirección IP del dispositivo conectado a una red WiFi. Retorna la IP como un String
   * o null si no se puede obtener.
   */
  private fun getDeviceIpAddress(): String? {
    try {
      // Usa el servicio WifiManager para obtener la información de la conexión
      val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
      val ipAddressInt = wifiManager.connectionInfo.ipAddress

      // El entero de la IP necesita ser convertido a un formato legible (e.g., 192.168.1.100)
      if (ipAddressInt == 0) return null // No hay dirección IP

      return String.format(
          Locale.getDefault(),
          "%d.%d.%d.%d",
          (ipAddressInt and 0xff),
          (ipAddressInt shr 8 and 0xff),
          (ipAddressInt shr 16 and 0xff),
          (ipAddressInt shr 24 and 0xff))
    } catch (e: Exception) {
      e.printStackTrace()
      return null
    }
  }

  private fun navigateToNextScreenAfterDelay() {
    // Handler().postDelayed({}, tiempo) está obsoleto. Esta es la forma moderna.
    Handler(Looper.getMainLooper())
        .postDelayed(
            {
              // El código dentro de este bloque se ejecutará después del tiempo de espera

              // Crea el Intent para ir a GameConfigurationActivity
              val intent = Intent(this, GameConfigurationActivity::class.java)
              startActivity(intent)

              // Opcional: Finaliza esta actividad para que el usuario no pueda volver a ella con el
              // botón "atrás"
              finish()
            },
            DELAY_MILLISECONDS)
  }

  /*override fun onMessageReceived(message: String) {
    // El oponente ha hecho una jugada. Actualiza el tablero.
    runOnUiThread {
      // Actualiza la UI aquí
    }
  }*/

  // ... implementar los otros métodos de ConnectionListener ...

  override fun onDestroy() {
    super.onDestroy()
    gameConnection.close() // ¡Muy importante liberar los recursos!
  }
}
