/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.tenant.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.ning.billing.tenant.api.DefaultTenantKV;
import com.ning.billing.tenant.api.TenantKV;
import com.ning.billing.tenant.dao.TenantKVSqlDao.TenantKVMapper;
import com.ning.billing.util.callcontext.InternalCallContext;
import com.ning.billing.util.dao.MapperBase;
import com.ning.billing.util.dao.UuidMapper;
import com.ning.billing.util.entity.dao.EntitySqlDao;
import com.ning.billing.util.entity.dao.EntitySqlDaoStringTemplate;

@EntitySqlDaoStringTemplate
@RegisterMapper({UuidMapper.class, TenantKVMapper.class})
public interface TenantKVSqlDao extends EntitySqlDao<TenantKVModelDao, TenantKV> {

    // TODO should take a context

    @SqlQuery
    public List<TenantKVModelDao> getTenantValueForKey(@Bind("key") final String key,
                                                       @Bind("tenantRecordId") Long tenantRecordId);

    @SqlUpdate
    public void insertTenantKeyValue(@Bind("id") String id,
                                     @Bind("key") final String key,
                                     @Bind("value") final String value,
                                     @Bind("tenantRecordId") Long tenantRecordId,
                                     @BindBean final InternalCallContext context);

    @SqlUpdate
    public void deleteTenantKey(@Bind("key") final String key,
                                @Bind("tenantRecordId") Long tenantRecordId);

    public class TenantKVMapper extends MapperBase implements ResultSetMapper<TenantKV> {

        @Override
        public TenantKV map(final int index, final ResultSet result, final StatementContext context) throws SQLException {
            final UUID id = getUUID(result, "id");
            final String key = result.getString("t_key");
            final String value = result.getString("t_value");
            final DateTime createdDate = getDateTime(result, "created_date");
            final DateTime updatedDate = getDateTime(result, "updated_date");
            return new DefaultTenantKV(id, key, value, createdDate, updatedDate);
        }
    }
}
