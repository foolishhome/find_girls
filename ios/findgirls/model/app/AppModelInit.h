//
//  AppModelInit.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/3.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>
#import "apphelper.h"

/**
 * 设置初始化对应的参数，必须实现。
 * 解耦品类业务逻辑代码。
 */
@protocol AppModelInit <NSObject>

/**
 * 应用程序名字。
 * @return 应用程序名字。
 */
@required - (NSString *)appName;

/**
 * 应用程序Id。
 * 特别注意这个是必须申请到的。
 * @return 应用程序Id。
 */
@required - (NSString *)appId;

/**
 * 应用程序版本。
 * @return 应用程序版本。
 */
@required - (NSString *)appVersion;

/**
 * 使用SDK的协议类型。
 * 一般使用B线，默认B线。
 * @return 使用SDK的协议类型。
 */
@required - (SdkProtocolType)sdkProtocolType;

@end
