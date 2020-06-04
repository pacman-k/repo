package by.epam.training.security;

import by.epam.training.core.Bean;
import by.epam.training.user.UserDto;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
@Bean
public class SecurityService {
    private static final Lock CREATOR_LOCK = new ReentrantLock();
    private static SecurityService instance;
    private SecurityContext securityContext;
    private Map<String, UserDto> loggedUsers = new HashMap<>();

    private SecurityService() throws SecurityContextException {

           securityContext = SecurityContext.getInstance();
    }

    public static SecurityService getInstance() throws SecurityContextException {
        if (instance == null) {
            CREATOR_LOCK.lock();
            try {
                if (instance == null) {
                    instance = new SecurityService();
                }
            } finally {
                CREATOR_LOCK.unlock();
            }
        }
        return instance;
    }

    public void login(String sessionId, UserDto userDto) {
        loggedUsers.put(sessionId, userDto);
    }

    public UserDto logout(String sessionId) {
        return loggedUsers.remove(sessionId);
    }

    public boolean canExecute(String commandName, String sessionId) {
        if (sessionId == null || !loggedUsers.containsKey(sessionId)) {
            return canExecute(commandName);
        }
        UserDto userDto = loggedUsers.get(sessionId);
        String userRole = userDto.getRoleDto().getRoleTypes().toString();
        return securityContext.isCommandAllowed(commandName, userRole);
    }

    public boolean canExecute(String commandName) {
        return securityContext.isCommandAllowed(commandName);
    }

}
