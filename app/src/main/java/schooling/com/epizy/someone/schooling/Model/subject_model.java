package schooling.com.epizy.someone.schooling.Model;

public class subject_model {
    public String id, name, room, teacher, note;

    public subject_model(String name, String room, String teacher, String note) {
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.note = note;
    }

    public void setId(String id) {
        this.id = id;
    }
}
