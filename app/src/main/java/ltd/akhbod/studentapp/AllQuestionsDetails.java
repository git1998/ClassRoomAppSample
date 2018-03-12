package ltd.akhbod.studentapp;

/**
 * Created by ibm on 01-03-2018.
 */

public class AllQuestionsDetails {

    String qPostMsg="",qPostImage="",qPostThumbImage,starred="",byme="";

    public AllQuestionsDetails(){


    }

    public AllQuestionsDetails(String qPostMsg, String qPostImage, String qPostThumbImage,String starred,String byme) {
        this.qPostMsg = qPostMsg;
        this.qPostImage = qPostImage;
        this.qPostThumbImage = qPostThumbImage;
        this.starred=starred;
        this.byme=byme;
    }

    public String getByme() {
        return byme;
    }

    public void setByme(String byme) {
        this.byme = byme;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public String getqPostMsg() {
        return qPostMsg;
    }

    public void setqPostMsg(String qPostMsg) {
        this.qPostMsg = qPostMsg;
    }

    public String getqPostImage() {
        return qPostImage;
    }

    public void setqPostImage(String qPostImage) {
        this.qPostImage = qPostImage;
    }

    public String getqPostThumbImage() {
        return qPostThumbImage;
    }

    public void setqPostThumbImage(String qPostThumbImage) {
        this.qPostThumbImage = qPostThumbImage;
    }
}
