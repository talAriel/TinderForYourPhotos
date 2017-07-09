package com.shutterfly.industrial_project;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import com.shutterfly.industrial_project.Utils.Image;
import com.shutterfly.industrial_project.gallery.Gallery;
import com.shutterfly.industrial_project.swipe.TinderCard;

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


public class SwipeActivity extends AppCompatActivity {

    private static final Integer NUM_OF_CARDS = 4;

    OkHttpClient client;

    private Context mContext;

    @BindView(R.id.swipeView)
    private SwipePlaceHolderView mSwipView;

    @BindView(R.id.backgroundGallery)
    private PlaceHolderView mGalleryView;

    public static ArrayList<Image> images = new ArrayList<>();
    public static Image deleted = null;

    static boolean isFirstTimeLiked  = true;
    static boolean isFirstTimeIgnored  = true;
    static boolean isFirstTimeDeleted  = true;
    public static boolean isClicked = true;

    enum Action {LIKE, IGNORE, DELETE};
    Action actionDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        ButterKnifeLite.bind(this);
        mContext = this.getApplicationContext();
        //setup swipe view
        mSwipView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mSwipView.getBuilder()
                .setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_HORIZONTAL)
                .setDisplayViewCount(NUM_OF_CARDS)
                .setWidthSwipeDistFactor(15)
                .setHeightSwipeDistFactor(20)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(0)
                        .setPaddingLeft(0)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_like_msg)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_ignore_msg));
        //setup gallery on background
        new SwipeExecuteServerCommand().execute("getUserPhotos");
        //buttons
        findViewById(R.id.likeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClick();
            }
        });
        findViewById(R.id.ignoreButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIgnoreClick();
            }

        });
        findViewById(R.id.trashButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrashClick();
            }
        });
        findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoClick();
            }
        });
        findViewById(R.id.exitSwipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitClick();
            }
        });
        findViewById(R.id.OKSwipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOKSwipeClick();
            }
        });
        findViewById(R.id.CancelSwipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelSwipeClick();
            }
        });
        //start swiping
        new SwipeExecuteServerCommand().execute("getFirstFour");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipView.disableTouchSwipe();
        mSwipView.addItemRemoveListener(new ItemRemovedListener() {

            @Override
            // what to do when a swipe is done
            public void onItemRemoved(int count) {
                if(count < NUM_OF_CARDS){
                    for(int i=0; i < NUM_OF_CARDS - count ; i++ ){
                        new SwipeExecuteServerCommand().execute("getNext");
                    }
                }
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                    mSwipView.enableTouchSwipe();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.likeButton)
    public void onLikeClick(){
        actionDone = Action.LIKE;
        if(isFirstTimeLiked){
            findViewById(R.id.likeDialogue).setVisibility(View.VISIBLE);
            findViewById(R.id.OKSwipeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.CancelSwipeButton).setVisibility(View.VISIBLE);
            isFirstTimeLiked = false;
        }else{
            onOKSwipeClick();
        }
    }

    @OnClick(R.id.ignoreButton)
    public void onIgnoreClick() {
        actionDone = Action.IGNORE;
        if(isFirstTimeIgnored){
            findViewById(R.id.ignoreDialogue).setVisibility(View.VISIBLE);
            findViewById(R.id.OKSwipeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.CancelSwipeButton).setVisibility(View.VISIBLE);
            isFirstTimeIgnored = false;
        }else{
            onOKSwipeClick();
        }
    }

    @OnClick(R.id.trashButton)
    private void onTrashClick() {
        actionDone = Action.DELETE;
        if(isFirstTimeDeleted){
            findViewById(R.id.deleteDialogue).setVisibility(View.VISIBLE);
            findViewById(R.id.OKSwipeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.CancelSwipeButton).setVisibility(View.VISIBLE);
            isFirstTimeDeleted = false;
        }else{
            onOKSwipeClick();
        }
    }

    @OnClick(R.id.undoButton)
    private void onUndoClick(){
        String arr[] = {"undoDelete", deleted.getUrl()};
        new SwipeExecuteServerCommand().execute(arr);
        findViewById(R.id.undoButton).setVisibility(View.INVISIBLE);
        deleted = null;
    }

    @OnClick(R.id.exitSwipeButton)
    public void onExitClick() {
        finish();
        new SwipeExecuteServerCommand().execute("setRetrieved");
        images.clear();
        Intent intent = new Intent(SwipeActivity.this, StoreActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.OKSwipeButton)
    public void onOKSwipeClick() {
        boolean swipeType = true;
        String command[];
        if(actionDone == Action.LIKE){
            String arr[] = {"updateRecord", images.get(0).getUrl(), "Y"};
            command = arr;
        }else if(actionDone == Action.IGNORE){
            swipeType = false;
            String arr[] = {"updateRecord", images.get(0).getUrl(), "N"};
            command = arr;
        }else if(actionDone == Action.DELETE) {
            swipeType = false;
            findViewById(R.id.undoButton).setVisibility(View.VISIBLE);
            deleted = images.get(0);
            String arr[] = {"deletePhoto", deleted.getUrl()};
            command = arr;
        } else {
            return;
        }
        if(findViewById(R.id.likeDialogue).getVisibility() == View.VISIBLE ||
                findViewById(R.id.ignoreDialogue).getVisibility() == View.VISIBLE ||
                findViewById(R.id.deleteDialogue).getVisibility() == View.VISIBLE){
            onCancelSwipeClick();
        }
        if (isClicked){
            mSwipView.doSwipe(swipeType);
        } else{
            isClicked = true;
        }
        new SwipeExecuteServerCommand().execute(command);
        new SwipeExecuteServerCommand().execute("getNext");
        images.remove(0);
    }

    @OnClick(R.id.CancelSwipeButton)
    public void onCancelSwipeClick() {
        if(findViewById(R.id.likeDialogue).getVisibility() == View.VISIBLE){
            findViewById(R.id.likeDialogue).setVisibility(View.GONE);
        } else if(findViewById(R.id.ignoreDialogue).getVisibility() == View.VISIBLE){
            findViewById(R.id.ignoreDialogue).setVisibility(View.GONE);
        } else {
            findViewById(R.id.deleteDialogue).setVisibility(View.GONE);
        }
        findViewById(R.id.OKSwipeButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.CancelSwipeButton).setVisibility(View.INVISIBLE);
    }

    /**
     * optional server commands:
     *              getUserPhotos - get all user photos
     *              getFirstFour - get first 4 photos to start the swiping
     *              updateRecord + url + Y/N - update like/ignore for a photo
     *              deletePhoto + url - delete the photo
     *              undoDelete + url - undo delete for photo
     *              getNext - get the next photo to show
     *              setRetrieved - reset all undecided photos
     */
    private class SwipeExecuteServerCommand extends AsyncTask<String, Void , String> {
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && !result.equals("\n")){
                ArrayList<Image> tmp = loadImagesFromJson(result);
                if(tmp.size() > NUM_OF_CARDS ){
                    mGalleryView.addView(new Gallery(mContext, tmp));
                }else{
                    for (Image image : tmp) {
                        SwipeActivity.images.add(image);
                        mSwipView.addView(new TinderCard(mContext, image.getUrl(), SwipeActivity.this));
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String[] actions) {
            client = new OkHttpClient();
            String url = "http://ec2-107-22-122-37.compute-1.amazonaws.com/server_wraper.php?goto=" + actions[0];
            switch (actions[0]){
                case "updateRecord":
                    url = url + "&url=" + actions[1] + "&liked=" + actions[2];
                    break;
                case "undoDelete":
                    url = url + "&url=" + actions[1];
                    break;
                case "deletePhoto":
                    url = url + "&url=" + actions[1];
                    break;
                default:
                    break;
            }
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
