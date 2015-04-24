//
//  LiveListHeader.h
//  yyfe
//
//  Created by computer-boy on 15/1/27.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LiveListHeader : UIView
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UIView *btnMore;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topSpace;
@end
