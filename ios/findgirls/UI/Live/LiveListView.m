//
//  LiveListView.m
//  yyfe
//
//  Created by computer-boy on 15/1/23.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "LiveListView.h"
#import "Http.h"
#import "LiveListViewCell.h"
#import "LiveListHeader.h"
#import "RefreshHeaderView.h"
#import "RefreshTableHeaderViewProvider.h"
#import "LoadMoreCell.h"

NSString * const host = @"http://localhost:9292/mobile/";
NSString * const method = @"girlslist";

NSString * const TAB_ID = @"tabId";
NSString * const T_RECOMMAND = @"all";
NSString * const T_STOCK = @"goodlook";
NSString * const T_GOLD = @"gold";
NSString * const T_EXTRA = @"extra";

NSString * const M_RECOMMEND = @"所有";
NSString * const M_STOCK = @"身材";
NSString * const M_GOLD = @"黄金";
NSString * const M_EXTRA = @"其他";

int const QUERY_PAGES = 12;
const int kLiveListRowSection = 0;
const int kLoadMoreRowSection = 1;

@interface LiveListView()<CellSelectDelegate, RefreshTableHeaderDelegate>
{
    NSMutableArray * _liveData;
    BOOL _noMoreData;
    NSMutableArray * _tabIds;
    NSMutableArray * _aryCells;
    NSIndexPath * _selectCellIndex;
    NSArray * _aryMark;
    RefreshHeaderView * _refreshView;
    BOOL _refreshHeaderLoading;
    NSDate * _lastReqNextPageDate;
    NSTimer * _requestTimer;
}
@property (nonatomic, assign) BOOL allowLoadMoreFlag;
@end

@implementation LiveListView
- (void)refreshHeaderDidTriggerRefresh:(RefreshHeaderView*)view
{
    [_liveData removeAllObjects];
    [_tabIds removeAllObjects];
    _noMoreData = NO;

    _refreshHeaderLoading = YES;
    [self queryURL:^(void) {
        _refreshHeaderLoading = NO;
        [_refreshView headerViewDidFinishLoading:self];
    }];
}
- (BOOL)refreshHeaderDataSourceIsLoading:(RefreshHeaderView*)view
{
    return _refreshHeaderLoading;
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    [_refreshView headerViewDidScroll:scrollView];
    
    if (scrollView.contentSize.height > scrollView.bounds.size.height && scrollView.decelerating && scrollView.contentOffset.y+scrollView.frame.size.height >= scrollView.contentSize.height )
    {
        NSTimeInterval interval = [[NSDate date] timeIntervalSinceDate:_lastReqNextPageDate];
        if (interval > 0.5)
        {
            _lastReqNextPageDate = [NSDate date];
            [self queryURL:^(void) {
                
            }];
        }
    }
}
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    [_refreshView headerViewDidEndDragging:scrollView];
}

- (IBAction)onNetDisconnectedRetry:(UIButton *)sender
{
    [self queryURL:^{
    }];
}

-(instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _lastReqNextPageDate = [NSDate date];
        _liveData = [[NSMutableArray alloc] init];
        _noMoreData = NO;
        _allowLoadMoreFlag = NO;
        
        _tabIds = [[NSMutableArray alloc] init];
        _aryCells = [[NSMutableArray alloc] init];
        _selectCellIndex = [NSIndexPath indexPathForRow:-1 inSection:-1];
        _showTabId = NO;
        _aryMark = @[M_RECOMMEND, M_STOCK, M_GOLD, M_EXTRA];
        
        _refreshView = [[RefreshHeaderView alloc] initWithFrame:CGRectZero];
        self.tableHeaderView = _refreshView;
        
        self.allowsSelection = NO;
        self.delegate = self;
        self.dataSource = self;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        
        _refreshView = [[RefreshHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.bounds.size.height, self.frame.size.width, self.bounds.size.height) provider:[RefreshTableHeaderViewProvider new]];
        _refreshView.delegate = self;
        [self addSubview:_refreshView];
        
        _requestTimer = nil;
    }
    return self;
}

- (long)valueOfLong:(NSDictionary*)dic key:(NSString*)key
{
    if (!dic || !key)
        return 0;
    NSNumber * obj = [dic objectForKey:key];
    if (!obj || [obj isKindOfClass:[NSNull null].class])
        return 0;
    return [obj longValue];
}

- (int)totalLiveData
{
    int count = 0;
    for (NSArray * livedata in _liveData) {
        count += livedata.count;
    }
    return count;
}

- (NSString*) URL
{
    NSString * url = [NSString stringWithFormat:@"%@%@?%@=%@&pId=%d&start=%d&pageSize=%d", host, method, TAB_ID, _tabId, 0, [self totalLiveData], QUERY_PAGES];
    return url;
}

- (void)queryURL:(void(^)(void))queryFinish
{
    if (_noMoreData) {
        [self stopTimer];
        return;
    }

    [self startTimer];
    
    NSString * url = [self URL];
    [Http getJSON:url resultHandler:^(BOOL success, NSDictionary* returnDict) {
        [self stopTimer];
        
        if (!success)
            return;

        if (returnDict) {
            int newDataCount = 0;
            @try {
                NSArray * dataAry = [returnDict objectForKey:@"data"];
                for (NSDictionary * data in dataAry) {
                    if (!data) continue;
                    NSMutableArray * aryLiveData = [[NSMutableArray alloc] init];
                    NSDictionary * liveList = [data objectForKey:@"liveList"];
                    for (NSDictionary * liveObj in liveList) {
                        LiveData * livedata = [[LiveData alloc] init];
                        
                        livedata.users = [self valueOfLong:liveObj key:@"users"];
                        livedata.tag = [liveObj objectForKey:@"tag"];
                        if ([livedata.tag isKindOfClass:[NSNull class]])
                            livedata.tag = @"";
                        livedata.thumb = [liveObj objectForKey:@"thumb"];
                        if ([livedata.thumb isKindOfClass:[NSNull class]])
                            livedata.thumb = @"";
                        livedata.liveTime = [self valueOfLong:liveObj key:@"liveTime"];
                        livedata.remark = [liveObj objectForKey:@"remark"];
                        if ([livedata.remark isKindOfClass:[NSNull class]])
                            livedata.remark = @"";
                        livedata.liveName = [liveObj objectForKey:@"liveName"];
                        if ([livedata.liveName isKindOfClass:[NSNull class]])
                            livedata.liveName = @"";

                        [aryLiveData addObject:livedata];
                    }
                    if (aryLiveData.count > 0) {
                        newDataCount += aryLiveData.count;
                        NSString * tabId = [data objectForKey:@"tabId"];
                        NSUInteger index = [_tabIds indexOfObject:tabId];
                        if (index == NSNotFound) {
                            [_liveData addObject:aryLiveData];
                            [_tabIds addObject:tabId];
                        } else {
                            NSMutableArray * ary = [_liveData objectAtIndex:index];
                            [ary addObjectsFromArray:aryLiveData];
                        }
                    }
                }
            }
            @catch (NSException *exception) {
                NSLog(@"parse json error: %@", exception.reason);
            }
            @finally {
                // 特定的推荐部分，不分页，只有一次请求
                if (newDataCount < QUERY_PAGES || [_tabId isEqualToString:T_RECOMMAND]) {
                    _noMoreData = YES;
                    _allowLoadMoreFlag = NO;
                } else {
                    _allowLoadMoreFlag = YES;
                }
                [self reloadData];
                if (queryFinish) {
                    queryFinish();
                }
            }
        }
    } errorHandler:^(NSError * error) {
        
    }];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return _liveData.count + 1;
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section >= _liveData.count || section >= _tabIds.count)
        return nil;
    
    static NSString * identifier = @"LiveHeaderIdentifier";
    LiveListHeader * header = [self dequeueReusableCellWithIdentifier:identifier];
    if (header == nil)
        header = [[[NSBundle mainBundle] loadNibNamed:@"LiveListHeader" owner:self options:nil] lastObject];
    
    NSString * secTitle = [_tabIds objectAtIndex:section];
    header.title.text = NSLocalizedString(secTitle, @"");
    [header setTag:section];
    
    if (section == 0)
        header.topSpace.constant = 0.0f;
    else
        header.topSpace.constant = 10.0f;
    
    if ([_aryMark indexOfObject:secTitle] != NSNotFound) {
        UITapGestureRecognizer * singleFingerTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleViewSingleTap:)];
        [header addGestureRecognizer:singleFingerTap];
        header.btnMore.hidden = NO;
    } else {
        header.btnMore.hidden = YES;
    }
    return header;
}

- (void)handleViewSingleTap:(UITapGestureRecognizer *)recognizer {
    NSLog(@"Clicked More");
    NSInteger section = recognizer.view.tag;
    if (section >= _liveData.count || section >= _tabIds.count)
        return;

    NSString * secMark = [_tabIds objectAtIndex:section];
    long index = [_aryMark indexOfObject:secMark];
    if (_liveDelegate && index != NSNotFound)
        [_liveDelegate activeTab:index];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section >= _liveData.count)
        return 1;
    NSArray * ary = [_liveData objectAtIndex:section];
    return (ary.count % 2 == 0)? ary.count / 2: ary.count / 2 + 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (_showTabId && section < _liveData.count)
        return 50.0f;
    return 0.0f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section >= _liveData.count) {
        UITableViewCell *moreCell = [[[NSBundle mainBundle] loadNibNamed:@"LoadMoreCell" owner:self options:nil] lastObject];
        [moreCell setFrame:CGRectMake(0, 0, tableView.frame.size.width, 30.0f)];
        if (!_allowLoadMoreFlag) {
            moreCell.hidden = YES;
        }
        return moreCell;
    }
    
    static NSString * identifier = @"LiveDataIdentifier";
    LiveListViewCell *cell = [self dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"LiveListViewCell" owner:self options:nil] lastObject];
        [_aryCells addObject:cell];
    }
    
    CGRect rc = CGRectMake(0, 0, tableView.frame.size.width, (tableView.frame.size.width - 6)/ 2);
    [cell setFrame:rc];
    cell.selectDelegate = self;
    
    if ([indexPath section] < _liveData.count) {
        NSArray * ary = [_liveData objectAtIndex:[indexPath section]];
        long row = [indexPath row];
        if (row * 2 < ary.count) {
            LiveData * data = [ary objectAtIndex:row * 2];
            cell.data1 = data;
            cell.index1 = [NSIndexPath indexPathForRow:row * 2 inSection:[indexPath section]];
            [cell setSelect:_selectCellIndex];
        }
        if (row * 2 + 1 < ary.count) {
            LiveData * data = [ary objectAtIndex:row * 2 + 1];
            cell.data2 = data;
            cell.index2 = [NSIndexPath indexPathForRow:row * 2 + 1 inSection:[indexPath section]];
            [cell setSelect:_selectCellIndex];
        }
    }
    return cell;
}

-(id)forwardingTargetForSelector:(SEL)aSelector
{
    return self;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section < _liveData.count)
        return (tableView.frame.size.width - 6)/ 2;
    return 30.0f;
}

-(void) onSelect:(LiveData*)data index:(NSIndexPath*)index
{
    _selectCellIndex = index;
    for (int i = 0; i < _aryCells.count; i++) {
        LiveListViewCell * cell = [_aryCells objectAtIndex:i];
        [cell eraseSelect];
        if (cell.index1 == index || cell.index2 == index)
            [cell setSelect:index];
    }
    if (_liveDelegate && data)
        [_liveDelegate toChannel:(uint32_t)data.sid ssid:(uint32_t)data.ssid];
}

- (void)startTimer
{
    [self stopTimer];
    _requestTimer = [NSTimer scheduledTimerWithTimeInterval:10.0f target:self selector:@selector(requestTimeout:) userInfo:nil repeats:NO];
}

- (void)stopTimer
{
    if (_requestTimer && [_requestTimer isValid])
        [_requestTimer invalidate];
    _requestTimer = nil;
}

- (void)requestTimeout:(NSTimer*)timer
{
    _allowLoadMoreFlag = NO;
    [self reloadData];
    
    // reset for timeout
    _refreshHeaderLoading = NO;
    [_refreshView headerViewDidFinishLoading:self];
    
    if (_netDisconnectedTips)
        _netDisconnectedTips.netDisconnectedTipsView.hidden = NO;
}

@end
