package com.sebastian.my_app;

/**
 * Created by Usuario on 27/08/2017.
 */

public class ItemData {
    String text;
    Integer imageid;
    public ItemData(String text, Integer imageid){
        this.text = text;
        this.imageid = imageid;
    }
    public String getText(){
        return text;
    }
    public Integer getImageid(){
        return imageid;
    }
}
