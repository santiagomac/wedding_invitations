package com.santiagomac.invitation.infrastructure.entry_points;

import com.santiagomac.invitation.application.dto.BulkCreateInvitationRequest;
import com.santiagomac.invitation.application.dto.CreateInvitationRequest;
import com.santiagomac.invitation.application.dto.CreateInvitationResponse;
import com.santiagomac.invitation.application.dto.InvitationAdminResponse;
import com.santiagomac.invitation.application.dto.InvitationStatsResponse;
import com.santiagomac.invitation.application.usecases.CreateInvitationUseCase;
import com.santiagomac.invitation.application.usecases.ListInvitationsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/invitations")
@RequiredArgsConstructor
public class InvitationAdminController {

    private final CreateInvitationUseCase createInvitationUseCase;
    private final ListInvitationsUseCase listInvitationsUseCase;

    @PostMapping
    public ResponseEntity<CreateInvitationResponse> create(@RequestBody CreateInvitationRequest request) {
        return ResponseEntity.ok(this.createInvitationUseCase.create(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CreateInvitationResponse>> createBulk(@RequestBody BulkCreateInvitationRequest request) {
        return ResponseEntity.ok(this.createInvitationUseCase.createBulk(request));
    }

    @GetMapping
    public ResponseEntity<List<InvitationAdminResponse>> listAll() {
        return ResponseEntity.ok(this.listInvitationsUseCase.listAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<InvitationStatsResponse> stats() {
        return ResponseEntity.ok(this.listInvitationsUseCase.getStats());
    }
}
