#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

typedef enum
{
    PullRefreshPulling = 0,
    PullRefreshNormal,
    PullRefreshLoading,
    PullRefreshFinish,
} PullRefreshState;

extern NSString* const queryPageNoDataNotify;

@class RefreshTableHeaderViewProvider;
@protocol RefreshTableHeaderDelegate;

@protocol RefreshTableHeaderViewManagerDelegate <NSObject>
@required
- (UIView*)createCustomView:(CGRect)frame;
- (void)setState:(PullRefreshState)aState progress:(float)progress;
- (NSInteger)pullAcceptOffset;
- (NSUInteger)headerViewHeight;
- (BOOL)useAnimatePullup;
- (BOOL)draggingDistanceMonitor;
@optional
- (NSInteger)scrollTopAcceptOffset;
- (NSInteger)scrollBottomAcceptOffset;
- (void)hideHeaderView;

@end

@class RefreshHeaderView;
@protocol RefreshTableHeaderDelegate<NSObject>
@required
- (void)refreshHeaderDidTriggerRefresh:(RefreshHeaderView*)view;
- (BOOL)refreshHeaderDataSourceIsLoading:(RefreshHeaderView*)view;
@optional
- (NSDate*)refreshHeaderDataSourceLastUpdate:(RefreshHeaderView*)view;
@end

@interface RefreshHeaderView : UIView
@property(nonatomic,weak) id<RefreshTableHeaderDelegate> delegate;
@property(nonatomic,weak) id<RefreshTableHeaderViewManagerDelegate> viewManageDelegate;

- (id)initWithFrame:(CGRect)frame provider:(id<RefreshTableHeaderViewManagerDelegate>)provider;
- (void)headerViewDidScroll:(UIScrollView *)scrollView;
- (void)headerViewDidEndDragging:(UIScrollView *)scrollView;
- (void)headerViewDidEndDraggingWithoutNotify:(UIScrollView *)scrollView;
- (void)headerViewDidFinishLoading:(UIScrollView *)scrollView;
- (void)headerViewNoMoreData;
@end
