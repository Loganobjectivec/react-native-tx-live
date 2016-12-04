//
//  RNTxLivePlayerView.m
//  RNTxLive
//
//  Created by Yechao Li on 2016/12/3.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RNTXLivePlayerView.h"

@interface RNTXLivePlayerView () <TXLivePlayListener>

{
    TXLivePlayer *_player;
    TXLivePlayConfig *_playerConfig;
}

@end

@implementation RNTXLivePlayerView

- (void)setRenderMode:(int)renderMode
{
    _renderMode = renderMode;
    [_player setRenderMode:renderMode];
}

- (void)setRenderRotation:(int)renderRotation
{
    _renderRotation = renderRotation;
    [_player setRenderRotation:renderRotation];
}

- (void)setHardwareDecode:(BOOL)hardwareDecode
{
    _hardwareDecode = hardwareDecode;
    [_player setEnableHWAcceleration:hardwareDecode];
}

- (void)setMute:(BOOL)mute
{
    _mute = mute;
    [_player setMute:mute];
}

- (instancetype)init
{
    self = [super init];
    _player = [[TXLivePlayer alloc] init];
    _playerConfig = [[TXLivePlayConfig alloc] init];
    _playerConfig.bAutoAdjustCacheTime = YES;
    _playerConfig.minAutoAdjustCacheTime = 1;
    _playerConfig.maxAutoAdjustCacheTime = 5;
    [_player setConfig:_playerConfig];
    if (self) {
        
    }
    return self;
}

-(void) onPlayEvent:(int)EvtID withParam:(NSDictionary*)param
{
    if (EvtID == PLAY_EVT_PLAY_BEGIN) {
        _onRNTXLivePlayerBegin(@{});
    } else if (EvtID == PLAY_EVT_PLAY_END) {
        _onRNTXLivePlayerEnd(@{});
    } else if (EvtID == PLAY_EVT_PLAY_PROGRESS) {
        _onRNTXLivePlayerProgress(@{});
    } else if (EvtID == PLAY_EVT_PLAY_LOADING) {
        _onRNTXLivePlayerLoading(@{});
    } else if (EvtID == PLAY_ERR_NET_DISCONNECT) {
        _onRNTXLivePlayerDisconnect(@{});
    }
}

-(void)onNetStatus:(NSDictionary*) param
{
}

-(BOOL)checkPlayUrl {
    if (!([_source hasPrefix:@"http:"] || [_source hasPrefix:@"https:"] || [_source hasPrefix:@"rtmp:"] )) {
        _onRNTXLivePlayerError(@{@"message": @"播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!"});
        return NO;
    }
    if ([_source hasPrefix:@"rtmp:"]) {
        _playType = PLAY_TYPE_LIVE_RTMP;
    } else if (([_source hasPrefix:@"https:"] || [_source hasPrefix:@"http:"]) && [_source rangeOfString:@".flv"].length > 0) {
        _playType = PLAY_TYPE_LIVE_FLV;
    } else{
        _onRNTXLivePlayerError(@{@"message": @"播放地址不合法，直播目前仅支持rtmp,flv播放方式!"});
        return NO;
    }
    return YES;
}


-(void)start
{
    dispatch_async(dispatch_get_main_queue(), ^{
        if (![self checkPlayUrl]) return;
        _player.delegate = self;
        [_player setupVideoWidget:self.bounds containView:self insertIndex:0];
        _player.enableHWAcceleration = _hardwareDecode;
        [_player setRenderMode:_renderMode];
        [_player setRenderRotation:_renderRotation];
        [_player setConfig:_playerConfig];
        [_player startPlay:_source type:_playType];
    });
}

-(void)stop
{
    dispatch_async(dispatch_get_main_queue(), ^{
        _player.delegate = nil;
        [_player removeVideoWidget];
        [_player stopPlay];
    });
}

-(void)pause
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [_player pause];
    });
}

-(void)resume
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [_player resume];
    });
}

@end
