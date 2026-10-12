package com.bills.electricity_backend.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/readings")
public class MeterReadingController {

    private final MeterReadingRepository meterReadingRepository;

    public MeterReadingController(MeterReadingRepository meterReadingRepository) {
        this.meterReadingRepository = meterReadingRepository;
    }

    // 1. Submit a new meter reading (Auto-calculates units consumed)
    @PostMapping("/submit")
    public ResponseEntity<?> submitReading(@RequestBody MeterReading reading) {
        if (reading.getCurrentReading() < reading.getPreviousReading()) {
            return ResponseEntity.badRequest().body("Error: Current reading cannot be less than previous reading!");
        }

        // Calculate consumed units
        double consumed = reading.getCurrentReading() - reading.getPreviousReading();
        reading.setUnitsConsumed(consumed);
        reading.setStatus("PENDING");

        MeterReading savedReading = meterReadingRepository.save(reading);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Meter reading submitted successfully!");
        response.put("readingId", savedReading.getId());
        response.put("unitsConsumed", consumed);
        response.put("status", savedReading.getStatus());

        return ResponseEntity.ok(response);
    }

    // 2. Get all pending readings for Admin review
    @GetMapping("/pending")
    public ResponseEntity<List<MeterReading>> getPendingReadings() {
        List<MeterReading> pendingList = meterReadingRepository.findByStatus("PENDING");
        return ResponseEntity.ok(pendingList);
    }

    // 3. Approve a reading and calculate bill total (Fixed tariff rate e.g., 25.5 per unit)
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveReading(@PathVariable Long id) {
        return meterReadingRepository.findById(id)
                .map(reading -> {
                    reading.setStatus("APPROVED");
                    meterReadingRepository.save(reading);

                    double tariffRate = 25.5; // Cost per unit
                    double totalBillAmount = reading.getUnitsConsumed() * tariffRate;

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Reading approved successfully!");
                    response.put("readingId", reading.getId());
                    response.put("userId", reading.getUserId());
                    response.put("unitsConsumed", reading.getUnitsConsumed());
                    response.put("totalBillAmount", totalBillAmount);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}