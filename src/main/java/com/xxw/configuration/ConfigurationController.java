package com.xxw.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xxw
 * @date 2018/8/8
 */
@Controller
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @RequestMapping("/v1/configuration/save")
    public void save(Configuration configuration) {
        configurationService.save(configuration);
    }
}
