package ru.praktikum_services.qa_scooter.models.bodies.listOrders;

import ru.praktikum_services.qa_scooter.models.entities.OrderEntity;

public class Orders extends OrderEntity {
    private String track;
    private String createdAt;
    private String updatedAt;
    private String status;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
