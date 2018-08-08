package com.xxw.configuration;

import com.xxw.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xxw
 * @date 2018/8/8
 */
@Service
public class ConfigurationService {

    private static final String SQL_CONSTRAINT_NAME_UK_KEY = "constraint [uk_key]";

    private static final String KEY_EXISTED = "键已存在";

    private static final String CONFIGURATION_NOT_EXISTED = "配置不存在";

    @Autowired
    private ConfigurationRepository configurationRepository;

    void save(Configuration configuration) {
        try {
            configurationRepository.save(configuration);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(SQL_CONSTRAINT_NAME_UK_KEY)) {
                throw new BusinessException(KEY_EXISTED);
            }
            throw e;
        }
    }

    public String find(String key, String defaultValue) {
        Configuration configuration = configurationRepository.findByKey(key);
        if (configuration == null) {
            return defaultValue;
        }
        return configuration.getValue();
    }

    Configuration find(Long id) {
        Optional<Configuration> configuration = configurationRepository.findById(id);
        if (!configuration.isPresent()) {
            throw new BusinessException(CONFIGURATION_NOT_EXISTED);
        }
        return configuration.get();
    }

    void update(Configuration configuration) {
        Configuration existed = find(configuration.getId());
        existed.setKey(configuration.getKey());
        existed.setValue(configuration.getValue());
        existed.setComment(configuration.getComment());
        save(existed);
    }

    void delete(Long id) {
        find(id);
        configurationRepository.deleteById(id);
    }

    Page<Configuration> findAll(final Configuration configuration, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "id"));
        return configurationRepository.findAll(new Specification<Configuration>() {
            @Override
            public Predicate toPredicate(Root<Configuration> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(configuration.getKey())) {
                    Predicate predicate = criteriaBuilder.like(root.get("key"), "%" + configuration.getKey() + "%");
                    predicateList.add(predicate);
                }
                if (!StringUtils.isEmpty(configuration.getComment())) {
                    Predicate predicate = criteriaBuilder.like(root.get("comment"), "%" + configuration.getComment() + "%");
                    predicateList.add(predicate);
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        }, pageable);
    }
}
