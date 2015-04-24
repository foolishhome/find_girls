//
//  NPTableSheet.h
//  NPUIFrameWork
//
//  Created by 123 on 13-12-26.
//  Copyright (c) 2013年 nicholaswu. All rights reserved.
//

#import <UIKit/UIKit.h>

@class NPTableSheet;

@protocol NPTableSheetDelegate <NSObject>
- (void)npTableSheet:(NPTableSheet*)tablesheet didSelectedAtIndex:(NSUInteger)index;

@optional
- (void)didTableSheetAppear:(NPTableSheet*)tablesheet;
- (void)didTableSheetDismiss:(NPTableSheet *)tablesheet;
- (BOOL)npTableSheet:(NPTableSheet*)tablesheet didSelectedImage:(NSUInteger)index;
@end

@interface NPTableSheet : UIView<UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate>
@property (weak,nonatomic) id<NPTableSheetDelegate> delegate;
@property (strong,nonatomic) NSString* operationID;

@property (assign,nonatomic) NSInteger  checkViewWidth;     //指定宽高
@property (assign,nonatomic) NSInteger  checkViewHeight;
@property (assign,nonatomic) NSInteger  beginOffsetX;
@property (assign,nonatomic) NSInteger  beginOffsetY;   //初始偏移
@property (assign,nonatomic) NSInteger  targetOffsetY;  //目标偏移
@property (assign,nonatomic) BOOL       showCoverPlate;
@property (strong,nonatomic) UIColor* textColor;
@property (strong,nonatomic) UIFont* textFont;
@property (strong,nonatomic) UIColor* selectedTextColor;
@property (assign,nonatomic) NSInteger  selectedIndex;// 
@property (assign,nonatomic) NSInteger cancelIndex;
@property (strong,nonatomic) UIColor*  defaultCheckViewBgColor;
@property (strong,nonatomic) NSArray*  checkViewsBgColorArray;
@property (assign,nonatomic) BOOL alreadyShowed;
@property (strong,nonatomic) NSString * selectedImage;
@property (assign,nonatomic) BOOL showSelectedImageOnlySelected;

- (id)initWithTitleList:(NSArray*)titleList delegate:(id<NPTableSheetDelegate>)delegate;
- (void)reloadData:(NSArray*)titleList;
- (void)showCheckViews;
- (void)showCheckViewsInView:(UIView*)view;
- (void)closeCheckViews;
- (NSString*)titleAtIndex:(NSInteger)index;
@end


@interface NPTableSheetCheckView : UIView
@property (strong,nonatomic) UILabel* titleLabel;
@property (strong,nonatomic) UIImageView* selectedImageView;
@property (assign,nonatomic) BOOL selected;
@property (strong,nonatomic) UIColor* textColor;
@property (strong,nonatomic) UIFont* textFont;
@property (strong,nonatomic) UIColor* selectedTextColor;

@property (strong,nonatomic) NSString * selectedImage;
@property (assign,nonatomic) BOOL showSelectedImageOnlySelected;
@end


@interface NPTableSheetCell : UITableViewCell
@property (strong,nonatomic) UILabel* titleLabel;
@property (strong,nonatomic) UIImageView* selectedImageView;

@end