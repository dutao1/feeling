package com.feeling.dao;

import org.slf4j.*;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {
    "classpath:spring-test.xml"
})
public class DaoTestBase {
    private static final Logger logger = LoggerFactory.getLogger(DaoTestBase.class);

}
