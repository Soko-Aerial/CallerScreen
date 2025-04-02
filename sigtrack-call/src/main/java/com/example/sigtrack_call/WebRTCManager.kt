package com.example.sigtrack_call

import android.content.Context
import android.util.Log
import org.webrtc.*

class WebRTCManager(private val context: Context, var signalingClient: SignalingClient) {
    private val eglBase = EglBase.create()
    val localView: SurfaceViewRenderer = SurfaceViewRenderer(context)
    val remoteView: SurfaceViewRenderer = SurfaceViewRenderer(context)
    private var peerConnectionFactory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null
    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var localStream: MediaStream? = null
    private var remoteVideoTrack: VideoTrack? = null
    private val iceServers = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    init {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBase.eglBaseContext))
            .createPeerConnectionFactory()
    }

    fun setupLocalTracks(videoCapturer: VideoCapturer) {
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast)
        videoCapturer.startCapture(1280, 720, 30)
        localVideoTrack = peerConnectionFactory.createVideoTrack("LOCAL_VIDEO", videoSource)

        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("LOCAL_AUDIO", audioSource)

        localStream = peerConnectionFactory.createLocalMediaStream("LOCAL_STREAM").apply {
            addTrack(localVideoTrack)
            addTrack(localAudioTrack)
        }
    }

    fun createPeerConnection(): PeerConnection? {
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                signalingClient.sendIceCandidate(candidate)
            }

            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {
                // Handle ICE candidates removed (optional)
            }

            override fun onSignalingChange(signalingState: PeerConnection.SignalingState) {
                // Handle signaling state changes
            }

            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState) {
                // Handle ICE connection state changes
            }

            override fun onIceConnectionReceivingChange(receiving: Boolean) {
                // Handle changes in receiving ICE connection (deprecated in newer versions)
            }

            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState) {
                // Handle ICE gathering state changes
            }

            override fun onAddStream(mediaStream: MediaStream) {
                // Handle incoming media stream (Deprecated: Use onTrack)
            }

            override fun onRemoveStream(mediaStream: MediaStream) {
                // Handle stream removal (Deprecated: Use onTrack)
            }

            override fun onDataChannel(dataChannel: DataChannel) {
                // Handle data channel events
            }

            override fun onRenegotiationNeeded() {
                // Handle renegotiation if needed
            }

            override fun onTrack(transceiver: RtpTransceiver) {
                // Handle new media tracks (Video/Audio)
            }
        })

        peerConnection?.addStream(localStream)
        return peerConnection
    }

    fun createOffer() {
        peerConnection?.createOffer(object : SdpObserverImpl() {
            override fun onCreateSuccess(sdp: SessionDescription?) {
                sdp?.let {
                    peerConnection?.setLocalDescription(SdpObserverImpl(), it)
                    signalingClient.sendOffer(it)
                }
            }
        }, MediaConstraints())
    }

    fun acceptCall(remoteSdp: String) {
        val sessionDescription = SessionDescription(SessionDescription.Type.OFFER, remoteSdp)
        peerConnection?.setRemoteDescription(SdpObserverImpl(), sessionDescription)
        peerConnection?.createAnswer(object : SdpObserverImpl() {
            override fun onCreateSuccess(sdp: SessionDescription?) {
                sdp?.let {
                    peerConnection?.setLocalDescription(SdpObserverImpl(), it)
                    signalingClient.sendAnswer(it)
                }
            }
        }, MediaConstraints())
    }

    fun onRemoteAnswer(remoteSdp: String) {
        val sessionDescription = SessionDescription(SessionDescription.Type.ANSWER, remoteSdp)
        peerConnection?.setRemoteDescription(SdpObserverImpl(), sessionDescription)
    }

    fun addIceCandidate(candidate: IceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    fun startCall() {
        peerConnection = createPeerConnection()
        createOffer()
    }


    init {
        // Initialize SurfaceViewRenderers
        localView.init(eglBase.eglBaseContext, null)
        remoteView.init(eglBase.eglBaseContext, null)
        localView.setMirror(true)
        remoteView.setMirror(false)
    }

    fun toggleCamera() {
        val videoTrack = localVideoTrack as VideoTrack
        videoTrack.setEnabled(!videoTrack.enabled())
    }

    fun toggleMute() {
        val audioTrack = localAudioTrack as AudioTrack
        audioTrack.setEnabled(!audioTrack.enabled())
    }


}
