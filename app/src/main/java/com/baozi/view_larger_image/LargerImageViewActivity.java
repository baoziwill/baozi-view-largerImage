package com.baozi.view_larger_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

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
public class LargerImageViewActivity extends AppCompatActivity {

    public static final String INTENT_ACTION_DATA = "data";
    public static final String INTENT_ACTION_INDEX = "index";

    @Bind(R.id.viewPager)
    NormalViewPager viewPager;
    //    @Bind(R.id.dot_index)
//    PagerDotIndex dotIndex;
//
    private ArrayList<String> source;
    private int index;


    public static void startActivity(Context context, int index, ArrayList<String> arr) {
        Intent intent = new Intent(context, LargerImageViewActivity.class);
        intent.putExtra(INTENT_ACTION_INDEX, index);
        intent.putStringArrayListExtra(INTENT_ACTION_DATA, arr);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_large_imageview);
        ButterKnife.bind(this);
        initIntent();
        initViews();
    }

    private void initIntent() {
        if (getIntent().hasExtra(INTENT_ACTION_INDEX)) {
            index = getIntent().getIntExtra(INTENT_ACTION_INDEX, 0);
        }
        if (getIntent().hasExtra(INTENT_ACTION_DATA)) {
            source = getIntent().getStringArrayListExtra(INTENT_ACTION_DATA);
        }
    }

    //
    private void initViews() {
//
        ImageAdapter imageAdapter = new ImageAdapter(source);
        viewPager.setAdapter(imageAdapter);
//        dotIndex.setViewPager(viewPager, source.size());
//
        viewPager.setCurrentItem(index);
//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }


    class ImageAdapter extends PagerAdapter {

        private int mChildCount = 0;
        private List<String> list;

        public ImageAdapter(List<String> list) {
            this.list = list;
        }

        public List<String> getList() {
            return list;
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
            //glide bug 会拉伸图片成正方形， 去掉动画效果后正常
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

}
