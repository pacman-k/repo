package by.epam.training.claimstatus;

import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Bean
@TransactionSupport
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {
    ClaimStatusDao claimStatusDao;

    @Override
    public void assignUndConsStatus(Long claimId) {
        try {
            claimStatusDao.assignUndConsStatus(claimId);
        } catch (DAOException e) {
            log.error("cant assignUndConsStatus :", e);
        }
    }

    @Override
    public void assignStatus(Long statusId, Long claimId) {
        try {
            claimStatusDao.assignStatus(statusId, claimId);
        } catch (DAOException e) {
            log.error(String.format("cant assign status(%d) to claim(%d) : ", statusId, claimId), e);
        }
    }

    @Override
    public Optional<ClaimStatusDto> getStatusByName(String statusName) {
        try {
            return Optional.of(claimStatusDao.getStatusByName(statusName));
        } catch (DAOException e) {
            log.error("cant get status by such name : " + statusName, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean save(ClaimStatusDto statusDto) {
        try {
            claimStatusDao.save(statusDto);
            return true;
        } catch (DAOException e) {
            log.error("cant save status : " + statusDto, e);
            return false;
        }
    }

    @Override
    public Optional<ClaimStatusDto> getById(Long id) {
        try {
            return Optional.of(claimStatusDao.getById(id));
        } catch (DAOException e) {
            log.error("cant get status by such id : " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<ClaimStatusDto> getAll() {
        try {
            return claimStatusDao.getAll();
        } catch (DAOException e) {
            log.error("cant get all statuses ", e);
            return new ArrayList<>();
        }
    }

}
