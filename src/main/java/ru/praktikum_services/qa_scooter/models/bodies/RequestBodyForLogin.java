package ru.praktikum_services.qa_scooter.models.bodies;

public class RequestBodyForLogin {
    private String login;
    private String password;

    public RequestBodyForLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RequestBodyForLogin(String password) {
        this.password = password;
    }
    public RequestBodyForLogin() {
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
