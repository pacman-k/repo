package by.epam.training;

import by.epam.training.claim.*;
import by.epam.training.claimstatus.ClaimStatusDaoImpl;
import by.epam.training.claimstatus.StatusServiceImpl;
import by.epam.training.core.BeanRegistry;
import by.epam.training.core.BeanRegistryImpl;
import by.epam.training.dao.*;
import by.epam.training.news.*;
import by.epam.training.role.RoleDaoImpl;
import by.epam.training.role.RoleServiceImpl;
import by.epam.training.security.CryptographyManagerImpl;
import by.epam.training.security.SecurityService;
import by.epam.training.user.*;
import by.epam.training.validator.*;
import by.epam.training.wallet.WalletDaoImpl;
import by.epam.training.wallet.WalletServiceImpl;
import by.epam.training.wantedPerson.WantedPersonDaoImpl;
import by.epam.training.wantedPerson.WantedPersonServiceImpl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ApplicationContext implements BeanRegistry {
    private final static AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private final static Lock INITIALIZE_LOCK = new ReentrantLock();
    private static ApplicationContext INSTANCE;

    private BeanRegistry beanRegistry = new BeanRegistryImpl();

    private ApplicationContext() {

    }

    public static void initialize() {
        INITIALIZE_LOCK.lock();
        try {
            if (INSTANCE != null && INITIALIZED.get()) {
                throw new IllegalStateException("Context was already initialized");
            } else {
                ApplicationContext context = new ApplicationContext();
                context.init();
                INSTANCE = context;
                INITIALIZED.set(true);
            }

        } finally {
            INITIALIZE_LOCK.unlock();
        }
    }

    public static ApplicationContext getInstance() {

        if (INSTANCE != null && INITIALIZED.get()) {
            return INSTANCE;
        } else {
            throw new IllegalStateException("Context wasn't initialized");
        }
    }

    @Override
    public void destroy() {
        ApplicationContext context = getInstance();
        DataSource dataSource = context.getBean(DataSource.class);
        dataSource.close();
        beanRegistry.destroy();
    }

    private void init() {
        registerDataSource();
        registerClasses();
    }

    private void registerDataSource() {
        ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        DataSource dataSource = new DataSourceImpl(connectionPool);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(dataSource, transactionManager);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager);
        registerBean(dataSource);
        registerBean(transactionManager);
        registerBean(connectionManager);
        registerBean(transactionInterceptor);
    }

    private void registerClasses() {
        registerBean(RegisterViewUserCommand.class);
        registerBean(LoginUserCommand.class);
        registerBean(RegisterSaveUserCommand.class);
        registerBean(LoginUserViewCommand.class);
        registerBean(UserListViewCommand.class);
        registerBean(LogoutUserCommand.class);
        registerBean(ViewNewsCommand.class);
        registerBean(AddNewsViewCommand.class);
        registerBean(AddNewsCommand.class);
        registerBean(DeleteUserCommand.class);
        registerBean(LockUserCommand.class);
        registerBean(UnLockUserCommand.class);
        registerBean(EditNewsViewCommand.class);
        registerBean(EditNewsCommand.class);
        registerBean(DeleteNewsCommand.class);
        registerBean(CreateClaimViewCommand.class);
        registerBean(CreateClaimCommand.class);
        registerBean(ViewActualClaimsCommand.class);
        registerBean(ViewUnderConsClaimsCommand.class);
        registerBean(ConfirmClaimCommand.class);
        registerBean(DeleteClaimCommand.class);
        registerBean(ProfileUserViewCommand.class);
        registerBean(ViewClaimDetailCommand.class);
        registerBean(EditUserViewCommand.class);
        registerBean(EditUserCommand.class);

        registerBean(RegisterReqValidator.class);
        registerBean(SaveNewsValidator.class);
        registerBean(CreateClaimValidator.class);
        registerBean(DeleteClaimValidator.class);
        registerBean(EditUserValidator.class);

        registerBean(WalletDaoImpl.class);
        registerBean(WalletServiceImpl.class);
        registerBean(RoleDaoImpl.class);
        registerBean(RoleServiceImpl.class);
        registerBean(UserDaoImpl.class);
        registerBean(UserServiceImpl.class);
        registerBean(CryptographyManagerImpl.class);
        registerBean(WantedPersonDaoImpl.class);
        registerBean(WantedPersonServiceImpl.class);
        registerBean(ClaimStatusDaoImpl.class);
        registerBean(StatusServiceImpl.class);
        registerBean(NewsServiceImpl.class);
        registerBean(NewsDaoImpl.class);
        registerBean(ClaimServiceImpl.class);
        registerBean(ClaimDaoImpl.class);
        registerBean(SecurityService.getInstance());
    }


    @Override
    public <T> void registerBean(T bean) {

        this.beanRegistry.registerBean(bean);
    }

    @Override
    public <T> void registerBean(Class<T> beanClass) {

        this.beanRegistry.registerBean(beanClass);
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        return this.beanRegistry.getBean(beanClass);
    }

    @Override
    public <T> T getBean(String name) {
        return this.beanRegistry.getBean(name);
    }

    @Override
    public <T> boolean removeBean(T bean) {
        return this.beanRegistry.removeBean(bean);
    }

}
