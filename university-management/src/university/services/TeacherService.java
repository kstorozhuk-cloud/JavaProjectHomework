package university.services;

import java.util.Arrays;

import university.entities.Teacher;
import university.enums.TeacherPosition;
public class TeacherService {

    private Teacher[] teachers = new Teacher[10];
    private int size = 0;
    private int nextId = 1;
    public Teacher add(String fullName, String email, TeacherPosition position) {
        Teacher teacher = new Teacher(nextId, fullName, email, position);
        ensureCapacity();
        teachers[size++] = teacher;
        nextId++;
        return teacher;
    }
    public Teacher[] getAll() {
        return Arrays.copyOf(teachers, size);
    }
    public Teacher findById(int id) {
        for (int i = 0; i < size; i++) {
            if (teachers[i].getId() == id) {
                return teachers[i];
            }
        }
        throw new IllegalArgumentException("Викладача з id=" + id + " не знайдено");
    }
    public void delete(int id) {
        for (int i = 0; i < size; i++) {
            if (teachers[i].getId() == id) {
                for (int j = i; j < size - 1; j++) {
                    teachers[j] = teachers[j + 1];
                }
                teachers[--size] = null;
                return;
            }
        }
        throw new IllegalArgumentException("Викладача з id=" + id + " не знайдено");
    }
    private void ensureCapacity() {
        if (size == teachers.length) {
            teachers = Arrays.copyOf(teachers, size * 2);
        }
    }
}
