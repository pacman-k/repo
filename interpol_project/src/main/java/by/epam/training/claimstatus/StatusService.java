package by.epam.training.claimstatus;

import by.epam.training.dao.Transactional;

import java.util.List;
import java.util.Optional;

public interface StatusService {
    void assignUndConsStatus(Long claimId);

    void assignStatus(Long statusId, Long claimId);

    Optional<ClaimStatusDto> getStatusByName(String statusName);

    @Transactional
    boolean save(ClaimStatusDto statusDto);

   Optional<ClaimStatusDto> getById(Long id);

    List<ClaimStatusDto> getAll();
}
