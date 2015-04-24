//
//  JsonHelper.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/9.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JsonHelper : NSObject

- (NSString*)JsonString:(id) object;

@end

@interface NSDictionary (JsonHelper)

- (NSString *)JSONString;

@end

