package com.baozi.view_larger_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baozi.view_larger_image.bean.ImageInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {


    @BindView(R.id.pic_list)
    TextView picList;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_3)
    ImageView img3;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.container)
    LinearLayout container;
//
//    String[] source = new String[]{
//            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/6.jpg?raw=true",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//            "http://p7.qhimg.com/dmfd/659_378_90/t0132be8b0b4ccacd7d.webp?size=638x368?zoom_out=80",
//    };

    String[] source = new String[]{
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/1.jpg",
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/2.jpg",
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/3.png",
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/4.jpg",
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/5.jpg",
            "https://raw.githubusercontent.com/baoziwill/baozi-view-largerImage/master/ImgSource/6.jpg",
    };
    ArrayList<String> picArr = new ArrayList<>();
    ArrayList<ImageInfo> imageArr = new ArrayList<>();
    private int statusHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);


        for (int i = 0; i < source.length; i++) {
            picArr.add(source[i]);
        }


        Glide.with(this).load(picArr.get(0)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img1);
        Glide.with(this).load(picArr.get(1)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img2);
        Glide.with(this).load(picArr.get(2)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img3);


    }

    private void initImageInfo() {
        statusHeight = getStatusHeight(this);

        ArrayList<ImageView> list = new ArrayList<>();
        list.add(img1);
        list.add(img2);
        list.add(img3);

        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = list.get(i);
            ImageInfo info = new ImageInfo();
            info.imageViewWidth = imageView.getWidth();
            info.imageViewHeight = imageView.getHeight();
            int[] points = new int[2];
            imageView.getLocationInWindow(points);
            info.imageViewX = points[0];
            info.imageViewY = points[1] - statusHeight;

            imageArr.add(info);
        }

    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedActivity.class);
        context.startActivity(intent);

    }

    @OnClick({R.id.img_1, R.id.img_2, R.id.img_3})
    public void onViewClicked(View view) {
        initImageInfo();
        switch (view.getId()) {
            case R.id.img_1:

                LargerImageViewActivity.startActivity(this, imageArr, 0, picArr);

                break;
            case R.id.img_2:
                LargerImageViewActivity.startActivity(this, imageArr, 1, picArr);

                break;
            case R.id.img_3:
                LargerImageViewActivity.startActivity(this, imageArr, 2, picArr);

                break;
        }
    }
}
