package com.shutterfly.industrial_project.swipe;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;

import com.shutterfly.industrial_project.R;
import com.shutterfly.industrial_project.SwipeActivity;


/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


@NonReusable
@Layout(R.layout.tinder_card)
public class TinderCard {

    @View(R.id.userPhoto)
    private ImageView photo;

    private Context mContext;
    private String mUrl;
    private SwipeActivity mParent;

    public TinderCard(Context context, String url, SwipeActivity parent) {
        mContext = context;
        mUrl = url.trim();
        mParent = parent;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext)
                .load(mUrl)
                .centerCrop()
                .into(photo);
    }

    @SwipeIn
    private void onSwipeIn(){
        mParent.isClicked = false;
        mParent.onLikeClick();
    }

    @SwipeOut
    private void onSwipedOut(){
        mParent.isClicked = false;
        mParent.onIgnoreClick();
    }

}