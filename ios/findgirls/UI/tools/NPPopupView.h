//
//  NPPopupSelectionView.h
//  NPUIFrameWork
//
//  Created by nicholaswu on 13-11-25.
//  Copyright (c) 2013年 nicholaswu. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NPPopupViewDelegate <NSObject>
@required
-(void) didPopUpViewClose;
@end

@interface NPPopupView : UIView
@property(weak,nonatomic) id<NPPopupViewDelegate>delegate;

-(void)setMainChildView:(UIView *)view;
-(void)showWithAnimation:(BOOL)bAnimation;
-(void)closeWidthAnimation:(BOOL)bAnimation;
@end
