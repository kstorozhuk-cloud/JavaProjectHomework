package university.util;

import university.entities.Enrollment;
import university.entities.Student;

public final class GPAUtils {

    private GPAUtils() {
    }
    public static double calculateGpa(Enrollment[] enrollments) {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Enrollment e : enrollments) {
            if (e.getGrade().isGraded()) {
                totalPoints += e.getGrade().getPoints() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }
    public static double averagePoints(Enrollment[] enrollments) {
        double sum = 0;
        int count = 0;
        for (Enrollment e : enrollments) {
            if (e.getGrade().isGraded()) {
                sum += e.getGrade().getPoints();
                count++;
            }
        }
        return count == 0 ? 0.0 : sum / count;
    }
    public static int countGraded(Enrollment[] enrollments) {
        int count = 0;
        for (Enrollment e : enrollments) {
            if (e.getGrade().isGraded()) {
                count++;
            }
        }
        return count;
    }
    public static void bubbleSortByName(Student[] students) {
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - 1 - i; j++) {
                if (students[j].getFullName().compareToIgnoreCase(students[j + 1].getFullName()) > 0) {
                    Student tmp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = tmp;
                }
            }
        }
    }
    public static void bubbleSortByGpaDesc(Student[] students, double[] gpa) {
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - 1 - i; j++) {
                if (gpa[j] < gpa[j + 1]) {
                    double tmpGpa = gpa[j];
                    gpa[j] = gpa[j + 1];
                    gpa[j + 1] = tmpGpa;

                    Student tmpStudent = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = tmpStudent;
                }
            }
        }
    }
}
