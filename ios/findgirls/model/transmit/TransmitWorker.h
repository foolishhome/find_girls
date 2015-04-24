//
//  TransmitWorker.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>

/**
 * TransmitWroker。
 * 解耦各个业务逻辑。减少恶心代码。
 */
@protocol TransmitWorker <NSObject>

@required - (NSUInteger)appId;
@optional - (void)onReceive:(NSData *)data;

@end
