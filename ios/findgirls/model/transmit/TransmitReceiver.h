//
//  TransmitReceiver.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>

@protocol TransmitWroker <NSObject>

@optional onReceive:(NSData *)data;

@end
