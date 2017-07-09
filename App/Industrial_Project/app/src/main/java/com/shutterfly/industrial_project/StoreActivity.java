package com.shutterfly.industrial_project;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.shutterfly.industrial_project.Utils.Image;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.shutterfly.industrial_project.Utils.LoadFromJson.loadImagesFromJson;


/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


public class StoreActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private OkHttpClient client;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        mContext = this.getApplicationContext();
        ButterKnifeLite.bind(this);
        // download Photos for products
        new StoreExecuteServerCommand().execute("getProductPhotos");
        //button
        findViewById(R.id.exitStoreButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitClick();
            }
        });
    }

    public void onExitClick() {
        finish();
        Intent intent = new Intent(StoreActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * optional server commands:
     *              getProductPhotos - get best 17 photos to show in products
     */
    private class StoreExecuteServerCommand extends AsyncTask<String, Void , String> {
        protected void onPostExecute(String result) {
            if (!result.isEmpty()){
                ArrayList<Image> imagesList = loadImagesFromJson(result);
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(0).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store1));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(1).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store2));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(2).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store3));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(3).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store4));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(4).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store5));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(5).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store6));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(6).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store7));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(7).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store8));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(8).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store9));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(9).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store10));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(10).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store11));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(11).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store12));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(12).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store13));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(13).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store14));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(14).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store15));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(15).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store16));
                Glide.with(StoreActivity.this.mContext).load(imagesList.get(16).getUrl()).centerCrop().into((ImageView) findViewById(R.id.store17));
            }
        }

        @Override
        protected String doInBackground(String[] actions) {
            client = new OkHttpClient();
            String action = actions[0];
            String url = "http://ec2-107-22-122-37.compute-1.amazonaws.com/server_wraper.php?goto=" + action;
            Request request = new Request.Builder()
                    .url(url.trim())
                    .build();
            Response response = null;
            String answer = null;
            try {
                response = client.newCall(request).execute();
                answer = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return answer;
        }
    }
}
