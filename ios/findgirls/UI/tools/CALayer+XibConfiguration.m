//
//  CALayer+XibConfiguration.m
//  yyfe
//
//  Created by computer-boy on 15/2/6.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "CALayer+XibConfiguration.h"

@implementation CALayer(XibConfiguration)

-(void)setBorderUIColor:(UIColor*)color
{
    self.borderColor = color.CGColor;
}

-(UIColor*)borderUIColor
{
    return [UIColor colorWithCGColor:self.borderColor];
}

@end