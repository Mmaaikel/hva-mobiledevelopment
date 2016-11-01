package nl.mmaaikel.homework;

/**
 * Created by Maikel on 19-09-16.
 */

public class Assignment {

    private long id;
    private String assignment;
    private Course course;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return assignment;
    }
}
