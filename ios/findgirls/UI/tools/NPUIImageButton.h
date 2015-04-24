//
//  NPUIImageButton.h
//  yyfe
//
//  Created by computer-boy on 15/2/10.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NPUIImageButton : UIButton
-(void) setBackgroundImage:(NSString *)normalImageName normalImageCapInsets:(CGPoint)normalImageCapInsets
          pressedImageName:(NSString*)pressedImageName pressedImageCapInsets:(CGPoint)pressedImageCapInsets;
-(void) setBackgroundImage:(UIImage *)normalImage pressImage:(UIImage*)pressImage;
//color 可以为 NIL
-(void) setNormalSateTitle:(NSString*)title fontColor:(UIColor *)color;
-(void) setPressedStateTitle:(NSString*)title fontColor:(UIColor *)color;
-(void) setTitleFont:(UIFont*)font;
@end
