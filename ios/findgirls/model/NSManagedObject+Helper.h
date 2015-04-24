//
//  NSManagedObject+Helper.h
//  yymedical
//
//  Created by computer-boy on 15/1/3.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "DBModel.h"

typedef void(^ListResult)(NSArray* result, NSError *error);
typedef void(^ObjectResult)(id result, NSError *error);
typedef id(^AsyncProcess)(NSManagedObjectContext *ctx, NSString *className);

@interface NSManagedObject (Helper)

+(id)createNew;

+(NSError*)save:(DBResult)handler;
+(void)delobject:(id)object;
+(void)deleteAllFromEntity;

+(id)syncQueryOne:(NSString*)predicate;
+(NSArray*)syncQuery:(NSString *)predicate orderby:(NSArray *)orders offset:(int)offset limit:(int)limit;

+(void)asyncQueryOne:(NSString*)predicate on:(ObjectResult)handler;
+(void)asyncQuery:(NSString *)predicate orderby:(NSArray *)orders offset:(int)offset limit:(int)limit on:(ListResult)handler;

+(void)asyncProcess:(AsyncProcess)processBlock result:(ListResult)resultBlock;

@end
