package com.training.modules.quartz.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class AccountArrearage {

    @Autowired
    private ReportMapper reportMapper;

    @Scheduled(cron = "0 0 15 * * ?")
    public void accountArrearage(){
        reportMapper.dropTableUserAppArrearge();
        reportMapper.dropTableOfficeAppArrearge();
        reportMapper.insertTableUserAppArrearge();
        reportMapper.insertTableOfficeAppArrearge();
    }
}
