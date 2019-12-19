package com.jackson.ext.test.vo;

import java.util.Map;

public class MockBizDataContext {
    private Map bizDataMap;

    public MockBizDataContext(Map bizDataMap){
        this.bizDataMap=bizDataMap;
    }

    @Override
    public String toString() {
        return "MockBizDataContext{" +
                "bizDataMap=" + bizDataMap +
                '}';
    }

    public Map getBizDataMap() {
        return bizDataMap;
    }

    public void setBizDataMap(Map bizDataMap) {
        this.bizDataMap = bizDataMap;
    }
}
