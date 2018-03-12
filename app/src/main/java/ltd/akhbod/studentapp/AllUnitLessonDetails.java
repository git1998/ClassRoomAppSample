package ltd.akhbod.studentapp;

/**
 * Created by ibm on 07-03-2018.
 */

public class AllUnitLessonDetails {

    private String unitname;
    private int lessonnos;

    public AllUnitLessonDetails(){

    }

    public AllUnitLessonDetails(String unitname, int lessonnos) {
        this.unitname = unitname;
        this.lessonnos = lessonnos;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public int getLessonnos() {
        return lessonnos;
    }

    public void setLessonnos(int lessonnos) {
        this.lessonnos = lessonnos;
    }
}
