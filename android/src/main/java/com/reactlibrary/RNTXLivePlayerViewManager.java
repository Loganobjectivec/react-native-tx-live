
package com.reactlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Map;

public class RNTXLivePlayerViewManager extends SimpleViewManager<TXCloudVideoView> implements ITXLivePlayListener {
    private static final String REACT_CLASS = "RNTXLivePlayer";
    private static final int COMMAND_START = 1;
    private static final int COMMAND_PAUSE = 2;
    private static final int COMMAND_RESUME = 3;
    private static final int COMMAND_STOP = 4;

    private TXCloudVideoView mVideoView = null;
    private TXLivePlayer mPlayer = null;

    private String mSource = "";
    private Boolean mHardwareDecode = false;
    private int mRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
    private int mRenderRotation = 0;
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private Boolean mMute = false;
    private TXLivePlayConfig mPlayConfig = null;
    private RCTEventEmitter mEventEmitter = null;

    enum Events {
        LOADING("onRNTXLivePlayerLoading"),
        PROGRESS("onRNTXLivePlayerProgress"),
        END("onRNTXLivePlayerEnd"),
        ERROR("onRNTXLivePlayerError"),
        DISCONNECT("onRNTXLivePlayerDisconnect"),
        BEGIN("onRNTXLivePlayerBegin");

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected TXCloudVideoView createViewInstance(ThemedReactContext reactContext) {
        mEventEmitter = reactContext.getJSModule(RCTEventEmitter.class);
        mVideoView = new TXCloudVideoView(reactContext);
        mPlayer = new TXLivePlayer(reactContext);
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        return mVideoView;
    }

    @Override
    public void onDropViewInstance(TXCloudVideoView view) {
        this.stop();
        mVideoView.onDestroy();
        super.onDropViewInstance(view);
    }

    @Override
    public void receiveCommand(TXCloudVideoView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_START:
                this.start();
                break;
            case COMMAND_PAUSE:
                this.pause();
                break;
            case COMMAND_STOP:
                this.stop();
                break;
            case COMMAND_RESUME:
                this.resume();
                break;
            default:
                Toast.makeText(root.getContext(), "NO SUPPORT COMMAND", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("start", COMMAND_START, "stop", COMMAND_STOP, "pause", COMMAND_PAUSE);
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (Events event : Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @ReactProp(name = "source")
    public void setSource(TXCloudVideoView v, String uri) {
        mSource = uri;
        this.start();
    }

    @ReactProp(name = "hardwareDecode")
    public void setHardwareDecode(TXCloudVideoView v, boolean enable) {
        this.stop();
        mHardwareDecode = enable;
        this.start();
    }

    @ReactProp(name = "renderMode")
    public void setRenderMode(TXCloudVideoView v, Integer mode) {
        if (mode == TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN) {
            mRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
        } else {
            mRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        }
        mPlayer.setRenderMode(mRenderMode);
    }

    @ReactProp(name = "renderRotation")
    public void setRenderRotation(TXCloudVideoView v, Integer rotation) {
        if (rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270) {
            mRenderRotation = rotation;
            mPlayer.setRenderRotation(mRenderRotation);
        }
    }

    @ReactProp(name = "mute")
    public void setMute(TXCloudVideoView v, Boolean mute) {
        mMute = mute;
        mPlayer.setMute(mMute);
    }

    public void setCacheMode(TXCloudVideoView v, Integer mode) {
        if (mode == 0) {
            mPlayConfig.setAutoAdjustCacheTime(true);
            mPlayConfig.setMinAutoAdjustCacheTime(1);
            mPlayConfig.setMaxAutoAdjustCacheTime(5);
        } else if (mode == 1) {
            mPlayConfig.setAutoAdjustCacheTime(true);
            mPlayConfig.setMinAutoAdjustCacheTime(1);
            mPlayConfig.setMaxAutoAdjustCacheTime(1);
        } else {
            mPlayConfig.setAutoAdjustCacheTime(false);
            mPlayConfig.setCacheTime(5);
        }
        this.start();
    }

    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        switch (i) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                mEventEmitter.receiveEvent(mVideoView.getId(), Events.BEGIN.toString(), Arguments.createMap());
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                mEventEmitter.receiveEvent(mVideoView.getId(), Events.LOADING.toString(), Arguments.createMap());
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                mEventEmitter.receiveEvent(mVideoView.getId(), Events.PROGRESS.toString(), Arguments.createMap());
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                mEventEmitter.receiveEvent(mVideoView.getId(), Events.END.toString(), Arguments.createMap());
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                mEventEmitter.receiveEvent(mVideoView.getId(), Events.DISCONNECT.toString(), Arguments.createMap());
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
    }

    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mSource) || (!mSource.startsWith("http://") && !mSource.startsWith("https://") && !mSource.startsWith("rtmp://"))) {
            return false;
        }
        if (mSource.startsWith("rtmp://")) {
            mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
            return true;
        }
        if ((mSource.startsWith("http://") || mSource.startsWith("https://")) && mSource.contains(".flv")) {
            mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
            return true;
        }
        return false;
    }

    private void start() {
        if (!this.checkPlayUrl()) {
            mEventEmitter.receiveEvent(mVideoView.getId(), Events.ERROR.toString(), Arguments.createMap());
            return;
        }
        mPlayer.setPlayListener(this);
        mPlayer.setPlayerView(mVideoView);
        mPlayer.enableHardwareDecode(mHardwareDecode);
        mPlayer.setRenderMode(mRenderMode);
        mPlayer.setRenderRotation(mRenderRotation);
        mPlayer.setConfig(mPlayConfig);
        mPlayer.startPlay(mSource, mPlayType);
    }

    private void pause() {
        mPlayer.pause();
    }

    private void resume() {
        mPlayer.resume();
    }

    private void stop() {
        mPlayer.setPlayListener(null);
        mPlayer.stopPlay(true);
        mPlayer.setPlayerView(null);
    }
}