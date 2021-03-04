package com.eplugger.core.convert;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eplugger.core.query.Criterion;
import com.eplugger.core.query.Expression;
import com.eplugger.core.query.JoinExpression;
import com.eplugger.core.query.QueryCriteria;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 条件转换器
 */
@Component
public class StringToQueryCriteria implements Converter<String, QueryCriteria> {

    @Override
    public QueryCriteria convert(String source) {
        JSONObject queryObj = JSONObject.parseObject(source);
        if(queryObj == null || queryObj.size() < 1) {
            return new QueryCriteria();
        }
        QueryCriteria queryCriteria =  new QueryCriteria();
        queryCriteria.add(convertJson2Criterion(queryObj));
        return queryCriteria;
    }

    /**
     * 转换json为查询条件
     * @param obj json对象
     * @return
     */
    private Criterion convertJson2Criterion(JSONObject obj) {
        if (obj.containsKey("items") && obj.get("items") != null) {
            String relation = obj.getString("relation");
            JSONArray array = obj.getJSONArray("items");
            if (array.size() == 1) {
                return convertExpression(array.getJSONObject(0));
            }
            List<Criterion> criterions = new ArrayList<>();
            for (Iterator iterator = array.iterator(); iterator.hasNext(); ) {
                JSONObject item = (JSONObject) iterator.next();
                criterions.add(convertJson2Criterion(item));
            }
            return new JoinExpression(criterions.toArray(new Criterion[]{}), relation);
        } else {
            return convertExpression(obj);
        }
    }

    private Expression convertExpression(JSONObject itemObj) {
        Object val = null;
        if (itemObj.get("val") instanceof JSONArray) {
            val = itemObj.getJSONArray("val").toArray();
        } else {
            val = itemObj.get("val");
        }
        return new Expression(itemObj.getString("name"), itemObj.getString("exp"), val);
//        return JSON.parseObject(itemObj.toJSONString(), new TypeReference<Expression>() {
//        });
    }
}
