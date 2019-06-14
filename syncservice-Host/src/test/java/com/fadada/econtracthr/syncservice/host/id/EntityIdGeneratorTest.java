package com.fadada.econtracthr.syncservice.host.id;

import com.fadada.econtracthr.syncservice.host.util.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes=MainApplication.class)
//@WebAppConfiguration
public class EntityIdGeneratorTest extends AbstractJUnit4SpringContextTests{
    private static Log log = LogFactory.getLog(EntityIdGeneratorTest.class);

    @Test
    public void myTest() {
        SpringContextUtil.setApplicationContext(applicationContext);
//        MyThread T1 = new MyThread("A");
//        T1.start();
    }
}
