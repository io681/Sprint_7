import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum_services.qa_scooter.api.CourierApi;
import ru.praktikum_services.qa_scooter.models.entities.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.praktikum_services.qa_scooter.models.entities.CourierGenerator.randomCourier;

@DisplayName("Логин курьера")
public class LoginCourierTest {
    private Courier courier;
    private CourierApi courierApi;
    @Before
    public void setUp() {
        courierApi = new CourierApi();
        courierApi.createCourier();
    }
    @Test
    @DisplayName("Проверка успешной авторизации")
    public void successLoginCourierTest (){
        courier = courierApi.getCourier();
        ValidatableResponse response = courierApi.loginCourier(courier.getLogin(),courier.getPassword());
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertNotNull("Пустой id в респонзе после авторизации", response.extract().path("id"));
    }
    @Test
    @DisplayName("Проверка авторизации без обязательного поля")
    public void checkRequiredFieldsLoginCourierTest (){
        courier = courierApi.getCourier();
        ValidatableResponse response = courierApi.loginCourier(courier.getPassword());
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Недостаточно данных для входа", response.extract().path("message"));
    }
    @Test
    @DisplayName("Проверка авторизации под несуществующим пользователем")
    public void incorrectLoginCourierTest (){
        Courier othercourier = randomCourier();
        ValidatableResponse response = courierApi.loginCourier(othercourier.getLogin(),othercourier.getPassword());
        assertEquals("Статус кода неверный",
                HttpStatus.SC_NOT_FOUND, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Учетная запись не найдена", response.extract().path("message"));
    }
    @After
    public void cleanTestData(){
        //авторизация курьера и получение id курьера
        courier = courierApi.getCourier();
        int courierId = courierApi
                .loginCourier(courier.getLogin(),courier.getPassword())
                .extract().path("id");

        //удаление курьера
        courierApi.deleteCourier(courierId);
    }
}
