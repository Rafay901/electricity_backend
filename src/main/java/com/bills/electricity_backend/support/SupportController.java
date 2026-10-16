package com.bills.electricity_backend.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    private final ComplaintRepository complaintRepository;
    private final NotificationRepository notificationRepository;

    public SupportController(ComplaintRepository complaintRepository, NotificationRepository notificationRepository) {
        this.complaintRepository = complaintRepository;
        this.notificationRepository = notificationRepository;
    }

    // 1. Customer submits a complaint ticket
    @PostMapping("/complaints")
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        Complaint savedComplaint = complaintRepository.save(complaint);
        return ResponseEntity.ok(savedComplaint);
    }

    // 2. Admin views all submitted customer complaints
    @GetMapping("/complaints/all")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintRepository.findAll());
    }

    // 3. Admin updates complaint status (e.g., OPEN -> RESOLVED)
    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        return complaintRepository.findById(id)
                .map(complaint -> {
                    complaint.setStatus(statusUpdate.get("status"));
                    complaintRepository.save(complaint);
                    return ResponseEntity.ok("Complaint status updated successfully.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Customer fetches their notifications/alerts
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationRepository.findByUserId(userId));
    }
}