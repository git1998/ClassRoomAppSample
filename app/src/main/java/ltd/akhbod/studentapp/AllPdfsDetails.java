package ltd.akhbod.studentapp;

/**
 * Created by ibm on 07-03-2018.
 */

public class AllPdfsDetails {

    String topicname,pdfurl;

    public AllPdfsDetails() {
    }

    public AllPdfsDetails(String topicname,String pdfurl) {
        this.topicname = topicname;
        this.pdfurl=pdfurl;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }
}
