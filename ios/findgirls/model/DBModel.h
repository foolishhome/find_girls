//
//  DBModel.h
//  yymedical
//
//  Created by computer-boy on 15/1/3.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import "ModelBase.h"

typedef void(^DBResult)(NSError* error);

@interface DBModel : ModelBase
@property (nonatomic, retain) NSManagedObjectContext * bgObjectContext;
@property (nonatomic, retain) NSManagedObjectContext * mainObjectContext;

@property (nonatomic, retain) NSString * modelName;
@property (nonatomic, retain) NSString * dbFileName;

+(DBModel*) sharedInstance;

-(void)closeModel;
-(NSError*)saveContext:(DBResult)handler;
-(NSManagedObjectContext *)createPrivateObjectContext;
@end
