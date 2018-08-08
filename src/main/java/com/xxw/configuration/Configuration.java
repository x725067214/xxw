package com.xxw.configuration;

import javax.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 配置
 *
 * @author xxw
 * @date 2018/8/8
 */
@Entity
@Table(name="configuration")
@EntityListeners(AuditingEntityListener.class)
public class Configuration {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 键
     */
    @Column(name = "`key`")
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 注释
     */
    private String comment;

    /**
     * 创建时间
     */
    @CreatedDate
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @LastModifiedDate
    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
