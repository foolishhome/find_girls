//
//  PermissionViewController.m
//  yyfe
//
//  Created by computer-boy on 15/3/2.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "WebViewController.h"
#import "NPUIImageButton.h"

@interface WebViewController ()<UIWebViewDelegate>
@property (weak, nonatomic) IBOutlet UIWebView *webView;

@end

@implementation WebViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.title = _navigateTitle;
    [_webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:_URL]]];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (!_delegate)
        return YES;
    
    NSString *requestString = [[[request URL]  absoluteString] stringByReplacingPercentEscapesUsingEncoding:
                               NSUTF8StringEncoding ];
    
    NSURL* url = [NSURL URLWithString: requestString];
    NSString* queryString = [url query];

    return [_delegate startLoadWithRequest:self queryString:queryString];
}

@end
