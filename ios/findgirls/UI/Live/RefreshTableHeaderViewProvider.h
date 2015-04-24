#import <Foundation/Foundation.h>
#import "RefreshHeaderView.h"

@interface RefreshTableHeaderViewProvider : NSObject<RefreshTableHeaderViewManagerDelegate>
@property (assign, nonatomic)PullRefreshState state;
@end


@interface RefreshTableHeaderViewProviderSimple : NSObject<RefreshTableHeaderViewManagerDelegate>
@end