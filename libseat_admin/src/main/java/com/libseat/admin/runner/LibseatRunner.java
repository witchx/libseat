package com.libseat.admin.runner;

import com.libseat.admin.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LibseatRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(LibseatRunner.class);

    @Autowired
    private BaseService baseService;

    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        logger.info(" >>>>>> Project startup completed, open = >;Begin tasks that require self-revelation!");

        baseService.startFinish();

        logger.info(" >>>>>> Project startup completed, open = >;End of the task that requires self-revelation!");
    }
}