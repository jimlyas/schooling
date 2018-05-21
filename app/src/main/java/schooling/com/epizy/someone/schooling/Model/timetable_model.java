package schooling.com.epizy.someone.schooling.Model;

public class timetable_model {
    public String name, type, day, id, jamawal, jamakhir;

    public timetable_model(String name,String type, String day, String jamawal, String jamakhir) {
        this.name = name;
        this.day = day;
        this.type = type;
        this.jamawal = jamawal;
        this.jamakhir = jamakhir;
    }

    public void setId(String id) {
        this.id = id;
    }
}