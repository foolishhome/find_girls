//
//  LoginModelInit.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>

/**
 * LoginModelInit。
 * 解耦各个业务逻辑。减少恶心代码。
 */
@protocol LoginModelInit <NSObject>

@required - (NSArray *)webDnsServers;
@required - (NSString *)proxyAttackServerIp;
@required - (NSArray *)proxyAttackServerPort;
@required - (BOOL)enableFallBack;

@end
