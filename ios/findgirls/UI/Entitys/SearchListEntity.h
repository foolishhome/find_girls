//
//  SearchListEntity.h
//  findgirls
//
//  Created by computer-boy on 15/4/22.
//  Copyright (c) 2015年 tc.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface SearchListEntity : NSManagedObject

@property (nonatomic, retain) NSNumber * sid;
@property (nonatomic, retain) NSDate * time;
@property (nonatomic, retain) NSString * title;

@end
