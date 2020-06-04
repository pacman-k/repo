package com.epam.lab.configuration;

import com.epam.lab.repository.*;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.lab.service")
@EnableTransactionManagement
public class ServiceTestConfig {


    @Bean
    public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new  ModelMapper();
    }


    @Bean
    public NewsSpecificationFactory newsSpecificationFactory(){
        return Mockito.mock(NewsSpecificationFactory.class);
    }



    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    @Bean
    public NewsDao newsDao(){
        return Mockito.mock(NewsDao.class);
    }
    @Bean
    public AuthorDao authorDao(){
        return Mockito.mock(AuthorDao.class);
    }
    @Bean
    public TagDao tagDao(){
        return Mockito.mock(TagDao.class);
    }
    @Bean
    public UsersDao usersDao(){
        return Mockito.mock(UsersDao.class);
    }
}
