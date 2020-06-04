package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.dao.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClaimService {
    @Transactional
    boolean registerClaim(ClaimDto claimDto);

    List<ClaimDto> getAllClaims();
    List<ClaimDto> findByStatus(ClaimStatusDto status);
    List<ClaimDto> findByCustomer(Long customerId);
    Optional<ClaimDto> findByWantedPers(Long persId);
    boolean setActualStatus(ClaimDto claimDto);
    Optional<ClaimDto> findById(Long id);
    @Transactional
    boolean deleteClaim(ClaimDto claimDto);
}
