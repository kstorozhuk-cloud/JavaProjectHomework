package university.entities;

import java.util.Objects;
public class Course {
    public static final int MIN_CREDITS = 1;
    public static final int MAX_CREDITS = 60;
    private final int id;
    private String name;
    private int credits;
    private Teacher teacher;
    public Course(int id, String name, int credits, Teacher teacher) {
        this.id = id;
        setName(name);
        setCredits(credits);
        setTeacher(teacher);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getCredits() {
        return credits;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва курсу не може бути порожньою");
        }
        this.name = name.trim();
    }
    public void setCredits(int credits) {
        if (credits < MIN_CREDITS || credits > MAX_CREDITS) {
            throw new IllegalArgumentException(
                    "Кількість кредитів має бути від " + MIN_CREDITS + " до " + MAX_CREDITS + ", а введено: " + credits);
        }
        this.credits = credits;
    }
    public void setTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Курс повинен мати викладача");
        }
        this.teacher = teacher;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return id == course.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Курс {id=" + id + ", назва='" + name + "', кредити=" + credits
                + ", викладач='" + teacher.getFullName() + "'}";
    }
}
