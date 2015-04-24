//
//  Http.h
//  yyfe
//
//  Created by computer-boy on 15/1/21.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef void (^HttpResultHandler)(BOOL success, NSDictionary* returnDict);
typedef void (^HttpErrorHandler)(NSError * error);
typedef void (^HttpProcessHandler)(double process);

@interface Http : NSObject
+ (void)get:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler;
+ (void)getJSON:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler;
+ (void)post:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler;
+ (void)put:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler;
@end
