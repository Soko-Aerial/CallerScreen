package com.example.sigtrack_call

import android.content.Context
import android.util.Log
import org.webrtc.*

class WebRTCManager(context: Context) {

    private val eglBase = EglBase.create()
    val localView: SurfaceViewRenderer = SurfaceViewRenderer(context)
    val remoteView: SurfaceViewRenderer = SurfaceViewRenderer(context)

    private val peerConnectionFactory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null
    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null
    private var videoCapturer: CameraVideoCapturer? = null

    private val iceServers = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    init {
        // Initialize WebRTC
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)

        // Create PeerConnectionFactory
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBase.eglBaseContext))
            .createPeerConnectionFactory()

        setupSurfaceViews()
        setupPeerConnection()
        setupLocalTracks()  // ✅ Fix: Now calling this function!
    }

    private fun setupSurfaceViews() {
        localView.init(eglBase.eglBaseContext, null)
        remoteView.init(eglBase.eglBaseContext, null)
    }

    fun setupLocalTracks() {
        // Create video capturer
        videoCapturer = getCameraCapturer()
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        localVideoTrack = peerConnectionFactory.createVideoTrack("local_video", videoSource)

        // Create audio track
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("local_audio", audioSource)

        // Attach local video stream to the renderer
        localVideoTrack?.addSink(localView)

        // Start capturing video
        videoCapturer?.startCapture(1280, 720, 30)
    }

    private fun getCameraCapturer(): CameraVideoCapturer? {
        val cameraEnumerator = Camera2Enumerator(localView.context)
        for (deviceName in cameraEnumerator.deviceNames) {
            if (cameraEnumerator.isFrontFacing(deviceName)) {
                return cameraEnumerator.createCapturer(deviceName, null)
            }
        }
        return null
    }

    private fun setupPeerConnection() {
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE

        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let {
                    // Send ICE candidate to remote peer
                    Log.d("WebRTC", "New ICE Candidate: ${it.sdp}")
                    // TODO: Send this candidate over signaling server
                }
            }

            override fun onAddStream(stream: MediaStream?) {
                stream?.videoTracks?.firstOrNull()?.addSink(remoteView)
            }

            override fun onSignalingChange(signalingState: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState?) {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
            override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {}
            override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {}
            override fun onTrack(transceiver: RtpTransceiver?) {}
            override fun onRemoveStream(p0: MediaStream?) {}
            override fun onRenegotiationNeeded() {}
            override fun onDataChannel(p0: DataChannel?) {}
        })
    }

    fun toggleCamera() {
        videoCapturer?.switchCamera(null)
    }

    fun toggleMute() {
        localAudioTrack?.setEnabled(!(localAudioTrack?.enabled() ?: true))
    }

    fun closeConnection() {
        peerConnection?.close()
        videoCapturer?.stopCapture()
    }
}
