package org.greenplum.pxf.api.io;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.util.EnumSet;

/**
 * Supported Data Types and OIDs (GPDB Data Type identifiers).
 * There's a one-to-one match between a Data Type and it's corresponding OID.
 */
public enum DataType {
    BOOLEAN(16),
    BYTEA(17),
    BIGINT(20),
    SMALLINT(21),
    INTEGER(23),
    TEXT(25),
    REAL(700),
    FLOAT8(701),
    /**
     * char(length), blank-padded string, fixed storage length
     */
    BPCHAR(1042),
    /**
     * varchar(length), non-blank-padded string, variable storage length
     */
    VARCHAR(1043),
    DATE(1082),
    TIME(1083),
    TIMESTAMP(1114),
    TIMESTAMP_WITH_TIME_ZONE(1184),
    NUMERIC(1700),

    INT2ARRAY(1005),
    INT4ARRAY(1007),
    INT8ARRAY(1016),
    BOOLARRAY(1000),
    TEXTARRAY(1009),
    FLOAT4ARRAY(1021),
    FLOAT8ARRAY(1022),

    UNSUPPORTED_TYPE(-1);

    private static final int[] OID_ARRAY;
    private static final DataType[] DATA_TYPES;
    private static final int[] NOT_TEXT = {BIGINT.OID, BOOLEAN.OID, BYTEA.OID,
            FLOAT8.OID, INTEGER.OID, REAL.OID, SMALLINT.OID};

    static {
        INT2ARRAY.typeElem = SMALLINT;
        INT4ARRAY.typeElem = INTEGER;
        INT8ARRAY.typeElem = BIGINT;
        BOOLARRAY.typeElem = BOOLEAN;
        TEXTARRAY.typeElem = TEXT;

        EnumSet<DataType> set = EnumSet.allOf(DataType.class);
        OID_ARRAY = new int[set.size()];
        DATA_TYPES = new DataType[set.size()];

        int index = 0;
        for (DataType type : set) {
            OID_ARRAY[index] = type.OID;
            DATA_TYPES[index] = type;
            index++;
        }
    }

    private final int OID;
    private DataType typeElem;

    DataType(int OID) {
        this.OID = OID;
    }

    /**
     * Utility method for converting an {@link #OID} to a {@link #DataType}.
     *
     * @param OID the oid to be converted
     * @return the corresponding DataType if exists, else returns {@link #UNSUPPORTED_TYPE}
     */
    public static DataType get(int OID) {
        for (int i = 0; i < OID_ARRAY.length; i++) {
            if (OID == OID_ARRAY[i]) {
                return DATA_TYPES[i];
            }
        }
        return UNSUPPORTED_TYPE;
    }

    public static boolean isArrayType(int OID) {
        DataType type = get(OID);
        return type != UNSUPPORTED_TYPE && type.typeElem != null;
    }

    public static boolean isTextForm(int OID) {
        for (int value : NOT_TEXT) {
            if (OID == value) return false;
        }
        return true;
    }

    public int getOID() {
        return OID;
    }

    public DataType getTypeElem() {
        return typeElem;
    }
}
