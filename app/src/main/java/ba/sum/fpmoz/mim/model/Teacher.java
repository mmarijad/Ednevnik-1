package ba.sum.fpmoz.mim.model;

public class Teacher {
    public String uid;
    public String name;
    public String email;
    public String course;
    public String uidPredmeta;

    public Teacher() {
    }

    public Teacher (String uid, String email, String name, String course,String uidPredmeta) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.course = course;
        this.uidPredmeta=uidPredmeta;
    }

    public String getUidPredmeta() {
        return uidPredmeta;
    }

    public void setUidPredmeta(String uidPredmeta) {
        this.uidPredmeta = uidPredmeta;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}