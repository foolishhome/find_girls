//
//  TransmitModel.h
//  yyfe
//
//  Created by computer-boy on 15/1/26.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ModelBase.h"
#import "TransmitModelInit.h"
#import "TransmitWorker.h"

@interface TransmitModel : ModelBase

+ (TransmitModel*)sharedInstance;

@property BOOL serviceReady;

- (void) initTransmitModel:(id<TransmitModelInit>) transmitModelInit;
- (BOOL) addTransmitWorker:(id<TransmitWorker>) transmitWorker;
- (void) send:(NSString *)data appId:(uint32_t) appId sid:(uint32_t) sid;

@end
