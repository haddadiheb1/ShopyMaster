package com.hanouti.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hanouti.app.data.CreditEntry;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CreditEntryDao_Impl implements CreditEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CreditEntry> __insertionAdapterOfCreditEntry;

  private final EntityDeletionOrUpdateAdapter<CreditEntry> __deletionAdapterOfCreditEntry;

  private final EntityDeletionOrUpdateAdapter<CreditEntry> __updateAdapterOfCreditEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForClient;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CreditEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCreditEntry = new EntityInsertionAdapter<CreditEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `credit_entries` (`id`,`clientId`,`deltaAmount`,`timestampMillis`,`note`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientId());
        statement.bindDouble(3, entity.getDeltaAmount());
        statement.bindLong(4, entity.getTimestampMillis());
        if (entity.getNote() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNote());
        }
      }
    };
    this.__deletionAdapterOfCreditEntry = new EntityDeletionOrUpdateAdapter<CreditEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `credit_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCreditEntry = new EntityDeletionOrUpdateAdapter<CreditEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `credit_entries` SET `id` = ?,`clientId` = ?,`deltaAmount` = ?,`timestampMillis` = ?,`note` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientId());
        statement.bindDouble(3, entity.getDeltaAmount());
        statement.bindLong(4, entity.getTimestampMillis());
        if (entity.getNote() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNote());
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteForClient = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM credit_entries WHERE clientId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM credit_entries";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CreditEntry entry, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCreditEntry.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<CreditEntry> entries,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCreditEntry.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CreditEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCreditEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CreditEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCreditEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteForClient(final int clientId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForClient.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, clientId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteForClient.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CreditEntry>> observeForClient(final int clientId) {
    final String _sql = "SELECT * FROM credit_entries WHERE clientId = ? ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_entries"}, new Callable<List<CreditEntry>>() {
      @Override
      @NonNull
      public List<CreditEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfDeltaAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "deltaAmount");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<CreditEntry> _result = new ArrayList<CreditEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpClientId;
            _tmpClientId = _cursor.getInt(_cursorIndexOfClientId);
            final double _tmpDeltaAmount;
            _tmpDeltaAmount = _cursor.getDouble(_cursorIndexOfDeltaAmount);
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new CreditEntry(_tmpId,_tmpClientId,_tmpDeltaAmount,_tmpTimestampMillis,_tmpNote);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAll(final Continuation<? super List<CreditEntry>> $completion) {
    final String _sql = "SELECT * FROM credit_entries ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CreditEntry>>() {
      @Override
      @NonNull
      public List<CreditEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfDeltaAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "deltaAmount");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<CreditEntry> _result = new ArrayList<CreditEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpClientId;
            _tmpClientId = _cursor.getInt(_cursorIndexOfClientId);
            final double _tmpDeltaAmount;
            _tmpDeltaAmount = _cursor.getDouble(_cursorIndexOfDeltaAmount);
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new CreditEntry(_tmpId,_tmpClientId,_tmpDeltaAmount,_tmpTimestampMillis,_tmpNote);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
