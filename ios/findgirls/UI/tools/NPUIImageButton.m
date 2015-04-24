//
//  NPUIImageButton.m
//  yyfe
//
//  Created by computer-boy on 15/2/10.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "NPUIImageButton.h"

@implementation NPUIImageButton

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (BOOL)canBecomeFirstResponder
{
    return YES;
}

-(void)setBackgroundImage:(NSString *)normalImageName normalImageCapInsets:(CGPoint)normalImageCapInsets pressedImageName:(NSString *)pressedImageName pressedImageCapInsets:(CGPoint)pressedImageCapInsets
{
    
    if (normalImageName != nil)
    {
        UIImage *normalImage = [UIImage imageNamed:normalImageName];
        if (normalImageCapInsets.x != 0 && normalImageCapInsets.y != 0)
        {
            normalImage = [normalImage stretchableImageWithLeftCapWidth:normalImageCapInsets.x topCapHeight:normalImageCapInsets.y];
        }
        [self setBackgroundImage:normalImage forState:UIControlStateNormal];
    }
    
    if (pressedImageName != nil)
    {
        UIImage *pressedImage = [UIImage imageNamed:pressedImageName];
        if (normalImageCapInsets.x != 0 && normalImageCapInsets.y != 0)
        {
            pressedImage = [pressedImage stretchableImageWithLeftCapWidth:pressedImageCapInsets.x topCapHeight:pressedImageCapInsets.y];
        }
        [self setBackgroundImage:pressedImage forState:UIControlStateSelected];
    }
    
}

-(void) setBackgroundImage:(UIImage *)normalImage pressImage:(UIImage*)pressImage
{
    if (normalImage != nil)
    {
        [self setBackgroundImage:normalImage forState:UIControlStateNormal];
    }
    
    if (pressImage != nil)
    {
        [self setBackgroundImage:pressImage forState:UIControlStateSelected];
    }
}

-(void)setNormalSateTitle:(NSString *)title fontColor:(UIColor *)color
{
    [self setTitle:title forState:UIControlStateNormal];
    if (color != nil)
    {
        [self setTitleColor:color forState:UIControlStateNormal];
    }
}

-(void)setPressedStateTitle:(NSString *)title fontColor:(UIColor *)color
{
    [self setTitle:title forState:UIControlStateHighlighted];
    if (color != nil)
    {
        [self setTitleColor:color forState:UIControlStateHighlighted];
    }
}

-(void)setTitleFont:(UIFont *)font
{
    self.titleLabel.font = font;
}

@end
