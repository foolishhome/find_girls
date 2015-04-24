//
//  LiveListViewCell.m
//  yyfe
//
//  Created by computer-boy on 15/1/24.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "LiveListViewCell.h"
#import "UIImageView+WebCache.h"

@implementation LiveData
@end

@interface LiveListViewCell()
@property (weak, nonatomic) IBOutlet UIView *cell1;
@property (weak, nonatomic) IBOutlet UIView *cell2;
@property (weak, nonatomic) IBOutlet UILabel *bottomLabel1;
@property (weak, nonatomic) IBOutlet UILabel *bottomLabel2;
@property (weak, nonatomic) IBOutlet UIImageView *image1;
@property (weak, nonatomic) IBOutlet UIImageView *image2;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel1;
@property (weak, nonatomic) IBOutlet UILabel *countLabel1;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel2;
@property (weak, nonatomic) IBOutlet UILabel *countLabel2;
@property (weak, nonatomic) IBOutlet UILabel *tag1;
@property (weak, nonatomic) IBOutlet UILabel *tag2;
@end

@implementation LiveListViewCell
- (void)awakeFromNib {
    // Initialization code
}

-(void)setSelectDelegate:(id<CellSelectDelegate>)selectDelegate
{
    _selectDelegate = selectDelegate;
    _cell2.hidden = YES;
    [self eraseSelect];
}

- (void)eraseSelect;
{
    [_cell1 setBackgroundColor:[UIColor clearColor]];
    [_cell2 setBackgroundColor:[UIColor clearColor]];
}

- (void)setSelect:(NSIndexPath*)index
{
//    if ([index compare:_index1] == NSOrderedSame)
//        [_cell1 setBackgroundColor:[UIColor grayColor]];
//    else if ([index compare:_index2] == NSOrderedSame)
//        [_cell2 setBackgroundColor:[UIColor grayColor]];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)setData1:(LiveData *)data1
{
    _data1 = data1;
    [_image1 sd_setImageWithURL:[NSURL URLWithString:data1.thumb] placeholderImage:[UIImage imageNamed:@"loading"]];
    _bottomLabel1.text = data1.liveName;
    _timeLabel1.text = [NSString stringWithFormat:@"%ld分钟", data1.liveTime];
    _countLabel1.text = [NSString stringWithFormat:@"%ld", data1.users];
    _tag1.text = data1.tag;
    _tag1.hidden = !(data1.tag && data1.tag.length > 0);
}

-(void)setData2:(LiveData *)data2
{
    _data2 = data2;
    [_image2 sd_setImageWithURL:[NSURL URLWithString:data2.thumb] placeholderImage:[UIImage imageNamed:@"loading"]];
    _bottomLabel2.text = data2.liveName;
    _timeLabel2.text = [NSString stringWithFormat:@"%ld分钟", data2.liveTime];
    _countLabel2.text = [NSString stringWithFormat:@"%ld", data2.users];
    _cell2.hidden = NO;
    _tag2.text = data2.tag;
    _tag2.hidden = !(data2.tag && data2.tag.length > 0);
}

-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint pt = [touch locationInView:self];
    
    CGPoint pt1 = [_cell1 convertPoint:pt fromView:self];
    if (CGRectContainsPoint(_cell1.bounds, pt1)) {
        if (_selectDelegate && _data1)
            [_selectDelegate onSelect:_data1 index:_index1];
    }

    pt1 = [_cell2 convertPoint:pt fromView:self];
    if (CGRectContainsPoint(_cell2.bounds, pt1)) {
        if (_selectDelegate && _data2)
            [_selectDelegate onSelect:_data2 index:_index2];
    }
}

@end
