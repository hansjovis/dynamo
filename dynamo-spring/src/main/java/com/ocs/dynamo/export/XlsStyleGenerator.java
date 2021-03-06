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
package com.ocs.dynamo.export;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.CellStyle;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.AttributeModel;

/**
 * An interface for a custom style generator
 * 
 * @author bas.rutten
 *
 * @param <ID>
 * @param <T>
 */
public interface XlsStyleGenerator<ID extends Serializable, T extends AbstractEntity<ID>> {

	/**
	 * Returns the style to use for the header
	 * 
	 * @param index
	 * @return
	 */
	CellStyle getHeaderStyle(int index);

	/**
	 * Returns the cell style for a certain cell
	 * 
	 * @param index
	 *            the column index of the cell
	 * @param entity
	 *            the entity for which a value is displayed in the cell
	 * @param value
	 *            the value of the cell
	 * @param attributeModel
	 *            the attribute model
	 * @return
	 */
	CellStyle getCellStyle(int index, T entity, Object value, AttributeModel am);

}
