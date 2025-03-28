package com.example.sigtrack_call

import android.util.Log
import org.webrtc.IceCandidate
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class SdpObserverImpl : SdpObserver {
    override fun onCreateSuccess(sdp: SessionDescription?) {
        Log.d("WebRTC", "SDP Created: ${sdp?.type}")
    }

    override fun onSetSuccess() {
        Log.d("WebRTC", "SDP Set Successfully")
    }

    override fun onCreateFailure(error: String?) {
        Log.e("WebRTC", "SDP Creation Failed: $error")
    }

    override fun onSetFailure(error: String?) {
        Log.e("WebRTC", "SDP Set Failed: $error")
    }



}
