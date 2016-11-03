package com.ufreedom.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufreedom.floatingview.Floating;
import com.ufreedom.floatingview.FloatingBuilder;
import com.ufreedom.floatingview.FloatingElement;
import com.ufreedom.floatingview.effect.ScaleFloatingTransition;
import com.ufreedom.floatingview.effect.TranslateFloatingTransition;
import com.ufreedom.floatingview.spring.ReboundListener;
import com.ufreedom.floatingview.spring.SimpleReboundListener;
import com.ufreedom.floatingview.spring.SpringHelper;
import com.ufreedom.floatingview.transition.BaseFloatingPathTransition;
import com.ufreedom.floatingview.transition.FloatingPath;
import com.ufreedom.floatingview.transition.FloatingTransition;
import com.ufreedom.floatingview.transition.PathPosition;
import com.ufreedom.floatingview.transition.YumFloating;

public class MainActivity extends AppCompatActivity {

    private Floating floating;
    private View icPlaneView;
    private View icPaperAirPlaneView;
    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floating = new Floating(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        screenWidth  = UIUtils.getScreenWidth(this);
        screenHeight = UIUtils.getScreenWidth(this);

        initLayout();
    }

    private void initLayout() {

        int margin  = UIUtils.dip2px(this, 15);
        int w       = screenWidth - margin * 2;
        int h       = (int) (w * 0.53f);

        // 自行车RootView
        RelativeLayout bikeRootView = (RelativeLayout) findViewById(R.id.itemBikeContainerView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bikeRootView.getLayoutParams();
        layoutParams.width  = w;
        layoutParams.height = h;

        // 闹钟RootView
        RelativeLayout clockRootView = (RelativeLayout) findViewById(R.id.itemClockContainerView);
        RelativeLayout.LayoutParams clockRootViewLayoutParams = (RelativeLayout.LayoutParams) clockRootView.getLayoutParams();
        clockRootViewLayoutParams.width  = w;
        clockRootViewLayoutParams.height = h;

        // 飞机
        icPlaneView = findViewById(R.id.icPlane);
        icPlaneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(icPlaneView.getMeasuredWidth(), icPlaneView.getMeasuredHeight()));
                imageView.setImageResource(R.drawable.floating_plane);

                FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(imageView)
                        .floatingTransition(new PlaneFloating())
                        .build();
                floating.startFloating(floatingElement);
            }
        });

        // 纸飞机(漂浮纸飞机)
        icPaperAirPlaneView = findViewById(R.id.icPaperAirPlane);
        icPaperAirPlaneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(icPaperAirPlaneView.getMeasuredWidth(), icPaperAirPlaneView.getMeasuredHeight()));
                imageView.setImageResource(R.drawable.paper_airplane);

                FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(imageView)
                        .floatingTransition(new TranslateFloatingTransition())
                        .build();
                floating.startFloating(floatingElement);
            }
        });

        // 命令窗口（漂浮文本）
        View icCommandLineView = findViewById(R.id.icCommandLine);
        icCommandLineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText("Hello FloatingView");

                FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(textView)
                        .offsetY(-v.getMeasuredHeight())
                        .floatingTransition(new ScaleFloatingTransition())
                        .build();
                floating.startFloating(floatingElement);
            }
        });

        // 点赞（点赞+122）
        View icLikeView = findViewById(R.id.icLike);
        icLikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(R.layout.ic_like)
                        .floatingTransition(new TranslateFloatingTransition())
                        .build();
                floating.startFloating(floatingElement);

            }
        });


        // 星星
        final View icStarView = findViewById(R.id.icStar);
        icStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(icStarView.getMeasuredWidth(), icStarView.getMeasuredHeight()));
                imageView.setImageResource(R.drawable.star_floating);

                final FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(imageView)
                        .floatingTransition(new StarFloating())
                        .build();

                SpringHelper.createWidthBouncinessAndSpeed(0f, 1f, 11, 15).reboundListener(new ReboundListener() {
                    @Override
                    public void onReboundUpdate(double currentValue) {
                        v.setScaleX((float) currentValue);
                        v.setScaleY((float) currentValue);
                    }

                    @Override
                    public void onReboundEnd() {
                        floating.startFloating(floatingElement);
                    }
                }).start();


            }
        });


        final View icBeerView = findViewById(R.id.icBeer);
        icBeerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(icBeerView.getMeasuredWidth(), icBeerView.getMeasuredHeight()));
                imageView.setImageResource(R.drawable.beer);

                FloatingElement floatingElement = new FloatingBuilder()
                        .anchorView(v)
                        .targetView(imageView)
                        .floatingTransition(new BeerFloating())
                        .build();
                floating.startFloating(floatingElement);
            }
        });
    }

    /** 自定义 飞机 动画 **/
    class PlaneFloating extends BaseFloatingPathTransition {

        @Override
        public FloatingPath getFloatingPath() {
            Path path = new Path();
            path.moveTo(0, 0);
            path.quadTo(100, -300, 0, -600);
            path.rLineTo(0, -screenHeight - 300);
            return FloatingPath.create(path, false);
        }

        @Override
        public void applyFloating(final YumFloating yumFloating) {

            ValueAnimator translateAnimator = ObjectAnimator.ofFloat(getStartPathPosition(), getEndPathPosition());
            translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    PathPosition floatingPosition = getFloatingPosition(value);
                    yumFloating.setTranslationX(floatingPosition.x);
                    yumFloating.setTranslationY(floatingPosition.y);

                }
            });
            translateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    yumFloating.setTranslationX(0);
                    yumFloating.setTranslationY(0);
                    yumFloating.setAlpha(0f);
                    yumFloating.clear();
                }
            });


            SpringHelper.createWidthBouncinessAndSpeed(0.0f, 1.0f, 14, 15)
                    .reboundListener(new SimpleReboundListener() {
                        @Override
                        public void onReboundUpdate(double currentValue) {
                            yumFloating.setScaleX((float) currentValue);
                            yumFloating.setScaleY((float) currentValue);
                        }
                    }).start(yumFloating);

            translateAnimator.setDuration(3000);
            translateAnimator.start();
        }
    }

    class StarFloating implements FloatingTransition {

        @Override
        public void applyFloating(final YumFloating yumFloating) {
            SpringHelper.createWidthBouncinessAndSpeed(0.0f, 1.0f, 10, 15)
                    .reboundListener(new SimpleReboundListener() {
                        @Override
                        public void onReboundUpdate(double currentValue) {
                            yumFloating.setScaleX((float) currentValue);
                            yumFloating.setScaleY((float) currentValue);
                        }
                    }).start(yumFloating);


            ValueAnimator rotateAnimator = ObjectAnimator.ofFloat(0, 360);
            rotateAnimator.setDuration(500);
            rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
            rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
            rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    yumFloating.setRotation((float) valueAnimator.getAnimatedValue());
                }
            });

            ValueAnimator translateAnimator = ObjectAnimator.ofFloat(0, 500);
            translateAnimator.setDuration(600);
            translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    yumFloating.setTranslationY(-(Float) valueAnimator.getAnimatedValue());
                }
            });
            translateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    yumFloating.setAlpha(0f);
                    yumFloating.clear();
                }
            });
            rotateAnimator.start();
            translateAnimator.start();
        }
    }


    class BeerFloating extends BaseFloatingPathTransition {


        @Override
        public FloatingPath getFloatingPath() {
            Path path = new Path();
            path.rLineTo(-100, 0);
            path.quadTo(0, -200, 100, 0);
            path.quadTo(0, 200, -100, 0);
            return FloatingPath.create(path, false);
        }

        @Override
        public void applyFloating(final YumFloating yumFloating) {
            ValueAnimator translateAnimator = ObjectAnimator.ofFloat(0, 500);
            translateAnimator.setDuration(600);
            translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    PathPosition floatingPosition = getFloatingPosition(value);
                    yumFloating.setTranslationX(floatingPosition.x);
                    yumFloating.setTranslationY(floatingPosition.y);

                }
            });


            ValueAnimator alphaAnimation = ObjectAnimator.ofFloat(1f, 0f);
            alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    yumFloating.setAlpha((Float) animation.getAnimatedValue());
                }
            });
            alphaAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    yumFloating.clear();
                }
            });
            alphaAnimation.setStartDelay(550);
            alphaAnimation.setDuration(300);
            translateAnimator.start();
            alphaAnimation.start();
        }
    }

}
