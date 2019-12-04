package com.example.administrator.searchforlovedones;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PageDetail extends Activity {

    private ImageView titImg;
    private Button btn_back;
    private TextView Title;
    private TextView Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_detail);
        titImg = findViewById(R.id.title_img);
        btn_back = findViewById(R.id.first_back);
        Title = findViewById(R.id.tit);
        Content = findViewById(R.id.con);
        //设置返回按钮的透明度
        btn_back.setAlpha(0.8f);
        Title.setText(getIntent().getStringExtra("title"));
        Content.setText(getIntent().getStringExtra("content"));
        Glide.with(getApplicationContext())
                .load(getResources().getDrawable(R.drawable.titleimg))
                .into(titImg);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}