package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDao;
import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.claimstatus.StatusTypes;
import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import by.epam.training.wantedPerson.WantedPersonDao;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
@Bean
@AllArgsConstructor
@TransactionSupport
public class ClaimServiceImpl implements ClaimService {
    ClaimDao claimDao;
    ClaimStatusDao claimStatusDao;
    WantedPersonDao wantedPersonDao;

    @Transactional
    @Override
    public boolean registerClaim(ClaimDto claimDto) {
        try {
            claimStatusDao.save(claimDto.getClaimStatus());
            wantedPersonDao.save(claimDto.getWantedPerson());
            claimDao.save(claimDto);
            return true;
        } catch (DAOException e) {
            log.error("Cant register claim", e);
            return false;
        }
    }

    @Override
    public List<ClaimDto> getAllClaims() {
        try {
            return claimDao.getAll().stream().map(this::putWantedPersAndStatus).collect(Collectors.toList());
        } catch (DAOException e) {
            log.error("Cant find any claims", e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<ClaimDto> findByStatus(ClaimStatusDto status) {
        try {
            return claimDao.getAllByStatus(status).stream().map(this::putWantedPersAndStatus).collect(Collectors.toList());
        } catch (DAOException e) {
            log.error("Cant find any claims by status :" + status, e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<ClaimDto> findByCustomer(Long customerId) {
        try {
            return claimDao.findByCustomer(customerId).stream().map(this::putWantedPersAndStatus).collect(Collectors.toList());
        } catch (DAOException e) {
            log.error("Cant find any claims of this customer :" + customerId, e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<ClaimDto> findByWantedPers(Long persId) {
        try {
            return Optional.of(putWantedPersAndStatus(claimDao.findByWantPers(persId).orElse(null)));
        } catch (DAOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean setActualStatus(ClaimDto claimDto) {
        try {
            ClaimStatusDto claimStatusDto = claimStatusDao.getStatusByName(StatusTypes.ACTUAL.toString());
            claimDto.setClaimStatus(claimStatusDto);
            claimDao.update(claimDto);
            return true;
        } catch (DAOException e) {
            return false;
        }
    }

    @Override
    public Optional<ClaimDto> findById(Long id) {
        try {
            return Optional.of(putWantedPersAndStatus(claimDao.getById(id)));
        } catch (DAOException e) {
            log.error("cant find claim by such id");
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean deleteClaim(ClaimDto claimDto) {
        try {
            claimDao.delete(claimDto);
            wantedPersonDao.delete(claimDto.getWantedPerson());
            return true;
        } catch (DAOException e) {
            log.error("cant delete claim :" + claimDto);
            return false;
        }
    }

    private ClaimDto putWantedPersAndStatus(ClaimDto claimDto) {
        try {
            Long statusId = claimDto.getClaimStatus().getId();
            claimDto.setClaimStatus(claimStatusDao.getById(statusId));
            Long wantedPersId = claimDto.getWantedPerson().getId();
            claimDto.setWantedPerson(wantedPersonDao.getById(wantedPersId));
            return claimDto;
        } catch (DAOException e) {
            log.error("cant put wanted person and status");
            return claimDto;
        }
    }

}
