package ru.praktikum_services.qa_scooter.api;
import io.restassured.RestAssured;

public class BaseApi {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    public BaseApi() {
    RestAssured.baseURI = BASE_URI;
}
}
