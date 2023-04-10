import io.restassured.RestAssured;
import ru.praktikum_services.qa_scooter.models.bodies.RequestBodyForLogin;
import ru.praktikum_services.qa_scooter.models.bodies.ResponseBodyAfterLogin;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.*;

public class CreateCourierTest {
    private  Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = randomCourier();
    }
    //Курьер успешно создается
    @Test
    public void createNewCourierSuccessTest() {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok",equalTo(true));
    }

    //Проверка, что нельзя создать двух одинаковых курьеров
    @Test
    public void createDoubleCourierIncorrectTest() {
        Courier courier = randomCourier();

        // первичное создание курьера
         given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");

        // повторное создание курьера
         given()
                 .header("Content-type", "application/json")
                 .body(courier)
                 .when()
                 .post("/api/v1/courier")
                 .then()
                 .statusCode(409)
                 .assertThat().body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    // Проверка обятельности поля при создании курьера
    @Test
    public void checkRequiredFieldsCreateCourierTest (){
        Courier courierWithoutPassword = randomCourierWithoutPassword();
        given()
                .header("Content-type", "application/json")
                .body(courierWithoutPassword)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
    //Удаление курьера для очистки  данных
    @After
    public void cleanTestData(){
        //авторизация курьера для получения id курьера
        RequestBodyForLogin requestBodyForLogin = new RequestBodyForLogin(courier.getLogin(),courier.getPassword());
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
