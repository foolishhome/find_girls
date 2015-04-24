//
//  ImModel.m
//  yyfe
//
//  Created by computer-boy on 15/2/11.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "ImModel.h"
#import "apphelper.h"
#import "imhelper.h"

static ImModel* g_imModelInstance;

@implementation ImModel

+ (ImModel*)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_imModelInstance = [ImModel new];
    });
    return g_imModelInstance;
}

- (void)initModel
{
    [super initModel];
}


@end
