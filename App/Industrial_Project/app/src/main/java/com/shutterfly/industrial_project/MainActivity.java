package com.shutterfly.industrial_project;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.placeholderview.PlaceHolderView;

import com.shutterfly.industrial_project.Utils.Image;
import com.shutterfly.industrial_project.gallery.Gallery;
import com.shutterfly.industrial_project.Utils.LoadFromJson;


/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


public class MainActivity extends AppCompatActivity {

    private OkHttpClient client;

    Context mContext;

    @BindView(R.id.galleryView)
    private PlaceHolderView mGalleryView;

    static boolean isFirstTime  = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        ButterKnifeLite.bind(this);
        //setup gallery
        new MainExecuteServerCommand().execute("getUserPhotos");
        //buttons
        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClick();
            }

        });
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetClick();
            }

        });
        findViewById(R.id.OKStartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOKStartClick();
            }

        });
        findViewById(R.id.CancelStartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelStartClick();
            }

        });
    }

    public void onStartClick() {
        if(isFirstTime){
            findViewById(R.id.startDialogue).setVisibility(View.VISIBLE);
            findViewById(R.id.OKStartButton).setVisibility(View.VISIBLE);
            findViewById(R.id.CancelStartButton).setVisibility(View.VISIBLE);
            isFirstTime = false;
        }else {
            onOKStartClick();
        }
    }

    public void onResetClick() {
        new MainExecuteServerCommand().execute("deleteDB");
        new MainExecuteServerCommand().execute("buildDB");
    }

    public void onOKStartClick() {
        finish();
        Intent intent = new Intent(MainActivity.this, SwipeActivity.class);
        startActivity(intent);
    }

    public void onCancelStartClick() {
        findViewById(R.id.startDialogue).setVisibility(View.GONE);
        findViewById(R.id.OKStartButton).setVisibility(View.GONE);
        findViewById(R.id.CancelStartButton).setVisibility(View.GONE);
    }

    /**
     * optional server commands:
     *              getUserPhotos - get all user photos
     *              deleteDB - delete all data on server
     *              buildDB - initiate DB
     */
    private class MainExecuteServerCommand extends AsyncTask<String, Void , String> {
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && !result.equals("\n")){
                ArrayList<Image> imagesList = LoadFromJson.loadImagesFromJson(result);
                mGalleryView.addView(new Gallery(mContext, imagesList));
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