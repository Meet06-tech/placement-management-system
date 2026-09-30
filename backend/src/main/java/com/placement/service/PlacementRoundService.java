package com.placement.service;

import com.placement.dto.RoundRequest;
import com.placement.dto.RoundResultRequest;
import com.placement.entity.*;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlacementRoundService {

    private final PlacementRoundRepository roundRepository;
    private final RoundResultRepository resultRepository;
    private final PlacementDriveRepository driveRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public PlacementRound createRound(Long driveId, RoundRequest request) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found"));

        // Business rule: round numbers are unique within a drive
        List<PlacementRound> existingRounds = roundRepository.findByPlacementDriveIdOrderByRoundNumber(driveId);
        short nextRoundNumber = existingRounds.isEmpty()
                ? (short) 1
                : (short) (existingRounds.get(existingRounds.size() - 1).getRoundNumber() + 1);

        PlacementRound round = PlacementRound.builder()
                .placementDrive(drive)
                .roundNumber(nextRoundNumber)
                .roundName(request.roundName())
                .roundType(request.roundType())
                .description(request.description())
                .build();

        return roundRepository.save(round);
    }

    public List<PlacementRound> getDriveRounds(Long driveId) {
        return roundRepository.findByPlacementDriveIdOrderByRoundNumber(driveId);
    }

    @Transactional
    public RoundResult updateRoundResult(Long roundId, RoundResultRequest request) {
        PlacementRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement round not found"));
        Application application = applicationRepository.findById(request.applicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Business rule: round must belong to same drive as application
        if (!round.getPlacementDrive().getId().equals(application.getPlacementDrive().getId())) {
            throw new IllegalArgumentException("Round does not belong to the same drive as the application");
        }

        RoundResultStatus resultStatus = RoundResultStatus.valueOf(request.status());
        Optional<RoundResult> existingResult = resultRepository.findByApplicationIdAndPlacementRoundId(request.applicationId(), roundId);

        RoundResult result;
        if (existingResult.isPresent()) {
            result = existingResult.get();
            result.setResult(resultStatus);        // field is 'result', not 'status'
            result.setRemarks(request.remarks());
        } else {
            result = RoundResult.builder()
                    .application(application)
                    .placementRound(round)
                    .result(resultStatus)          // builder method is 'result', not 'status'
                    .remarks(request.remarks())
                    .build();
        }

        RoundResult savedResult = resultRepository.save(result);

        // Business rule: synchronize application status with round results
        syncApplicationStatus(application, resultStatus);

        return savedResult;
    }

    /** Synchronize application.status based on the round result. */
    private void syncApplicationStatus(Application application, RoundResultStatus resultStatus) {
        if (resultStatus == RoundResultStatus.REJECTED || resultStatus == RoundResultStatus.ABSENT) {
            application.setStatus(ApplicationStatus.REJECTED);
        } else if (resultStatus == RoundResultStatus.SELECTED) {
            // Only mark SELECTED if this was the final round result
            application.setStatus(ApplicationStatus.IN_PROGRESS);
        }
    }

    public List<RoundResult> getApplicationResults(Long applicationId) {
        return resultRepository.findByApplicationId(applicationId);
    }
}
