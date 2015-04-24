//
//  LiveViewController.h
//  yyfe
//
//  Created by computer-boy on 15/1/20.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewPagerController.h"

@protocol LiveDelegate <NSObject>
-(void) toChannel:(uint32_t)sid ssid:(uint32_t)ssid;
-(void) activeTab:(int)index;
@end

@protocol SearchDelegate <NSObject>
-(void) toSearchChannel:(uint32_t)sid;
@end

@interface LiveViewController : ViewPagerController<LiveDelegate, SearchDelegate>
-(void) toChannel:(uint32_t)sid ssid:(uint32_t)ssid;
-(void) toSearchChannel:(uint32_t)sid;
@end
