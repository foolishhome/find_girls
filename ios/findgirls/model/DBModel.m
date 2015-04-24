//
//  DBModel.m
//  yymedical
//
//  Created by computer-boy on 15/1/3.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "DBModel.h"

@interface DBModel ()
{
    NSPersistentStoreCoordinator * _coordinator;
}
@end

@implementation DBModel

static DBModel* g_dbModelInstance;
+(DBModel*)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_dbModelInstance = [DBModel new];
    });
    return g_dbModelInstance;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

    NSSet* todoMOCObjects = [_mainObjectContext registeredObjects];
    NSEnumerator* todoMOCObjectsEnum = [todoMOCObjects objectEnumerator];
    NSManagedObject* mobject;
    while(mobject = [todoMOCObjectsEnum nextObject])
    {
        [[mobject managedObjectContext] refreshObject:mobject mergeChanges:NO]; // should release memory!
    }

    todoMOCObjects = [_bgObjectContext registeredObjects];
    todoMOCObjectsEnum = [todoMOCObjects objectEnumerator];
    while(mobject = [todoMOCObjectsEnum nextObject])
    {
        [[mobject managedObjectContext] refreshObject:mobject mergeChanges:NO]; // should release memory!
    }
}

-(void) clearData
{
    [super clearData];
}

-(void)closeModel
{
    // Release CoreData chain
    _mainObjectContext = nil;
    _bgObjectContext = nil;
    _coordinator = nil;
}

-(void)initModel
{
    [super initModel];
    [self initCoreDataStack];
}

-(void)initCoreDataStack
{
    if (_bgObjectContext && _mainObjectContext) {
        [self saveContext:nil];
        [self closeModel];
    }
    
    _coordinator = [self persistentStoreCoordinator];
    if (_coordinator != nil) {
        _bgObjectContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
        [_bgObjectContext setPersistentStoreCoordinator:_coordinator];
        
        _mainObjectContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSMainQueueConcurrencyType];
        [_mainObjectContext setParentContext:_bgObjectContext];
    }
}

- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
    NSPersistentStoreCoordinator *persistentStoreCoordinator = nil;
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:_dbFileName];

    NSError *error = nil;
    persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    NSString *failureReason = @"There was an error creating or loading the application's saved data.";
    if (![persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        // Report any error we got.
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[NSLocalizedDescriptionKey] = @"Failed to initialize the application's saved data";
        dict[NSLocalizedFailureReasonErrorKey] = failureReason;
        dict[NSUnderlyingErrorKey] = error;
        error = [NSError errorWithDomain:@"com.yy.medical" code:9999 userInfo:dict];
        // Replace this with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }

    return persistentStoreCoordinator;
}

- (NSURL *)applicationDocumentsDirectory {
    // The directory the application uses to store the Core Data store file. This code uses a directory named "com.yy.ios.yymedical" in the application's documents directory.
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSManagedObjectModel *)managedObjectModel
{
    static NSManagedObjectModel *managedObjectModel = nil;
    if (managedObjectModel)
        return managedObjectModel;
    
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:_modelName withExtension:@"momd"];
    managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return managedObjectModel;
}

-(NSError*)saveContext:(DBResult)handler{
    // http://www.cocoanetics.com/2012/07/multi-context-coredata/
    NSError * error;
    if (![NSThread isMainThread]) {
        NSLog(@"call save data in Main thread");
        return error;
    }

    if ([_mainObjectContext hasChanges]) {
        [_mainObjectContext save:&error];
        [_bgObjectContext performBlock:^{
            __block NSError *inner_error = nil;
            [_bgObjectContext save:&inner_error];
            if (handler) {
                [_mainObjectContext performBlock:^{
                    handler(error);
                }];
            }
        }];
    }
    return error;
}

- (NSManagedObjectContext *)createPrivateObjectContext
{
    NSManagedObjectContext *ctx = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    [ctx setParentContext:_mainObjectContext];
    
    return ctx;
}

@end
