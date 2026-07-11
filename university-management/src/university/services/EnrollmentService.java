package university.services;

import java.util.Arrays;

import university.entities.Course;
import university.entities.Enrollment;
import university.entities.Student;
public class EnrollmentService {

    private Enrollment[] enrollments = new Enrollment[10];
    private int size = 0;
    private int nextId = 1;
    public Enrollment enroll(Student student, Course course, String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Семестр не може бути порожнім");
        }
        String sem = semester.trim();
        for (int i = 0; i < size; i++) {
            Enrollment e = enrollments[i];
            if (e.getStudent().getId() == student.getId()
                    && e.getCourse().getId() == course.getId()
                    && e.getSemester().equalsIgnoreCase(sem)) {
                throw new IllegalArgumentException("Студент '" + student.getFullName()
                        + "' вже зарахований на курс '" + course.getName() + "' у семестрі " + sem);
            }
        }
        Enrollment enrollment = new Enrollment(nextId, student, course, sem);
        ensureCapacity();
        enrollments[size++] = enrollment;
        nextId++;
        return enrollment;
    }
    public Enrollment[] getAll() {
        return Arrays.copyOf(enrollments, size);
    }
    public Enrollment findById(int id) {
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getId() == id) {
                return enrollments[i];
            }
        }
        throw new IllegalArgumentException("Зарахування з id=" + id + " не знайдено");
    }
    public Enrollment[] getByStudent(int studentId) {
        Enrollment[] result = new Enrollment[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getStudent().getId() == studentId) {
                result[count++] = enrollments[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Enrollment[] getByCourse(int courseId) {
        Enrollment[] result = new Enrollment[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getCourse().getId() == courseId) {
                result[count++] = enrollments[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Enrollment[] getBySemester(String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Семестр не може бути порожнім");
        }
        String sem = semester.trim();
        Enrollment[] result = new Enrollment[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getSemester().equalsIgnoreCase(sem)) {
                result[count++] = enrollments[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public Enrollment[] getUnpaid() {
        Enrollment[] result = new Enrollment[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (!enrollments[i].isPaid()) {
                result[count++] = enrollments[i];
            }
        }
        return Arrays.copyOf(result, count);
    }
    public int deleteByStudent(int studentId) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getStudent().getId() != studentId) {
                enrollments[newSize++] = enrollments[i];
            }
        }
        int removed = size - newSize;
        for (int i = newSize; i < size; i++) {
            enrollments[i] = null;
        }
        size = newSize;
        return removed;
    }
    public int deleteByCourse(int courseId) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (enrollments[i].getCourse().getId() != courseId) {
                enrollments[newSize++] = enrollments[i];
            }
        }
        int removed = size - newSize;
        for (int i = newSize; i < size; i++) {
            enrollments[i] = null;
        }
        size = newSize;
        return removed;
    }

    private void ensureCapacity() {
        if (size == enrollments.length) {
            enrollments = Arrays.copyOf(enrollments, size * 2);
        }
    }
}
