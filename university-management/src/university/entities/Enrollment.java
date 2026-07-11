package university.entities;

import java.util.Objects;

import university.enums.Grade;
import university.interfaces.Payable;
public class Enrollment implements Payable {
    private final int id;
    private final Student student;
    private final Course course;
    private final String semester;
    private Grade grade;
    private boolean paid;
    public Enrollment(int id, Student student, Course course, String semester) {
        if (student == null) {
            throw new IllegalArgumentException("Зарахування повинно мати студента");
        }
        if (course == null) {
            throw new IllegalArgumentException("Зарахування повинно мати курс");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Семестр не може бути порожнім");
        }
        this.id = id;
        this.student = student;
        this.course = course;
        this.semester = semester.trim();
        this.grade = Grade.NA;
        this.paid = false;
    }
    public int getId() {
        return id;
    }
    public Student getStudent() {
        return student;
    }
    public Course getCourse() {
        return course;
    }
    public String getSemester() {
        return semester;
    }
    public Grade getGrade() {
        return grade;
    }
    public void setGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Оцінка не може бути порожньою");
        }
        this.grade = grade;
    }
    @Override
    public boolean isPaid() {
        return paid;
    }
    @Override
    public void markPaid() {
        this.paid = true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enrollment that = (Enrollment) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Зарахування {id=" + id
                + ", студент='" + student.getFullName() + "' (id=" + student.getId() + ")"
                + ", курс='" + course.getName() + "'"
                + ", семестр='" + semester + "'"
                + ", оцінка=" + grade
                + ", оплата=" + (paid ? "так" : "ні") + "}";
    }
}
