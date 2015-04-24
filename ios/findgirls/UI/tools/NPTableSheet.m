//
//  NPTableSheet.m
//  NPUIFrameWork
//
//  Created by 123 on 13-12-26.
//  Copyright (c) 2013年 nicholaswu. All rights reserved.
//

#import "NPTableSheet.h"

@implementation NPTableSheet
{
    UITableView*    _mainTableView;
    NSMutableArray*        _checkViews;  //NPTableSheetCheckView
    
    NSArray*        _titleArray;//NSString
    NSInteger       _cellHeight;
    NSInteger       _statusBarHeight;
    NSInteger       _screenWidth;
    NSInteger       _screenHeight;
    NSInteger       _tableViewWidth;
    NSInteger       _tableViewHeight;
}

- (id)initWithTitleList:(NSArray*)titleList delegate:(id<NPTableSheetDelegate>)delegate
{
    _delegate = delegate;
    _titleArray = titleList;
    _cancelIndex = -1;
    _selectedIndex = 0;
    _showCoverPlate = NO;
    _cellHeight = 44;
    _checkViewHeight = _cellHeight;
    _statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
    _beginOffsetX = 0;
    _beginOffsetY = -_checkViewHeight;
    _targetOffsetY = _statusBarHeight;
    _checkViews = [NSMutableArray new];
    
    _selectedTextColor = [UIColor colorWithRed:3.f/255 green:175.f/255 blue:189.f/255 alpha:1];
    _textFont = [UIFont systemFontOfSize:16];
    _textColor = [UIColor whiteColor];
    _checkViewsBgColorArray = @[[UIColor colorWithRed:(float)44/255 green:(float)47/255 blue:(float)54/255 alpha:1],[UIColor colorWithRed:(float)54/255 green:(float)57/255 blue:(float)64/255 alpha:1],[UIColor colorWithRed:(float)63/255 green:(float)66/255 blue:(float)73/255 alpha:1],[UIColor colorWithRed:(float)72/255 green:(float)75/255 blue:(float)82/255 alpha:1]];
    _defaultCheckViewBgColor = nil;
    _screenWidth = [UIScreen mainScreen].bounds.size.width;
    _screenHeight = [UIScreen mainScreen].bounds.size.height;
    
    _tableViewWidth = _screenWidth;
    _checkViewWidth = _screenWidth;
    
    _showSelectedImageOnlySelected = YES;
    
    return [self initWithFrame:CGRectMake(0, 0, _screenWidth, _screenHeight)];
}

- (void)reloadData:(NSArray*)titleList
{
    _titleArray = titleList;

    for (NPTableSheetCheckView* oneview in _checkViews)
    {
        [oneview removeFromSuperview];
    }
    [_checkViews removeAllObjects];
    [self createCheckViews];
    
    for (NPTableSheetCheckView* oneview in _checkViews)
    {
        NSUInteger index = [self indexOfCheckView:oneview];
        if (index != NSNotFound)
        {
            NSInteger targetOffsetY = _targetOffsetY + index * _checkViewHeight;
            CGRect targetRect = CGRectMake(_beginOffsetX, targetOffsetY, _checkViewWidth, _checkViewHeight);
            oneview.frame = targetRect;
        }
    }
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        self.backgroundColor = [UIColor clearColor];
        self.userInteractionEnabled = YES;
        UITapGestureRecognizer* tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(closeCheckViews)];
        tapRecognizer.delegate = self;
        [self addGestureRecognizer:tapRecognizer];

    }
    return self;
}

- (void)dealloc
{

}

- (NSString*)titleAtIndex:(NSInteger)index
{
    if (index <= [_titleArray count]-1)
    {
        return _titleArray[index];
    }
    return nil;
}

- (void)createCheckViews
{
    for (int i = 0; i < [_titleArray count]; i ++)
    {
        NPTableSheetCheckView* oneview = [[NPTableSheetCheckView alloc] initWithFrame:CGRectMake(_beginOffsetX, _beginOffsetY, _checkViewWidth, _checkViewHeight)];
        oneview.backgroundColor = [self getColorFromColorArray:i];
        oneview.titleLabel.text = [_titleArray objectAtIndex:i];
        oneview.selectedTextColor = _selectedTextColor;
        oneview.textColor = _textColor;
        oneview.textFont = _textFont;
        oneview.selectedImage = _selectedImage;
        oneview.showSelectedImageOnlySelected = _showSelectedImageOnlySelected;
        oneview.selected = (_selectedIndex == i);//be last obj to set
        [_checkViews addObject:oneview];
    }
    
    for (long j = [_checkViews count]-1; j>=0; j--)
    {
        [self addSubview:_checkViews[j]];
    }
    
}

- (UIColor*)getColorFromColorArray:(NSUInteger)index
{
    if (_defaultCheckViewBgColor)
    {
        return _defaultCheckViewBgColor;
    }
    if (index <= [_checkViewsBgColorArray count]-1)
    {
        return _checkViewsBgColorArray[index];
    }
    return [UIColor whiteColor];
}

- (void)showCheckViews
{
    if (_alreadyShowed)
        return;
    _alreadyShowed = YES;
    [self createCheckViews];
    
    UIWindow* window = [UIApplication sharedApplication].windows.lastObject;
    [window addSubview:self];

    [self checkViewAnimationShow];
}

- (void)showCheckViewsInView:(UIView*)view
{
    if (_alreadyShowed)
        return;
    _alreadyShowed = YES;
    [self createCheckViews];
    if ([view isKindOfClass:[UIView class]])
    {
        [view addSubview:self];
    }
    [self checkViewAnimationShow];
}

- (void)checkViewAnimationShow
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
    [UIView setAnimationDuration:0.3];
    
    for (NPTableSheetCheckView* oneview in _checkViews)
    {
        NSUInteger index = [self indexOfCheckView:oneview];
        if (index != NSNotFound)
        {
            NSInteger targetOffsetY = _targetOffsetY + index * _checkViewHeight;
            CGRect targetRect = CGRectMake(_beginOffsetX, targetOffsetY, _checkViewWidth, _checkViewHeight);
            oneview.frame = targetRect;
        }
    }
    if (_showCoverPlate)
        self.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.5];
    
    [UIView commitAnimations];
    
    if (_delegate && [_delegate respondsToSelector:@selector(didTableSheetAppear:)])
    {
        [_delegate didTableSheetAppear:self];
    }
}

- (void)closeCheckViews
{
    [UIView animateWithDuration:0.3 animations:^{
        for (NPTableSheetCheckView* oneview in _checkViews)
        {
            oneview.frame = CGRectMake(_beginOffsetX, _beginOffsetY, _checkViewWidth, _checkViewHeight);
        }
        self.backgroundColor = [UIColor clearColor];
    } completion:^(BOOL finished) {
        if (_delegate && [_delegate respondsToSelector:@selector(didTableSheetDismiss:)])
        {
            [_delegate didTableSheetDismiss:self];
        }
        _checkViews = nil;
        [self removeFromSuperview];
    }];
    _alreadyShowed = NO;
}

- (NSUInteger)indexOfCheckView:(NPTableSheetCheckView*)checkView
{
    return [_checkViews indexOfObject:checkView];
}

- (void)tapOnCheckView:(CGPoint)tapPoint
{
    NSUInteger tapViewIndex = (tapPoint.y - _targetOffsetY) / _checkViewHeight;
    if (tapViewIndex != _cancelIndex)
    {
        [self updateCheckViewSelectedStatus:tapViewIndex];
        if (_delegate)
        {
            if (_checkViewWidth - (tapPoint.x - _beginOffsetX) < _checkViewHeight &&
                [_delegate respondsToSelector:@selector(npTableSheet:didSelectedImage:)])
            {
                if (![_delegate npTableSheet:self didSelectedImage:tapViewIndex])
                    return;
            }
            else if ([_delegate respondsToSelector:@selector(npTableSheet:didSelectedAtIndex:)])
            {
                [_delegate npTableSheet:self didSelectedAtIndex:tapViewIndex];
            }
        }
    }
    [self closeCheckViews];
}

- (void)updateCheckViewSelectedStatus:(NSUInteger)selectedIndex
{
    for (int i = 0; i < [_checkViews count]; i++)
    {
        NPTableSheetCheckView* oneview = [_checkViews objectAtIndex:i];
        oneview.selected = (i == selectedIndex);
    }
}














































- (void)createSheetTableView
{
    _tableViewHeight = _cellHeight * [_titleArray count] + _statusBarHeight;
    _mainTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, -_tableViewHeight, _tableViewWidth, _tableViewHeight)];
    _mainTableView.bounces = NO;
    _mainTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    _mainTableView.userInteractionEnabled = YES;
    _mainTableView.backgroundColor = [UIColor blackColor];
    
    _mainTableView.contentInset = UIEdgeInsetsMake(_statusBarHeight, 0, 0, 0);

    
    _mainTableView.delegate = self;
    _mainTableView.dataSource = self;
    
    [self addSubview:_mainTableView];
    
    
}

- (void)showSheet
{
    [self createSheetTableView];
    
    UIWindow* window = [UIApplication sharedApplication].windows.lastObject;
    [window addSubview:self];
    
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationCurve:7];
    [UIView setAnimationDuration:0.2];
    
    _mainTableView.frame = CGRectMake(0, 0, _tableViewWidth, _tableViewHeight);
    self.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.5];
    
    [UIView commitAnimations];
}

- (void)closeSheet
{
    [UIView animateWithDuration:0.2 animations:^{
        self.backgroundColor = [UIColor clearColor];
        _mainTableView.frame = CGRectMake(0, -_tableViewHeight, _tableViewWidth, _tableViewHeight);
    } completion:^(BOOL finished) {
        if (finished)
        {
            [_mainTableView removeFromSuperview];
            _mainTableView = nil;
            [self removeFromSuperview];
        }
    }];
}


- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NPTableSheetCell* onecell;
    NSString* cellIdentity = @"cellIdentity";
    onecell = [tableView dequeueReusableCellWithIdentifier:cellIdentity];
    if (!onecell)
    {
        onecell = [[NPTableSheetCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentity];
    }
    onecell.selectionStyle = UITableViewCellSelectionStyleNone;
    UILabel* titleLabel = onecell.titleLabel;
    UIImageView* imageView = onecell.selectedImageView;
    if (_selectedIndex == indexPath.row || !_showSelectedImageOnlySelected)
        imageView.image = [UIImage imageNamed:_selectedImage];
    else
        imageView.image = nil;
    
    UIColor* cellbgcolor;
    if (_checkViewsBgColorArray && [_checkViewsBgColorArray isKindOfClass:[NSArray class]])
    {
        if (indexPath.row <= [_checkViewsBgColorArray count]-1)
            cellbgcolor = _checkViewsBgColorArray[indexPath.row];
    }
    
    if (cellbgcolor)
    {
        onecell.backgroundColor = cellbgcolor;
        onecell.contentView.backgroundColor = cellbgcolor;
        onecell.backgroundView.backgroundColor = cellbgcolor;
    }
    
    titleLabel.text = _titleArray[indexPath.row];
    return onecell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (_titleArray && [_titleArray isKindOfClass:[NSArray class]])
    {
        return [_titleArray count];
    }
    return 0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row != _cancelIndex)
    {
        _selectedIndex = indexPath.row;
        [tableView reloadData];
        if (_delegate && [_delegate respondsToSelector:@selector(npTableSheet:didSelectedAtIndex:)])
        {
            [_delegate npTableSheet:self didSelectedAtIndex:indexPath.row];
        }
        
    }
    
    [self closeSheet];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return _cellHeight;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch{
    CGPoint touchPoint = [touch locationInView:self];
    if (CGRectContainsPoint(CGRectMake(_beginOffsetX, _targetOffsetY, _checkViewWidth, _checkViewHeight*[_titleArray count]), touchPoint))
    {
        [self tapOnCheckView:touchPoint];
        return NO;
    }
    return YES;
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





@implementation NPTableSheetCheckView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, frame.size.width - 60, 30)];
        _titleLabel.font = _textFont;
        _titleLabel.textColor = _textColor;
        _titleLabel.textAlignment = NSTextAlignmentRight;
        _titleLabel.center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
        _titleLabel.backgroundColor = [UIColor clearColor];
        _selectedImageView = [[UIImageView alloc] initWithFrame:CGRectMake(frame.size.width - 25, self.bounds.size.height/2-20/2, 20, 20)];
        _selectedImageView.backgroundColor = [UIColor clearColor];
        [self addSubview:_titleLabel];
        [self addSubview:_selectedImageView];
    }
    return self;
}

- (void)setSelected:(BOOL)selected
{
    _selected = selected;
    if (_selected)
    {
        _selectedImageView.image = [UIImage imageNamed:_selectedImage];
        _titleLabel.textColor = _selectedTextColor;
    }
    else
    {
        if (_showSelectedImageOnlySelected)
            _selectedImageView.image = nil;
        else
            _selectedImageView.image = [UIImage imageNamed:_selectedImage];
        _titleLabel.textColor = _textColor;
    }
}

@end




@implementation NPTableSheetCell
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self)
    {
        NSInteger screenWidth = [UIScreen mainScreen].bounds.size.width;
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, screenWidth - 60, 30)];
        _titleLabel.font = [UIFont systemFontOfSize:16];
        _titleLabel.textColor = [UIColor whiteColor];
        _titleLabel.textAlignment = NSTextAlignmentRight;
        _titleLabel.center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
        _titleLabel.backgroundColor = [UIColor clearColor];
        _selectedImageView = [[UIImageView alloc] initWithFrame:CGRectMake(screenWidth - 25, self.bounds.size.height/2-20/2, 20, 20)];
        _selectedImageView.backgroundColor = [UIColor clearColor];

        [self.contentView addSubview:_titleLabel];
        [self.contentView addSubview:_selectedImageView];
    }
    return self;
}

@end
