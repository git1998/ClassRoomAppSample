package ltd.akhbod.studentapp;

import java.io.Serializable;

/**
 * Created by ibm on 05-03-2018.
 */

public class AllUnitLessonSerializeDetails implements Serializable {

    private static final long serialVersionUID=1L;
    private String unitname;
    private int lessonnos;

    public AllUnitLessonSerializeDetails(){

    }

    public AllUnitLessonSerializeDetails(String unitname, int lessonnos) {
        this.unitname = unitname;
        this.lessonnos = lessonnos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
