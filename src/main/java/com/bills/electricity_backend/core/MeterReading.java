package com.bills.electricity_backend.core;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings")
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double previousReading;

    @Column(nullable = false)
    private Double currentReading;

    @Column(nullable = false)
    private Double unitsConsumed;

    @Column(nullable = false)
    private String status; // "PENDING" or "APPROVED"

    @Column(nullable = false)
    private LocalDateTime readingDate;

    public MeterReading() {
        this.readingDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public MeterReading(Long userId, Double previousReading, Double currentReading) {
        this.userId = userId;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
        this.unitsConsumed = currentReading - previousReading;
        this.status = "PENDING";
        this.readingDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getPreviousReading() { return previousReading; }
    public void setPreviousReading(Double previousReading) { this.previousReading = previousReading; }

    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double currentReading) { this.currentReading = currentReading; }

    public Double getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Double unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDateTime readingDate) { this.readingDate = readingDate; }
}