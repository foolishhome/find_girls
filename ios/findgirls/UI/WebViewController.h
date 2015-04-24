//
//  PermissionViewController.h
//  yyfe
//
//  Created by computer-boy on 15/3/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"

@class WebViewController;
@protocol WebDelegate
- (BOOL)startLoadWithRequest:(WebViewController*)ctrl queryString:(NSString*)queryString;
@end

@interface WebViewController : BaseViewController
@property(nonatomic, retain) NSString * URL;
@property(nonatomic, retain) NSString * navigateTitle;
@property(nonatomic, retain) id<WebDelegate> delegate;
@end
