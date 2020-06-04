package by.epam.training.claimstatus;

import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;

public interface ClaimStatusDao extends CrudDao<ClaimStatusDto, Long> {
    void assignUndConsStatus(Long claimId) throws DAOException;
    void assignStatus(Long statusId, Long claimId) throws DAOException;
    ClaimStatusDto getStatusByName(String statusName) throws DAOException;
}
