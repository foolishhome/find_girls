//
//  LiveListView.h
//  yyfe
//
//  Created by computer-boy on 15/1/23.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LiveViewController.h"
#import "NetDisconnectedTips.h"

@interface LiveListView : UITableView<UITableViewDataSource, UITableViewDelegate>
@property(nonatomic, retain) NSString * tabId;
@property(nonatomic, retain) id<LiveDelegate> liveDelegate;
@property(nonatomic) BOOL showTabId;
@property(nonatomic, retain) NetDisconnectedTips * netDisconnectedTips;

- (void)queryURL:(void(^)(void))queryFinish;
@end
