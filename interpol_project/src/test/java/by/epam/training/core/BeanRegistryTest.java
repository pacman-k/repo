package by.epam.training.core;

import by.epam.training.command.ServletCommand;
import by.epam.training.dao.ConnectionManagerImpl;
import by.epam.training.user.*;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@Log4j
@RunWith(JUnit4.class)
public class BeanRegistryTest {

    @Test
    public void shouldRegisterAndReturnServiceByInterface() {

        BeanRegistry provider = new BeanRegistryImpl();
        UserService serviceImpl = new UserServiceImpl(null, null, null, null);
        provider.registerBean(serviceImpl);
        UserService serviceFound = provider.getBean(UserService.class);
        Assert.assertEquals(serviceImpl, serviceFound);
        log.info("-------shouldRegisterAndReturnServiceByInterface is completed--------");
    }

    @Test
    public void shouldRegisterAndReturnServiceByName() {

        BeanRegistry provider = new BeanRegistryImpl();
        UserService serviceImpl = new UserServiceImpl(null, null, null, null);
        provider.registerBean(serviceImpl);
        UserService serviceFound = provider.getBean("UserService");
        Assert.assertEquals(serviceImpl, serviceFound);
        log.info("-------shouldRegisterAndReturnServiceByName is completed--------");
    }

    @Test
    public void shouldRegisterAndInjectAndReturnNewServiceByName() {

        BeanRegistry provider = new BeanRegistryImpl();
        provider.registerBean(UserServiceImpl.class);
        provider.registerBean(LoginUserCommand.class);
        ServletCommand serviceFound = provider.getBean("loginUser");
        Assert.assertNotNull(serviceFound);
        Assert.assertTrue(serviceFound instanceof LoginUserCommand);
        UserService userService = ((LoginUserCommand) serviceFound).getUserService();
        Assert.assertNotNull(userService);
        log.info("-------shouldRegisterAndInjectAndReturnNewServiceByName is completed--------");
    }

    @Test
    public void shouldRegisterAndInjectAndReturnNewServiceByClass() {

        BeanRegistry provider = new BeanRegistryImpl();
        provider.registerBean(LoginUserCommand.class);
        provider.registerBean(UserServiceImpl.class);
        LoginUserCommand serviceFound = provider.getBean(LoginUserCommand.class);
        Assert.assertNotNull(serviceFound);
        UserService userService = serviceFound.getUserService();
        Assert.assertNotNull(userService);
        log.info("-------shouldRegisterAndInjectAndReturnNewServiceByClass is completed--------");
    }

    @Test
    public void shouldRegisterAndInjectSameAndReturnNewServiceByClass() {

        BeanRegistry provider = new BeanRegistryImpl();
        provider.registerBean(LoginUserCommand.class);
        provider.registerBean(UserServiceImpl.class);
        provider.registerBean(UserDaoImpl.class);
        provider.registerBean(ConnectionManagerImpl.class);
        UserService firstService = provider.getBean("UserService");
        LoginUserCommand serviceFound = provider.getBean(LoginUserCommand.class);
        Assert.assertNotNull(serviceFound);
        UserService userService = serviceFound.getUserService();
        Assert.assertEquals(firstService, userService);
    }

}