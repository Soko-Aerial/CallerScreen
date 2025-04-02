package com.example.sigtrack_call

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import java.util.concurrent.TimeUnit

class SignalingClient(
    private val serverUrl: String,
    private val webRTCManager: WebRTCManager,
    private val onIncomingCall: (String) -> Unit, // Callback to trigger UI navigation
    private val onCallDeclined: () -> Unit // Callback for handling declined calls
) {
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private var webSocket: WebSocket? = null

    init {
        connect()
    }

    private fun connect() {
        val request = Request.Builder().url(serverUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("SignalingClient", "Connected to signaling server")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("SignalingClient", "Received message: $text")
                val json = JSONObject(text)

                when (json.getString("type")) {
                    "offer" -> {
                        val sdp = json.getString("sdp")
                        val callerId = json.optString("callerId", "Unknown Caller") // ✅ Extract caller ID
                        webRTCManager.acceptCall(sdp)
                        onIncomingCall(callerId)
                    }
                    "answer" -> {
                        val sdp = json.getString("sdp")
                        webRTCManager.onRemoteAnswer(sdp)
                    }
                    "candidate" -> {
                        val candidate = IceCandidate(
                            json.getString("sdpMid"),
                            json.getInt("sdpMLineIndex"),
                            json.getString("candidate")
                        )
                        webRTCManager.addIceCandidate(candidate)
                    }
                    "call_declined" -> {
                        Log.d("SignalingClient", "Call was declined")
                        onCallDeclined() // Handle declined call in UI
                    }
                }
            }


            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("SignalingClient", "WebSocket error: ${t.message}")
                reconnect()
            }
        })
    }

    private fun reconnect() {
        Log.d("SignalingClient", "Attempting to reconnect...")
        webSocket?.cancel()
        connect()
    }

    fun sendOffer(sdp: SessionDescription) {
        val json = JSONObject()
        json.put("type", "offer")
        json.put("sdp", sdp.description)
        webSocket?.send(json.toString())
    }

    fun sendAnswer(sdp: SessionDescription) {
        val json = JSONObject()
        json.put("type", "answer")
        json.put("sdp", sdp.description)
        webSocket?.send(json.toString())
    }

    fun sendIceCandidate(candidate: IceCandidate) {
        val json = JSONObject()
        json.put("type", "candidate")
        json.put("sdpMid", candidate.sdpMid)
        json.put("sdpMLineIndex", candidate.sdpMLineIndex)
        json.put("candidate", candidate.sdp)
        webSocket?.send(json.toString())
    }

    fun sendCallDeclineMessage() {
        val json = JSONObject()
        json.put("type", "call_declined")
        webSocket?.send(json.toString())
    }
}
