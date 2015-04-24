//
//  AppModel.m
//  yymedical
//
//  Created by YYNewBusinessLineB on 15/1/12.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "AppModel.h"

#import "apphelper.h"

static AppModel* g_appModelInstance;

@interface AppModel()

/**
 * 应用程序初始化数据。
 */
@property (nonatomic, retain) id<AppModelInit> appModelInit;

@end

@implementation AppModel

+ (AppModel*)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_appModelInstance = [AppModel new];
    });
    return g_appModelInstance;
}

- (void)initModel
{
    [super initModel];
    [[AppHelper sharedObject] initWithAppName:[self.appModelInit appName]
                                   identifier:[self.appModelInit appId]
                                      version:[NSString stringWithFormat:@"%@%@", [self.appModelInit appId], [self.appModelInit appVersion]]
                                 protocolType:[self.appModelInit sdkProtocolType]];
}

- (void)clearData
{
}

- (void)initAppModel:(id<AppModelInit>) appModelInit
{
    self.appModelInit = appModelInit;
}

- (NSString *)appName
{
    return [self.appModelInit appName];
}

- (NSString *)appId
{
    return [self.appModelInit appId];
}

- (NSString *)appVersion
{
    return [self.appModelInit appVersion];
}

@end
