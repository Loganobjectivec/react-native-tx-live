
#import "RNTXLivePlayerManager.h"
#import "RCTViewManager.h"
#import "RNTXLivePlayerView.h"
#import "RCTModuleData.h"
#import "RCTModuleMethod.h"
#import "RCTBridgeModule.h"

@implementation RNTXLivePlayerManager

{
    RNTXLivePlayerView *_playerView;
}

- (dispatch_queue_t)methodQueue
{
    NSLog(@"get main queue");
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

-(UIView *)view
{
    _playerView = [[RNTXLivePlayerView alloc] init];
    return _playerView;
}

RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerLoading, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerProgress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerBegin, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerEnd, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRNTXLivePlayerDisconnect, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(source, NSString)
RCT_EXPORT_VIEW_PROPERTY(renderMode, int)
RCT_EXPORT_VIEW_PROPERTY(renderRotation, int)
RCT_EXPORT_VIEW_PROPERTY(hardwareDecode, BOOL)

RCT_EXPORT_METHOD(start:(nonnull NSNumber *)reactTag)
{
    [_playerView start];
}

RCT_EXPORT_METHOD(stop:(nonnull NSNumber *)reactTag)
{
    [_playerView stop];
}

RCT_EXPORT_METHOD(pause:(nonnull NSNumber *)reactTag)
{
    [_playerView pause];
}

RCT_EXPORT_METHOD(resume:(nonnull NSNumber *)reactTag)
{
    [_playerView resume];
}

RCT_EXPORT_METHOD(mute:(nonnull NSNumber *)reactTag params:(BOOL)enable)
{
    [_playerView setMute:enable];
}

@end
