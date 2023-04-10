import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterLogin;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {
    private Courier courier;
    private RequestBodyForLogin requestBodyForLogin;
    private ResponseBodyAfterLogin responseBodyAfterLogin;
    private String courierId;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        //создание курьера
        courier = randomCourier();
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok",equalTo(true));

        //авторизация курьера для получения id курьера
        requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
        responseBodyAfterLogin = given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .body().as(ResponseBodyAfterLogin.class);
        //получение id курьера
        courierId = responseBodyAfterLogin.getId();
    }
    @Test
    @DisplayName("Успешное создание курьера")

    public void DeleteSuccessCourierTest() {
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/"+ courierId)
                .then()
                .statusCode(200)
                .assertThat().body("ok",equalTo(true));
    }
    @Test
    @DisplayName("Некорректное удаление без id курьера")
    @Description("Тест будет падать, т.к. сервер возвращает другую ошибку 404 и  другой message (не по спецификации)")
    public void DeleteCourierWithoutCourierIdTest() {
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для удаления курьера"));
    }
    @Test
    @DisplayName("Некорректное удаление с несуществующим id курьера")
    public void DeleteCourierWithFakeCourierIdTest() {
        String fakeCourierId = "999999";
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/" + fakeCourierId)
                .then()
                .statusCode(404)
                .assertThat().body("message",equalTo("Курьера с таким id нет."));
    }
    @After
    public void cleanTestData(){
        //авторизация курьера для получения id курьера
        requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
        responseBodyAfterLogin = given()
                .header("Content-type", "application/json")
                .body(requestBodyForLogin)
                .when()
                .post("/api/v1/courier/login")
                .body().as(ResponseBodyAfterLogin.class);

        //получение id курьера
        courierId = responseBodyAfterLogin.getId();

        //удаление курьера
        given()
                .header("Content-type", "application/json")
                .body("{}")
                .when()
                .delete("/api/v1/courier/"+ courierId);
    }
}
