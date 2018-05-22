package schooling.com.epizy.someone.schooling.Model;

public class timetable_model {
    public String name, type, day, id, start_Hour, end_Hour;

    public timetable_model(String name, String type, String day, String start_Hour, String end_Hour) {
        this.name = name;
        this.day = day;
        this.type = type;
        this.start_Hour = start_Hour;
        this.end_Hour = end_Hour;
    }

    public void setId(String id) {
        this.id = id;
    }
}