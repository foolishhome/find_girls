#import <UIKit/UIKit.h>
typedef void (^actionHandler)(void);
typedef NS_ENUM(NSUInteger, UZYSPullToRefreshState) {
    UZYSPullToRefreshStateNone =0,
    UZYSPullToRefreshStateStopped,
    UZYSPullToRefreshStateTriggering,
    UZYSPullToRefreshStateTriggered,
    UZYSPullToRefreshStateLoading,
    
};


@interface NPRadialProgressActivityIndicator : UIView

@property (nonatomic,assign) BOOL isObserving;
@property (nonatomic,assign) double progress;
@property (nonatomic, assign) CGFloat originalTopInset;

@property (nonatomic,assign) UZYSPullToRefreshState state;
@property (nonatomic,weak) UIScrollView *scrollView;
@property (nonatomic,copy) actionHandler pullToRefreshHandler;
- (id)initWithImage:(UIImage *)image;

- (void)setProgress:(double)progress;
- (void)manuallyTriggered;
- (void)stopIndicatorAnimation;

@end
