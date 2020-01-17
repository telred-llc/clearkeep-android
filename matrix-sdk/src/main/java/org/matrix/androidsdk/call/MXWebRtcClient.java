/*
 * Copyright 2015 OpenMarket Ltd
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.androidsdk.call;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.oney.WebRTCModule.EglUtils;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MXWebRtcClient {
    private static final String LOG_TAG = MXWebRtcClient.class.getSimpleName();

    private static final String VIDEO_TRACK_ID = "ARDAMSv0";
    private static final String AUDIO_TRACK_ID = "ARDAMSa0";

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 360;
    private static final int DEFAULT_FPS = 30;

    private static final int CAMERA_TYPE_FRONT = 1;
    private static final int CAMERA_TYPE_REAR = 2;
    private static final int CAMERA_TYPE_UNDEFINED = -1;

    // Executor thread is started once in private ctor and is used for all
    // peer connection API calls to ensure new peer connection factory is
    // created on the same thread as previously destroyed factory.
    private static final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    static private String mFrontCameraName = null;
    static private String mBackCameraName = null;

    private MXWebRtcCall mCall;
    private MXWebRtcView mFullScreenRTCView = null;
    private MXWebRtcView mPipRTCView = null;

    private PeerConnectionFactory mPeerConnectionFactory = null;
    private PeerConnection mPeerConnection = null;
    private CameraVideoCapturer mCameraVideoCapturer = null;

    private VideoSource mVideoSource = null;
    private VideoTrack mLocalVideoTrack = null;
    private VideoTrack mRemoteVideoTrack = null;
    private AudioSource mAudioSource = null;
    private AudioTrack mLocalAudioTrack = null;
    private MediaStream mLocalMediaStream = null;
    private SurfaceTextureHelper mSurfaceTextureHelper;

    private boolean mIsCameraSwitched;
    private boolean mIsCameraUnplugged = false;
    private boolean mUsingLargeLocalRenderer = true;
    private int mCameraInUse = CAMERA_TYPE_UNDEFINED;

    private PeerConnection.Observer mPeerConnectionObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(LOG_TAG, "mPeerConnection: onSignalingChange " + signalingState);
        }

        @Override
        public void onIceConnectionChange(final PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(LOG_TAG, "mPeerConnection: onIceConnectionChange " + iceConnectionState);
            if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
                if ((null != mLocalVideoTrack) && mUsingLargeLocalRenderer && mCall.isVideo()) {
                    mLocalVideoTrack.setEnabled(false);

                    // in conference call, there is no local preview,
                    // the local attendee video is sent by the server among the others conference attendees.
                    if (!mCall.isConference()) {
                        // add local preview, only for 1:1 call
                        //mLocalVideoTrack.addRenderer(mSmallLocalRenderer);
                        mPipRTCView.post(() -> {
                            mPipRTCView.setStream(mLocalMediaStream);
                            mPipRTCView.setVisibility(View.VISIBLE);
                            // to be able to display the avatar video above the large one
                            mPipRTCView.setZOrder(1);
                        });
                    }

                    mLocalVideoTrack.setEnabled(true); // TODO put into post above?
                    mUsingLargeLocalRenderer = false;

                    mCall.getCallView().post(() -> {
                        if (null != mCall.getCallView()) {
                            mCall.getCallView().invalidate();
                        }
                    });
                }

                mCall.dispatchOnStateDidChange(IMXCall.CALL_STATE_CONNECTED);
/*
            } else if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
                // TODO: try reconnect?
            } else if (iceConnectionState == PeerConnection.IceConnectionState.CLOSED) {
 */
            } else if (iceConnectionState == PeerConnection.IceConnectionState.FAILED) {
                mCall.dispatchOnCallError(IMXCall.CALL_ERROR_ICE_FAILED);
                close();
            }
        }

        @Override
        public void onIceConnectionReceivingChange(boolean var1) {
            Log.d(LOG_TAG, "mPeerConnection: onIceConnectionReceivingChange " + var1);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] var1) {
            Log.d(LOG_TAG, "mPeerConnection: onIceCandidatesRemoved " + Arrays.toString(var1));
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(LOG_TAG, "mPeerConnection: onIceGatheringChange " + iceGatheringState);
        }

        @Override
        public void onAddTrack(RtpReceiver var1, MediaStream[] var2) {
            Log.d(LOG_TAG, "mPeerConnection: onAddTrack " + var1 + " -- " + Arrays.toString(var2));
        }

        @Override
        public void onIceCandidate(final IceCandidate iceCandidate) {
            Log.d(LOG_TAG, "mPeerConnection: onIceCandidate " + iceCandidate);
//            mCall.sendNewCandidate(iceCandidate);
        }

        @Override
        public void onAddStream(final MediaStream mediaStream) {
            Log.d(LOG_TAG, "mPeerConnection: onAddStream " + mediaStream);
            if ((mediaStream.videoTracks.size() == 1) && !mCall.isCallEnded()) {
                mRemoteVideoTrack = mediaStream.videoTracks.get(0);
                mRemoteVideoTrack.setEnabled(true);
                mFullScreenRTCView.post(() -> {
                    mFullScreenRTCView.setStream(mediaStream);
                    mFullScreenRTCView.setVisibility(View.VISIBLE);
                });
            }
        }

        @Override
        public void onRemoveStream(final MediaStream mediaStream) {
            Log.d(LOG_TAG, "mPeerConnection: onRemoveStream " + mediaStream);
            if (null != mRemoteVideoTrack) {
                mRemoteVideoTrack.dispose();
                mRemoteVideoTrack = null;
                mediaStream.videoTracks.get(0).dispose();
            }
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.d(LOG_TAG, "mPeerConnection: onDataChannel " + dataChannel);
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(LOG_TAG, "mPeerConnection: onRenegotiationNeeded");
            // can be ignored while connection is not up
        }
    };

    /**
     * Constructor
     *
     * @param session the session
     * @param context the context
     */
    MXWebRtcClient(MXSession session, Context context, MXWebRtcCall call) {
        if (null == session) {
            throw new AssertionError("MXWebRtcCall : session cannot be null");
        }

        if (null == context) {
            throw new AssertionError("MXWebRtcCall : context cannot be null");
        }

        // privacy
        Log.d(LOG_TAG, "constructor");
        mCall = call;

        mExecutor.execute(() -> {
            PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions());
            Log.d(LOG_TAG, "initializePeerConnectionFactory(): done");
            if (call.isVideo()) {
                createVideoPeerConnectionFactory();
            } else {
                createAudioPeerConnectionFactory();
            }
            createLocalStream();
            // call createOffer only for outgoing calls
            if (!call.isIncoming()) {
                createOffer(call.isVideo());
            }
        });
    }

    /**
     * Tells if the camera2 Api is supported
     *
     * @param context the context
     * @return true if the Camera2 API is supported
     */
    private static boolean useCamera2(Context context) {
        return Camera2Enumerator.isSupported(context);
    }

    /**
     * Get a camera enumerator
     *
     * @param context the context
     * @return the camera enumerator
     */
    private static CameraEnumerator getCameraEnumerator(Context context) {
        if (useCamera2(context)) {
            return new Camera2Enumerator(context);
        } else {
            return new Camera1Enumerator(false);
        }
    }

    /**
     * Initialize the video call UI
     *
     * @param aLocalVideoPosition position of the local video attendee
     */
    void initVideoCallUI(ViewGroup view, VideoLayoutConfiguration aLocalVideoPosition) {
        view.post(() -> {
            try {
                Log.d(LOG_TAG, "initVideoCallUI() building UI");

                mFullScreenRTCView = new MXWebRtcView(mCall.mContext);
                mFullScreenRTCView.setBackgroundColor(ContextCompat.getColor(mCall.mContext, android.R.color.black));
                view.addView(mFullScreenRTCView,
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                mFullScreenRTCView.setVisibility(View.GONE);

                mPipRTCView = new MXWebRtcView(mCall.mContext);
                view.addView(mPipRTCView,
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                mPipRTCView.setBackgroundColor(ContextCompat.getColor(mCall.mContext, android.R.color.transparent));
                mPipRTCView.setVisibility(View.GONE);

                if (null != aLocalVideoPosition) {
                    updateWebRtcViewLayout(aLocalVideoPosition);
                    Log.d(LOG_TAG, "initVideoCallUI(): " + aLocalVideoPosition);
                } else {
                    updateWebRtcViewLayout(new VideoLayoutConfiguration(5, 5, 25, 25));
                }
                view.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Log.e(LOG_TAG, "initVideoCallUI(): Exception Msg =" + e.getMessage(), e);
            }
        });
    }

    /**
     * Signal hangup via WebRTC.
     */
    void hangup() {
        mExecutor.execute(() -> {
            Log.d(LOG_TAG, "hangup(): close peer connection");
            mPeerConnection.close();
        });
    }

    /**
     * Close the connection
     */
    void close() {
        Log.d(LOG_TAG, "close()");
        if (null != mFullScreenRTCView) {
            mFullScreenRTCView.post(() -> mFullScreenRTCView.setStream(null));
        }
        if (null != mPipRTCView) {
            mPipRTCView.post(() -> mPipRTCView.setStream(null));
        }
        mExecutor.execute(this::closeInternal);
    }

    @Override
    protected void finalize() {
        Log.d(LOG_TAG, "finalize()");
        close();
    }

    private boolean isSwitchCameraSupported() {
        String[] deviceNames = getCameraEnumerator(mCall.mContext).getDeviceNames();
        return (null != deviceNames) && (0 != deviceNames.length);
    }

    boolean switchRearFrontCamera() {
        if ((null != mCameraVideoCapturer) && (isSwitchCameraSupported())) {
            try {
                mCameraVideoCapturer.switchCamera(null);

                // toggle the video capturer instance
                if (CAMERA_TYPE_FRONT == mCameraInUse) {
                    mCameraInUse = CAMERA_TYPE_REAR;
                } else {
                    mCameraInUse = CAMERA_TYPE_FRONT;
                }

                // compute camera switch new status
                mIsCameraSwitched = !mIsCameraSwitched;

                return true;
            } catch (Exception e) {
                Log.e(LOG_TAG, "switchRearFrontCamera(): failed " + e.getMessage(), e);
            }
        } else {
            Log.w(LOG_TAG, "switchRearFrontCamera(): failure - invalid values");
        }
        return false;
    }

    void muteVideoRecording(boolean muteValue) {
        Log.d(LOG_TAG, "muteVideoRecording(): muteValue=" + muteValue);

        if (!mCall.isCallEnded()) {
            if (mCall.isVideo() && null != mLocalVideoTrack) {
                mLocalVideoTrack.setEnabled(!muteValue);
            } else {
                Log.d(LOG_TAG, "muteVideoRecording(): failure - no mLocalVideoTrack");
            }
        } else {
            Log.d(LOG_TAG, "muteVideoRecording(): the call is ended");
        }
    }

    boolean isVideoRecordingMuted() {
        boolean isMuted = false;

        if (!mCall.isCallEnded()) {
            if (null != mLocalVideoTrack) {
                isMuted = !mLocalVideoTrack.enabled();
                Log.d(LOG_TAG, "isVideoRecordingMuted() = " + isMuted);
            } else {
                Log.w(LOG_TAG, "isVideoRecordingMuted(): no local video track");
            }
        } else {
            Log.d(LOG_TAG, "isVideoRecordingMuted(): the call is ended");
        }

        return isMuted;
    }

    boolean isCameraSwitched() {
        return mIsCameraSwitched;
    }

    /**
     * The activity is paused.
     */
    void onPause() {
        try {
            Log.d(LOG_TAG, "onPause with active call");

            // unplugged the camera to avoid loosing the video when the application is suspended
            if (mCall.isVideo() && !isVideoRecordingMuted()) {
                muteVideoRecording(true);
                mIsCameraUnplugged = true;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "onPause failed " + e.getMessage(), e);
        }
    }

    /**
     * The activity is resumed.
     */
    void onResume() {
        try {
            Log.d(LOG_TAG, "onResume with active call");

            if (mIsCameraUnplugged) {
                muteVideoRecording(false);
                mIsCameraUnplugged = false;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "onResume failed " + e.getMessage(), e);
        }
    }

    /**
     * Update the webRtcView layout
     *
     * @param aLocalVideoPosition the video configuration
     */
    void updateWebRtcViewLayout(VideoLayoutConfiguration aLocalVideoPosition) {
        if (null != mPipRTCView) {
            mPipRTCView.post(() -> {
                final DisplayMetrics displayMetrics = mPipRTCView.getResources().getDisplayMetrics();

                int screenWidth = (aLocalVideoPosition.mDisplayWidth > 0) ? aLocalVideoPosition.mDisplayWidth : displayMetrics.widthPixels;
                int screenHeight = (aLocalVideoPosition.mDisplayHeight > 0) ? aLocalVideoPosition.mDisplayHeight : displayMetrics.heightPixels;

                int x = screenWidth * aLocalVideoPosition.mX / 100;
                int y = screenHeight * aLocalVideoPosition.mY / 100;
                int width = screenWidth * aLocalVideoPosition.mWidth / 100;
                int height = screenHeight * aLocalVideoPosition.mHeight / 100;

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.leftMargin = x;
                params.topMargin = y;

                mPipRTCView.setLayoutParams(params);
            });
        }
    }

    /**
     * Some new ICE candidates are received
     *
     * @param candidates the channel candidates
     */
    void addIceCandidates(final List<IceCandidate> candidates) {
        mExecutor.execute(() -> {
            for (IceCandidate cand : candidates) {
                Log.d(LOG_TAG, "onNewCandidates(): addIceCandidate " + cand);
                mPeerConnection.addIceCandidate(cand);
            }
        });
    }

    /**
     * Set remote description for incoming calls
     */
    void setRemoteDescription(final SessionDescription description) {
        mExecutor.execute(() -> {
            Log.d(LOG_TAG, "setRemoteDescription");
            mPeerConnection.setRemoteDescription(new BaseSdpObserver() {
                @Override
                public void onSetSuccess() {
                    Log.d(LOG_TAG, "setRemoteDescription onSetSuccess");
//                    mCall.onIncomingPrepared();
                    if (mCall.isIncoming()) {
                        createAnswer(mCall.isVideo());
                    }
                }
            }, description);
        });
    }

    private void createAnswer(boolean isVideo) {
        mExecutor.execute(() -> {
            Log.d(LOG_TAG, "createAnswer()");
            MediaConstraints constraints = new MediaConstraints();
            constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
            constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", isVideo ? "true" : "false"));
            mPeerConnection.createAnswer(new BaseSdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    Log.d(LOG_TAG, "createAnswer onCreateSuccess");
                    final SessionDescription sdp = new SessionDescription(sessionDescription.type, sessionDescription.description);
                    // must be done to before sending the invitation message
                    mPeerConnection.setLocalDescription(new BaseSdpObserver() {
                        @Override
                        public void onSetSuccess() {
                            Log.d(LOG_TAG, "setLocalDescription onSetSuccess");
                            // TODO run in UI thread?
//                            mCall.sendAnswer(sdp);
                            mCall.dispatchOnStateDidChange(IMXCall.CALL_STATE_CONNECTING);
                        }
                    }, sdp);
                }
            }, constraints);
        });
    }

    private void createOffer(boolean isVideo) {
        mExecutor.execute(() -> {
            Log.d(LOG_TAG, "createOffer(): isVideo = " + isVideo);
            MediaConstraints constraints = new MediaConstraints();
            constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
            constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", isVideo ? "true" : "false"));
            mPeerConnection.createOffer(new BaseSdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    Log.d(LOG_TAG, "createOffer onCreateSuccess");
                    final SessionDescription sdp = new SessionDescription(sessionDescription.type, sessionDescription.description);

                    // must be done to before sending the invitation message
                    mPeerConnection.setLocalDescription(new BaseSdpObserver() {
                        @Override
                        public void onSetSuccess() {
                            Log.d(LOG_TAG, "setLocalDescription onSetSuccess");
//                            mCall.sendInvite(sdp);
                            mCall.dispatchOnStateDidChange(IMXCall.CALL_STATE_INVITE_SENT);
                        }
                    }, sdp);
                }
            }, constraints);
            mCall.dispatchOnStateDidChange(IMXCall.CALL_STATE_WAIT_CREATE_OFFER);
        });
    }

    private void createVideoPeerConnectionFactory() {
        Log.d(LOG_TAG, "createVideoPeerConnectionFactory()");

        DefaultVideoEncoderFactory defaultVideoEncoderFactory =
                new DefaultVideoEncoderFactory(
                        EglUtils.getRootEglBase().getEglBaseContext(),
                        /* enableIntelVp8Encoder */true,
                        /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory =
                new DefaultVideoDecoderFactory(EglUtils.getRootEglBase().getEglBaseContext());

        mPeerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();

        createVideoTrack();
        createAudioTrack();
    }

    private void createAudioPeerConnectionFactory() {
        Log.d(LOG_TAG, "createAudioPeerConnectionFactory()");

        mPeerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();
        createAudioTrack();
    }

    private void createLocalStream() {
        Log.d(LOG_TAG, "createLocalStream(): IN");

        // check there is at least one stream to start a call
        if ((null == mLocalVideoTrack) && (null == mLocalAudioTrack)) {
            Log.d(LOG_TAG, "createLocalStream(): CALL_ERROR_CALL_INIT_FAILED");

            //dispatchOnCallError(CALL_ERROR_CALL_INIT_FAILED);
            //hangup("no_stream");
            //terminate(IMXCall.END_CALL_REASON_UNDEFINED);
            return;
        }

        // create our local stream to add our audio and video tracks
        mLocalMediaStream = mPeerConnectionFactory.createLocalMediaStream("ARDAMS");
        // add video track to local stream
        if (null != mLocalVideoTrack) {
            mLocalMediaStream.addTrack(mLocalVideoTrack);
        }
        // add audio track to local stream
        if (null != mLocalAudioTrack) {
            mLocalMediaStream.addTrack(mLocalAudioTrack);
        }

        if (null != mFullScreenRTCView) {
            mFullScreenRTCView.post(() -> {
                mFullScreenRTCView.setStream(mLocalMediaStream);
                mFullScreenRTCView.setVisibility(View.VISIBLE);
            });
        }

        // define at least on server
        if (mCall.getIceServers().isEmpty()) {
            Log.d(LOG_TAG, "createLocalStream(): No iceServers found ");
        }

        // define constraints
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(mCall.getIceServers());
        rtcConfig.enableRtpDataChannel = true;

        // start connecting to the other peer by creating the peer connection
        mPeerConnection = mPeerConnectionFactory.createPeerConnection(rtcConfig, mPeerConnectionObserver);

        if (null == mPeerConnection) {
            Log.e(LOG_TAG, "createLocalStream(): create peer connection failed");
            mCall.dispatchOnCallError(IMXCall.CALL_ERROR_ICE_FAILED);
            close();
            return;
        }

        // send our local video and audio stream to make it seen by the other part
        mPeerConnection.addStream(mLocalMediaStream);
        Log.d(LOG_TAG, "createLocalStream(): OUT");
    }

    /**
     * Test if the camera is not used by another app.
     * It is used to prevent crashes at org.webrtc.Camera1Session.create(Camera1Session.java:80)
     * when the front camera is not available.
     *
     * @param context    the context
     * @param isFrontOne true if the camera is the
     * @return true if the camera is used.
     */
    private boolean isCameraInUse(Context context, boolean isFrontOne) {
        boolean isUsed = false;

        if (!useCamera2(context)) {
            int cameraId = -1;
            int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(i, info);

                if ((info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) && isFrontOne) {
                    cameraId = i;
                    break;
                } else if ((info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) && !isFrontOne) {
                    cameraId = i;
                    break;
                }
            }

            if (cameraId >= 0) {
                android.hardware.Camera c = null;
                try {
                    c = android.hardware.Camera.open(cameraId);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "isCameraInUse() : failed " + e.getMessage(), e);
                } finally {
                    isUsed = (null == c);
                    if (c != null) {
                        c.release();
                    }
                }
            }
        }

        return isUsed;
    }

    /**
     * @return true if the device has a camera device
     */
    private boolean hasCameraDevice() {
        CameraEnumerator enumerator = getCameraEnumerator(mCall.mContext);
        String[] deviceNames = enumerator.getDeviceNames();
        int cameraCount = 0;

        mBackCameraName = mFrontCameraName = null;

        if (null != deviceNames) {
            for (String deviceName : deviceNames) {
                if (enumerator.isFrontFacing(deviceName) && !isCameraInUse(mCall.mContext, true)) {
                    mFrontCameraName = deviceName;
                } else if (enumerator.isBackFacing(deviceName) && !isCameraInUse(mCall.mContext, false)) {
                    mBackCameraName = deviceName;
                }
            }

            cameraCount = deviceNames.length;
        }

        Log.d(LOG_TAG, "hasCameraDevice():  camera number= " + cameraCount);
        Log.d(LOG_TAG, "hasCameraDevice():  frontCameraName=" + mFrontCameraName + " backCameraName=" + mBackCameraName);

        return (null != mFrontCameraName) || (null != mBackCameraName);
    }

    /**
     * Create the video capturer
     *
     * @param cameraName the selected camera name
     * @return the video capturer
     */
    private CameraVideoCapturer createVideoCapturer(String cameraName) {
        CameraVideoCapturer cameraVideoCapturer = null;

        CameraEnumerator camerasEnumerator = getCameraEnumerator(mCall.mContext);
        final String[] deviceNames = camerasEnumerator.getDeviceNames();


        if ((null != deviceNames) && (deviceNames.length > 0)) {
            for (String name : deviceNames) {
                if (name.equals(cameraName)) {
                    cameraVideoCapturer = camerasEnumerator.createCapturer(name, null);
                    if (null != cameraVideoCapturer) {
                        break;
                    }
                }
            }

            if (null == cameraVideoCapturer) {
                cameraVideoCapturer = camerasEnumerator.createCapturer(deviceNames[0], null);
            }
        }

        return cameraVideoCapturer;
    }

    /**
     * Create the local video stack
     */
    private void createVideoTrack() { // permission crash
        Log.d(LOG_TAG, "createVideoTrack");

        // create the local renderer only if there is a camera on the device
        if (hasCameraDevice()) {
            try {
                if (null != mCameraVideoCapturer) {
                    mCameraVideoCapturer.dispose();
                    mCameraVideoCapturer = null;
                }

                if (null != mFrontCameraName) {
                    mCameraVideoCapturer = createVideoCapturer(mFrontCameraName);

                    if (null == mCameraVideoCapturer) {
                        Log.e(LOG_TAG, "Cannot create Video Capturer from front camera");
                    } else {
                        mCameraInUse = CAMERA_TYPE_FRONT;
                    }
                }

                if ((null == mCameraVideoCapturer) && (null != mBackCameraName)) {
                    mCameraVideoCapturer = createVideoCapturer(mBackCameraName);

                    if (null == mCameraVideoCapturer) {
                        Log.e(LOG_TAG, "Cannot create Video Capturer from back camera");
                    } else {
                        mCameraInUse = CAMERA_TYPE_REAR;
                    }
                }
            } catch (Exception ex2) {
                // catch exception due to Android M permissions, when
                // a call is received and the permissions (camera and audio) were not yet granted
                Log.e(LOG_TAG, "createVideoTrack(): Exception Msg=" + ex2.getMessage(), ex2);
            }

            if (null != mCameraVideoCapturer) {
                Log.d(LOG_TAG, "createVideoTrack find a video capturer");

                try {
                    // Following instruction here: https://stackoverflow.com/questions/55085726/webrtc-create-peerconnectionfactory-object
                    EglBase rootEglBase = EglUtils.getRootEglBase();
                    mSurfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
                    mVideoSource = mPeerConnectionFactory.createVideoSource(mCameraVideoCapturer.isScreencast());
                    mCameraVideoCapturer.initialize(mSurfaceTextureHelper, mCall.mContext, mVideoSource.getCapturerObserver());

                    mCameraVideoCapturer.startCapture(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FPS);

                    mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, mVideoSource);
                    mLocalVideoTrack.setEnabled(true);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "createVideoSource fails with exception " + e.getMessage(), e);

                    mLocalVideoTrack = null;

                    if (null != mVideoSource) {
                        mVideoSource.dispose();
                        mVideoSource = null;
                    }
                }
            } else {
                Log.e(LOG_TAG, "createVideoTrack(): Cannot create Video Capturer - no camera available");
            }
        }
    }

    /**
     * Create the local audio stack
     */
    private void createAudioTrack() {
        Log.d(LOG_TAG, "createAudioTrack");

        MediaConstraints audioConstraints = new MediaConstraints();

        // add all existing audio filters to avoid having echos
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation2", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googDAEchoCancellation", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googTypingNoiseDetection", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl2", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression2", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAudioMirroring", "false"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googHighpassFilter", "true"));

        mAudioSource = mPeerConnectionFactory.createAudioSource(audioConstraints);
        mLocalAudioTrack = mPeerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, mAudioSource);
    }

    /**
     * Cleanup the connection
     */
    private void closeInternal() {
        Log.d(LOG_TAG, "closeInternal() IN");

        if (null != mPeerConnection) {
            Log.d(LOG_TAG, "Dispose mPeerConnection.");
            mPeerConnection.dispose();
            mPeerConnection = null;
        }

        if (null != mCameraVideoCapturer) {
            Log.d(LOG_TAG, "Stopping capture.");
            try {
                mCameraVideoCapturer.stopCapture();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Log.d(LOG_TAG, "Dispose mCameraVideoCapturer.");
            mCameraVideoCapturer.dispose();
            mCameraVideoCapturer = null;
        }

        if (null != mVideoSource) {
            Log.d(LOG_TAG, "Dispose mVideoSource.");
            mVideoSource.dispose();
            mVideoSource = null;
        }

        if (null != mAudioSource) {
            Log.d(LOG_TAG, "Dispose mAudioSource.");
            mAudioSource.dispose();
            mAudioSource = null;
        }
/*
        FIXME Those do always block forever
        if (null != mLocalVideoTrack) {
            Log.d(LOG_TAG, "Dispose mLocalVideoTrack.");
            mLocalVideoTrack.dispose();
            mLocalVideoTrack = null;
        }

        if (null != mLocalAudioTrack) {
            Log.d(LOG_TAG, "Dispose mLocalAudioTrack.");
            mLocalAudioTrack.dispose();
            mLocalAudioTrack = null;
        }
*/

        if (null != mSurfaceTextureHelper) {
            Log.d(LOG_TAG, "Dispose mSurfaceTextureHelper.");
            mSurfaceTextureHelper.dispose();
            mSurfaceTextureHelper = null;
        }

        // FIXME Sometimes blocks forever
        if (null != mPeerConnectionFactory) {
            Log.d(LOG_TAG, "Dispose mPeerConnectionFactory.");
            mPeerConnectionFactory.dispose();
            mPeerConnectionFactory = null;
        }

        Log.d(LOG_TAG, "closeInternal() OUT");
    }

    private class BaseSdpObserver implements SdpObserver {

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(LOG_TAG, "SdpObserver onCreateSuccess");
        }

        @Override
        public void onSetSuccess() {
            Log.d(LOG_TAG, "SdpObserver onSetSuccess");
        }

        @Override
        public void onCreateFailure(String s) {
            Log.e(LOG_TAG, "SdpObserver onCreateFailure " + s);
            mExecutor.execute(() -> mCall.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED));
            // TODO hangup(null);
        }

        @Override
        public void onSetFailure(String s) {
            Log.e(LOG_TAG, "SdpObserver onSetFailure " + s);
            mExecutor.execute(() -> mCall.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED));
            // TODO hangup(null);
        }
    }
}