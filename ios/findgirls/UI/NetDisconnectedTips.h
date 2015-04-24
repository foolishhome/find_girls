//
//  NetDisconnectedTips.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/3/18.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "NetDisconnectedTipsView.h"

typedef enum SdkLinkState {
    SdkLinkStateNotConnected,
    SdkLinkStateConnecting,
    SdkLinkStateConnected
}SdkLinkState;

@interface NetDisconnectedTips : NSObject

@property (nonatomic, retain)NetDisconnectedTipsView* netDisconnectedTipsView;

+ (NetDisconnectedTips *)initWithView:(UIView *)parentView;

@end
