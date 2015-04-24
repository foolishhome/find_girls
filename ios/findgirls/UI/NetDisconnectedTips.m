//
//  NetDisconnectedTips.m
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/3/18.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "NetDisconnectedTips.h"

@interface NetDisconnectedTips()

@end

@implementation NetDisconnectedTips

+ (SdkLinkState)linkState
{
    // not finish james
    return SdkLinkStateConnected;
}

+ (NetDisconnectedTips *)initWithView:(UIView *)referenceView
{
    NetDisconnectedTips* instance = [[NetDisconnectedTips alloc] init];
    
    NSArray* nibGuruVideoNavigateBar =  [[NSBundle mainBundle] loadNibNamed:@"NetDisconnectedTipsView" owner:referenceView.superview options:nil];
    instance.netDisconnectedTipsView = [nibGuruVideoNavigateBar objectAtIndex:0];
    [referenceView.superview addSubview:instance.netDisconnectedTipsView];
    if([NetDisconnectedTips linkState] == SdkLinkStateConnected ||
       [NetDisconnectedTips linkState] == SdkLinkStateConnecting) {
        instance.netDisconnectedTipsView.hidden = YES;
    }
    [instance.netDisconnectedTipsView setTranslatesAutoresizingMaskIntoConstraints:NO];
    
    NSLayoutConstraint* leftConstraints = [NSLayoutConstraint constraintWithItem:instance.netDisconnectedTipsView attribute:NSLayoutAttributeLeft relatedBy:NSLayoutRelationEqual toItem:referenceView attribute:NSLayoutAttributeLeft multiplier:1 constant:0];
    NSLayoutConstraint* topConstraints = [NSLayoutConstraint constraintWithItem:instance.netDisconnectedTipsView attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:referenceView attribute:NSLayoutAttributeTop multiplier:1 constant:0];
    NSLayoutConstraint* widthConstraints = [NSLayoutConstraint constraintWithItem:instance.netDisconnectedTipsView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:referenceView attribute:NSLayoutAttributeWidth multiplier:1 constant:0];
    NSLayoutConstraint* heightConstraints = [NSLayoutConstraint constraintWithItem:instance.netDisconnectedTipsView attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:referenceView attribute:NSLayoutAttributeHeight multiplier:1 constant:0];
    [referenceView.superview addConstraint:heightConstraints];
    [referenceView.superview addConstraint:widthConstraints];
    [referenceView.superview addConstraint:leftConstraints];
    [referenceView.superview addConstraint:topConstraints];
    [instance.netDisconnectedTipsView.retryButton addTarget:instance action:@selector(onNetDisconnectedRetry:) forControlEvents:UIControlEventTouchUpInside];
    return instance;
}

- (IBAction)onNetDisconnectedRetry:(UIButton *)sender
{
    if ([NetDisconnectedTips linkState] == SdkLinkStateConnected) {
        self.netDisconnectedTipsView.hidden = YES;
    }
}

@end
