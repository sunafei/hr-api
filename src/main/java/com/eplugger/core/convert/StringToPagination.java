package com.eplugger.core.convert;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eplugger.core.query.Pagination;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 前端传递分页JSON字符串转分页对象
 * creat_user: AFei Sun
 * creat_date: 2018/4/23
 **/
@Component
public class StringToPagination implements Converter<String, Pagination> {
    @Override
    public Pagination convert(String source) {
        JSONObject pageObj = JSON.parseObject(source);
        return new Pagination(pageObj.getInteger("page") - 1, pageObj.getInteger("size"), pageObj.getString("orderName"), pageObj.getInteger("orderType"));
//        return JSONObject.toJavaObject(pageObj, Pagination.class);
    }
}
