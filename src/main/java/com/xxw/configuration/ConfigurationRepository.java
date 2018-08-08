package com.xxw.configuration;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author xxw
 * @date 2018/8/8
 */
interface ConfigurationRepository extends PagingAndSortingRepository<Configuration, Long>, JpaSpecificationExecutor {

    /**
     * 通过键查询配置
     *
     * @param key 键
     * @return 配置
     */
    Configuration findByKey(String key);
}
