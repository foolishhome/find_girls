//
//  Http.m
//  yyfe
//
//  Created by computer-boy on 15/1/21.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "Http.h"
#import "AFNetworking/AFNetworking.h"

@implementation Http

+ (void)get:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler
{
}

+ (void)getJSON:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler
{
    // headers
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"GET"];

    // set request
    AFHTTPRequestOperationManager *manager = [[AFHTTPRequestOperationManager alloc] init];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/x-javascript", @"application/javascript", @"text/html", @"application/xml", @"application/json", @"text/plain", @"text/javascript", nil];
    AFHTTPRequestOperation *requestOperation = [manager HTTPRequestOperationWithRequest:request success:^(AFHTTPRequestOperation *operation, id responseObject) {
        // success
        if (resultHandler) {
            resultHandler(true, responseObject);
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        // failure
        if (errorHandler) {
            errorHandler(error);
        }
    }];
    
    // fire the request
    [requestOperation start];
}

+ (void)post:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler
{
    
}

+ (void)put:(NSString*)url resultHandler:(HttpResultHandler)resultHandler errorHandler:(HttpErrorHandler)errorHandler
{
    
}

@end
