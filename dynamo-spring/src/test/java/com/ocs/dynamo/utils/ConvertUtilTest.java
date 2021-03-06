package com.ocs.dynamo.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ocs.dynamo.domain.TestEntity;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.domain.model.EntityModelFactory;
import com.ocs.dynamo.domain.model.impl.EntityModelFactoryImpl;

public class ConvertUtilTest {

    private EntityModelFactory emf = new EntityModelFactoryImpl();

    @Test
    public void testConvertSearchValue() {
        EntityModel<TestEntity> model = emf.getModel(TestEntity.class);

        Object obj = ConvertUtil.convertSearchValue(model.getAttributeModel("age"), "12");
        Assert.assertTrue(obj instanceof Long);
        Assert.assertEquals(12L, ((Long) obj).longValue());

        obj = ConvertUtil.convertSearchValue(model.getAttributeModel("discount"), "12,34");
        Assert.assertTrue(obj instanceof BigDecimal);
        Assert.assertEquals(12.34, ((BigDecimal) obj).doubleValue(), 0.0001);

        obj = ConvertUtil.convertSearchValue(model.getAttributeModel("id"), "17");
        Assert.assertTrue(obj instanceof Integer);
        Assert.assertEquals(17, ((Integer) obj).intValue());

        obj = ConvertUtil.convertSearchValue(model.getAttributeModel("birthWeek"), "2015-05");
        Assert.assertTrue(obj instanceof Date);
        Assert.assertEquals(DateUtils.createDate("26012015"), obj);
    }

    @Test
    public void testConvertToPresentationValue() {
        EntityModel<TestEntity> model = emf.getModel(TestEntity.class);

        Object s = ConvertUtil.convertToPresentationValue(model.getAttributeModel("age"), 12L);
        Assert.assertEquals("12", s);

        s = ConvertUtil.convertToPresentationValue(model.getAttributeModel("discount"),
                BigDecimal.valueOf(17.79));
        Assert.assertEquals("17,79", s);

        s = ConvertUtil.convertToPresentationValue(model.getAttributeModel("id"), 1234);
        Assert.assertEquals("1234", s);

        s = ConvertUtil.convertToPresentationValue(model.getAttributeModel("birthWeek"),
                DateUtils.createDate("01022015"));
        Assert.assertEquals("2015-05", s);
    }
}
