import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterLogin;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    private Courier courier;
    private RequestBodyForLogin requestBodyForLogin;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        // создание курьера
        courier = randomCourier();
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }
    @Test
    @DisplayName("Проверка успешной авторизации")
    public void successLoginCourierTest (){
        requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
        given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    // Проверка обязательности поля при авторизации
    @Test
    @DisplayName("Проверка авторизации без обязательного поля")
    public void checkRequiredFieldsLoginCourierTest (){
        requestBodyForLogin = new RequestBodyForLogin(courier.getPassword());
        given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Проверка авторизации под несуществующим пользователем")
    public void incorrectLoginCourierTest (){
        Courier othercourier = randomCourier();
        requestBodyForLogin = new RequestBodyForLogin(othercourier.getLogin(), courier.getPassword());
        given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message",equalTo("Учетная запись не найдена"));
    }

    @After
    public void cleanTestData(){
        //авторизация курьера для получения id курьера
        requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
        ResponseBodyAfterLogin responseBodyAfterLogin = given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .body().as(ResponseBodyAfterLogin.class);

        //получение id курьера
        String courierId = responseBodyAfterLogin.getId();

        //удаление курьера
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/"+ courierId);
    }
}
