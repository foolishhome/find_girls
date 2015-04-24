//
//  LoginModel.h
//  yymedical
//
//  Created by computer-boy on 14/12/30.
//  Copyright (c) 2014年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>

#import "ModelBase.h"
#import "sdkhelpertypes.h"
#import "LoginModelInit.h"

/**
 * 登录成功。
 */
static NSString *const M_loginNotify                = @"LoginNotify";
static NSString *const KeyLoginState                = @"LoginState";                /**(LoginState)     */
static NSString *const KeyLoginNickname             = @"LoginNickname";             /**(NSString *)     */
static NSString *const KeyLoginResult               = @"LoginResult";               /**(LoginResult)    */
static NSString *const KeyWhyLoginFailed            = @"WhyLoginFailed";            /**(SdkLoginResult) */

/**
 * 登出。
 */
static NSString *const M_logoutNotify               = @"LogoutNotify";
static NSString *const KeyOldLoginState             = @"OldLoginState";             /**(LoginState)     */
static NSString *const KeyWhyLogout                 = @"WhyLogout";                 /**(WhyLogout)      */

/**
 * 需要输入验证码确认。
 */
static NSString *const M_imageVerifyCode            = @"ImageVerifyCode";
static NSString *const KeyVerifyCodeImageData       = @"VerifyCodeImageData";       /**(NSData *)       */
static NSString *const KeyVerifyCodeImageId         = @"VerifyCodeImageId";         /**(NSString *)     */
static NSString *const KeyVerifyCodeContext         = @"VerifyCodeContext";         /**(NSData *)       */
static NSString *const KeyWhyVerify                 = @"WhyVerify";                 /**(NSString *)     */

/**
 * 登录状态。
 */
typedef enum
{
    GuestLogin,         /**匿名登录。*/
    UserLogin,          /**用户登录。*/
    Logout,             /**登出。通过[LogoutNotify oldLoginState]判断旧的状态。*/
}
LoginState;

/**
 * 登录结果。
 */
typedef enum
{
    LoginSucess,        /**登录成功。*/
    LoginFailed,        /**登录失败。*/
}
LoginResult;

/**
 * 登出原因。
 */
typedef enum
{
    UserLogout,         /**主动退出。*/
    Kicked,             /**被踢退出。*/
}
WhyLogout;

@interface LoginModel : ModelBase

+ (LoginModel*)sharedInstance;

@property (nonatomic)LoginState loginState;

@property (nonatomic, retain, getter=getAutoLoginAccount)NSString* autoLoginAccount;
- (BOOL)loginWithAuto;

/**
 * 初始化登录模块数据。
 * @param loginModelInit 登录数据。
 */
- (void)initLoginModel:(id<LoginModelInit>) loginModelInit;

/**
 * 用户登录。
 * @param name 用户名。
 * @param password 密码。
 * @param loginOption 登录选项。
 */
- (void)login:(NSString *)name password:(NSString *)password loginOption:(SdkLoginOption *)loginOption;

/**
 * 匿名登录。
 */
- (void)guestLogin;

/**
 * 确认验证码。如果成功，就会直接返回登录成功。否则继续返回M_imageCodeVerify，并告知原因。
 * @param verifyCode 用户输入的验证码。
 * @param imageId 由M_imageCodeVerify返回的验证码ID。
 * @param context 由M_imageCodeVerify返回的上下文。
 */
- (void)loginVerify:(NSString *)verifyCode imageId:(NSString *)imageId context:(NSData *)context;

/**
 * 登出。
 */
- (void)logout;

@end

