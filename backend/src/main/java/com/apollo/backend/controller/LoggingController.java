package com.apollo.backend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LoggingController {
 
    Logger logger = LoggerFactory.getLogger(LoggingController.class);
 
    @RequestMapping(method = RequestMethod.GET, value = "/logs")
    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");
 
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return "Howdy! Check out the Logs to see the output... " + formatter.format(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logs/info")
    public String indexInfo() {
        logger.info("An INFO Message");
 
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return "Howdy! Check out the Logs to see the output Info... " + formatter.format(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logs/warn")
    public String indexWarn() {
        logger.warn("A WARN Message");
 
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return "Howdy! Check out the Logs to see the output Warn... " + formatter.format(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logs/error")
    public String indexError() {
        logger.error("An ERROR Message");
 
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return "Howdy! Check out the Logs to see the output Error... " + formatter.format(date);
    }
}