/*
 * Copyright 2021 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.datatools.graphql.service;

import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.data.PersistenceHints;
import io.jmix.datatools.EntityRestore;
import io.jmix.graphql.datafetcher.GqlEntityValidationException;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@GraphQLApi
@Service("dtgql_EntityRestoreService")
public class EntityRestoreGraphQLService {
    @Autowired
    protected EntityRestore entityRestore;
    @Autowired
    protected Metadata metadata;
    @Autowired
    protected DataManager dataManager;

    @GraphQLMutation(description = "Restores soft deleted entities. Returns amount of restored entities (initially selected + dependent).")
    public int restoreEntities(@GraphQLNonNull @GraphQLArgument(name = "className", description = "Soft-delete entity metaclass name")
                                       String className,
                               @GraphQLNonNull @GraphQLArgument(name = "ids")
                                       Collection<String> ids) {
        try {
            MetaClass metaClass = metadata.getClass(className);
            Collection<Object> convertedIds = convertIds(ids);
            List<Object> entities = dataManager.load(metaClass.getJavaClass())
                    .ids(convertedIds)
                    .hint(PersistenceHints.SOFT_DELETION, false)
                    .list();
            return entityRestore.restoreEntities(entities);
        } catch (IllegalArgumentException e) {
            throw new GqlEntityValidationException(e, e.getMessage());
        }
    }

    protected Collection<Object> convertIds(Collection<String> ids) {

        //todo support not only UUID types of id
        return ids.stream().map(UUID::fromString).collect(Collectors.toList());
    }
}
