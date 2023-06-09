package ru.praktikum_services.qa_scooter.api;

import io.restassured.response.ValidatableResponse;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import ru.praktikum_services.qa_scooter.models.entities.Courier;

import static io.restassured.RestAssured.given;

public class CourierApi extends BaseApi {
    private static final String PATH_COURIER_API = "api/v1/courier/";
    private Courier courier;
    private RequestBodyForLogin requestBodyForLogin;

    //конструкторы
    public CourierApi() {
        super();
    }

    public RequestBodyForLogin getRequestBodyForLogin() {
        return requestBodyForLogin;
    }
    //создание курьера
    public ValidatableResponse createCourier (Courier courier) {
        this.courier = courier;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH_COURIER_API)
                .then();
    }
    // удаление курьера
    public ValidatableResponse deleteCourier (int courierId) {
        return given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete(PATH_COURIER_API + String.valueOf(courierId))
                .then();
    }
    public ValidatableResponse deleteCourier () {
        return given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete(PATH_COURIER_API)
                .then();
    }
    //авторизация курьера
    public ValidatableResponse loginCourier (String login, String password) {
        requestBodyForLogin = new RequestBodyForLogin(login,password);
        return given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post(PATH_COURIER_API + "login")
                .then();
    }
    public ValidatableResponse loginCourier (String password) {
        requestBodyForLogin = new RequestBodyForLogin(password);
        return given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post(PATH_COURIER_API + "login")
                .then();
    }
}
