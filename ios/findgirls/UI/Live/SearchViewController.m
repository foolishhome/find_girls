//
//  SearchViewController.m
//  yyfe
//
//  Created by computer-boy on 15/3/23.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "SearchViewController.h"
#import "NPComboBox.h"
#import "NPUIImageButton.h"
#import "SearchListEntity.h"
#import "NSManagedObject+Helper.h"
#import "SearchCell.h"

@interface SearchViewController ()<UIAlertViewDelegate, UISearchBarDelegate, UITableViewDataSource, UITableViewDelegate>
{
    NSArray * _arySearchList;
    
    UISearchBar * _searchBar;
    UIBarButtonItem * _searchBtn;
    UIBarButtonItem * _cancelBtn;
}
@property (weak, nonatomic) IBOutlet UITableView *searchList;

@end

@implementation SearchViewController
static NSString * identifier = @"SearchCellIdentifier";

-(UITextField*)findTextFieldInSubviewsRecursively:(UIView*)view
{
    if([view isKindOfClass:[UITextField class]]){
        return (UITextField*)view;
    }
    
    for (UIView *subView in view.subviews){
        UITextField* field = [self findTextFieldInSubviewsRecursively:subView];
        if(field != nil){
            return field;
        }
    }
    return nil;
}

-(id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        [_searchList registerNib:[UINib nibWithNibName:@"SearchCell" bundle:nil] forCellReuseIdentifier:identifier];
        
        _searchBar = [[UISearchBar alloc]initWithFrame:CGRectMake(0,0,420,30)];
        _searchBar.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
        // 真机这里遇到崩溃的情况，虚拟机没事，气死了
        // [_searchBar setReturnKeyType:UIReturnKeyJoin];
        [[self findTextFieldInSubviewsRecursively:_searchBar] setReturnKeyType:UIReturnKeyJoin];
        _searchBar.placeholder = NSLocalizedString(@"searchinputplaceholder", @"");
        _searchBar.delegate = self;
        
        NPUIImageButton * v = [[NPUIImageButton alloc] initWithFrame:CGRectMake(0, 0, 36, 36)];
        [v setBackgroundImage:[UIImage imageNamed:@"search"] pressImage:[UIImage imageNamed:@"search"]];
        [v addTarget:self action:@selector(onSearch:) forControlEvents:UIControlEventTouchUpInside];
        _searchBtn = [[UIBarButtonItem alloc] initWithCustomView:v];
        
        v = [[NPUIImageButton alloc] initWithFrame:CGRectMake(0, 0, 36, 36)];
        [v setBackgroundImage:[UIImage imageNamed:@"actionbar_back"] pressImage:[UIImage imageNamed:@"actionbar_back_down"]];
        [v addTarget:self action:@selector(onCancelSearch:) forControlEvents:UIControlEventTouchUpInside];
        _cancelBtn = [[UIBarButtonItem alloc] initWithCustomView:v];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
   
    _arySearchList = [SearchListEntity syncQuery:nil orderby:@[@"-time"] offset:0 limit:5];
    
    self.navigationItem.titleView = _searchBar;
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0) {
        // Add a negative spacer on iOS >= 7.0
        UIBarButtonItem *negativeSpacer = [[UIBarButtonItem alloc]
                                           initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                           target:nil action:nil];
        negativeSpacer.width = -15;
        [self.navigationItem setLeftBarButtonItems:[NSArray arrayWithObjects:negativeSpacer, _cancelBtn, nil]];
        
        negativeSpacer = [[UIBarButtonItem alloc]
                                           initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                           target:nil action:nil];
        negativeSpacer.width = -10;
        [self.navigationItem setRightBarButtonItems:[NSArray arrayWithObjects:negativeSpacer, _searchBtn, nil]];
    } else {
        // Just set the UIBarButtonItem as you would normally
        [self.navigationItem setLeftBarButtonItem:_cancelBtn];
        [self.navigationItem setRightBarButtonItem:_searchBtn];
    }
    
    [_searchBar becomeFirstResponder];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 40.0f;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (!_arySearchList || _arySearchList.count == 0)
        return 0;
    return _arySearchList.count + 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int row = [indexPath row];
    if (row < _arySearchList.count) {
        SearchCell * cell = [_searchList dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[[NSBundle mainBundle] loadNibNamed:@"SearchCell" owner:nil options:nil] lastObject];
            [cell setFrame:CGRectMake(0, 0, _searchList.frame.size.width, 40.0f)];
        }
        SearchListEntity * item = [_arySearchList objectAtIndex:row];
        cell.titleLabel.text = item.title;
        cell.deleteBtn.tag = [item.sid integerValue];
        [cell.deleteBtn addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(deleteBtnSingleTap:)]];
        return cell;
    } else if (row == _arySearchList.count) {
        UITableViewCell * cell = [[UITableViewCell alloc] initWithFrame:CGRectZero];
        cell.textLabel.text = @"清除搜索记录";
        cell.textLabel.textAlignment = NSTextAlignmentRight;
        cell.textLabel.textColor = [UIColor colorWithRed:71.0/255.0f green:160.0/255.0f blue:217.0/255.0f alpha:1];
        return cell;
    }
    return nil;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    int index = [indexPath row];
    if (_arySearchList && index <= _arySearchList.count) {
        if (index == _arySearchList.count) {
            // 清除
            [SearchListEntity deleteAllFromEntity];
            _arySearchList = @[];
            [_searchList reloadData];
        } else {
            SearchListEntity * item = [_arySearchList objectAtIndex:index];
            [self.navigationController popViewControllerAnimated:NO];
            [_delegate toSearchChannel:[item.sid unsignedIntValue]];
        }
    }
}

- (void)deleteBtnSingleTap:(UITapGestureRecognizer *)recognizer {
    NSInteger sid = recognizer.view.tag;
    if (sid > 0) {
        SearchListEntity * searchItem = [SearchListEntity syncQueryOne:[NSString stringWithFormat:@"sid=\'%d\'", sid]];
        if (searchItem) {
            [SearchListEntity delobject:searchItem];
        }

        _arySearchList = [SearchListEntity syncQuery:nil orderby:@[@"-time"] offset:0 limit:5];
        [_searchList reloadData];
    }
}

- (void)onSearch:(id)sender {
    [self searchBarSearchButtonClicked:_searchBar];
}

- (void)onCancelSearch:(id)sender {
    [self.navigationController popViewControllerAnimated:NO];
}

#pragma mark - search
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    if (!searchBar.text || searchBar.text.length <= 0)
        return;
    
    NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
    NSNumber * sid = [f numberFromString:searchBar.text];
    if ([sid unsignedIntValue] == 0) {
        UIAlertView *alv = [[UIAlertView alloc] initWithTitle:@"错误!" message:@"请输入正确的频道号" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alv show];
        return;
    }
    [self.navigationController popViewControllerAnimated:NO];
    [_delegate toSearchChannel:[sid unsignedIntValue]];
}

@end
