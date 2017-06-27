package com.baozi.view_larger_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {


    @Bind(R.id.pic_list)
    TextView picList;
    @Bind(R.id.img_1)
    ImageView img1;
    @Bind(R.id.img_2)
    ImageView img2;
    @Bind(R.id.img_3)
    ImageView img3;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.container)
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);


        for (int i = 0; i < source.length; i++) {
            picArr.add(source[i]);
        }

        Glide.with(this).load(picArr.get(1)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img1);
        Glide.with(this).load(picArr.get(3)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img2);
        Glide.with(this).load(picArr.get(5)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img3);




    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedActivity.class);
        context.startActivity(intent);

    }

    @OnClick({R.id.img_1, R.id.img_2, R.id.img_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_1:
                LargerImageViewActivity.startActivity(this, 0, picArr);
                break;
            case R.id.img_2:
                LargerImageViewActivity.startActivity(this, 1, picArr);

                break;
            case R.id.img_3:
                LargerImageViewActivity.startActivity(this, 2, picArr);

                break;
        }
    }
}
