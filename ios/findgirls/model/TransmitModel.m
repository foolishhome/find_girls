//
//  TransmitModel.m
//  yyfe
//
//  Created by computer-boy on 15/1/26.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "TransmitModel.h"
#import "apphelper.h"
#import "TransmitHelper.h"

@interface TransmitModel()

/**
 * 设置初始化对应的参数，必须实现。
 * 解耦品类业务逻辑代码。
 * @see TransmitModelInit。
 */
@property (nonatomic, retain) id<TransmitModelInit> transmitModelInit;

/**
 * 解耦各个业务逻辑NSDictionary<appId, NSMutableArray<TransmitWorker>>。
 * UI可以注册多个TransmitWorker，但是同一个会被忽略。
 * @see TransmitWorker。
 */
@property (nonatomic, retain) NSDictionary* transmitWorkers;

@end

@implementation TransmitModel

static TransmitModel* g_transmitModelInstance;

+ (TransmitModel*)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_transmitModelInstance = [TransmitModel new];
        g_transmitModelInstance.serviceReady = NO;
        g_transmitModelInstance.transmitWorkers = [NSDictionary dictionary];
    });
    return g_transmitModelInstance;
}

- (void)initModel
{
    [super initModel];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onServiceReady:) name:kSvcReadyNotification object:nil];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onReceive:) name:kSvcDataNotification object:nil];
}

- (void)clearData
{
    [super clearData];
}

- (void) initTransmitModel:(id<TransmitModelInit>) transmitModelInit
{
    self.transmitModelInit = transmitModelInit;
}

- (BOOL) addTransmitWorker:(id<TransmitWorker>) transmitWorker
{
    NSMutableArray* transmitWorkerOfAppid = [self.transmitWorkers objectForKey:@([transmitWorker appId])];
    if(transmitWorkerOfAppid == nil) {
        transmitWorkerOfAppid = [NSMutableArray arrayWithObject:transmitWorker];
        [self.transmitWorkers setValue:transmitWorkerOfAppid forKey:[NSString stringWithFormat:@"%d", [transmitWorker appId]]];
    }
    else {
        if(![transmitWorkerOfAppid containsObject:transmitWorker]) {
            [transmitWorkerOfAppid addObject:transmitWorker];
        }
    }
    return NO;
}

- (void) send:(NSString *)data appId:(uint32_t) appId sid:(uint32_t) sid
{
    [[[AppHelper sharedObject] transmitHelper] sendServiceData:data appId:appId sid:sid];
}

/**
 * 以下为service通道回调。
 */

- (void)onServiceReady:(NSNotification *)notification
{
    [[[AppHelper sharedObject] transmitHelper] subscribeApp:[self.transmitModelInit serviceIds]];
    self.serviceReady = true;
}

- (void)onReceive:(NSNotification *)notification
{
    NSMutableArray* transmitWorkerOfAppid = [self.transmitWorkers objectForKey:[NSString stringWithFormat:@"%d", [[notification.userInfo objectForKey:kSvcAppIdUserInfoKey] integerValue]]];
    if(transmitWorkerOfAppid != nil) {
        for(id<TransmitWorker> transmitWorker in transmitWorkerOfAppid) {
            [transmitWorker onReceive:[notification.userInfo objectForKey:kSvcDataUserInfoKey]];
        }
    }
}

@end
