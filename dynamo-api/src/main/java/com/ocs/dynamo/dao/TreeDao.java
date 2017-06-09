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
package com.ocs.dynamo.dao;

import java.util.List;

import com.ocs.dynamo.domain.AbstractEntity;

/**
 * DAO for managing entities that support a tree structure
 *
 * @param <ID>
 *            type of the primary key of the entity
 * @param <T>
 *            type of the entity
 */
public interface TreeDao<ID, T extends AbstractEntity<ID>> extends BaseDao<ID, T> {

    /**
     * Find all root objects.
     * 
     * @return All root domain objects
     */
    List<T> findByParentIsNull();

    /**
     * Find all children for a given parent.
     * 
     * @param parent
     *            The parent
     * @return ALl children for the given parent
     */
    List<T> findByParent(T parent);

}
