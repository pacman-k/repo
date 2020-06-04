package by.epam.training.listener;

import by.epam.training.ApplicationContext;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Log4j
public class ApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext.initialize();
        log.info("Context has been initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext.getInstance().destroy();
        log.info("Context has been destroyed");
    }
}
