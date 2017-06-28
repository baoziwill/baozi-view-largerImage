package com.baozi.view_larger_image;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baozi.view_larger_image.bean.ImageInfo;
import com.baozi.view_larger_image.widget.NormalViewPager;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 *
 */
public class LargerImageViewActivity extends AppCompatActivity implements ViewTreeObserver.OnPreDrawListener {

    public static final String INTENT_ACTION_DATA = "data";
    public static final String INTENT_ACTION_INDEX = "index";
    public static final String IMAGE_INFO = "IMAGE_INFO";


    public final static String TOP = "top";
    public final static String LEFT = "left";
    public final static String WIDTH = "width";
    public final static String HEIGHT = "height";


    private static final int DURATION = 666;
    public static final int ANIMATE_DURATION = 400;


    private int intentTop;
    private int intentLeft;
    private int intentWidth;
    private int intentHeight;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private ColorDrawable colorDrawable;

    @Bind(R.id.pre_iv)
    ImageView preIv;
    @Bind(R.id.rootView)
    RelativeLayout rootView;
    @Bind(R.id.viewPager)
    NormalViewPager viewPager;
    //    @Bind(R.id.dot_index)
//    PagerDotIndex dotIndex;
//
    private ArrayList<String> source;
    private int index;
    private ArrayList<ImageInfo> infoList;
    private ImageAdapter imageAdapter;
    private int screenWidth;
    private int screenHeight;
    private int imageHeight;
    private int imageWidth;


    public static void startActivity(Context context, ArrayList<ImageInfo> imageInfoList, int index, ArrayList<String> arr) {
        Intent intent = new Intent(context, LargerImageViewActivity.class);
        intent.putParcelableArrayListExtra(IMAGE_INFO, imageInfoList);
        intent.putExtra(INTENT_ACTION_INDEX, index);
        intent.putStringArrayListExtra(INTENT_ACTION_DATA, arr);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_large_imageview);
        ButterKnife.bind(this);
        initIntent(savedInstanceState);
        initViews();
    }

    private void initIntent(Bundle savedInstanceState) {
        if (getIntent().hasExtra(INTENT_ACTION_INDEX)) {
            index = getIntent().getIntExtra(INTENT_ACTION_INDEX, 0);
        }
        if (getIntent().hasExtra(INTENT_ACTION_DATA)) {
            source = getIntent().getStringArrayListExtra(INTENT_ACTION_DATA);
        }
        if (getIntent().hasExtra(IMAGE_INFO)) {
            infoList = getIntent().getParcelableArrayListExtra(IMAGE_INFO);
        }


        colorDrawable = new ColorDrawable(Color.BLACK);


//        if (savedInstanceState == null) {
//            ViewTreeObserver observer = preIv.getViewTreeObserver();
//            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    preIv.getViewTreeObserver().removeOnPreDrawListener(this);
//                    //坐标的获取设置
//                    int[] screenLocation = new int[2];
//                    preIv.getLocationOnScreen(screenLocation);
//                    mLeftDelta = intentLeft - screenLocation[0];
//                    mTopDelta = intentTop - screenLocation[1];
//
//                    mWidthScale = (float) intentWidth / preIv.getWidth();
//                    mHeightScale = (float) intentHeight / preIv.getHeight();
//                    //开启缩放动画
//                    enterAnimation();
//
//                    return true;
//                }
//            });
//        }


    }

    //进入动画
    public void enterAnimation() {
        //设置imageview动画的初始值
        preIv.setPivotX(0);
        preIv.setPivotY(0);
        preIv.setScaleX(mWidthScale);
        preIv.setScaleY(mHeightScale);
        preIv.setTranslationX(mLeftDelta);
        preIv.setTranslationY(mTopDelta);
        //设置动画
        TimeInterpolator sDecelerator = new DecelerateInterpolator();
        //设置imageview缩放动画，以及缩放开始位置
        preIv.animate().setDuration(DURATION).scaleX(1).scaleY(1).
                translationX(0).translationY(0).setInterpolator(sDecelerator);

        // 设置activity主布局背景颜色DURATION毫秒内透明度从透明到不透明
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(DURATION);
        bgAnim.start();
    }


    private void initViews() {
        imageAdapter = new ImageAdapter(source);
        viewPager.setAdapter(imageAdapter);
//        dotIndex.setViewPager(viewPager, source.size());
//
        viewPager.setCurrentItem(index);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @Override
    public boolean onPreDraw() {
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;


        final ImageView imageView = (ImageView) imageAdapter.getPrimaryItem();

        final ImageInfo imageData = infoList.get(index);
        final float vx = imageData.imageViewWidth * 1.0f / screenWidth;
        final float vy = imageData.imageViewHeight * 1.0f / screenHeight;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(animation -> {
            long duration = animation.getDuration();
            long playTime = animation.getCurrentPlayTime();
            float fraction = duration > 0 ? (float) playTime / duration : 1f;
            if (fraction > 1) fraction = 1;
            imageView.setTranslationX(evaluateInt(fraction, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2, 0));
            imageView.setTranslationY(evaluateInt(fraction, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2, 0));
            imageView.setScaleX(evaluateFloat(fraction, vx, 1));
            imageView.setScaleY(evaluateFloat(fraction, vy, 1));
            imageView.setAlpha(fraction);
            rootView.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, Color.BLACK));
        });
        addIntoListener(valueAnimator);
        valueAnimator.setDuration(ANIMATE_DURATION);
        valueAnimator.start();

        return true;
    }

    /**
     * 进场动画过程监听
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    class ImageAdapter extends PagerAdapter {

        private int mChildCount = 0;
        private List<String> list;
        private View currentView;

        public ImageAdapter(List<String> list) {
            this.list = list;
        }

        public List<String> getList() {
            return list;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            currentView = (View) object;
        }

        public View getPrimaryItem() {
            return currentView;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            } else if (list.size() == 1) {
                return 1;
            } else {
                return list.size();
            }
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imgUrl = list.get(position);
            final PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            loadImg(photoView, imgUrl);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        private void loadImg(final PhotoView photoView, String imgUrl) {
            DrawableTypeRequest<String> request = Glide.with(LargerImageViewActivity.this).load(imgUrl);
//            request.error(R.drawable.placeholder_img_load_err);
            request.placeholder(R.mipmap.ic_launcher);
            request.dontAnimate();
            request.listener(new PhotoRequestListener(new SoftReference<>(photoView)));
            if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(".gif")) {
                request.asGif();
                request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
            } else {
                request.asBitmap();
                request.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            request.into(photoView);

        }

        class PhotoRequestListener implements RequestListener<String, GlideDrawable> {
            SoftReference<PhotoView> photoViewSoftReference;

            public PhotoRequestListener(SoftReference<PhotoView> photoViewSoftReference) {
                this.photoViewSoftReference = photoViewSoftReference;
            }

            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                PhotoView photoView = photoViewSoftReference.get();
                if (photoView != null) {
                    photoView.setOnPhotoTapListener((view, v, v1) -> loadImg(photoView, model));
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                PhotoView photoView = photoViewSoftReference.get();
                if (photoView != null) {
                    photoView.setOnPhotoTapListener((view, v, v1) -> finish());
                    photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                return false;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * Integer 估值器
     */
    public Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * Float 估值器
     */
    public Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * Argb 估值器
     */
    public int evaluateArgb(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24//
                | (startR + (int) (fraction * (endR - startR))) << 16//
                | (startG + (int) (fraction * (endG - startG))) << 8//
                | (startB + (int) (fraction * (endB - startB)));
    }
}
