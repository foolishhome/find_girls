//
//  LiveViewController.m
//  yyfe
//
//  Created by computer-boy on 15/1/20.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "LiveViewController.h"
#import "LiveListView.h"
#import "NPUIImageButton.h"
#import "WizardViewController.h"
#import "NetDisconnectedTips.h"
#import "DBModel.h"
#import "SearchViewController.h"

@interface LiveViewController ()<ViewPagerDataSource, ViewPagerDelegate>
{
    uint32_t _sid;
    uint32_t _ssid;
    BOOL _saveToEntity;
    
    NSMutableArray * _tabAry;
    
    UIBarButtonItem * _searchBtn;
    
    NetDisconnectedTips* netDisconnectedTipsRecommend;
    LiveListView* recommendView;
    NetDisconnectedTips* netDisconnectedTipsStock;
    LiveListView* stockView;
    NetDisconnectedTips* netDisconnectedTipsGold;
    LiveListView* goldView;
    NetDisconnectedTips* netDisconnectedTipsExtra;
    LiveListView* extraView;
}
@end

extern NSString * const T_RECOMMAND;
extern NSString * const T_STOCK;
extern NSString * const T_GOLD;
extern NSString * const T_EXTRA;

static NSArray * tabTitle;
@implementation LiveViewController

- (void)fullLayout:(UIView *)parentView subView:(UIView *)subView
{
    NSArray *horizontalConstraints;
    NSArray *verticalConstraints;
    horizontalConstraints = [NSLayoutConstraint constraintsWithVisualFormat:@"H:|[subView]|"
                                                                    options:0
                                                                    metrics:nil
                                                                      views:@{@"subView" : subView}];
    
    verticalConstraints = [NSLayoutConstraint constraintsWithVisualFormat:@"V:|[subView]|"
                                                                  options:0
                                                                  metrics:nil
                                                                    views:@{@"subView" : subView}];
    
    [subView setTranslatesAutoresizingMaskIntoConstraints:NO];
    [parentView addConstraints:horizontalConstraints];
    [parentView addConstraints:verticalConstraints];
}

-(id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.tabsViewBackgroundColor = [UIColor whiteColor];
        self.contentViewBackgroundColor = [UIColor colorWithRed:245.0/255.0 green:245.0/255.0 blue:245.0/255.0 alpha:1];
        self.indicatorColor = [UIColor colorWithRed:72.0/255.0 green:160.0/255.0 blue:219.0/255.0 alpha:1];
        self.setTextColorAsIndicatorColor = YES;
        
        tabTitle = @[NSLocalizedString(@"mrecommend", @""), NSLocalizedString(@"mstock", @""), NSLocalizedString(@"mgold", @""), NSLocalizedString(@"mextra", @"")];
        
        CGRect rc = CGRectMake(0, 0, 400, 500);
        _tabAry = [[NSMutableArray alloc] init];
        
        UIView* pageView = [[UIView alloc] init];
        LiveListView * view = [[LiveListView alloc] initWithFrame:rc];
        recommendView = view;
        view.tabId = T_RECOMMAND;
        view.showTabId = YES;
        view.liveDelegate = self;
        [pageView addSubview:view];
        [self fullLayout:pageView subView:view];
        [_tabAry addObject:pageView];
        [view queryURL:nil];

        pageView = [[UIView alloc] init];
        view = [[LiveListView alloc] initWithFrame:rc];
        stockView = view;
        view.tabId = T_STOCK;
        view.liveDelegate = self;
        [pageView addSubview:view];
        [self fullLayout:pageView subView:view];
        [_tabAry addObject:pageView];
        [view queryURL:nil];

        pageView = [[UIView alloc] init];
        view = [[LiveListView alloc] initWithFrame:rc];
        goldView = view;
        view.tabId = T_GOLD;
        view.liveDelegate = self;
        [pageView addSubview:view];
        [self fullLayout:pageView subView:view];
        [_tabAry addObject:pageView];
        [view queryURL:nil];

        pageView = [[UIView alloc] init];
        view = [[LiveListView alloc] initWithFrame:rc];
        extraView = view;
        view.tabId = T_EXTRA;
        view.liveDelegate = self;
        [pageView addSubview:view];
        [self fullLayout:pageView subView:view];
        [_tabAry addObject:pageView];
        [view queryURL:nil];

        self.dataSource = self;
        self.delegate = self;
        
        NPUIImageButton * v = [[NPUIImageButton alloc] initWithFrame:CGRectMake(0, 0, 36, 36)];
        [v setBackgroundImage:[UIImage imageNamed:@"search"] pressImage:[UIImage imageNamed:@"search"]];
        [v addTarget:self action:@selector(onSearch:) forControlEvents:UIControlEventTouchUpInside];
        _searchBtn = [[UIBarButtonItem alloc] initWithCustomView:v];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onAppDidFinishLaunch:) name:UIApplicationDidFinishLaunchingNotification object:nil];

    return self;
}

-(void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)onAppDidFinishLaunch:(NSNotification *)notification
{
    netDisconnectedTipsRecommend = [NetDisconnectedTips initWithView:recommendView];
    [netDisconnectedTipsRecommend.netDisconnectedTipsView.retryButton addTarget:recommendView action:@selector(onNetDisconnectedRetry:) forControlEvents:UIControlEventTouchUpInside];
    recommendView.netDisconnectedTips = netDisconnectedTipsRecommend;
    
    netDisconnectedTipsStock = [NetDisconnectedTips initWithView:stockView];
    [netDisconnectedTipsStock.netDisconnectedTipsView.retryButton addTarget:stockView action:@selector(onNetDisconnectedRetry:) forControlEvents:UIControlEventTouchUpInside];
    stockView.netDisconnectedTips = netDisconnectedTipsStock;
    
    netDisconnectedTipsGold = [NetDisconnectedTips initWithView:goldView];
    [netDisconnectedTipsGold.netDisconnectedTipsView.retryButton addTarget:goldView action:@selector(onNetDisconnectedRetry:) forControlEvents:UIControlEventTouchUpInside];
    goldView.netDisconnectedTips = netDisconnectedTipsGold;
    
    netDisconnectedTipsExtra = [NetDisconnectedTips initWithView:extraView];
    [netDisconnectedTipsExtra.netDisconnectedTipsView.retryButton addTarget:extraView action:@selector(onNetDisconnectedRetry:) forControlEvents:UIControlEventTouchUpInside];
    extraView.netDisconnectedTips = netDisconnectedTipsExtra;
}

- (void)viewDidLoad {
    self.tabHeight = 40.0f;
    self.tabWidth = self.view.frame.size.width / 4;
    self.view.backgroundColor = [UIColor colorWithRed:245.0/255.0 green:245.0/255.0 blue:245.0/255.0 alpha:1];

    [super viewDidLoad];

    self.navigationItem.title = NSLocalizedString(@"live_navigate_title", nil);
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0) {
        // Add a negative spacer on iOS >= 7.0
        UIBarButtonItem *negativeSpacer = [[UIBarButtonItem alloc]
                                           initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                           target:nil action:nil];
        negativeSpacer.width = -10;
        [self.navigationItem setRightBarButtonItems:[NSArray arrayWithObjects:negativeSpacer, _searchBtn, nil]];
    } else {
        // Just set the UIBarButtonItem as you would normally
        [self.navigationItem setRightBarButtonItem:_searchBtn];
    }
    
//    NSString * account = [LoginModel sharedInstance].autoLoginAccount;
//    if (account && account.length > 0)
//        [[LoginModel sharedInstance] loginWithAuto];

    NSUserDefaults * def = [NSUserDefaults standardUserDefaults];
    BOOL hasShowWizard = [def boolForKey:@"wizard"];
    if (!hasShowWizard) {
        NSArray * ary = [NSArray arrayWithObjects:@"wizard1", @"wizard3", @"wizard4", nil];
        [WizardViewController showWizard:self pics:ary];

        [def setBool:YES forKey:@"wizard"];
        [def synchronize];
    }
}

- (void)onSearch:(id)sender {
    SearchViewController * s = [self.storyboard instantiateViewControllerWithIdentifier:@"SearchViewController"];
    s.delegate = self;
    [self.navigationController pushViewController:s animated:NO];
}

-(void)viewDidAppear:(BOOL)animated
{
    [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger: UIInterfaceOrientationPortrait] forKey:@"orientation"];
}

- (NSUInteger)numberOfTabsForViewPager:(ViewPagerController *)viewPager
{
    return tabTitle.count;
}

- (UIView *)viewPager:(ViewPagerController *)viewPager viewForTabAtIndex:(NSUInteger)index
{
    UILabel *label = [UILabel new];
    label.text = (index < tabTitle.count)? tabTitle[index]: @"";
    label.backgroundColor = [UIColor clearColor];
    label.font = [UIFont boldSystemFontOfSize:17.0];
    label.textColor = [UIColor colorWithRed:100.0/255.0f green:100.0/255.0f blue:100.0/255.0f alpha:1];
    label.textAlignment = NSTextAlignmentCenter;
    [label sizeToFit];
    return label;
}

- (UIView *)viewPager:(ViewPagerController *)viewPager contentViewForTabAtIndex:(NSUInteger)index
{
    return (index < _tabAry.count)? [_tabAry objectAtIndex:index]: nil;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

    [[DBModel sharedInstance] didReceiveMemoryWarning];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqual:@"Segue_toChannel"]) {
//        ChannelViewController * destination = [segue destinationViewController];
//        destination.sid = _sid;
//        destination.ssid = _ssid;
//        destination.saveToEntity = _saveToEntity;
    }
}

-(void) toChannel:(uint32_t)sid ssid:(uint32_t)ssid
{
    _sid = sid;
    _ssid = ssid;
    _saveToEntity = NO;
    [self performSegueWithIdentifier:@"Segue_toChannel" sender:self];
}

-(void) activeTab:(int)index
{
    [self switchTab:index];
}

-(void) toSearchChannel:(uint32_t)sid
{
    _sid = sid;
    _ssid = sid;
    _saveToEntity = YES;
    [self performSegueWithIdentifier:@"Segue_toChannel" sender:self];
}

@end
