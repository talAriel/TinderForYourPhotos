package com.shutterfly.industrial_project.Utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;


/*
 *  Created by:
 *              Ariel Tal
 *              Schwartz Adi
 */


public class LoadFromJson {

    public static ArrayList<Image> loadImagesFromJson(String json){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(json);
            ArrayList<Image> imageList = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                Image image = gson.fromJson(array.getString(i), Image.class);
                imageList.add(image);
            }
            return imageList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
