package com.feeling.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志
 * @author dutao
 *
 */
public class LogInfo {
    public static final Logger USER_LOG = LoggerFactory.getLogger("USER_SERVICE.LOG");

    public static final Logger EVENT_LOG = LoggerFactory.getLogger("EVENT_SERVICE.LOG");
    
    public static final Logger WEB_LOG = LoggerFactory.getLogger("WEB.LOG");
}
