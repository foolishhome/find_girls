//
//  NPPopupSelectionView.m
//  NPUIFrameWork
//
//  Created by nicholaswu on 13-11-25.
//  Copyright (c) 2013年 nicholaswu. All rights reserved.
//

#import "NPPopupView.h"

@implementation NPPopupView
{
    UIView *_mainChildView;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        self.userInteractionEnabled = YES;
    }
    return self;
}

-(void)setMainChildView:(UIView *)view
{
    _mainChildView = view;
    [self addSubview:view];
}

-(void)showWithAnimation:(BOOL)bAnimation
{
    UIWindow *window = [UIApplication sharedApplication].windows.lastObject;
    [window addSubview:self];
    
    NSInteger screenwidth = [UIScreen mainScreen].bounds.size.width;
    NSInteger screenheight =[UIScreen mainScreen].bounds.size.height;
    int statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
    screenheight -= statusBarHeight;
    
    if (bAnimation)
    {
        if (_mainChildView)
            _mainChildView.frame = CGRectMake(0, screenheight,
                                          screenwidth, screenheight);
        
        [UIView animateWithDuration:0.4f animations:^{
            CGRect rc = _mainChildView.frame;
            rc.origin.y = [UIApplication sharedApplication].statusBarFrame.size.height;
            _mainChildView.frame = rc;
        }];
    }
    else
    {
        if (_mainChildView)
            _mainChildView.frame = CGRectMake(0, 0,
                                          screenwidth, screenheight);
    }
}

-(void)closeWidthAnimation:(BOOL)bAnimation
{
    if (bAnimation && _mainChildView)
    {
        [UIView animateWithDuration:0.4f animations:^{
            NSInteger screenheight =[UIScreen mainScreen].bounds.size.height;
            CGRect rc = _mainChildView.frame;
            rc.origin.y = screenheight;
            _mainChildView.frame = rc;
            
        } completion:^(BOOL finished) {
            [_mainChildView removeFromSuperview];
            [self removeFromSuperview];
            _mainChildView = nil;
            
            if (_delegate)
            {
                [_delegate didPopUpViewClose];
            }
        }];
    }
    else
    {
        if (_mainChildView)
        {
            [_mainChildView removeFromSuperview];
            _mainChildView = nil;
        }
        [self removeFromSuperview];
        if (_delegate)
        {
            [_delegate didPopUpViewClose];
        }
    }
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
