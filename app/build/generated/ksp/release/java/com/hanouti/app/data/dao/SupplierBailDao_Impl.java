package com.hanouti.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hanouti.app.data.SupplierBail;
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
public final class SupplierBailDao_Impl implements SupplierBailDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SupplierBail> __insertionAdapterOfSupplierBail;

  private final EntityDeletionOrUpdateAdapter<SupplierBail> __deletionAdapterOfSupplierBail;

  private final EntityDeletionOrUpdateAdapter<SupplierBail> __updateAdapterOfSupplierBail;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SupplierBailDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSupplierBail = new EntityInsertionAdapter<SupplierBail>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `supplier_bails` (`id`,`supplierName`,`amount`,`photoUri`,`timestampMillis`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplierBail entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSupplierName());
        statement.bindDouble(3, entity.getAmount());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhotoUri());
        }
        statement.bindLong(5, entity.getTimestampMillis());
      }
    };
    this.__deletionAdapterOfSupplierBail = new EntityDeletionOrUpdateAdapter<SupplierBail>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `supplier_bails` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplierBail entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSupplierBail = new EntityDeletionOrUpdateAdapter<SupplierBail>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `supplier_bails` SET `id` = ?,`supplierName` = ?,`amount` = ?,`photoUri` = ?,`timestampMillis` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplierBail entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSupplierName());
        statement.bindDouble(3, entity.getAmount());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhotoUri());
        }
        statement.bindLong(5, entity.getTimestampMillis());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM supplier_bails";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SupplierBail bail, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSupplierBail.insertAndReturnId(bail);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<SupplierBail> bails,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSupplierBail.insert(bails);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final SupplierBail bail, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSupplierBail.handle(bail);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SupplierBail bail, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSupplierBail.handle(bail);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Flow<List<SupplierBail>> observeAll() {
    final String _sql = "SELECT * FROM supplier_bails ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"supplier_bails"}, new Callable<List<SupplierBail>>() {
      @Override
      @NonNull
      public List<SupplierBail> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSupplierName = CursorUtil.getColumnIndexOrThrow(_cursor, "supplierName");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final List<SupplierBail> _result = new ArrayList<SupplierBail>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SupplierBail _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSupplierName;
            _tmpSupplierName = _cursor.getString(_cursorIndexOfSupplierName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            _item = new SupplierBail(_tmpId,_tmpSupplierName,_tmpAmount,_tmpPhotoUri,_tmpTimestampMillis);
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
  public Object getAll(final Continuation<? super List<SupplierBail>> $completion) {
    final String _sql = "SELECT * FROM supplier_bails ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SupplierBail>>() {
      @Override
      @NonNull
      public List<SupplierBail> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSupplierName = CursorUtil.getColumnIndexOrThrow(_cursor, "supplierName");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final List<SupplierBail> _result = new ArrayList<SupplierBail>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SupplierBail _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSupplierName;
            _tmpSupplierName = _cursor.getString(_cursorIndexOfSupplierName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            _item = new SupplierBail(_tmpId,_tmpSupplierName,_tmpAmount,_tmpPhotoUri,_tmpTimestampMillis);
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

  @Override
  public Flow<SupplierBail> observeById(final int id) {
    final String _sql = "SELECT * FROM supplier_bails WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"supplier_bails"}, new Callable<SupplierBail>() {
      @Override
      @Nullable
      public SupplierBail call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSupplierName = CursorUtil.getColumnIndexOrThrow(_cursor, "supplierName");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final SupplierBail _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSupplierName;
            _tmpSupplierName = _cursor.getString(_cursorIndexOfSupplierName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            _result = new SupplierBail(_tmpId,_tmpSupplierName,_tmpAmount,_tmpPhotoUri,_tmpTimestampMillis);
          } else {
            _result = null;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
