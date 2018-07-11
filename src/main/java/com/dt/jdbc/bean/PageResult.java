package com.dt.jdbc.bean;

import java.util.List;
import java.util.Map;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/11
 */
public class PageResult<T> {

    private List<T> objectList;

    private List<Map<String, Object>> mapList;

    private PageSupport limit;

    public List<T> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
    }

    public List<Map<String, Object>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
    }

    public PageSupport getLimit() {
        return limit;
    }

    public void setLimit(PageSupport limit) {
        this.limit = limit;
    }
}
