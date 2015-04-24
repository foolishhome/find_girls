//
//  CALayer+XibConfiguration.h
//  yyfe
//
//  Created by computer-boy on 15/2/6.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>

@interface CALayer(XibConfiguration)

// This assigns a CGColor to borderColor.
@property(nonatomic, assign) UIColor* borderUIColor;

@end