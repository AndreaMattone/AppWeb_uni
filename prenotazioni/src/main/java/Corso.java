public class Corso {
    private String courseName;

    public Corso(String courseName){
        this.courseName=courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String toString() {
        return courseName;
    }
}