/**
 *
 */
package com.feeling.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import com.feeling.log.LogInfo;

/**
 * @author dutao
 */
@Controller
@RequestMapping("/feeling")
public class BaseController {
	private static final Logger logger =LogInfo.WEB_LOG;
	
	public static final int defaultCommentPageSize = 20;
	
	public static final int defaultUserEventPageSize = 20;
	
	public void writeErrorLog(final String message, final Throwable t) {
		if (logger.isErrorEnabled()) {
			logger.error(message, t);
		}
	}

	public void writeErrorLog(final String message) {
		if (logger.isErrorEnabled()) {
			logger.error(message);
		}
	}

	public void writeWarnLog(final String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(message);
		}
	}

	public void writeInfoLog(final String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	public void writeDebugLog(final String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}
}
