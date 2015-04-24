//
//  LiveListViewCell.h
//  yyfe
//
//  Created by computer-boy on 15/1/24.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LiveData : NSObject
//{"users":1582,"tag":"","ssid":85827422,"sid":85827422,"thumb":"http://image.yy.com/yysnapshot/60798a0f94e399ce36a7ba7bda7f877c6b008ccb?size=320","liveTime":35,"remark":"","asid":null,"liveName":"【融汇金银】黄老师"}
@property(nonatomic) long users;
@property(nonatomic, retain) NSString * tag;
@property(nonatomic) long ssid;
@property(nonatomic) long sid;
@property(nonatomic, retain) NSString * thumb;
@property(nonatomic) long liveTime;
@property(nonatomic, retain) NSString * remark;
@property(nonatomic) long asid;
@property(nonatomic, retain) NSString * liveName;
@end

@protocol CellSelectDelegate <NSObject>
-(void) onSelect:(LiveData*)data index:(NSIndexPath*)index;
@end

@interface LiveListViewCell : UITableViewCell
@property(nonatomic, retain) id<CellSelectDelegate> selectDelegate;

@property (retain, nonatomic) LiveData * data1;
@property (nonatomic, retain) NSIndexPath * index1;
@property (retain, nonatomic) LiveData * data2;
@property (nonatomic, retain) NSIndexPath * index2;

- (void)eraseSelect;
- (void)setSelect:(NSIndexPath*)index;
@end
