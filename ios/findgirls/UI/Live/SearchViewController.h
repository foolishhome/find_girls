//
//  SearchViewController.h
//  yyfe
//
//  Created by computer-boy on 15/3/23.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LiveViewController.h"

@interface SearchViewController : UIViewController
@property(nonatomic, retain) id<SearchDelegate> delegate;
@end
