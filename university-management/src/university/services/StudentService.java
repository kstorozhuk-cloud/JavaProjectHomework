package university.services;

import java.util.Arrays;

import university.entities.Student;
import university.enums.StudentStatus;
public class StudentService {

    private Student[] students = new Student[10];
    private int size = 0;
    private int nextId = 1;
    public Student add(String fullName, String email, int yearOfStudy) {
        Student student = new Student(nextId, fullName, email, yearOfStudy);
        ensureCapacity();
        students[size++] = student;
        nextId++;
        return student;
    }
    public Student[] getAll() {
        return Arrays.copyOf(students, size);
    }
    public Student findById(int id) {
        for (int i = 0; i < size; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        throw new IllegalArgumentException("Студента з id=" + id + " не знайдено");
    }
    public void delete(int id) {
        for (int i = 0; i < size; i++) {
            if (students[i].getId() == id) {
                for (int j = i; j < size - 1; j++) {
                    students[j] = students[j + 1];
                }
                students[--size] = null;
                return;
            }
        }
        throw new IllegalArgumentException("Студента з id=" + id + " не знайдено");
    }
    public Student[] filterByStatus(StudentStatus status) {
        Student[] result = new Student[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (students[i].getStatus() == status) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Student[] filterByYear(int year) {
        Student[] result = new Student[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (students[i].getYearOfStudy() == year) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Student[] searchByNameOrEmail(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Пошуковий запит не може бути порожнім");
        }
        String q = query.trim().toLowerCase();
        Student[] result = new Student[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (students[i].getFullName().toLowerCase().contains(q)
                    || students[i].getEmail().toLowerCase().contains(q)) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    private void ensureCapacity() {
        if (size == students.length) {
            students = Arrays.copyOf(students, size * 2);
        }
    }
}
