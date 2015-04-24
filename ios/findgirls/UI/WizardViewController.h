//
//  WizardViewController.h
//  yyfe
//
//  Created by computer-boy on 15/3/5.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WizardViewController : UIViewController
@property(nonatomic, retain) NSArray * pics;

+ (void)showWizard:(UIViewController*)parent pics:(NSArray*)pics;
@end
