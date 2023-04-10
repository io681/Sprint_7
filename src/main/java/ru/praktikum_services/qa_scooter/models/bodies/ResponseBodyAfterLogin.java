package ru.praktikum_services.qa_scooter.models.bodies;

public class ResponseBodyAfterLogin {
    private String id;

    public ResponseBodyAfterLogin(String id) {
        this.id = id;
    }

    public ResponseBodyAfterLogin() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
