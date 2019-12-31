package com.example.margat.controller

import com.example.margat.activity.MainActivity
import com.example.margat.config.WebConfig
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketController {

    private lateinit var mSocket: Socket
    var mActivity: MainActivity

    constructor(mainActivity: MainActivity) {
        this.mActivity = mainActivity
        connectToServerSocket()
    }

    fun connectToServerSocket() {
        try {
            mSocket = IO.socket("${WebConfig.ipAddress}:${WebConfig.portNo}")
            mSocket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun getSocket(): Socket {
        return mSocket
    }
}