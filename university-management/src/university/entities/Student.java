package university.entities;

import university.enums.StudentStatus;
public class Student extends Person {
    public static final int MIN_YEAR = 1;
    public static final int MAX_YEAR = 6;
    private int yearOfStudy;
    private StudentStatus status;
    public Student(int id, String fullName, String email, int yearOfStudy) {
        super(id, fullName, email);
        setYearOfStudy(yearOfStudy);
        this.status = StudentStatus.ACTIVE;
    }
    public int getYearOfStudy() {
        return yearOfStudy;
    }
    public StudentStatus getStatus() {
        return status;
    }
    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy < MIN_YEAR || yearOfStudy > MAX_YEAR) {
            throw new IllegalArgumentException(
                    "Рік навчання має бути від " + MIN_YEAR + " до " + MAX_YEAR + ", а введено: " + yearOfStudy);
        }
        this.yearOfStudy = yearOfStudy;
    }
    public void setStatus(StudentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Статус студента не може бути порожнім");
        }
        this.status = status;
    }
    @Override
    public String toString() {
        return "Студент {" + super.toString() + ", рік навчання=" + yearOfStudy + ", статус=" + status + "}";
    }
}
