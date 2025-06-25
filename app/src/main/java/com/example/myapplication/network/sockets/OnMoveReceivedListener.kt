package com.example.myapplication.network.sockets

interface OnMoveReceivedListener {
  fun onMoveReceived(action: String, row: Int, col: Int)
}
