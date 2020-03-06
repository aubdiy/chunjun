/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.flinkx.metadatasync.reader.reader;

import com.dtstack.flinkx.config.DataTransferConfig;
import com.dtstack.flinkx.config.ReaderConfig;
import com.dtstack.flinkx.metadatasync.reader.inputformat.MetaDataInputFormat;
import com.dtstack.flinkx.metadatasync.reader.inputformat.MetaDataInputFormatBuilder;
import com.dtstack.flinkx.reader.DataReader;
import com.dtstack.flinkx.reader.MetaColumn;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

import java.util.List;

/**
 * @author : tiezhu
 * @date : 2020/3/5
 * @description :
 */
public class MetaDataReader extends DataReader {
    protected String dbUrl;
    protected String table;
    protected String username;
    protected String pasword;

    public MetaDataReader(DataTransferConfig config, StreamExecutionEnvironment env) {
        super(config, env);
        ReaderConfig readerConfig = config.getJob().getContent().get(0).getReader();

        dbUrl = readerConfig.getParameter().getConnection().get(0).getJdbcUrl().get(0);
        table = readerConfig.getParameter().getConnection().get(0).getTable().get(0);
        username = readerConfig.getParameter().getStringVal("username");
        pasword = readerConfig.getParameter().getStringVal("password");
    }

    @Override
    public DataStream<Row> readData() {
        MetaDataInputFormatBuilder builder = new MetaDataInputFormatBuilder(new MetaDataInputFormat());

        builder.setDBUrl(dbUrl);
        builder.setUsername(username);
        builder.setPassword(pasword);
        builder.setTable(table);

        return createInput(builder.finish());
    }
}
