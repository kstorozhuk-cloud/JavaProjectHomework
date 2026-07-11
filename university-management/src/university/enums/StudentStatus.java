package university.enums;
public enum StudentStatus {
    ACTIVE("Навчається"),
    ON_LEAVE("Академічна відпустка"),
    EXPELLED("Відрахований"),
    GRADUATED("Випускник");

    private final String title;
    StudentStatus(String title) {
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
