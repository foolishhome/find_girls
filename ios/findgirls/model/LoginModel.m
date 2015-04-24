//
//  LoginModel.m
//  yymedical
//
//  Created by computer-boy on 14/12/30.
//  Copyright (c) 2014年 yy.com. All rights reserved.
//

#import "LoginModel.h"

// import yysdk.
#import "apphelper.h"
#import "loginhelper.h"
#import "selfinfohelper.h"

// 这些是ＳＤＫ未明文的回调。
static NSString *const kLoginFailedNotification = @"LoginFailedNotification";
static NSString *const kLogoutNotification = @"LogoutNotification";

static LoginModel* g_loginModelInstance;

@interface LoginModel()

/**
 * 初始化数据，必须实现。
 */
@property (nonatomic, retain) id<LoginModelInit> loginModelInit;

@end

@implementation LoginModel

+ (LoginModel*)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_loginModelInstance = [LoginModel new];
        g_loginModelInstance.loginState = Logout;
    });
    return g_loginModelInstance;
}

- (void)initModel
{
    [super initModel];
    
    [[AppHelper sharedObject].loginHelper setProxyAServerHost:[self.loginModelInit proxyAttackServerIp]
                                                        ports:[self.loginModelInit proxyAttackServerPort]
                                                webDnsServers:[self.loginModelInit webDnsServers]
                                             isEnableFallback:[self.loginModelInit enableFallBack]];
    
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onLogin:) name:kLoginNotification object:nil];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onGuestLogin:) name:kGuestLoginNotification object:nil];/*
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onLogout:) name:kLoginFailedNotification object:nil];*/
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onLogout:) name:kLogoutNotification object:nil];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onKickOff:) name:kKickOffNotification object:nil];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(onImageCodeVerify:) name:kImageCodeVerifyNotification object:nil];
}

- (void)clearData
{
    [super clearData];
}

- (void)initLoginModel:(id<LoginModelInit>) loginModelInit
{
    self.loginModelInit = loginModelInit;
}

- (void)login:(NSString *)name password:(NSString *)password loginOption:(SdkLoginOption *)loginOption
{
    [[[AppHelper sharedObject] loginHelper] loginWithName:name password:password loginOption:loginOption];
}

- (void)guestLogin
{
    [[[AppHelper sharedObject] loginHelper] guestLogin];
}

- (void)loginVerify:(NSString *)verifyCode imageId:(NSString *)imageId context:(NSData *)context
{
    SdkCallResult sdkCallResult = [[[AppHelper sharedObject] loginHelper] answerImageCode:verifyCode imageId:imageId context:context];
    if(sdkCallResult == SdkCallResultOk) {
        // just do what you want.
    }
}

- (void)logout
{
    [[[AppHelper sharedObject] loginHelper] logout];
    LoginState oldLoginState = self.loginState;
    self.loginState = Logout;
    NSNotification* logoutResult = [NSNotification
                                    notificationWithName:M_logoutNotify
                                    object:nil
                                    userInfo:@{KeyOldLoginState:@(oldLoginState),
                                               KeyWhyLogout:@(UserLogout)}];
    [[NSNotificationCenter defaultCenter]postNotification:logoutResult];
}

- (LoginResult)loginResultConvert:(SdkLoginResult) sdkLoginResult loginState:(LoginState) loginState
{
    switch(sdkLoginResult) {
        case SdkLoginResultSucceeded:               {/**< 登录成功 */
            self.loginState = loginState;
            return LoginSucess;
        }
        case SdkLoginResultUnknownError:            {/**< 未知错误 */
            break;
        }
        case SdkLoginResultInvalidReq:              {/**< 无效请求 */
            break;
        }
        case SdkLoginResultPasswdError:             {/**< 密码错误 */
            break;
        }
        case SdkLoginResultUserNonexist:            {/**< 用户不存在 */
            break;
        }
        case SdkLoginResultUserKick:                {/**< 用户在另一处登录 */
            break;
        }
        case SdkLoginResultUserGlobalban:           {/**< 用户被封禁 */
            break;
        }
        case SdkLoginResultUserFreeze:              {/**< 用户被冻结 */
            break;
        }
        case SdkLoginResultUdbNotenable:            {/**< TODO: 不知道是什么错误? */
            break;
        }
        case SdkLoginResultServerUnreachable:       {/**< 不能连接服务器 */
            break;
        }
        case SdkLoginResultProxyOutOfDate:          {/**< 代理过期 */
            break;
        }
        case SdkLoginResultRetryVerify:             {/**< 密码输入错误次数过多，警告客户端，此时可以出提示框提示用户，再错试可能会冻结 */
            break;
        }
        case SdkLoginResultRetryFreeze:             {/**< 密码输入错误次数过多，帐号被冻结，一段时间内不能再登 */
            break;
        }
        case SdkLoginResultAccountNotAvailable:     {/**< 匿名帐号已经用光了 */
            break;
        }
        case SdkLoginResultTimeout:                 {/**< 登录超时*/
            break;
        }
        case SdkLoginResultPending:                 {/**< 正在处理登录请求 */
            break;
        }
        case SdkLoginResultInvalidLoginInfo:        {/**< 登录信息不全（用户名或密码为空）*/
            break;
        }
        case SdkLoginResultNoNetwork:               {/**< 当前无网络 */
            break;
        }
        case SdkLoginResultUserNotInHistory:        {/**< 登录用户名在本地登录历史列表中不存在 */
            break;
        }
        case SdkLoginResultLbsError:                {/**< LBS error*/
            break;
        }
        case SdkLoginResultServerError:             {/**< Server Error*/
            break;
        }
        case SdkLoginResultNetBroken:               {/**< Net Broken*/
            break;
        }
        case SdkLoginResultLinkdClosed:             {/**< Linkd Closed*/
            break;
        }
    }
    
    self.loginState = Logout;
    return LoginFailed;
}

// 以下为回调。

- (void)onLogin:(NSNotification *)notification
{
    LoginResult loginResult = [self loginResultConvert:[[notification.userInfo objectForKey:kLoginResultUserInfoKey] intValue] loginState:UserLogin];
    NSNotification* loginNotify = [NSNotification
                                   notificationWithName:M_loginNotify
                                   object:nil
                                   userInfo:@{KeyLoginState:@(self.loginState),
                                              KeyLoginNickname:[notification.userInfo objectForKey:kLoginNameUserInfoKey],
                                              KeyLoginResult:@(loginResult),
                                              KeyWhyLoginFailed:@([[notification.userInfo objectForKey:kLoginResultUserInfoKey] intValue])}];
    [[NSNotificationCenter defaultCenter]postNotification:loginNotify];
}

- (void)onGuestLogin:(NSNotification *)notification
{
    self.loginState = GuestLogin;
    LoginResult loginResult = [self loginResultConvert:[[notification.userInfo objectForKey:kGuestLoginResultUserInfoKey] intValue] loginState:GuestLogin];
    NSNotification* loginNotify = [NSNotification
                                   notificationWithName:M_loginNotify
                                   object:nil
                                   userInfo:@{KeyLoginState:@(self.loginState),
                                              KeyLoginResult:@(loginResult),
                                              KeyWhyLoginFailed:@([[notification.userInfo objectForKey:kLoginResultUserInfoKey] intValue])}];
    [[NSNotificationCenter defaultCenter]postNotification:loginNotify];
}

- (void)onLogout:(NSNotification *)notification
{
    LoginState oldLoginState = self.loginState;
    self.loginState = Logout;
    NSNotification* logoutResult = [NSNotification
                                    notificationWithName:M_logoutNotify
                                    object:nil
                                    userInfo:@{KeyOldLoginState:@(oldLoginState),
                                               KeyWhyLogout:@(UserLogout)}];
    [[NSNotificationCenter defaultCenter]postNotification:logoutResult];
}

- (void)onKickOff:(NSNotification *)notification
{
    LoginState oldLoginState = self.loginState;
    self.loginState = Logout;
    NSNotification* logoutResult = [NSNotification
                                    notificationWithName:M_logoutNotify
                                    object:nil
                                    userInfo:@{KeyOldLoginState:@(oldLoginState),
                                               KeyWhyLogout:@(Kicked)}];
    [[NSNotificationCenter defaultCenter]postNotification:logoutResult];
}

- (void)onImageCodeVerify:(NSNotification *)notification
{
    NSNotification* verifyResult = [NSNotification
                                    notificationWithName:M_imageVerifyCode
                                    object:nil
                                    userInfo:@{KeyVerifyCodeImageData:[notification.userInfo objectForKey:kImageCodeImageDataUserInfoKey],
                                               KeyVerifyCodeImageId:[notification.userInfo objectForKey:kImageCodeImageIdUserInfoKey],
                                               KeyVerifyCodeContext:[notification.userInfo objectForKey:kImageCodeContextUserInfoKey],
                                               KeyWhyVerify:[notification.userInfo objectForKey:kImageCodeReasonUserInfoKey]}];
    [[NSNotificationCenter defaultCenter]postNotification:verifyResult];
}

-(void)setAutoLoginAccount:(NSString *)autoLoginAccount
{
    NSUserDefaults * def = [NSUserDefaults standardUserDefaults];
    [def setObject:autoLoginAccount forKey:@"autoLogin"];
    [def synchronize];
}

-(NSString *)getAutoLoginAccount
{
    NSUserDefaults * def = [NSUserDefaults standardUserDefaults];
    return [def objectForKey:@"autoLogin"];
}

- (BOOL)loginWithAuto
{
    NSString * account = [self getAutoLoginAccount];

    LoginHelper * loginHelper = [[AppHelper sharedObject] loginHelper];
    NSArray * Accounts = [loginHelper accountHistory];
    for (SdkAccountInfo * info in Accounts) {
        if ([account isEqualToString:info.name]) {
            [loginHelper loginByAccountHistory:account loginOption:[SdkLoginOption defaultOption]];
            return YES;
        }
    }
    return NO;
}

@end