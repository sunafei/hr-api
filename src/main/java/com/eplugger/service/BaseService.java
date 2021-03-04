package com.eplugger.service;

import com.eplugger.module.base.BizEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * AFei Sun
 * Created in 18:54 2018/7/21
 */
public class BaseService {
    protected void setOperationInfo(BizEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {// 新增
            entity.setCreateDate(System.currentTimeMillis());
        }
        entity.setLastEditDate(System.currentTimeMillis());
    }
}
