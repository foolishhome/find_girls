//
//  BaseViewController.m
//  yyfe
//
//  Created by computer-boy on 15/3/24.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "BaseViewController.h"
#import "NPUIImageButton.h"

@interface BaseViewController ()
{
    UIBarButtonItem * _backBtn;
}
@end

@implementation BaseViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    NPUIImageButton * v = [[NPUIImageButton alloc] initWithFrame:CGRectMake(0, 0, 36, 36)];
    [v setBackgroundImage:[UIImage imageNamed:@"actionbar_back"] pressImage:[UIImage imageNamed:@"actionbar_back_down"]];
    [v addTarget:self action:@selector(onBack:) forControlEvents:UIControlEventTouchUpInside];
    _backBtn = [[UIBarButtonItem alloc] initWithCustomView:v];
}

-(void)viewWillAppear:(BOOL)animated
{
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0) {
        // Add a negative spacer on iOS >= 7.0
        UIBarButtonItem *negativeSpacer = [[UIBarButtonItem alloc]
                                           initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                           target:nil action:nil];
        negativeSpacer.width = -15;
        [self.navigationItem setLeftBarButtonItems:[NSArray arrayWithObjects:negativeSpacer, _backBtn, nil]];
    } else {
        // Just set the UIBarButtonItem as you would normally
        [self.navigationItem setRightBarButtonItem:_backBtn];
    }
}

- (void)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
