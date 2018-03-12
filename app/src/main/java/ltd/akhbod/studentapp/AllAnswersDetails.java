package ltd.akhbod.studentapp;

/**
 * Created by ibm on 18-02-2018.
 */

public class AllAnswersDetails {

    String answermsg="",mainimage="",thumbimage="";

    public AllAnswersDetails(){


    }

    public AllAnswersDetails(String answermsg, String thumbimage,String mainimage) {
        this.answermsg = answermsg;
        this.thumbimage = thumbimage;
        this.mainimage=mainimage;
    }

    public String getMainimage() {return mainimage;}

    public void setMainimage(String mainimage) {
        this.mainimage = mainimage;
    }

    public String getAnswermsg() {
        return answermsg;
    }

    public void setAnswermsg(String answermsg) {
        this.answermsg = answermsg;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public void setThumbimage(String thumbimage) {
        this.thumbimage = thumbimage;
    }
}
