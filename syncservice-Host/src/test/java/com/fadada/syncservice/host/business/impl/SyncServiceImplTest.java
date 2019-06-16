package com.fadada.syncservice.host.business.impl;

import com.fadada.syncservice.host.SyncServiceApp;
import com.fadada.syncservice.host.business.SyncService;
import com.fadada.syncservice.host.util.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SyncServiceApp.class)
@WebAppConfiguration
public class SyncServiceImplTest extends AbstractJUnit4SpringContextTests{
    private static Log log = LogFactory.getLog(SyncServiceImplTest.class);
    @Autowired
    SyncService identityService;
    @Test
    public void myTest() {
        SpringContextUtil.setApplicationContext(applicationContext);
        identityService.selectList();
    }
}
