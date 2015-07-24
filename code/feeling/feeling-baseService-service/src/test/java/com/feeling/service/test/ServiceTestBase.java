package com.feeling.service.test;

import org.slf4j.*;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {
    "classpath:spring*.xml"
})
public class ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(ServiceTestBase.class);

}
