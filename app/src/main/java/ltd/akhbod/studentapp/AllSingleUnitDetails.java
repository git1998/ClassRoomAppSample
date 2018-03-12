package ltd.akhbod.studentapp;

/**
 * Created by ibm on 05-03-2018.
 */

public class AllSingleUnitDetails {

    private String testname;
    int nosq;

    public AllSingleUnitDetails(String testname, int nosq) {
        this.testname = testname;
        this.nosq = nosq;
    }

    public AllSingleUnitDetails(){

    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public int getNosq() {
        return nosq;
    }

    public void setNosq(int nosq) {
        this.nosq = nosq;
    }
}
