package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.RoundRequest;
import com.placement.dto.RoundResultRequest;
import com.placement.service.PlacementRoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
public class PlacementRoundController {

    private final PlacementRoundService roundService;

    @PostMapping("/drive/{driveId}")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> createRound(@PathVariable Long driveId, @Valid @RequestBody RoundRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Round created", roundService.createRound(driveId, request)));
    }

    @GetMapping("/drive/{driveId}")
    public ResponseEntity<ApiResponse> getDriveRounds(@PathVariable Long driveId) {
        return ResponseEntity.ok(new ApiResponse(true, "Rounds fetched", roundService.getDriveRounds(driveId)));
    }

    @PutMapping("/{roundId}/result")
    @PreAuthorize("hasAuthority('COORDINATOR')")
    public ResponseEntity<ApiResponse> updateRoundResult(@PathVariable Long roundId, @Valid @RequestBody RoundResultRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Result updated", roundService.updateRoundResult(roundId, request)));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApiResponse> getApplicationResults(@PathVariable Long applicationId) {
        return ResponseEntity.ok(new ApiResponse(true, "Results fetched", roundService.getApplicationResults(applicationId)));
    }
}
