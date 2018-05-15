package schooling.com.epizy.someone.schooling.Model;

public class teacher_model {
    public String id, name, phone, subject;

    public teacher_model( String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void setId(String id) {
        this.id = id;
    }
}
