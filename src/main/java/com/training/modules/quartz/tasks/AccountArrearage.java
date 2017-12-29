package com.training.modules.reportforms.service;

import com.training.modules.reportforms.dao.ReportMapper;
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
