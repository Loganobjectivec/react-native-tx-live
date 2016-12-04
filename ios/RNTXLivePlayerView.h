//
//  RNTxLivePlayerView.h
//  RNTxLive
//
//  Created by Yechao Li on 2016/12/3.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "TXLivePlayer.h"
#import <UIKit/UIKit.h>
#import "RCTViewManager.h"

@interface RNTXLivePlayerView : UIView

@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerLoading;
@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerEnd;
@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerBegin;
@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerError;
@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerProgress;
@property (nonatomic, copy) RCTBubblingEventBlock onRNTXLivePlayerDisconnect;

@property (nonatomic, copy) NSString *source;
@property (nonatomic, assign) int playType;
@property (nonatomic, assign) int renderMode;
@property (nonatomic, assign) int renderRotation;
@property (nonatomic, assign) BOOL hardwareDecode;
@property (nonatomic, assign) BOOL mute;

-(void)start;
-(void)stop;
-(void)pause;
-(void)resume;

@end
