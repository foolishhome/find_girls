//
//  NPComboBox.h
//  yyfe
//
//  Created by computer-boy on 15/2/7.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@class NPComboBox;
@protocol NPComboDelegate <NSObject>
- (void)npComboBox:(NPComboBox*)comboBox didSelectedAtIndex:(NSUInteger)index;

@optional
- (void)didAppear:(NPComboBox*)comboBox;
- (void)didDismiss:(NPComboBox *)comboBox;
- (BOOL)npComboBox:(NPComboBox*)comboBox didSelectedImage:(NSUInteger)index;
@end


@interface NPComboBox : UIView
@property(nonatomic, retain) UIView * inputTextField;
@property(nonatomic, retain) UIView * parentView;
@property(nonatomic) BOOL alreadyShowed;
@property(nonatomic, retain) id<NPComboDelegate> comboxDelegate;
@property (strong,nonatomic) NSString * selectedImage;
@property (assign,nonatomic) BOOL showSelectedImageOnlySelected;

- (void)reloadData:(NSArray*)list;
- (void)showCombox:(NSArray*)list rectToParent:(CGRect)rect;
- (void)closeCombox;
@end
