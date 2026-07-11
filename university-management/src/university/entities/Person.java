package university.entities;

import java.util.Objects;

public abstract class Person {
    private final int id;
    private String fullName;
    private String email;
    protected Person(int id, String fullName, String email) {
        this.id = id;
        setFullName(fullName);
        setEmail(email);
    }
    public int getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        this.fullName = fullName.trim();
    }
    public void setEmail(String email) {
        String value = email == null ? "" : email.trim();
        if (!value.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("Неправильний формат email: '" + email + "'");
        }
        this.email = value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
    @Override
    public String toString() {
        return "id=" + id + ", ПІБ='" + fullName + "', email='" + email + "'";
    }
}
