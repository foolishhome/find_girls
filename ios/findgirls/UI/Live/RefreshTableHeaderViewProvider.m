#import "RefreshTableHeaderViewProvider.h"
#import "NPRadialProgressActivityIndicator.h"
#define TEXT_COLOR	 [UIColor colorWithRed:0.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:1.0]
#define FLIP_ANIMATION_DURATION 0.18f

@implementation RefreshTableHeaderViewProvider
{
    UIView*                                 _internalView;
    UIActivityIndicatorView *               _activityView;
    UILabel *                               _lastUpdatedLabel;
    UILabel *                               _statusLabel;
    CALayer *                               _arrowImage;
    NPRadialProgressActivityIndicator*    _radialIndicator;
    NSDate*                                 _lastLoadingDate;
}

- (UIView*)createCustomView:(CGRect)frame
{
    
    frame = CGRectMake(frame.origin.x, 0, frame.size.width, frame.size.height);
    _internalView = [[UIView alloc] initWithFrame:frame];
    
    _internalView.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    _internalView.backgroundColor = [UIColor colorWithRed:233.f/255 green:233.f/255 blue:233.f/255 alpha:1.0];
    _internalView.clipsToBounds = YES;
    /*
     UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0.0f, frame.size.height - 30.0f, _internalView.frame.size.width, 20.0f)];
     label.autoresizingMask = UIViewAutoresizingFlexibleWidth;
     label.font = [UIFont systemFontOfSize:12.0f];
     label.textColor = TEXT_COLOR;
     label.shadowColor = [UIColor colorWithWhite:0.9f alpha:1.0f];
     label.shadowOffset = CGSizeMake(0.0f, 1.0f);
     label.backgroundColor = [UIColor clearColor];
     label.textAlignment = UITextAlignmentCenter;
     [_internalView addSubview:label];
     _lastUpdatedLabel=label;
     
     label = [[UILabel alloc] initWithFrame:CGRectMake(0.0f, _internalView.frame.size.height - 48.0f, _internalView.frame.size.width, 20.0f)];
     label.autoresizingMask = UIViewAutoresizingFlexibleWidth;
     label.font = [UIFont boldSystemFontOfSize:13.0f];
     label.textColor = TEXT_COLOR;
     label.shadowColor = [UIColor colorWithWhite:0.9f alpha:1.0f];
     label.shadowOffset = CGSizeMake(0.0f, 1.0f);
     label.backgroundColor = [UIColor clearColor];
     label.textAlignment = UITextAlignmentCenter;
     [_internalView addSubview:label];
     _statusLabel=label;
     
     CALayer *layer = [CALayer layer];
     layer.frame = CGRectMake(25.0f, frame.size.height - 65.0f, 30.0f, 55.0f);
     layer.contentsGravity = kCAGravityResizeAspect;
     layer.contents = (id)[UIImage imageNamed:@"blueArrow.png"].CGImage;
     
     #if __IPHONE_OS_VERSION_MAX_ALLOWED >= 40000
     if ([[UIScreen mainScreen] respondsToSelector:@selector(scale)])
     {
     layer.contentsScale = [[UIScreen mainScreen] scale];
     }
     #endif
     
     [[_internalView layer] addSublayer:layer];
     _arrowImage=layer;
     
     UIActivityIndicatorView *view = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
     view.frame = CGRectMake(25.0f, frame.size.height - 38.0f, 20.0f, 20.0f);
     [_internalView addSubview:view];
     _activityView = view;
     */
    [self setState:PullRefreshNormal progress:0];
    
    
    _radialIndicator = [[NPRadialProgressActivityIndicator alloc] initWithFrame:CGRectMake(160-25.f/2, frame.size.height - 25/2, 25, 25)];
    [_radialIndicator setProgress:0];
    [_internalView addSubview:_radialIndicator];
    
    return _internalView;
}



- (void)setState:(PullRefreshState)aState progress:(float)progress
{
    switch (aState)
    {
        case PullRefreshPulling:
        {
            /* _statusLabel.text = NSLocalizedString(@"Release to update", @"Release to update");
             [CATransaction begin];
             [CATransaction setAnimationDuration:FLIP_ANIMATION_DURATION];
             _arrowImage.transform = CATransform3DMakeRotation((M_PI / 180.0) * 180.0f, 0.0f, 0.0f, 1.0f);
             [CATransaction commit];*/
            [_radialIndicator setProgress:progress];
            _radialIndicator.frame = CGRectMake(320/2-25.f/2, _internalView.frame.size.height - 25/2 + [self pullAcceptOffset]*progress/2, 25, 25);
        }
            break;
        case PullRefreshNormal:
        {/*
          if (_state == PullRefreshPulling)
          {
          [CATransaction begin];
          [CATransaction setAnimationDuration:FLIP_ANIMATION_DURATION];
          _arrowImage.transform = CATransform3DIdentity;
          [CATransaction commit];
          }
          _statusLabel.text = NSLocalizedString(@"pull to update", @"pull to update");
          [_activityView stopAnimating];
          [CATransaction begin];
          [CATransaction setValue:(id)kCFBooleanTrue forKey:kCATransactionDisableActions];
          _arrowImage.hidden = NO;
          _arrowImage.transform = CATransform3DIdentity;
          [CATransaction commit];
          
          [self updateTimeText];
          */
            [_radialIndicator setProgress:progress];
            _radialIndicator.frame = CGRectMake(320/2-25.f/2, _internalView.frame.size.height - 25/2 + [self pullAcceptOffset]*progress/2, 25, 25);
        }
            break;
        case PullRefreshLoading:
        {/*
          _statusLabel.text = NSLocalizedString(@"loading", @"loading");
          [_activityView startAnimating];
          [CATransaction begin];
          [CATransaction setValue:(id)kCFBooleanTrue forKey:kCATransactionDisableActions];
          _arrowImage.hidden = YES;
          [CATransaction commit];
          */
            _lastLoadingDate = [NSDate date];
            [UIView beginAnimations:nil context:NULL];
            [UIView setAnimationDuration:0.1];
            [UIView setAnimationCurve:7];
            _radialIndicator.frame = CGRectMake(320/2-25.f/2, _internalView.frame.size.height - 25/2 + [self pullAcceptOffset]/2 , 25, 25);
            [UIView commitAnimations];
            [_radialIndicator manuallyTriggered];
            
        }
            break;
        case PullRefreshFinish:
        {
            if ([[NSDate date] timeIntervalSinceDate:_lastLoadingDate] > 0.3)   //为了别让转圈消失得太快而做延时
            {
                [_radialIndicator stopIndicatorAnimation];
            }
            else
            {
                double delayInSeconds = 0.3;
                dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayInSeconds * NSEC_PER_SEC));
                dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
                    [_radialIndicator stopIndicatorAnimation];
                });
            }
            
            
            aState = PullRefreshNormal;
        }
            break;
        default:
            break;
    }
    _state = aState;
}
- (NSInteger)pullAcceptOffset
{
    return -40;
}

- (NSUInteger)headerViewHeight
{
    return 40;
}

- (BOOL)useAnimatePullup
{
    return YES;
}

- (BOOL)draggingDistanceMonitor
{
    return YES;
}

- (void)updateTimeText
{
    NSDate *date = [NSDate date];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setAMSymbol:@"AM"];
    [formatter setPMSymbol:@"PM"];
    [formatter setDateFormat:@"MM/dd/yyyy hh:mm:a"];
    _lastUpdatedLabel.text = [NSString stringWithFormat:@"最近更新时间: %@", [formatter stringFromDate:date]];
}
@end






@implementation RefreshTableHeaderViewProviderSimple
{
    UIView* _internalView;
    NSDate* _lastRefreshTime;
    BOOL _allowNextCmd;
    UIActivityIndicatorView *_activityView;
}
- (UIView*)createCustomView:(CGRect)frame
{
    frame = CGRectMake(frame.origin.x, 0, frame.size.width, frame.size.height);
    _internalView = [[UIView alloc] initWithFrame:frame];
    
    _lastRefreshTime = [NSDate date];
    _allowNextCmd = YES;
    UIActivityIndicatorView *view = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    
    view.frame = CGRectMake([UIScreen mainScreen].bounds.size.width/2-view.frame.size.width/2, frame.size.height - 25.0f, 20.0f, 20.0f);
    [_internalView addSubview:view];
    _activityView = view;
    [_activityView startAnimating];
    
    [self setState:PullRefreshNormal progress:0];
    
    return _internalView;
    
}
- (void)setState:(PullRefreshState)aState progress:(float)progress
{
    
}
- (NSInteger)pullAcceptOffset
{
    return -20;
}
- (NSUInteger)headerViewHeight
{
    return 20;
}
- (BOOL)useAnimatePullup
{
    return NO;
}
- (BOOL)draggingDistanceMonitor
{
    return NO;
}

- (NSInteger)scrollTopAcceptOffset
{
    return -5;
}
- (void)hideHeaderView
{
    [_activityView stopAnimating];
}
@end


