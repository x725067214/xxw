package com.xxw.configuration;

import com.xxw.exception.BusinessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xxw
 * @date 2018/8/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationServiceTest {

    @Autowired
    private ConfigurationService configurationService;

    @Test
    @Transactional
    public void save() {
        Configuration configuration = new Configuration();
        configuration.setKey("timeout");
        configuration.setValue("30");
        configuration.setComment("超时时间");
        configurationService.save(configuration);
        Assert.assertNotNull(configuration.getId());

        Configuration duplicateKey = new Configuration();
        duplicateKey.setKey("timeout");
        duplicateKey.setValue("30");
        duplicateKey.setComment("超时时间");
        try {
            configurationService.save(duplicateKey);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("键已存在"));
        }
    }

    @Test
    @Transactional
    public void find() {
        Configuration configuration = new Configuration();
        configuration.setKey("timeout");
        configuration.setValue("30");
        configuration.setComment("超时时间");
        configurationService.save(configuration);

        String value = configurationService.find("timeout", "100");
        Assert.assertTrue(value.equals("30"));

        String defaultValue = configurationService.find("retryCount", "3");
        Assert.assertTrue(defaultValue.equals("3"));
    }

    @Test
    @Transactional
    public void findById() {
        Configuration configuration = new Configuration();
        configuration.setKey("timeout");
        configuration.setValue("30");
        configuration.setComment("超时时间");
        configurationService.save(configuration);

        Configuration existed = configurationService.find(configuration.getId());
        Assert.assertNotNull(existed);

        try {
            configurationService.find(0L);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("配置不存在"));
        }
    }

    @Test
    @Transactional
    public void update() {
        Configuration configuration = new Configuration();
        configuration.setKey("timeout");
        configuration.setValue("30");
        configuration.setComment("超时时间");
        configurationService.save(configuration);

        Configuration latest = new Configuration();
        latest.setId(configuration.getId());
        latest.setKey("timeout1");
        latest.setValue("100");
        latest.setComment("超时时间1");
        configurationService.update(latest);

        Configuration existed = configurationService.find(configuration.getId());
        Assert.assertTrue(existed.getKey().equals(latest.getKey()));

        Configuration notExisted = new Configuration();
        notExisted.setId(0L);
        notExisted.setKey("timeout");
        notExisted.setValue("30");
        notExisted.setComment("超时时间");
        try {
            configurationService.update(notExisted);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("配置不存在"));
        }

        Configuration duplicateKey = new Configuration();
        duplicateKey.setKey("retryCount");
        duplicateKey.setValue("3");
        duplicateKey.setComment("重试次数");
        configurationService.save(duplicateKey);

        duplicateKey.setKey("timeout1");
        try {
            configurationService.update(duplicateKey);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("键已存在"));
        }
    }

    @Test
    @Transactional
    public void remove() {
        Configuration configuration = new Configuration();
        configuration.setKey("timeout");
        configuration.setValue("30");
        configuration.setComment("超时时间");
        configurationService.save(configuration);

        configurationService.delete(configuration.getId());
        try {
            configurationService.find(configuration.getId());
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("配置不存在"));
        }

        try {
            configurationService.delete(0L);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().equals("配置不存在"));
        }
    }

    @Test
    @Transactional
    public void findAll() {
        Configuration timeout = new Configuration();
        timeout.setKey("timeout");
        timeout.setValue("30");
        timeout.setComment("超时时间");
        configurationService.save(timeout);

        Configuration retryCount = new Configuration();
        retryCount.setKey("retryCount");
        retryCount.setValue("3");
        retryCount.setComment("重试次数");
        configurationService.save(retryCount);

        Pageable pageable = PageRequest.of(0, 10);
        Configuration configuration = new Configuration();
        Page<Configuration> page = configurationService.findAll(configuration, pageable);
        Assert.assertTrue(page.getTotalElements() == 2L);

        configuration.setKey("t");
        configuration.setComment("时间");
        page = configurationService.findAll(configuration, pageable);
        Assert.assertTrue(page.getTotalElements() == 1L);
    }
}
