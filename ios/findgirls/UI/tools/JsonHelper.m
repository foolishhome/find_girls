//
//  JsonHelper.m
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/9.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "JsonHelper.h"

@implementation JsonHelper

- (NSString*)JsonString:(id)object
{
    return [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:object options:NSJSONWritingPrettyPrinted error:nil] encoding:NSUTF8StringEncoding];
}

@end


@implementation NSDictionary (JsonHelper)

- (NSString *)JSONString;
{
    return [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:self options:NSJSONWritingPrettyPrinted error:nil] encoding:NSUTF8StringEncoding];
}

@end