package com.hanouti.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hanouti.app.data.SaleItem;
import com.hanouti.app.data.SaleTransaction;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
public final class SalesDao_Impl implements SalesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaleTransaction> __insertionAdapterOfSaleTransaction;

  private final EntityInsertionAdapter<SaleItem> __insertionAdapterOfSaleItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllItems;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSales;

  public SalesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaleTransaction = new EntityInsertionAdapter<SaleTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sales` (`id`,`timestampMillis`,`totalAmount`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaleTransaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestampMillis());
        statement.bindDouble(3, entity.getTotalAmount());
      }
    };
    this.__insertionAdapterOfSaleItem = new EntityInsertionAdapter<SaleItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sale_items` (`id`,`transactionId`,`productBarcode`,`productName`,`priceAtSale`,`quantity`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaleItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTransactionId());
        statement.bindString(3, entity.getProductBarcode());
        statement.bindString(4, entity.getProductName());
        statement.bindDouble(5, entity.getPriceAtSale());
        statement.bindLong(6, entity.getQuantity());
      }
    };
    this.__preparedStmtOfDeleteAllItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sale_items";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllSales = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sales";
        return _query;
      }
    };
  }

  @Override
  public Object insertSale(final SaleTransaction sale,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSaleTransaction.insertAndReturnId(sale);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertItems(final List<SaleItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaleItem.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSales(final List<SaleTransaction> sales,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfSaleTransaction.insertAndReturnIdsList(sales);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllItems.acquire();
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
          __preparedStmtOfDeleteAllItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllSales(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSales.acquire();
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
          __preparedStmtOfDeleteAllSales.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailyTotalRow>> observeDailyTotals() {
    final String _sql = "SELECT date(timestampMillis/1000, 'unixepoch', 'localtime') AS day, SUM(totalAmount) AS total FROM sales GROUP BY day ORDER BY day DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<List<DailyTotalRow>>() {
      @Override
      @NonNull
      public List<DailyTotalRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDay = 0;
          final int _cursorIndexOfTotal = 1;
          final List<DailyTotalRow> _result = new ArrayList<DailyTotalRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyTotalRow _item;
            final String _tmpDay;
            _tmpDay = _cursor.getString(_cursorIndexOfDay);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new DailyTotalRow(_tmpDay,_tmpTotal);
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
  public Flow<List<SaleTransaction>> observeSalesForDate(final String date) {
    final String _sql = "SELECT * FROM sales WHERE date(timestampMillis/1000, 'unixepoch', 'localtime') = ? ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<List<SaleTransaction>>() {
      @Override
      @NonNull
      public List<SaleTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final List<SaleTransaction> _result = new ArrayList<SaleTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SaleTransaction _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            _item = new SaleTransaction(_tmpId,_tmpTimestampMillis,_tmpTotalAmount);
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
  public Flow<List<SaleItem>> observeItemsForSale(final int transactionId) {
    final String _sql = "SELECT * FROM sale_items WHERE transactionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, transactionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sale_items"}, new Callable<List<SaleItem>>() {
      @Override
      @NonNull
      public List<SaleItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionId");
          final int _cursorIndexOfProductBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "productBarcode");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfPriceAtSale = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtSale");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final List<SaleItem> _result = new ArrayList<SaleItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SaleItem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpTransactionId;
            _tmpTransactionId = _cursor.getInt(_cursorIndexOfTransactionId);
            final String _tmpProductBarcode;
            _tmpProductBarcode = _cursor.getString(_cursorIndexOfProductBarcode);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final double _tmpPriceAtSale;
            _tmpPriceAtSale = _cursor.getDouble(_cursorIndexOfPriceAtSale);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            _item = new SaleItem(_tmpId,_tmpTransactionId,_tmpProductBarcode,_tmpProductName,_tmpPriceAtSale,_tmpQuantity);
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
  public Flow<Integer> observeSalesCount() {
    final String _sql = "SELECT COUNT(*) FROM sales";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Flow<Double> observeTodaySalesAmount() {
    final String _sql = "SELECT IFNULL(SUM(totalAmount), 0) FROM sales WHERE date(timestampMillis/1000, 'unixepoch', 'localtime') = date('now','localtime')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<Integer> observeSalesCountInRange(final long startMillis, final long endMillis) {
    final String _sql = "SELECT COUNT(*) FROM sales WHERE timestampMillis BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startMillis);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endMillis);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Flow<Double> observeSalesAmountInRange(final long startMillis, final long endMillis) {
    final String _sql = "SELECT IFNULL(SUM(totalAmount), 0) FROM sales WHERE timestampMillis BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startMillis);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endMillis);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getAllSales(final Continuation<? super List<SaleTransaction>> $completion) {
    final String _sql = "SELECT * FROM sales ORDER BY timestampMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SaleTransaction>>() {
      @Override
      @NonNull
      public List<SaleTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampMillis");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final List<SaleTransaction> _result = new ArrayList<SaleTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SaleTransaction _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestampMillis;
            _tmpTimestampMillis = _cursor.getLong(_cursorIndexOfTimestampMillis);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            _item = new SaleTransaction(_tmpId,_tmpTimestampMillis,_tmpTotalAmount);
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
