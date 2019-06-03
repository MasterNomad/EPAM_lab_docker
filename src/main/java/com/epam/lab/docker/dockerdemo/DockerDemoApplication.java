package com.epam.lab.docker.dockerdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RestController
public class DockerDemoApplication {

    private Logger logger = LoggerFactory.getLogger(DockerDemoApplication.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DockerDemoApplication.class, args);
    }

    @PostConstruct
    public void initDataBase() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS counter_table (" +
                "id SERIAL PRIMARY KEY, " +
                "request_counter INT NULL );");
        jdbcTemplate.execute("INSERT INTO counter_table (request_counter) VALUES ('0');");
    }

    @RequestMapping("/")
    public String home() {
        logger.info("Rest controller was called");
        jdbcTemplate.update("UPDATE counter_table " +
                "SET request_counter = request_counter + 1 " +
                "WHERE id = 1");
        return "Docker: " +
                jdbcTemplate.queryForMap("SELECT request_counter " +
                        "FROM counter_table " +
                        "WHERE id = 1");
    }

}
