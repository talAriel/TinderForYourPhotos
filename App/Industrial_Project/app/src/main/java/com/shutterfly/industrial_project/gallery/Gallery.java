package com.shutterfly.industrial_project.gallery;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import java.util.ArrayList;

import com.shutterfly.industrial_project.Utils.Image;
import com.shutterfly.industrial_project.R;

/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


@Animate(Animation.CARD_TOP_IN_DESC)
@NonReusable
@Layout(R.layout.gallery)
public class Gallery {

    @View(R.id.galleryPlaceHolder)
    private PlaceHolderView mPlaceHolderView;

    private Context mContext;
    private ArrayList<Image> mImageList;

    public Gallery(Context context, ArrayList<Image> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Resolve
    private void onResolved() {
        mPlaceHolderView.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));

        for(Image image : mImageList) {
            mPlaceHolderView.addView(new GalleryImage(mContext, image.getUrl()));
        }
    }
}
