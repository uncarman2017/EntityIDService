package com.fadada.econtracthr.entityidservice.host.business;


import com.fadada.econtracthr.entityidservice.host.entity.EntityIdConfPO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实体id生成服务类
 *
 * @author songjiangtao
 */
public class EntityIdHandler {

    private static final int RETRY_COUNT = 5;
    private final ReentrantLock lock = new ReentrantLock();
    private String lastGeneratedId = "";         //最后生成的id
    private String idCode = "";                  //主键，单据类型
    private String fixPrefix = "";             //固定前缀
    private String datePattern = "";            //日期格式
    private boolean includeBizPrefix = false;    //是否包含业务前缀
    private int numDigit = 0;                    //数字部分位数
    private String datePrefix = "";              //日期前缀
    private long maxValue = 0;                   //最大键值
    private long nextValule = 0;                 //下一个键值

    //@Autowired
    private EntityIdConfService entityIdConfService;

    public EntityIdHandler(String idCode, EntityIdConfService entityIdConfService) {
        this.idCode = idCode.trim().toLowerCase();
        this.entityIdConfService = entityIdConfService;
        //entityIdConfService = (EntityIdConfService) SpringContextUtil.getBean(EntityIdConfService.class);
        retrieveFromDB(RETRY_COUNT);// 注意不能直接使用这个类，否则在多线程的情况下，会出问题。
    }

    /**
     * 获得List
     *
     * @param bizCode
     * @param reqCount
     * @return
     */
    public List<String> getNextEntityIds(String bizCode, Integer reqCount) {
        lock.lock();
        try {
            List<String> entityIds = new ArrayList<>();
            for (int i = 0; i < reqCount; i++) {
                entityIds.add(getNextEntityId(bizCode));
            }
            return entityIds;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获得EntityId
     *
     * @param bizCode
     * @return
     */
    public String getNextEntityId(String bizCode) {//synchronized
        lock.lock();
        try {
            String stringValue = getNextStringValue();
            String nextEntityId = this.fixPrefix.trim()
                    + (this.includeBizPrefix ? bizCode : "")
                    + this.datePrefix.trim()
                    + stringValue;
//            logServiceProxy.infoAsync(LogDTO.of().setTitle("获取实体id")
//                    .setContent(bizCode + ":" + nextEntityId)
//                    .putTag("thread", Thread.currentThread().getName()));
            this.lastGeneratedId = nextEntityId;
            return nextEntityId;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取下一个字符串的键值。如果不符合长度，前面用零补齐；
     *
     * @return
     */
    public String getNextStringValue() {//synchronized
        String s = Long.toString(getNextLongValue());
        return StringUtils.leftPad(s, this.numDigit, "0");
    }

    /**
     * 获取下一个长整型的键值；
     *
     * @return
     */
    public long getNextLongValue() {
        //如果日期前缀变化
        String tempDatePrefix = getTempDatePrefix(this.datePattern);
        if (!tempDatePrefix.trim().equalsIgnoreCase("") &&
                !tempDatePrefix.equalsIgnoreCase(this.datePrefix)) {
            retrieveFromDB(RETRY_COUNT);
        }
        //如果内存id资源耗光,需要同步数据库
        if (nextValule > maxValue) {
            retrieveFromDB(RETRY_COUNT);
        }
        //内存id正常增长
        if (nextValule <= maxValue) {
            //线程随机睡眠，生产环境注释
            //threadRandomSleep();
            return (nextValule++);
        } else {
//            logServiceProxy.errorAsync(LogDTO.of().setTitle("获取下id下一个")
//                    .setContent("最终获取实体id失败！请检查id配置...")
//                    .putTag("idCode", idCode));
            return -1L;
        }
    }

    /**
     * 更新数据库，刷新内存池。数据库中要记录两个值：nextValue,datePrefix,其中datePrefix只是表示当前的，并不是与nextValue对应，nextValue是未来使用值。
     * 数据库要记录最后一次被更新时对应的日期前缀，以便当服务崩溃重启时，用它来和当前日期比较，判断数据库的nextValue是否回归poolSize+1。
     * 如果是日期前缀变更，更新数据库nextValue为pooSize+1。数据库中管理的只是当前日期前缀段内的递增数量；
     * 每次服务重启，要查询数据库中的日期前缀是否已过时,如果已过时，则准备更新数据库日期为当前。
     * 如果是第一次初始化即系统第一次投入使用，置数据库NextBatchStartValue值为1，如果不是第一次启动，置数据库nextBatchStartValue=NextBatchStartValue+poolSize，
     * 因此也有可能最多浪费一个poolsize的id，作为内存未和数据库及时通信的损失。
     *
     * @param retryCount
     */
    private void retrieveFromDB(int retryCount) {
        boolean success = false;
        if (retryCount == 0) {
//            logServiceProxy.errorAsync(LogDTO.of().setTitle("最终获取实体id失败！放弃继续获取...")
//                    .setContent("")
//                    .putTag("idCode", idCode));
            return;
        }

        try {
            EntityIdConfPO entityIdConf = entityIdConfService.selectById(idCode);
            String tempDatePrefix = getTempDatePrefix(entityIdConf.getDatePattern());
            //如果日期前缀发生变更
            if (!tempDatePrefix.trim().equalsIgnoreCase("") &&
                    !tempDatePrefix.equalsIgnoreCase(entityIdConf.getDatePrefix())) {
                entityIdConf.setDatePrefix(tempDatePrefix);
                entityIdConf.setNextBatchStartValue(entityIdConf.getPoolSize() + 1L);
            } else {
                //如果日期前缀没有变化，则增加步长,(在服务重启时这将损失id资源)
                entityIdConf.setNextBatchStartValue(entityIdConf.getNextBatchStartValue() + entityIdConf.getPoolSize());
            }

            entityIdConf.setLastGeneratedId(this.lastGeneratedId);
            //先更新数据库，再更新内存。
            if (entityIdConfService.updateById(entityIdConf)) {
                updateMemoryFieldFromDB(entityIdConf);
            }
            success = true;
        } catch (Exception e) {
//            logServiceProxy.errorAsync(LogDTO.of().setTitle("获取序列号异常！进行下一次尝试...")
//                    .setContent(e.toString())
//                    .putTag("idCode", idCode));
        }

        if (!success) {
            // Call this method again, but sleep briefly to try to avoid thread contention.
            try {
                Thread.sleep(75);
            } catch (InterruptedException ie) {
            }
            retrieveFromDB(retryCount - 1);
        }
    }

    /**
     * 获得临时 日期前缀
     *
     * @param datePattern
     * @return
     */
    private String getTempDatePrefix(String datePattern) {
        if (datePattern.trim().equalsIgnoreCase("")) return "";
        LocalDate localDate = LocalDate.now();
        return localDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 根据数据库更新内存值
     *
     * @param entityIdConf
     */
    private void updateMemoryFieldFromDB(EntityIdConfPO entityIdConf) {
        if (entityIdConf != null) {
            this.fixPrefix = entityIdConf.getFixPrefix() == null ? "".trim() : entityIdConf.getFixPrefix().trim();
            this.numDigit = entityIdConf.getNumDigit();
            this.includeBizPrefix = entityIdConf.getIncludeBizPrefix() == null ? Boolean.FALSE : entityIdConf.getIncludeBizPrefix();
            this.datePattern = entityIdConf.getDatePattern() == null ? "".trim() : entityIdConf.getDatePattern().trim();
            this.datePrefix = entityIdConf.getDatePrefix() == null ? "" : entityIdConf.getDatePrefix().trim();
            this.maxValue = entityIdConf.getNextBatchStartValue() - 1L;
            this.nextValule = entityIdConf.getNextBatchStartValue() - entityIdConf.getPoolSize();
        }
    }

    /**
     * 字母前缀增长
     *
     * @param letter
     * @return
     */
    private char increaseLetterPrefix(char letter) {
        if (letter < 'A' || letter > 'Z') {
//            logServiceProxy.errorAsync(LogDTO.of().setTitle("获取序列号失败,字母前缀溢出！").setContent(String.valueOf(letter)));
            return 'A';
        }
        letter = ++letter;
        if ((letter == 'I') || (letter == 'O'))
            return ++letter;
        else
            return letter;
    }

    /**
     * 线程以随机时间休眠.测试使用。
     */
    private void threadRandomSleep() {
        long sleepTime = (long) (new Random().nextInt(100) % (100) + 1);
        try {
            Thread.currentThread().sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
