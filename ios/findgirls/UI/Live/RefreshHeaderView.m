#import "RefreshHeaderView.h"
#import "RefreshTableHeaderViewProvider.h"

NSString* const queryPageNoDataNotify = @"queryPageNoDataNotify";
@implementation RefreshHeaderView
{
    PullRefreshState                            _state;
    NSDate*                                     _lastRefreshTime;
    BOOL                                        _allowNextCmd;
    UIActivityIndicatorView*                    _activityView;
    BOOL                                        _queryNoMoreRow;
    id<RefreshTableHeaderViewManagerDelegate>   _provider;
}
- (id)initWithFrame:(CGRect)frame provider:(id<RefreshTableHeaderViewManagerDelegate>)provider
{
    self = [super initWithFrame:frame];
    if (self != nil)
    {
        self.backgroundColor = [UIColor colorWithRed:233.f/255 green:233.f/255 blue:233.f/255 alpha:1];
        _provider = provider;
        self.viewManageDelegate = provider;
        _allowNextCmd = YES;
        _queryNoMoreRow = NO;
        _lastRefreshTime = [NSDate date];
        UIView* internalView = [self.viewManageDelegate createCustomView:frame];
        internalView.backgroundColor = [UIColor clearColor];
        [self addSubview:internalView];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(headerViewNoMoreData) name:queryPageNoDataNotify object:nil];
    }
    return self;
}

- (void)dealloc
{
    self.delegate=nil;
    self.viewManageDelegate = nil;
    [[NSNotificationCenter defaultCenter] removeObserver:self name:queryPageNoDataNotify object:nil];
}

#pragma mark -
#pragma mark ScrollView Methods

- (void)headerViewDidScroll:(UIScrollView *)scrollView
{
    if ([self.viewManageDelegate draggingDistanceMonitor])
    {
        BOOL _loading = NO;
        if ([self.delegate respondsToSelector:@selector(refreshHeaderDataSourceIsLoading:)])
        {
            _loading = [self.delegate refreshHeaderDataSourceIsLoading:self];
        }
        if (_state == PullRefreshLoading)
        {
            CGFloat offset = MAX(scrollView.contentOffset.y * -1, 0);
            offset = MIN(offset, [self.viewManageDelegate headerViewHeight]);
            UIEdgeInsets orgInset = scrollView.contentInset;
            scrollView.contentInset = UIEdgeInsetsMake(offset, orgInset.left, orgInset.bottom, orgInset.right);
        }
        else if (scrollView.isDragging)
        {
            
            if (scrollView.contentOffset.y > [self.viewManageDelegate pullAcceptOffset] && scrollView.contentOffset.y < 0.0f && !_loading)
            {
                //need pull
                NSInteger pullOffset = [_viewManageDelegate pullAcceptOffset];
                [self setState:PullRefreshNormal progress:scrollView.contentOffset.y/pullOffset];
            }
            else if (scrollView.contentOffset.y < [self.viewManageDelegate pullAcceptOffset] && !_loading)
            {
                //can release
                NSInteger pullOffset = [_viewManageDelegate pullAcceptOffset];
                [self setState:PullRefreshPulling progress:scrollView.contentOffset.y/pullOffset];
            }
            if (scrollView.contentInset.top != 0)
            {
                UIEdgeInsets orgInset = scrollView.contentInset;
                scrollView.contentInset = UIEdgeInsetsMake(0, orgInset.left, orgInset.bottom, orgInset.right);
            }
        }
    }
    
    
    if ([self.viewManageDelegate respondsToSelector:@selector(scrollTopAcceptOffset)] && scrollView.contentOffset.y <= [self.viewManageDelegate scrollTopAcceptOffset] && scrollView.decelerating && !_queryNoMoreRow)
    {
        [self triggerLoadMore:scrollView topQuery:YES];
    }
}

- (void)headerViewDidEndDragging:(UIScrollView *)scrollView
{
    if ([self.viewManageDelegate draggingDistanceMonitor])
    {
        BOOL _loading = NO;
        if ([self.delegate respondsToSelector:@selector(refreshHeaderDataSourceIsLoading:)])
        {
            _loading = [self.delegate refreshHeaderDataSourceIsLoading:self];
        }
        if (scrollView.contentOffset.y <= [self.viewManageDelegate pullAcceptOffset] && !_loading)
        {
            if ([self.delegate respondsToSelector:@selector(refreshHeaderDidTriggerRefresh:)])
            {
                [self.delegate refreshHeaderDidTriggerRefresh:self];
            }
            [self setState:PullRefreshLoading progress:0];
            UIEdgeInsets orgInset = scrollView.contentInset;
            [UIView beginAnimations:nil context:NULL];
            [UIView setAnimationDuration:0.1];
            scrollView.contentInset = UIEdgeInsetsMake(orgInset.top + [self.viewManageDelegate headerViewHeight], orgInset.left, orgInset.bottom, orgInset.right);
            [UIView commitAnimations];
        }
    }
    
    if ([self.viewManageDelegate respondsToSelector:@selector(scrollTopAcceptOffset)] && !_queryNoMoreRow)
    {
        if ( scrollView.contentOffset.y <= [self.viewManageDelegate pullAcceptOffset] )
        {
            [self triggerLoadMore:scrollView topQuery:YES];
        }
    }
}
- (void)headerViewDidEndDraggingWithoutNotify:(UIScrollView *)scrollView
{   //+仅改变 refreshscrollview 为『加载中』状态
    [self setState:PullRefreshPulling progress:1];
    [self setState:PullRefreshLoading progress:0];
    UIEdgeInsets orgInset = scrollView.contentInset;
    scrollView.contentInset = UIEdgeInsetsMake(orgInset.top + [self.viewManageDelegate headerViewHeight], orgInset.left, orgInset.bottom, orgInset.right);
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3];
    CGFloat headerViewHeight = [self.viewManageDelegate headerViewHeight];
    scrollView.contentOffset = CGPointMake(0, -headerViewHeight);
    [UIView commitAnimations];
}

- (void)headerViewDidFinishLoading:(UIScrollView *)scrollView
{
    UIEdgeInsets orgInset = scrollView.contentInset;
    
    if ([self.viewManageDelegate useAnimatePullup])
    {
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:0.3];
        [scrollView setContentInset:UIEdgeInsetsMake(0.0f, orgInset.left, orgInset.bottom, orgInset.right)];
        [UIView commitAnimations];
    }
    else
    {
        [scrollView setContentInset:UIEdgeInsetsMake(0.0f, orgInset.left, orgInset.bottom, orgInset.right)];
    }
    [self setState:PullRefreshFinish progress:0];
}

- (void)headerViewNoMoreData
{
    _queryNoMoreRow = YES;
    if ([self.viewManageDelegate respondsToSelector:@selector(hideHeaderView)])
    {
        [self.viewManageDelegate hideHeaderView];
    }
}
- (void)triggerLoadMore:(UIScrollView *)scrollView topQuery:(BOOL)topQuery
{
    NSTimeInterval interval = [[NSDate date] timeIntervalSinceDate:_lastRefreshTime];
    if (interval > 0.3)
    {
        if (_allowNextCmd)
        {
            _lastRefreshTime = [NSDate date];
            [self setState:PullRefreshLoading progress:0];
            _allowNextCmd = NO;
            UIEdgeInsets orgInset = scrollView.contentInset;
            UIEdgeInsets newInset = UIEdgeInsetsZero;
            if (topQuery)
                newInset = UIEdgeInsetsMake(30.0f, orgInset.left, orgInset.bottom, orgInset.right);
            else
                newInset = UIEdgeInsetsMake(orgInset.top, orgInset.left, 30.0f, orgInset.right);
            
            [UIView beginAnimations:nil context:NULL];
            [UIView setAnimationDuration:.3];
            [scrollView setContentInset:newInset];
            [UIView commitAnimations];
            
            [self performSelector:@selector(delayRefresh) withObject:nil afterDelay:0.1];
        }
    }
}

- (void)delayRefresh
{
    if ([self.delegate respondsToSelector:@selector(refreshHeaderDidTriggerRefresh:)])
    {
        [self.delegate refreshHeaderDidTriggerRefresh:self];
    }
    _allowNextCmd = YES;
}
- (void)setState:(PullRefreshState)aState progress:(float)progress
{
    switch (aState)
    {
        case PullRefreshPulling:
            [self.viewManageDelegate setState:PullRefreshPulling progress:progress];
            break;
        case PullRefreshNormal:
            [self.viewManageDelegate setState:PullRefreshNormal progress:progress];
            break;
        case PullRefreshLoading:
            [self.viewManageDelegate setState:PullRefreshLoading progress:0];
            break;
        case PullRefreshFinish:
            [self.viewManageDelegate setState:PullRefreshFinish progress:0];
            break;
        default:
            break;
    }
    _state = aState;
}

@end

