//
//  NSManagedObject+Helper.m
//  yymedical
//
//  Created by computer-boy on 15/1/3.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "NSManagedObject+Helper.h"
#include "DBModel.h"

@implementation NSManagedObject (Helper)

+(id)createNew
{
    NSString *className = [NSString stringWithUTF8String:object_getClassName(self)];
    return [NSEntityDescription insertNewObjectForEntityForName:className inManagedObjectContext:[DBModel sharedInstance].mainObjectContext];
}

+(NSError*)save:(DBResult)handler
{
    return [[DBModel sharedInstance] saveContext:handler];
}

+(void)delobject:(id)object
{
    [[DBModel sharedInstance].mainObjectContext deleteObject:object];
}

+(void)deleteAllFromEntity
{
    NSString *entityName = [NSString stringWithUTF8String:object_getClassName(self)];
    NSManagedObjectContext *ctx = [DBModel sharedInstance].mainObjectContext;
    NSFetchRequest * allRecords = [[NSFetchRequest alloc] init];
    [allRecords setEntity:[NSEntityDescription entityForName:entityName inManagedObjectContext:ctx]];
    [allRecords setIncludesPropertyValues:NO];
    NSError * error = nil;
    NSArray * result = [ctx executeFetchRequest:allRecords error:&error];
    for (NSManagedObject * profile in result) {
        [ctx deleteObject:profile];
    }
    NSError *saveError = nil;
    [ctx save:&saveError];
}

+(id)syncQueryOne:(NSString*)predicate
{
    NSManagedObjectContext *ctx = [DBModel sharedInstance].mainObjectContext;
    NSFetchRequest *fetchRequest = [self makeRequest:ctx predicate:predicate orderby:nil offset:0 limit:1];
    NSError* error = nil;
    NSArray* results = [ctx executeFetchRequest:fetchRequest error:&error];
    if ([results count] <= 0) {
        return nil;
    }
    return results[0];
}

+(NSArray*)syncQuery:(NSString *)predicate orderby:(NSArray *)orders offset:(int)offset limit:(int)limit
{
    NSManagedObjectContext *ctx = [DBModel sharedInstance].mainObjectContext;
    NSFetchRequest *fetchRequest = [self makeRequest:ctx predicate:predicate orderby:orders offset:offset limit:limit];
    
    NSError* error = nil;
    NSArray* results = [ctx executeFetchRequest:fetchRequest error:&error];
    if (error) {
        NSLog(@"error: %@", error);
        return @[];
    }
    return results;
}

+(void)asyncQueryOne:(NSString*)predicate on:(ObjectResult)handler
{
    NSManagedObjectContext *ctx = [[DBModel sharedInstance] createPrivateObjectContext];
    [ctx performBlock:^{
        NSFetchRequest *fetchRequest = [self makeRequest:ctx predicate:predicate orderby:nil offset:0 limit:1];
        NSError* error = nil;
        NSArray* results = [ctx executeFetchRequest:fetchRequest error:&error];
        if (error) {
            NSLog(@"error: %@", error);
            [[DBModel sharedInstance].mainObjectContext performBlock:^{
                handler(@[], error);
            }];
        }
        if ([results count] < 1) {
            [[DBModel sharedInstance].mainObjectContext performBlock:^{
                handler(@[], nil);
            }];
        }
        NSManagedObjectID *objId = ((NSManagedObject*)results[0]).objectID;
        [[DBModel sharedInstance].mainObjectContext performBlock:^{
            handler([[DBModel sharedInstance].mainObjectContext objectWithID:objId], nil);
        }];
    }];
}

+(void)asyncQuery:(NSString *)predicate orderby:(NSArray *)orders offset:(int)offset limit:(int)limit on:(ListResult)handler
{
    NSManagedObjectContext *ctx = [[DBModel sharedInstance] createPrivateObjectContext];
    [ctx performBlock:^{
        NSFetchRequest *fetchRequest = [self makeRequest:ctx predicate:predicate orderby:orders offset:offset limit:limit];
        NSError* error = nil;
        NSArray* results = [ctx executeFetchRequest:fetchRequest error:&error];
        if (error) {
            NSLog(@"error: %@", error);
            [[DBModel sharedInstance].mainObjectContext performBlock:^{
                handler(@[], error);
            }];
        }
        if ([results count] < 1) {
            [[DBModel sharedInstance].mainObjectContext performBlock:^{
                handler(@[], nil);
            }];
        }
        NSMutableArray *result_ids = [[NSMutableArray alloc] init];
        for (NSManagedObject *item  in results) {
            [result_ids addObject:item.objectID];
        }
        [[DBModel sharedInstance].mainObjectContext performBlock:^{
            NSMutableArray *final_results = [[NSMutableArray alloc] init];
            for (NSManagedObjectID *oid in result_ids) {
                [final_results addObject:[[DBModel sharedInstance].mainObjectContext objectWithID:oid]];
            }
            handler(final_results, nil);
        }];
    }];
}

+(void)asyncProcess:(AsyncProcess)processBlock result:(ListResult)resultBlock
{
    NSString *className = [NSString stringWithUTF8String:object_getClassName(self)];
    NSManagedObjectContext *ctx = [[DBModel sharedInstance] createPrivateObjectContext];
    [ctx performBlock:^{
        id resultList = processBlock(ctx, className);
        if (resultList) {
            if ([resultList isKindOfClass:[NSError class]]) {
                [[DBModel sharedInstance].mainObjectContext performBlock:^{
                    resultBlock(nil, resultList);
                }];
            }
            if ([resultList isKindOfClass:[NSArray class]]) {
                NSMutableArray *idArray = [[NSMutableArray alloc] init];
                for (NSManagedObject *obj in resultList) {
                    [idArray addObject:obj.objectID];
                }
                NSArray *objectIdArray = [idArray copy];
                [[DBModel sharedInstance].mainObjectContext performBlock:^{
                    NSMutableArray *objArray = [[NSMutableArray alloc] init];
                    for (NSManagedObjectID *robjId in objectIdArray) {
                        [objArray addObject:[[DBModel sharedInstance].mainObjectContext objectWithID:robjId]];
                    }
                    if (resultBlock) {
                        resultBlock([objArray copy], nil);
                    }
                }];
            }
            
        }else{
            resultBlock(nil, nil);
        }
    }];
}

+(NSFetchRequest*)makeRequest:(NSManagedObjectContext*)ctx predicate:(NSString*)predicate orderby:(NSArray*)orders offset:(int)offset limit:(int)limit
{
    NSString *className = [NSString stringWithUTF8String:object_getClassName(self)];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:className inManagedObjectContext:ctx]];
    if (predicate) {
        [fetchRequest setPredicate:[NSPredicate predicateWithFormat:predicate]];
    }
    NSMutableArray *orderArray = [[NSMutableArray alloc] init];
    if (orders != nil) {
        for (NSString *order in orders) {
            NSSortDescriptor *orderDesc = nil;
            if ([[order substringToIndex:1] isEqualToString:@"-"]) {
                orderDesc = [[NSSortDescriptor alloc] initWithKey:[order substringFromIndex:1]
                                                        ascending:NO];
            } else {
                orderDesc = [[NSSortDescriptor alloc] initWithKey:order
                                                        ascending:YES];
            }
            [orderArray addObject:orderDesc];
        }
        [fetchRequest setSortDescriptors:orderArray];
    }
    if (offset > 0) {
        [fetchRequest setFetchOffset:offset];
    }
    if (limit > 0) {
        [fetchRequest setFetchLimit:limit];
    }
    return fetchRequest;
}

@end
