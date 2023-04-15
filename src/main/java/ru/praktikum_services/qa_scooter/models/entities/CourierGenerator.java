package ru.praktikum_services.qa_scooter.models.entities;

import static ru.praktikum_services.qa_scooter.utils.RandomUtil.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        Courier courier =  new Courier();
        courier.setLogin(randomString(5));
        courier.setPassword (randomString(8));
        courier.setFirstName(randomString(10));
        return courier;
    }
    public static Courier randomCourierWithoutPassword() {
        Courier courier =  new Courier();
        courier.setLogin(randomString(8));
        courier.setFirstName(randomString(10));
        return courier;
    }
}
