//
//  WizardViewController.m
//  yyfe
//
//  Created by computer-boy on 15/3/5.
//  Copyright (c) 2015年 yy.com. All rights reserved.
//

#import "WizardViewController.h"

@interface WizardViewController ()<UIScrollViewDelegate>
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageCtrl;

@end

@implementation WizardViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self.view setBackgroundColor:[UIColor colorWithRed:125.0/255.0f green:195.0/255.0f blue:255.0/255.0f alpha:1]];
}

-(void)viewWillAppear:(BOOL)animated
{
    CGRect frame = self.view.frame;
    [_scrollView setContentSize:CGSizeMake(frame.size.width * (_pics.count + 1), frame.size.height)];
    
    CGRect rc = frame;
    for (int i = 0; i < _pics.count; i++) {
        NSString * pic = [_pics objectAtIndex:i];
        
        UIImage *image1 = [UIImage imageNamed:pic];
        CGFloat height = rc.size.width * image1.size.height / image1.size.width;
        CGRect imageRc = CGRectMake(rc.origin.x, rc.origin.y + (rc.size.height - height) / 2, rc.size.width, height);
        imageRc = CGRectOffset(imageRc, rc.size.width * i, 0);
        UIImageView* imageView1 = [[UIImageView alloc] initWithFrame:imageRc];
        [imageView1 setImage:image1];  //加载图片help01.png到imageView1中。
        [_scrollView addSubview:imageView1];
    }
    
    _pageCtrl.numberOfPages = _pics.count;
}

+ (void)showWizard:(UIViewController*)parent pics:(NSArray*)pics
{
    WizardViewController * wizard = [parent.storyboard instantiateViewControllerWithIdentifier:@"WizardViewController"];
    wizard.pics = pics;
    [parent presentViewController:wizard animated:NO completion:nil];
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    //更新UIPageControl的当前页
    CGPoint offset = scrollView.contentOffset;
    CGRect bounds = scrollView.frame;
    int page = offset.x / bounds.size.width;
    [_pageCtrl setCurrentPage:page];
    
    NSLog(@"%d", page);
    
    if (page >=_pics.count) {
        [UIView animateWithDuration:0.5 animations:^{
            self.view.alpha = 0.3;
        } completion:^(BOOL b){
            [self dismissViewControllerAnimated:NO completion:nil];
        }];
    }
}

@end
