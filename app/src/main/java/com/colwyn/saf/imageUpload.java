package com.colwyn.saf;

public class imageUpload {

    private String imageName;
    private String imageUrl;

    public imageUpload () {
        //Empty Constructor
    }

    public imageUpload(String imgname, String imgurl) {

        if(imgname.trim().equals("")){
            imgname = "No Name";
        }

        imageName = imgname;
        imageUrl = imgurl;
    }

    public String GetimgName() {
        return imageName;
    }

    public void setName(String imgname){
        imageName = imgname;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setUrl(String imgurl){
        imageUrl = imgurl;
    }


}
