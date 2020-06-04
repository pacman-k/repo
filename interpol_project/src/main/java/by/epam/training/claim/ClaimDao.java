package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;

import java.util.List;
import java.util.Optional;

public interface ClaimDao extends CrudDao<ClaimDto, Long> {
    List<ClaimDto> findByCustomer(Long customerId) throws DAOException;
    List<ClaimDto> getAllByStatus (ClaimStatusDto status) throws DAOException;
    Optional <ClaimDto> findByWantPers(Long persId) throws DAOException;

}
