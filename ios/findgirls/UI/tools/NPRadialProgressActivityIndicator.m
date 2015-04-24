#import "NPRadialProgressActivityIndicator.h"

#define DEGREES_TO_RADIANS(x) (x)/180.0*M_PI
#define RADIANS_TO_DEGREES(x) (x)/M_PI*180.0

#define PulltoRefreshThreshold 80.0

@interface NPRadialProgressActivityIndicatorBackgroundLayer : CALayer

@end
@implementation NPRadialProgressActivityIndicatorBackgroundLayer
- (id)init
{
    self = [super init];
    if(self) {
        self.contentsScale = [UIScreen mainScreen].scale;
        [self setNeedsDisplay];
    }
    return self;
}

- (void)drawInContext:(CGContextRef)ctx
{
    //内圈背景色
    CGContextSetFillColorWithColor(ctx, [UIColor clearColor].CGColor);
    CGContextFillEllipseInRect(ctx,CGRectInset(self.bounds, 2, 2));

    //外圈背景色
    CGContextSetStrokeColorWithColor(ctx, [UIColor clearColor].CGColor);
    CGContextSetLineWidth(ctx, 2.5);
    CGContextStrokeEllipseInRect(ctx, CGRectInset(self.bounds, 1, 1));
}
@end

/*-----------------------------------------------------------------*/
@interface NPRadialProgressActivityIndicator()
@property (nonatomic, strong) UIActivityIndicatorView *activityIndicatorView;  //Loading Indicator
@property (nonatomic, strong) NPRadialProgressActivityIndicatorBackgroundLayer *backgroundLayer;
@property (nonatomic, strong) CAShapeLayer *shapeLayer;
@property (nonatomic, strong) CALayer *imageLayer;
@property (nonatomic, strong) UIImage *imageIcon;

@end
@implementation NPRadialProgressActivityIndicator

- (id)init
{//0.0f, frame.size.height - 30.0f
    self = [super initWithFrame:CGRectMake(0, -25, 25, 25)];
    if(self)
    {
        [self _commonInit];
    }
    return self;
}
- (id)initWithImage:(UIImage *)image
{
    self = [super initWithFrame:CGRectMake(0, -25, 25, 25)];
    if(self)
    {
        self.imageIcon =image;
        [self _commonInit];
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if(self) {
        [self _commonInit];
    }
    return self;
}

- (void)_commonInit
{
    self.contentMode = UIViewContentModeRedraw;
    self.state = UZYSPullToRefreshStateNone;
    
    //init actitvity indicator
    _activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    _activityIndicatorView.hidesWhenStopped = YES;
    _activityIndicatorView.frame = self.bounds;
    [self addSubview:_activityIndicatorView];
    
    //init background layer
    NPRadialProgressActivityIndicatorBackgroundLayer *backgroundLayer = [[NPRadialProgressActivityIndicatorBackgroundLayer alloc] init];
    backgroundLayer.frame = self.bounds;

    [self.layer addSublayer:backgroundLayer];
    self.backgroundLayer = backgroundLayer;
    
    if(!self.imageIcon)
        self.imageIcon = [UIImage imageNamed:@"refreshheader"];
    
    //init icon layer
    CALayer *imageLayer = [CALayer layer];
    imageLayer.contentsScale = [UIScreen mainScreen].scale;
    imageLayer.frame = self.bounds;
    imageLayer.contents = (id)self.imageIcon.CGImage;
    [self.layer addSublayer:imageLayer];
    self.imageLayer = imageLayer;
//    self.imageLayer.transform = CATransform3DMakeRotation(DEGREES_TO_RADIANS(180),0,0,1);

    //init arc draw layer
    CAShapeLayer *shapeLayer = [[CAShapeLayer alloc] init];
    shapeLayer.frame = self.bounds;
    shapeLayer.fillColor = nil;
    shapeLayer.strokeColor = [UIColor colorWithRed:190.0/255.f green:190.0/255.f blue:190.0/255.f alpha:1].CGColor;//外圈动画 颜色
    shapeLayer.strokeEnd = 0;
    shapeLayer.shadowColor = [UIColor colorWithWhite:1 alpha:0.8].CGColor;
    shapeLayer.shadowOpacity = 0.7;
    shapeLayer.shadowRadius = 20;
    shapeLayer.contentsScale = [UIScreen mainScreen].scale;
    shapeLayer.lineWidth = 1;

    [self.layer addSublayer:shapeLayer];
    self.shapeLayer = shapeLayer;
}
- (void)layoutSubviews
{
    [super layoutSubviews];
    self.shapeLayer.frame = self.bounds;
    [self updatePath];
}
- (void)updatePath
{
    CGPoint center = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMidY(self.bounds));
    self.shapeLayer.path = [UIBezierPath bezierPathWithArcCenter:center radius:self.bounds.size.width/2 - 1 startAngle:-M_PI_2 endAngle:-M_PI_2 + 2 * M_PI clockwise:NO].CGPath;
}

#pragma mark - property
- (void)setProgress:(double)progress
{
    static double prevProgress;
    
    if(progress > 1.0)
    {
        progress = 1.0;
    }
    
    self.alpha = 1.0 * progress;

    if (progress >= 0 && progress <=1.0) {
        //rotation Animation
        CABasicAnimation *animationImage = [CABasicAnimation animationWithKeyPath:@"transform.rotation"];
//        animationImage.fromValue = [NSNumber numberWithFloat:DEGREES_TO_RADIANS(360-360*prevProgress)];
//        animationImage.toValue = [NSNumber numberWithFloat:DEGREES_TO_RADIANS(360-360*progress)];
        animationImage.duration = 0.2;
        animationImage.removedOnCompletion = NO;
        animationImage.fillMode = kCAFillModeForwards;
        [self.imageLayer addAnimation:animationImage forKey:@"animation"];

        //strokeAnimation
        CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"strokeEnd"];
        animation.fromValue = [NSNumber numberWithFloat:((CAShapeLayer *)self.shapeLayer.presentationLayer).strokeEnd];
        animation.toValue = [NSNumber numberWithFloat:progress];
        animation.duration = 0.15 + 0.35*(fabs([animation.fromValue doubleValue] - [animation.toValue doubleValue]));
        animation.removedOnCompletion = NO;
        animation.fillMode = kCAFillModeForwards;
        [self.shapeLayer addAnimation:animation forKey:@"animation"];
    }
    _progress = progress;
    prevProgress = progress;
}
-(void)setLayerOpacity:(CGFloat)opacity
{
    self.imageLayer.opacity = opacity;
    self.backgroundLayer.opacity = opacity;
    self.shapeLayer.opacity = opacity;
}
-(void)setLayerHidden:(BOOL)hidden
{
    self.imageLayer.hidden = hidden;
    self.shapeLayer.hidden = hidden;
    self.backgroundLayer.hidden = hidden;
}

-(void)actionStopState
{
    self.state = UZYSPullToRefreshStateNone;
    [UIView animateWithDuration:0.2 delay:0.0 options:UIViewAnimationOptionCurveEaseInOut|UIViewAnimationOptionAllowUserInteraction animations:^{
        self.activityIndicatorView.transform = CGAffineTransformMakeScale(1, 1);//改为完成后不缩放
    } completion:^(BOOL finished) {
        if (finished)
        {
            self.activityIndicatorView.transform = CGAffineTransformMakeScale(1, 1);
            [self.activityIndicatorView stopAnimating];
            
            [self setLayerHidden:NO];
            [self setLayerOpacity:1.0];
            [self setProgress:0];
        }
    }];
}
-(void)actionTriggeredState
{
    self.state = UZYSPullToRefreshStateLoading;
    
    [UIView animateWithDuration:0.1 delay:0.0 options:UIViewAnimationOptionCurveEaseInOut|UIViewAnimationOptionAllowUserInteraction animations:^{
        [self setLayerOpacity:0.0];
    } completion:^(BOOL finished) {
        [self setLayerHidden:YES];
    }];

    [self.activityIndicatorView startAnimating];
}

#pragma mark - public method
- (void)stopIndicatorAnimation
{
    [self actionStopState];
}
- (void)manuallyTriggered
{
    [self actionTriggeredState];
}


@end