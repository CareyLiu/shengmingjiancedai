package com.smarthome.magic.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;


import com.smarthome.magic.db.table.HistoryRecord;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "history_record_table".
*/
public class HistoryRecordDao extends AbstractDao<HistoryRecord, Long> {

    public static final String TABLENAME = "history_record_table";

    /**
     * Properties of entity HistoryRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Date = new Property(2, Long.class, "date", false, "DATE");
        public final static Property Type = new Property(3, int.class, "type", false, "TYPE");
    }


    public HistoryRecordDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"history_record_table\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"NAME\" TEXT," + // 1: name
                "\"DATE\" INTEGER," + // 2: date
                "\"TYPE\" INTEGER NOT NULL );"); // 3: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"history_record_table\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HistoryRecord entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(3, date);
        }
        stmt.bindLong(4, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HistoryRecord entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(3, date);
        }
        stmt.bindLong(4, entity.getType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public HistoryRecord readEntity(Cursor cursor, int offset) {
        HistoryRecord entity = new HistoryRecord( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // date
            cursor.getInt(offset + 3) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HistoryRecord entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setType(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HistoryRecord entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HistoryRecord entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HistoryRecord entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
