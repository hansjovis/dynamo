/*
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ocs.dynamo.importer.impl;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.ocs.dynamo.exception.OCSImportException;
import com.ocs.dynamo.importer.XlsField;
import com.ocs.dynamo.importer.dto.AbstractDTO;
import com.ocs.dynamo.utils.ClassUtils;

/**
 * Base class for smart upload functionality
 * 
 * @author bas.rutten
 * @param <R>
 *            the type of a single row
 * @param <U>
 *            the type of a single cell or field
 */
public abstract class BaseImporter<R, U> {

    private static final double PERCENTAGE_FACTOR = 100.;

    /**
     * Counts the number of rows in the input. This method will count all rows, including the
     * header, and will not check if any of the rows are valid
     * 
     * @param bytes
     * @param row
     * @param column
     * @return
     */
    public abstract int countRows(byte[] bytes, int row, int column);

    /**
     * Retrieves a boolean value from the input and falls back to a default if the value is empty or
     * not defined
     * 
     * @param unit
     * @param field
     * @return
     */
    protected abstract Boolean getBooleanValueWithDefault(U unit, XlsField field);

    @SuppressWarnings("unchecked")
    private Object getFieldValue(PropertyDescriptor d, U unit, XlsField field) {
        Object obj = null;
        if (String.class.equals(d.getPropertyType())) {
            String value = getStringValueWithDefault(unit, field);
            if (value != null) {
                value = value.trim();
            }
            obj = StringUtils.isEmpty(value) ? null : value;
        } else if (d.getPropertyType().isEnum()) {
            String value = getStringValueWithDefault(unit, field);
            if (value != null) {
                value = value.trim();
                try {
                    obj = Enum.valueOf(d.getPropertyType().asSubclass(Enum.class),
                            value.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new OCSImportException("Value " + value
                            + " cannot be translated to a valid enumeration value", ex);
                }
            }

        } else if (Number.class.isAssignableFrom(d.getPropertyType())) {
            // numeric field

            Double value = getNumericValueWithDefault(unit, field);
            if (value != null) {

                // if the field represents a percentage but it is
                // received as a
                // fraction, we multiply it by 100
                if (field.percentage() && isPercentageCorrectionSupported()) {
                    value = PERCENTAGE_FACTOR * value;
                }

                // illegal negative value
                if (field.cannotBeNegative() && value < 0.0) {
                    throw new OCSImportException("Negative value " + value + " found for field '"
                            + d.getName() + "'");
                }

                if (Integer.class.equals(d.getPropertyType())) {
                    obj = new Integer(value.intValue());
                } else if (BigDecimal.class.equals(d.getPropertyType())) {
                    obj = BigDecimal.valueOf(value.doubleValue());
                } else {
                    // by default, use a double
                    obj = value;
                }
            }
        } else if (Boolean.class.isAssignableFrom(d.getPropertyType())) {
            return getBooleanValueWithDefault(unit, field);
        }
        return obj;
    }

    /**
     * Retrieves a numeric value from the input and falls back to a default if the value is empty or
     * not defined
     * 
     * @param unit
     * @param field
     * @return
     */
    protected abstract Double getNumericValueWithDefault(U unit, XlsField field);

    /**
     * Retrieves a string from the input and falls back to a default if the value is empty or not
     * defined
     * 
     * @param unit
     * @param field
     * @return
     */
    protected abstract String getStringValueWithDefault(U unit, XlsField field);

    /**
     * Retrieves a unit (a single cell or field) from a row
     * 
     * @param row
     * @param field
     * @return
     */
    protected abstract U getUnit(R row, XlsField field);

    /**
     * Indicates whether fraction values are automatically converted to percentages
     * 
     * @return
     */
    public abstract boolean isPercentageCorrectionSupported();

    /**
     * Checks whether a field index is within the range of available collumns
     * 
     * @param row
     * @param field
     * @return
     */
    protected abstract boolean isWithinRange(R row, XlsField field);

    /**
     * Processes a single row from the input and turns it into an object
     * 
     * @param rowNum
     * @param row
     * @param clazz
     * @return
     */
    public <T extends AbstractDTO> T processRow(int rowNum, R row, Class<T> clazz) {
        T t = ClassUtils.instantiateClass(clazz);
        t.setRowNum(rowNum);

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor d : descriptors) {
            XlsField field = ClassUtils.getAnnotation(clazz, d.getName(), XlsField.class);
            if (field != null) {
                if (isWithinRange(row, field)) {
                    U unit = getUnit(row, field);

                    Object obj = getFieldValue(d, unit, field);
                    if (obj != null) {
                        ClassUtils.setFieldValue(t, d.getName(), obj);
                    } else if (field.required()) {
                        // a required value is missing!
                        throw new OCSImportException("Required value for field '" + d.getName()
                                + "' is missing");
                    }
                } else {
                    throw new OCSImportException("Row doesn't have enough columns");
                }
            }
        }

        return t;
    }
}
