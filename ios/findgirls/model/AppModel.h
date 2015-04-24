//
//  AppModel.h
//  yymedical
//
//  Created by YYNewBusinessLineB on 15/1/12.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "ModelBase.h"
#import "AppModelInit.h"

@interface AppModel : ModelBase

+ (AppModel*)sharedInstance;

- (void)initAppModel:(id<AppModelInit>) appModelInit;

/**
 * 应用程序名字。
 * @return 应用程序名字。
 */
- (NSString *)appName;

/**
 * 应用程序Id。
 * 特别注意这个是必须申请到的。
 * @return 应用程序Id。
 */
- (NSString *)appId;

/**
 * 应用程序版本。
 * @return 应用程序版本。
 */
- (NSString *)appVersion;

@end
