package ltd.akhbod.studentapp;

/**
 * Created by ibm on 16-02-2018.
 */

public class AllPostDetails {

    String qpostmassage = "", qthumburl="",apostmassage="",aimageurl="",savebyme="no",byme="yes",starred="no";


    AllPostDetails(){

    }

    public AllPostDetails(String qpostmassage,String qthumburl, String apostmassage, String aimageurl) {
        this.qpostmassage = qpostmassage;
        this.qthumburl = qthumburl;
        this.apostmassage = apostmassage;
        this.aimageurl = aimageurl;
    }

    public String getQPostmassage() {
        return qpostmassage;
    }

    public void setQPostmassage(String postmassage) {
        this.qpostmassage = postmassage;
    }

    public String getQthumburl() {
        return qthumburl;
    }

    public void setQthumburl(String qthumburl) {
        this.qthumburl = qthumburl;
    }

    public String getApostmassage() {
        return apostmassage;
    }

    public void setApostmassage(String apostmassage) {
        this.apostmassage = apostmassage;
    }

    public String getAimageurl() {
        return aimageurl;
    }

    public void setAimageurl(String aimageurl) {
        this.aimageurl = aimageurl;
    }

    public String getSavebyme() {
        return savebyme;
    }

    public void setSavebyme(String savebyme) {
        this.savebyme = savebyme;
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
}