package university;

import java.util.Arrays;
import java.util.Scanner;

import university.entities.Course;
import university.entities.Enrollment;
import university.entities.Student;
import university.entities.Teacher;
import university.enums.Grade;
import university.enums.StudentStatus;
import university.enums.TeacherPosition;
import university.services.CourseService;
import university.services.EnrollmentService;
import university.services.StudentService;
import university.services.TeacherService;
import university.util.GPAUtils;
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final StudentService studentService = new StudentService();
    private static final TeacherService teacherService = new TeacherService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    СИСТЕМА УПРАВЛІННЯ УНІВЕРСИТЕТОМ (CLI)");
        System.out.println("==============================================");

        if (readYesNo("Завантажити демонстраційні дані? (y/n): ")) {
            seedDemoData();
            System.out.println("Демо-дані завантажено: 5 студентів, 3 викладачі, 4 курси, 9 зарахувань.");
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        studentsMenu();
                        break;
                    case "2":
                        teachersMenu();
                        break;
                    case "3":
                        coursesMenu();
                        break;
                    case "4":
                        enrollmentsMenu();
                        break;
                    case "5":
                        reportsMenu();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'. Спробуйте ще раз.");
                }
            } catch (RuntimeException e) {
                System.out.println("Неочікувана помилка: " + e.getMessage());
            }
        }
        System.out.println("Роботу завершено. До побачення!");
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("============ ГОЛОВНЕ МЕНЮ ============");
        System.out.println("1. Студенти");
        System.out.println("2. Викладачі");
        System.out.println("3. Курси");
        System.out.println("4. Зарахування");
        System.out.println("5. Звіти / Пошук");
        System.out.println("0. Вихід");
        System.out.println("======================================");
    }
    private static void studentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- СТУДЕНТИ ---");
            System.out.println("1. Додати студента");
            System.out.println("2. Показати всіх студентів");
            System.out.println("3. Оновити дані студента");
            System.out.println("4. Видалити студента");
            System.out.println("5. Змінити статус студента");
            System.out.println("6. Фільтри та сортування");
            System.out.println("0. Назад");
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        addStudent();
                        break;
                    case "2":
                        printStudents(studentService.getAll());
                        break;
                    case "3":
                        updateStudent();
                        break;
                    case "4":
                        deleteStudent();
                        break;
                    case "5":
                        changeStudentStatus();
                        break;
                    case "6":
                        studentFiltersMenu();
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    private static void addStudent() {
        String name = readLine("ПІБ студента: ");
        String email = readLine("Email: ");
        int year = readInt("Рік навчання (" + Student.MIN_YEAR + "-" + Student.MAX_YEAR + "): ");
        Student student = studentService.add(name, email, year);
        System.out.println("Додано: " + student);
    }
    private static void updateStudent() {
        Student student = studentService.findById(readInt("ID студента: "));
        System.out.println("Поточні дані: " + student);
        System.out.println("(Натисніть Enter, щоб залишити поле без змін)");
        String name = readLine("Нове ПІБ: ");
        if (!name.isEmpty()) {
            student.setFullName(name);
        }
        String email = readLine("Новий email: ");
        if (!email.isEmpty()) {
            student.setEmail(email);
        }
        String yearStr = readLine("Новий рік навчання (" + Student.MIN_YEAR + "-" + Student.MAX_YEAR + "): ");
        if (!yearStr.isEmpty()) {
            student.setYearOfStudy(parseInt(yearStr));
        }
        System.out.println("Оновлено: " + student);
    }
    private static void deleteStudent() {
        int id = readInt("ID студента: ");
        Student student = studentService.findById(id);
        int removedEnrollments = enrollmentService.deleteByStudent(id);
        studentService.delete(id);
        System.out.println("Видалено студента '" + student.getFullName()
                + "' та його зарахувань: " + removedEnrollments + ".");
    }
    private static void changeStudentStatus() {
        Student student = studentService.findById(readInt("ID студента: "));
        System.out.println("Поточний статус: " + student.getStatus());
        StudentStatus status = chooseFrom("Новий статус (номер): ", StudentStatus.values());
        student.setStatus(status);
        System.out.println("Оновлено: " + student);
    }
    private static void studentFiltersMenu() {
        System.out.println("1. За статусом");
        System.out.println("2. За роком навчання");
        System.out.println("3. Усі, відсортовані за ПІБ (bubble sort)");
        String choice = readLine("Ваш вибір: ");
        switch (choice) {
            case "1":
                StudentStatus status = chooseFrom("Статус (номер): ", StudentStatus.values());
                printStudents(studentService.filterByStatus(status));
                break;
            case "2":
                printStudents(studentService.filterByYear(readInt("Рік навчання: ")));
                break;
            case "3":
                Student[] sorted = studentService.getAll();
                GPAUtils.bubbleSortByName(sorted);
                printStudents(sorted);
                break;
            default:
                System.out.println("Невідомий пункт меню: '" + choice + "'.");
        }
    }
    private static void printStudents(Student[] students) {
        if (students.length == 0) {
            System.out.println("Записів немає.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("Всього: " + students.length);
    }
    private static void teachersMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- ВИКЛАДАЧІ ---");
            System.out.println("1. Додати викладача");
            System.out.println("2. Показати всіх викладачів");
            System.out.println("3. Оновити дані викладача");
            System.out.println("4. Видалити викладача");
            System.out.println("0. Назад");
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        addTeacher();
                        break;
                    case "2":
                        printTeachers(teacherService.getAll());
                        break;
                    case "3":
                        updateTeacher();
                        break;
                    case "4":
                        deleteTeacher();
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    private static void addTeacher() {
        String name = readLine("ПІБ викладача: ");
        String email = readLine("Email: ");
        TeacherPosition position = chooseFrom("Посада (номер): ", TeacherPosition.values());
        System.out.println("Додано: " + teacherService.add(name, email, position));
    }
    private static void updateTeacher() {
        Teacher teacher = teacherService.findById(readInt("ID викладача: "));
        System.out.println("Поточні дані: " + teacher);
        System.out.println("(Натисніть Enter, щоб залишити поле без змін)");
        String name = readLine("Нове ПІБ: ");
        if (!name.isEmpty()) {
            teacher.setFullName(name);
        }
        String email = readLine("Новий email: ");
        if (!email.isEmpty()) {
            teacher.setEmail(email);
        }
        if (readYesNo("Змінити посаду? (y/n): ")) {
            teacher.setPosition(chooseFrom("Нова посада (номер): ", TeacherPosition.values()));
        }
        System.out.println("Оновлено: " + teacher);
    }
    private static void deleteTeacher() {
        int id = readInt("ID викладача: ");
        Teacher teacher = teacherService.findById(id);
        Course[] courses = courseService.filterByTeacher(id);
        if (courses.length > 0) {
            throw new IllegalArgumentException("Неможливо видалити викладача '" + teacher.getFullName()
                    + "': за ним закріплено курсів — " + courses.length
                    + ". Спочатку змініть викладача у цих курсах або видаліть їх.");
        }
        teacherService.delete(id);
        System.out.println("Викладача '" + teacher.getFullName() + "' видалено.");
    }
    private static void printTeachers(Teacher[] teachers) {
        if (teachers.length == 0) {
            System.out.println("Записів немає.");
            return;
        }
        for (Teacher t : teachers) {
            System.out.println(t);
        }
        System.out.println("Всього: " + teachers.length);
    }
    private static void coursesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- КУРСИ ---");
            System.out.println("1. Додати курс");
            System.out.println("2. Показати всі курси");
            System.out.println("3. Оновити дані курсу");
            System.out.println("4. Видалити курс");
            System.out.println("5. Фільтри (за викладачем / кредитами)");
            System.out.println("0. Назад");
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        addCourse();
                        break;
                    case "2":
                        printCourses(courseService.getAll());
                        break;
                    case "3":
                        updateCourse();
                        break;
                    case "4":
                        deleteCourse();
                        break;
                    case "5":
                        courseFiltersMenu();
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    private static void addCourse() {
        if (teacherService.getAll().length == 0) {
            throw new IllegalArgumentException("Спочатку додайте хоча б одного викладача (меню «Викладачі»)");
        }
        String name = readLine("Назва курсу: ");
        int credits = readInt("Кількість кредитів (" + Course.MIN_CREDITS + "-" + Course.MAX_CREDITS + "): ");
        printTeachers(teacherService.getAll());
        Teacher teacher = teacherService.findById(readInt("ID викладача: "));
        System.out.println("Додано: " + courseService.add(name, credits, teacher));
    }
    private static void updateCourse() {
        Course course = courseService.findById(readInt("ID курсу: "));
        System.out.println("Поточні дані: " + course);
        System.out.println("(Натисніть Enter, щоб залишити поле без змін)");
        String name = readLine("Нова назва: ");
        if (!name.isEmpty()) {
            course.setName(name);
        }
        String creditsStr = readLine("Нова кількість кредитів: ");
        if (!creditsStr.isEmpty()) {
            course.setCredits(parseInt(creditsStr));
        }
        if (readYesNo("Змінити викладача? (y/n): ")) {
            printTeachers(teacherService.getAll());
            course.setTeacher(teacherService.findById(readInt("ID нового викладача: ")));
        }
        System.out.println("Оновлено: " + course);
    }
    private static void deleteCourse() {
        int id = readInt("ID курсу: ");
        Course course = courseService.findById(id);
        int removedEnrollments = enrollmentService.deleteByCourse(id);
        courseService.delete(id);
        System.out.println("Видалено курс '" + course.getName()
                + "' та пов'язаних зарахувань: " + removedEnrollments + ".");
    }
    private static void courseFiltersMenu() {
        System.out.println("1. За викладачем");
        System.out.println("2. За кількістю кредитів");
        String choice = readLine("Ваш вибір: ");
        switch (choice) {
            case "1":
                printTeachers(teacherService.getAll());
                printCourses(courseService.filterByTeacher(readInt("ID викладача: ")));
                break;
            case "2":
                printCourses(courseService.filterByCredits(readInt("Кількість кредитів: ")));
                break;
            default:
                System.out.println("Невідомий пункт меню: '" + choice + "'.");
        }
    }
    private static void printCourses(Course[] courses) {
        if (courses.length == 0) {
            System.out.println("Записів немає.");
            return;
        }
        for (Course c : courses) {
            System.out.println(c);
        }
        System.out.println("Всього: " + courses.length);
    }
    private static void enrollmentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- ЗАРАХУВАННЯ ---");
            System.out.println("1. Зарахувати студента на курс");
            System.out.println("2. Показати всі зарахування");
            System.out.println("3. Поставити оцінку");
            System.out.println("4. Позначити оплату");
            System.out.println("5. Зарахування студента (з GPA)");
            System.out.println("6. Транскрипт студента");
            System.out.println("0. Назад");
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        enrollStudent();
                        break;
                    case "2":
                        printEnrollments(enrollmentService.getAll());
                        break;
                    case "3":
                        setGrade();
                        break;
                    case "4":
                        markPaid();
                        break;
                    case "5":
                        showStudentEnrollments();
                        break;
                    case "6":
                        printTranscript();
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    private static void enrollStudent() {
        Student student = studentService.findById(readInt("ID студента: "));
        Course course = courseService.findById(readInt("ID курсу: "));
        String semester = readLine("Семестр (наприклад, 2026-1): ");
        System.out.println("Створено: " + enrollmentService.enroll(student, course, semester));
    }
    private static void setGrade() {
        Enrollment enrollment = enrollmentService.findById(readInt("ID зарахування: "));
        System.out.println("Поточне: " + enrollment);
        Grade grade = chooseFrom("Оцінка (номер): ", Grade.values());
        enrollment.setGrade(grade);
        System.out.println("Оновлено: " + enrollment);
    }
    private static void markPaid() {
        Enrollment enrollment = enrollmentService.findById(readInt("ID зарахування: "));
        if (enrollment.isPaid()) {
            System.out.println("Це зарахування вже оплачено.");
            return;
        }
        enrollment.markPaid();
        System.out.println("Оплату зафіксовано: " + enrollment);
    }
    private static void showStudentEnrollments() {
        Student student = studentService.findById(readInt("ID студента: "));
        Enrollment[] enrollments = enrollmentService.getByStudent(student.getId());
        if (enrollments.length == 0) {
            System.out.println("У студента '" + student.getFullName() + "' немає зарахувань.");
            return;
        }
        printEnrollments(enrollments);
        System.out.printf("GPA студента: %.2f%n", GPAUtils.calculateGpa(enrollments));
    }
    private static void printTranscript() {
        Student student = studentService.findById(readInt("ID студента: "));
        Enrollment[] enrollments = enrollmentService.getByStudent(student.getId());
        System.out.println("================ ТРАНСКРИПТ ================");
        System.out.println("Студент: " + student.getFullName() + " (id=" + student.getId() + ")");
        System.out.println("Email:   " + student.getEmail());
        System.out.println("Рік навчання: " + student.getYearOfStudy() + ", статус: " + student.getStatus());
        System.out.println("--------------------------------------------");
        if (enrollments.length == 0) {
            System.out.println("Зарахувань немає.");
        } else {
            for (String semester : distinctSemesters(enrollments)) {
                System.out.println("Семестр " + semester + ":");
                for (Enrollment e : enrollments) {
                    if (e.getSemester().equalsIgnoreCase(semester)) {
                        System.out.printf("  %-32s %2d кред. | оцінка: %-2s | оплата: %s%n",
                                e.getCourse().getName(), e.getCourse().getCredits(),
                                e.getGrade(), e.isPaid() ? "так" : "НІ");
                    }
                }
            }
            System.out.println("--------------------------------------------");
            System.out.printf("GPA (зважений за кредитами): %.2f%n", GPAUtils.calculateGpa(enrollments));
        }
        System.out.println("============================================");
    }
    private static String[] distinctSemesters(Enrollment[] enrollments) {
        String[] semesters = new String[enrollments.length];
        int count = 0;
        for (Enrollment e : enrollments) {
            boolean exists = false;
            for (int i = 0; i < count; i++) {
                if (semesters[i].equalsIgnoreCase(e.getSemester())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                semesters[count++] = e.getSemester();
            }
        }
        return Arrays.copyOf(semesters, count);
    }
    private static void printEnrollments(Enrollment[] enrollments) {
        if (enrollments.length == 0) {
            System.out.println("Записів немає.");
            return;
        }
        for (Enrollment e : enrollments) {
            System.out.println(e);
        }
        System.out.println("Всього: " + enrollments.length);
    }
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- ЗВІТИ / ПОШУК ---");
            System.out.println("1. Пошук студентів (за частиною ПІБ або email)");
            System.out.println("2. Неоплачені зарахування");
            System.out.println("3. Середній бал по курсу");
            System.out.println("4. Середній бал за семестр");
            System.out.println("5. Топ-N студентів за GPA");
            System.out.println("0. Назад");
            String choice = readLine("Ваш вибір: ");
            try {
                switch (choice) {
                    case "1":
                        printStudents(studentService.searchByNameOrEmail(readLine("Частина ПІБ або email: ")));
                        break;
                    case "2":
                        showUnpaid();
                        break;
                    case "3":
                        averageGpaByCourse();
                        break;
                    case "4":
                        averageGpaBySemester();
                        break;
                    case "5":
                        topStudentsByGpa();
                        break;
                    case "0":
                        back = true;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню: '" + choice + "'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    private static void showUnpaid() {
        Enrollment[] unpaid = enrollmentService.getUnpaid();
        if (unpaid.length == 0) {
            System.out.println("Неоплачених зарахувань немає.");
            return;
        }
        System.out.println("Неоплачені зарахування:");
        for (Enrollment e : unpaid) {
            System.out.println("  " + e.getStudent().getFullName()
                    + " — курс '" + e.getCourse().getName() + "' (семестр " + e.getSemester() + ")");
        }
        System.out.println("Всього: " + unpaid.length);
    }
    private static void averageGpaByCourse() {
        Course course = courseService.findById(readInt("ID курсу: "));
        Enrollment[] enrollments = enrollmentService.getByCourse(course.getId());
        int graded = GPAUtils.countGraded(enrollments);
        if (graded == 0) {
            System.out.println("По курсу '" + course.getName() + "' ще немає виставлених оцінок.");
            return;
        }
        System.out.printf("Середній бал по курсу '%s': %.2f (оцінок: %d)%n",
                course.getName(), GPAUtils.averagePoints(enrollments), graded);
    }
    private static void averageGpaBySemester() {
        String semester = readLine("Семестр (наприклад, 2026-1): ");
        Enrollment[] enrollments = enrollmentService.getBySemester(semester);
        int graded = GPAUtils.countGraded(enrollments);
        if (graded == 0) {
            System.out.println("За семестр '" + semester + "' немає виставлених оцінок.");
            return;
        }
        System.out.printf("Середній бал за семестр %s: %.2f (оцінок: %d)%n",
                semester, GPAUtils.averagePoints(enrollments), graded);
    }
    private static void topStudentsByGpa() {
        int n = readInt("Скільки студентів показати (N): ");
        if (n < 1) {
            throw new IllegalArgumentException("N має бути не менше 1");
        }
        Student[] all = studentService.getAll();
        Student[] rated = new Student[all.length];
        double[] gpa = new double[all.length];
        int count = 0;
        for (Student s : all) {
            Enrollment[] enrollments = enrollmentService.getByStudent(s.getId());
            if (GPAUtils.countGraded(enrollments) > 0) {
                rated[count] = s;
                gpa[count] = GPAUtils.calculateGpa(enrollments);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Ще немає студентів з виставленими оцінками.");
            return;
        }
        rated = Arrays.copyOf(rated, count);
        gpa = Arrays.copyOf(gpa, count);
        GPAUtils.bubbleSortByGpaDesc(rated, gpa);
        int top = Math.min(n, count);
        System.out.println("ТОП-" + top + " студентів за GPA:");
        for (int i = 0; i < top; i++) {
            System.out.printf("  %d. %-32s GPA: %.2f%n", i + 1, rated[i].getFullName(), gpa[i]);
        }
    }
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }
    private static int readInt(String prompt) {
        return parseInt(readLine(prompt));
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Очікувалося ціле число, а введено: '" + value + "'");
        }
    }

    private static boolean readYesNo(String prompt) {
        return readLine(prompt).equalsIgnoreCase("y");
    }
    private static <T> T chooseFrom(String prompt, T[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
        int num = readInt(prompt);
        if (num < 1 || num > options.length) {
            throw new IllegalArgumentException("Немає варіанта з номером " + num);
        }
        return options[num - 1];
    }
    private static void seedDemoData() {
        Teacher t1 = teacherService.add("Петренко Віктор Сергійович", "v.petrenko@univ.edu.ua", TeacherPosition.PROFESSOR);
        Teacher t2 = teacherService.add("Сидоренко Олена Миколаївна", "o.sydorenko@univ.edu.ua", TeacherPosition.DOCENT);
        Teacher t3 = teacherService.add("Ковальчук Ігор Петрович", "i.kovalchuk@univ.edu.ua", TeacherPosition.ASSISTANT);
        Course ai = courseService.add("Штучний інтелект", 7, t1);
        Course ds = courseService.add("Аналіз даних", 6, t2);
        Course network = courseService.add("Комп'ютерні мережі", 5, t3);
        Course crypto = courseService.add("Криптографія", 5, t1);
        Student s1 = studentService.add("Даниленко Максим Андрійович", "m.danylenko@stud.univ.edu.ua", 4);
        Student s2 = studentService.add("Марченко Юлія Олегівна", "y.marchenko@stud.univ.edu.ua", 3);
        Student s3 = studentService.add("Козак Тарас Володимирович", "t.kozak@stud.univ.edu.ua", 2);
        Student s4 = studentService.add("Савченко Вікторія Ігорівна", "v.savchenko@stud.univ.edu.ua", 1);
        Student s5 = studentService.add("Пономаренко Андрій Віталійович", "a.ponomarenko@stud.univ.edu.ua", 2);
        s5.setStatus(StudentStatus.ON_LEAVE);
        Enrollment e1 = enrollmentService.enroll(s1, ai, "2026-1");
        e1.setGrade(Grade.A);
        e1.markPaid();
        Enrollment e2 = enrollmentService.enroll(s1, ds, "2026-1");
        e2.setGrade(Grade.B);
        e2.markPaid();
        enrollmentService.enroll(s1, network, "2026-2"); // без оцінки, не оплачено

        Enrollment e4 = enrollmentService.enroll(s2, ai, "2026-1");
        e4.setGrade(Grade.B);
        e4.markPaid();
        Enrollment e5 = enrollmentService.enroll(s2, crypto, "2026-1");
        e5.setGrade(Grade.A);

        Enrollment e6 = enrollmentService.enroll(s3, ds, "2026-1");
        e6.setGrade(Grade.C);
        e6.markPaid();
        Enrollment e7 = enrollmentService.enroll(s3, ai, "2026-2");
        e7.setGrade(Grade.B);

        Enrollment e8 = enrollmentService.enroll(s4, network, "2026-2");
        e8.markPaid();

        Enrollment e9 = enrollmentService.enroll(s5, crypto, "2026-1");
        e9.setGrade(Grade.D);
    }
}
