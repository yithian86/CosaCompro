package yithian.cosacompro.db.dbclasses;

import java.util.Date;

public class List {
    private String gList_name;
    private Date start;
    private Date end;

    public List() {
    }

    public List(String gList_name, Date start, Date end) {
        this.gList_name = gList_name;
        this.start = start;
        this.end = end;
    }

    // GETTERS
    public String getList_name() {
        return gList_name;
    }

    // SETTERS
    public void setList_name(String gList_name) {
        this.gList_name = gList_name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
