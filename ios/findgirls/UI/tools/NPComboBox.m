//
//  NPComboBox.m
//  yyfe
//
//  Created by computer-boy on 15/2/7.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "NPComboBox.h"
#import "NPTableSheet.h"

@interface NPComboBox ()<NPTableSheetDelegate>
{
    NPTableSheet * _tablesheet;
    UIView * _popupView;
}
@end

@implementation NPComboBox

-(instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _tablesheet = nil;
        
        // 背后贴一个全屏的mask来处理点击取消combox
        _popupView = [[UIView alloc]initWithFrame:frame];
        [_popupView setBackgroundColor:[UIColor clearColor]];
        _popupView.userInteractionEnabled = YES;
    }
    return self;
}

- (void)showCombox:(NSArray*)list rectToParent:(CGRect)rect
{
    [_popupView setFrame:CGRectMake(0, 0, _parentView.frame.size.width, _parentView.frame.size.height)];
    [_parentView bringSubviewToFront:_inputTextField];
    [_parentView insertSubview:_popupView belowSubview:_inputTextField];
    
    _tablesheet = [[NPTableSheet alloc] initWithTitleList:list delegate:self];
    _tablesheet.showSelectedImageOnlySelected = _showSelectedImageOnlySelected;
    _tablesheet.selectedImage = _selectedImage;
    
    CGRect rc = rect;
    _tablesheet.beginOffsetX = rc.origin.x;
    _tablesheet.beginOffsetY = rc.origin.y + rc.size.height + 2;
    _tablesheet.targetOffsetY = rc.origin.y + rc.size.height + 2;
    
    _tablesheet.checkViewWidth = rc.size.width + 2;
    _tablesheet.checkViewHeight = rc.size.height;
    _tablesheet.selectedIndex = 0;
    _tablesheet.defaultCheckViewBgColor = [UIColor whiteColor];
    _tablesheet.textColor = [UIColor grayColor];
    [_tablesheet showCheckViewsInView:_popupView];
    _alreadyShowed = YES;
}

- (void)closeCombox
{
    if (_tablesheet)
    {
        [_tablesheet closeCheckViews];
        _tablesheet = NULL;
    }
    [_popupView removeFromSuperview];
    _alreadyShowed = NO;
}

- (void)reloadData:(NSArray*)list
{
    [_tablesheet reloadData:list];
}

-(BOOL)npTableSheet:(NPTableSheet *)tablesheet didSelectedImage:(NSUInteger)index
{
    if (_comboxDelegate && [_comboxDelegate respondsToSelector:@selector(npComboBox:didSelectedImage:)])
        return [_comboxDelegate npComboBox:self didSelectedImage:index];
    return YES;
}

- (void)npTableSheet:(NPTableSheet*)tablesheet didSelectedAtIndex:(NSUInteger)index
{
    if (_comboxDelegate)
        [_comboxDelegate npComboBox:self didSelectedAtIndex:index];
}

- (void)didTableSheetAppear:(NPTableSheet*)tablesheet
{
    if (_comboxDelegate && [_comboxDelegate respondsToSelector:@selector(didAppear:)])
        [_comboxDelegate didAppear:self];
}

- (void)didTableSheetDismiss:(NPTableSheet *)tablesheet
{
    [self closeCombox];
    if (_comboxDelegate && [_comboxDelegate respondsToSelector:@selector(didDismiss:)])
        [_comboxDelegate didDismiss:self];
}

@end
