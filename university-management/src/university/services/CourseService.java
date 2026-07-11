package university.services;

import java.util.Arrays;

import university.entities.Course;
import university.entities.Teacher;

public class CourseService {

    private Course[] courses = new Course[10];
    private int size = 0;
    private int nextId = 1;
    public Course add(String name, int credits, Teacher teacher) {
        Course course = new Course(nextId, name, credits, teacher);
        ensureCapacity();
        courses[size++] = course;
        nextId++;
        return course;
    }
    public Course[] getAll() {
        return Arrays.copyOf(courses, size);
    }
    public Course findById(int id) {
        for (int i = 0; i < size; i++) {
            if (courses[i].getId() == id) {
                return courses[i];
            }
        }
        throw new IllegalArgumentException("Курс з id=" + id + " не знайдено");
    }
    public void delete(int id) {
        for (int i = 0; i < size; i++) {
            if (courses[i].getId() == id) {
                for (int j = i; j < size - 1; j++) {
                    courses[j] = courses[j + 1];
                }
                courses[--size] = null;
                return;
            }
        }
        throw new IllegalArgumentException("Курс з id=" + id + " не знайдено");
    }
    public Course[] filterByTeacher(int teacherId) {
        Course[] result = new Course[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (courses[i].getTeacher().getId() == teacherId) {
                result[count++] = courses[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Course[] filterByCredits(int credits) {
        Course[] result = new Course[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (courses[i].getCredits() == credits) {
                result[count++] = courses[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    private void ensureCapacity() {
        if (size == courses.length) {
            courses = Arrays.copyOf(courses, size * 2);
        }
    }
}
