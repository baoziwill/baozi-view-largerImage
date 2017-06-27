package com.baozi.view_larger_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);


    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedActivity.class);
        context.startActivity(intent);

    }

    @OnClick({R.id.img_1, R.id.img_2, R.id.img_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_1:

                break;
            case R.id.img_2:

                break;
            case R.id.img_3:

                break;
        }
    }
}
