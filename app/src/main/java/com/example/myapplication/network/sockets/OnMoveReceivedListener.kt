package com.example.myapplication.network.sockets

interface OnMoveReceivedListener {
  fun onMoveReceived(turno: Int, action: String, row: Int, col: Int)
}
