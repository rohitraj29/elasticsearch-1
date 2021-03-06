/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.monitor.process;

import org.elasticsearch.bootstrap.Bootstrap;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 */
public class ProcessInfo implements Streamable, Serializable, ToXContent {

    long refreshInterval;

    private long id;

    private long maxFileDescriptors = -1;

    private boolean mlockall;

    ProcessInfo() {

    }

    public ProcessInfo(long id, long maxFileDescriptors) {
        this.id = id;
        this.maxFileDescriptors = maxFileDescriptors;
        this.mlockall = Bootstrap.isMemoryLocked();
    }

    public long refreshInterval() {
        return this.refreshInterval;
    }

    public long getRefreshInterval() {
        return this.refreshInterval;
    }

    /**
     * The process id.
     */
    public long id() {
        return this.id;
    }

    /**
     * The process id.
     */
    public long getId() {
        return id();
    }

    public long maxFileDescriptors() {
        return this.maxFileDescriptors;
    }

    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }

    public boolean mlockAll() {
        return mlockall;
    }

    public boolean isMlockall() {
        return mlockall;
    }

    static final class Fields {
        static final XContentBuilderString PROCESS = new XContentBuilderString("process");
        static final XContentBuilderString REFRESH_INTERVAL = new XContentBuilderString("refresh_interval");
        static final XContentBuilderString REFRESH_INTERVAL_IN_MILLIS = new XContentBuilderString("refresh_interval_in_millis");
        static final XContentBuilderString ID = new XContentBuilderString("id");
        static final XContentBuilderString MAX_FILE_DESCRIPTORS = new XContentBuilderString("max_file_descriptors");
        static final XContentBuilderString MLOCKALL = new XContentBuilderString("mlockall");
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(Fields.PROCESS);
        builder.timeValueField(Fields.REFRESH_INTERVAL_IN_MILLIS, Fields.REFRESH_INTERVAL, refreshInterval);
        builder.field(Fields.ID, id);
        builder.field(Fields.MAX_FILE_DESCRIPTORS, maxFileDescriptors);
        builder.field(Fields.MLOCKALL, mlockall);
        builder.endObject();
        return builder;
    }

    public static ProcessInfo readProcessInfo(StreamInput in) throws IOException {
        ProcessInfo info = new ProcessInfo();
        info.readFrom(in);
        return info;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        refreshInterval = in.readLong();
        id = in.readLong();
        maxFileDescriptors = in.readLong();
        mlockall = in.readBoolean();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeLong(refreshInterval);
        out.writeLong(id);
        out.writeLong(maxFileDescriptors);
        out.writeBoolean(mlockall);
    }
}
