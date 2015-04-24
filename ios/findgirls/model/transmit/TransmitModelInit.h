//
//  TransmitModelInit.h
//  yyfe
//
//  Created by YYNewBusinessLineB on 15/2/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#pragma once

#import <Foundation/Foundation.h>

/**
 * 设置初始化对应的参数，必须实现。
 * 解耦品类业务逻辑代码。
 * @see TransmitModel。
 */
@protocol TransmitModelInit <NSObject>

/**
 * 需要操作的serviceId。
 * @return 需要操作的serviceId。
 */
@required - (NSArray *)serviceIds;

@end