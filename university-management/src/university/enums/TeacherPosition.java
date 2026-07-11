package university.enums;
public enum TeacherPosition {
    ASSISTANT("Асистент"),
    LECTURER("Викладач"),
    DOCENT("Доцент"),
    PROFESSOR("Професор");

    private final String title;
    TeacherPosition(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    @Override
    public String toString() {
        return name() + " (" + title + ")";
    }
}
