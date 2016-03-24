package yithian.cosacompro.db.dbclasses;

public class GList {
    private int gList_id;
    private String gList_name;
    private String start;
    private String end;

    public GList() {
    }

    public GList(String gList_name, String start, String end) {
        this.gList_name = gList_name;
        this.start = start;
        this.end = end;
    }

    public GList(int gList_id, String gList_name, String start, String end) {
        this.gList_id = gList_id;
        this.gList_name = gList_name;
        this.start = start;
        this.end = end;
    }

    // GETTERS
    public String getGList_name() {
        return gList_name;
    }

    public String getGList_start() {
        return start;
    }

    public String getGList_end() {
        return end;
    }

    public int getGList_id() {
        return gList_id;
    }

    // SETTERS
    public void setGList_name(String gList_name) {
        this.gList_name = gList_name;
    }

    public void setGList_start(String start) {
        this.start = start;
    }

    public void setGList_end(String end) {
        this.end = end;
    }
}
