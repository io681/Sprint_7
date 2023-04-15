package ru.praktikum_services.qa_scooter.models.entities;

import java.util.List;

import static ru.praktikum_services.qa_scooter.utils.DateGenerator.getCurrentDate;
import static ru.praktikum_services.qa_scooter.utils.RandomUtil.*;

public class OrderGenerator {
    public static OrderEntity randomOrder(List<String> color) {
        OrderEntity orderEntity =  new OrderEntity();
        orderEntity.setFirstName(randomString(5));
        orderEntity.setLastName (randomString(8));
        orderEntity.setAddress(randomString(15));
        orderEntity.setMetroStation(randomString(10));
        orderEntity.setPhone(randomString(10));
        orderEntity.setRentTime(randomNumber());
        orderEntity.setDeliveryDate(getCurrentDate());
        orderEntity.setComment(randomString(10));
        orderEntity.setColor(color);
        return orderEntity;
    }
}