package by.epam.training.security;

import by.epam.training.util.PropertiesWorker;
import by.epam.training.util.PropertiesWorkerException;
import lombok.extern.log4j.Log4j;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Log4j
public class SecurityContext {
    public static SecurityContext instance;

    private final static String SECURITY_PROP_PATH = "security.properties";
    private static final Lock CREATOR_LOCK = new ReentrantLock();
    private final static String ALLOW_TO_ANON = "command.{0}.allowAnonymous";
    private final static String ALLOW_TO_ROLES = "command.{0}.roles";
    private final static String DELIMITER = "\\s*\\,\\s*";
    private final static String ALL = "all";

    private Set<String> roleNames;
    private Map<String, Set<String>> commandPermission = new HashMap<>();

    private SecurityContext() {
        try {
            Properties property = PropertiesWorker.createConfig(SECURITY_PROP_PATH);
            String[] roles = property.getProperty("roles.names").split(DELIMITER);
            roleNames = Arrays.stream(roles).collect(Collectors.toSet());
            String[] commands = property.getProperty("command.names").split(DELIMITER);
            Set<String> commandsSet = Arrays.stream(commands).collect(Collectors.toSet());
            for (String command : commandsSet) {
                String allowedToAnonymous = property.getProperty(MessageFormat.format(ALLOW_TO_ANON, command));
                if ("true".equals(allowedToAnonymous)) {
                    commandPermission.put(command, Collections.singleton(ALL));
                } else {
                    String[] allowedRoles = property.getProperty(MessageFormat.format(ALLOW_TO_ROLES, command))
                            .split(DELIMITER);
                    Set<String> allowedRolesSet = Arrays.stream(allowedRoles).collect(Collectors.toSet());
                    commandPermission.put(command, allowedRolesSet);
                }
            }
        } catch (PropertiesWorkerException | NullPointerException e) {
            log.error("Cant create security context", e);
            throw new SecurityContextException("Failed to initialise security context", e);
        }
    }

    public static SecurityContext getInstance() {
        CREATOR_LOCK.lock();
        try {
            if (instance == null) {
                instance = new SecurityContext();
            }
        } finally {
            CREATOR_LOCK.unlock();
        }
        return instance;
    }

    public boolean isCommandAllowed(String commandName) {
        if (!commandPermission.containsKey(commandName)) {
            return false;
        }
        return commandPermission.get(commandName).contains(ALL);
    }

    public boolean isCommandAllowed(String commandName, String roleName) {
        if (!commandPermission.containsKey(commandName)) {
            return false;
        }
        if (isCommandAllowed(commandName)) {
            return true;
        }
        return commandPermission.get(commandName).stream().anyMatch(roleName::equals);

    }


}
