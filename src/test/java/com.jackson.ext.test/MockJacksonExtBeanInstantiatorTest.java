package com.jackson.ext.test;

import com.jackson.ext.test.Enum.MockEnumWithNonDefaultCreator;
import com.jackson.ext.test.vo.MockBizDataContext;
import com.jackson.ext.test.vo.MockPojoWithNonDefaultConstructor;
import com.jackson.ext.test.vo.MockPojoWithNonStaticInnerPojo_Outer;
import com.jackson.ext.util.Jackson_Enhance_Utils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class MockJacksonExtBeanInstantiatorTest {
    @Test
    public void testJasckExtBeanInstantiator_withSerializer(){
        MockBizDataContext mockBizDataContext = buildMockBizDataContext();
        String mockBizDataContextText= Jackson_Enhance_Utils.serialize(mockBizDataContext);
        System.out.println(mockBizDataContextText);
        Assert.assertNotNull(mockBizDataContextText);
    }

    @Test
    public void testJasckExtBeanInstantiator_withDeserializer(){
        MockBizDataContext mockBizDataContext = buildMockBizDataContext();
        String mockBizDataContextText= Jackson_Enhance_Utils.serialize(mockBizDataContext);
        MockBizDataContext mockBizDataContext_new=Jackson_Enhance_Utils.deserialize(mockBizDataContextText,MockBizDataContext.class);
        Assert.assertNotNull(mockBizDataContext_new);
    }

    private MockBizDataContext buildMockBizDataContext() {
        Map bizDataMap=new HashMap();
        MockPojoWithNonDefaultConstructor pojo1=new MockPojoWithNonDefaultConstructor(123,"test1","yilami1","11111111");
        MockPojoWithNonDefaultConstructor pojo2=new MockPojoWithNonDefaultConstructor(234,"test2","yilami2","22222222");
        List mockPojoWithNonDefaultConstructorList=new ArrayList();
        mockPojoWithNonDefaultConstructorList.add(pojo1);
        mockPojoWithNonDefaultConstructorList.add(pojo2);
        bizDataMap.put("MockPojoWithNonDefaultConstructor1",pojo1);
        bizDataMap.put("MockPojoWithNonDefaultConstructor2",pojo2);
        bizDataMap.put("createTime",new Date());
        bizDataMap.put("MockEnumWithNonDefaultCreator", MockEnumWithNonDefaultCreator.NO);
        bizDataMap.put("MockPojoWithNonDefaultConstructorList",mockPojoWithNonDefaultConstructorList);
        MockPojoWithNonStaticInnerPojo_Outer mockPojoWithNonStaticInnerPojo_outer=new MockPojoWithNonStaticInnerPojo_Outer("Outer[1]");
        MockPojoWithNonStaticInnerPojo_Outer.MockPojoWithNonStaticInnerPojo_Inner mockPojoWithNonStaticInnerPojo_inner=mockPojoWithNonStaticInnerPojo_outer.new MockPojoWithNonStaticInnerPojo_Inner("Inner[1]");
        mockPojoWithNonStaticInnerPojo_outer.setMockPojoWithNonStaticInnerPojo_inner(mockPojoWithNonStaticInnerPojo_inner);
        bizDataMap.put("MockPojoWithNonStaticInnerPojo_Outer",mockPojoWithNonStaticInnerPojo_outer);
        bizDataMap.put("MockPojoWithNonStaticInnerPojo_Inner",mockPojoWithNonStaticInnerPojo_inner);
        return new MockBizDataContext(bizDataMap);
    }

}
