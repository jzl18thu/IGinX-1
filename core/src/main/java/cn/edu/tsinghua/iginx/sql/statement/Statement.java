package cn.edu.tsinghua.iginx.sql.statement;

import cn.edu.tsinghua.iginx.exceptions.ExecutionException;
import cn.edu.tsinghua.iginx.thrift.ExecuteSqlResp;

public abstract class Statement {

    public StatementType statementType = StatementType.NULL;

    public ExecuteSqlResp execute(long sessionId) throws ExecutionException {
        throw new ExecutionException("this function should not be executed.");
    }

    public enum StatementType {
        NULL,
        SELECT,
        INSERT,
        DELETE,
        ADD_STORAGE_ENGINE,
        SHOW_REPLICATION,
        COUNT_POINTS,
        CLEAR_DATA,
        DELETE_TIME_SERIES,
        SHOW_TIME_SERIES,
        SHOW_CLUSTER_INFO,
        ADD_USER,
        UPDATE_USER,
        DELETE_USER,
        SHOW_USER
    }
}