package com.hanouti.app.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.hanouti.app.data.dao.ClientDao;
import com.hanouti.app.data.dao.ClientDao_Impl;
import com.hanouti.app.data.dao.CreditEntryDao;
import com.hanouti.app.data.dao.CreditEntryDao_Impl;
import com.hanouti.app.data.dao.ProductDao;
import com.hanouti.app.data.dao.ProductDao_Impl;
import com.hanouti.app.data.dao.SalesDao;
import com.hanouti.app.data.dao.SalesDao_Impl;
import com.hanouti.app.data.dao.SupplierBailDao;
import com.hanouti.app.data.dao.SupplierBailDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ClientDao _clientDao;

  private volatile CreditEntryDao _creditEntryDao;

  private volatile SupplierBailDao _supplierBailDao;

  private volatile ProductDao _productDao;

  private volatile SalesDao _salesDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `clients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `credit` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `credit_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` INTEGER NOT NULL, `deltaAmount` REAL NOT NULL, `timestampMillis` INTEGER NOT NULL, `note` TEXT, FOREIGN KEY(`clientId`) REFERENCES `clients`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_credit_entries_clientId` ON `credit_entries` (`clientId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_credit_entries_timestampMillis` ON `credit_entries` (`timestampMillis`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `supplier_bails` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `supplierName` TEXT NOT NULL, `amount` REAL NOT NULL, `photoUri` TEXT, `timestampMillis` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`barcode` TEXT NOT NULL, `name` TEXT NOT NULL, `price` REAL NOT NULL, PRIMARY KEY(`barcode`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sales` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestampMillis` INTEGER NOT NULL, `totalAmount` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sale_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transactionId` INTEGER NOT NULL, `productBarcode` TEXT NOT NULL, `productName` TEXT NOT NULL, `priceAtSale` REAL NOT NULL, `quantity` INTEGER NOT NULL, FOREIGN KEY(`transactionId`) REFERENCES `sales`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sale_items_transactionId` ON `sale_items` (`transactionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sale_items_productBarcode` ON `sale_items` (`productBarcode`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6efcf6590c551df767259d762f5f5a68')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `clients`");
        db.execSQL("DROP TABLE IF EXISTS `credit_entries`");
        db.execSQL("DROP TABLE IF EXISTS `supplier_bails`");
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `sales`");
        db.execSQL("DROP TABLE IF EXISTS `sale_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsClients = new HashMap<String, TableInfo.Column>(3);
        _columnsClients.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("credit", new TableInfo.Column("credit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClients = new TableInfo("clients", _columnsClients, _foreignKeysClients, _indicesClients);
        final TableInfo _existingClients = TableInfo.read(db, "clients");
        if (!_infoClients.equals(_existingClients)) {
          return new RoomOpenHelper.ValidationResult(false, "clients(com.hanouti.app.data.Client).\n"
                  + " Expected:\n" + _infoClients + "\n"
                  + " Found:\n" + _existingClients);
        }
        final HashMap<String, TableInfo.Column> _columnsCreditEntries = new HashMap<String, TableInfo.Column>(5);
        _columnsCreditEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditEntries.put("clientId", new TableInfo.Column("clientId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditEntries.put("deltaAmount", new TableInfo.Column("deltaAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditEntries.put("timestampMillis", new TableInfo.Column("timestampMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditEntries.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCreditEntries = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCreditEntries.add(new TableInfo.ForeignKey("clients", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCreditEntries = new HashSet<TableInfo.Index>(2);
        _indicesCreditEntries.add(new TableInfo.Index("index_credit_entries_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        _indicesCreditEntries.add(new TableInfo.Index("index_credit_entries_timestampMillis", false, Arrays.asList("timestampMillis"), Arrays.asList("ASC")));
        final TableInfo _infoCreditEntries = new TableInfo("credit_entries", _columnsCreditEntries, _foreignKeysCreditEntries, _indicesCreditEntries);
        final TableInfo _existingCreditEntries = TableInfo.read(db, "credit_entries");
        if (!_infoCreditEntries.equals(_existingCreditEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "credit_entries(com.hanouti.app.data.CreditEntry).\n"
                  + " Expected:\n" + _infoCreditEntries + "\n"
                  + " Found:\n" + _existingCreditEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsSupplierBails = new HashMap<String, TableInfo.Column>(5);
        _columnsSupplierBails.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplierBails.put("supplierName", new TableInfo.Column("supplierName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplierBails.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplierBails.put("photoUri", new TableInfo.Column("photoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplierBails.put("timestampMillis", new TableInfo.Column("timestampMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSupplierBails = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSupplierBails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSupplierBails = new TableInfo("supplier_bails", _columnsSupplierBails, _foreignKeysSupplierBails, _indicesSupplierBails);
        final TableInfo _existingSupplierBails = TableInfo.read(db, "supplier_bails");
        if (!_infoSupplierBails.equals(_existingSupplierBails)) {
          return new RoomOpenHelper.ValidationResult(false, "supplier_bails(com.hanouti.app.data.SupplierBail).\n"
                  + " Expected:\n" + _infoSupplierBails + "\n"
                  + " Found:\n" + _existingSupplierBails);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(3);
        _columnsProducts.put("barcode", new TableInfo.Column("barcode", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("price", new TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.hanouti.app.data.Product).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsSales = new HashMap<String, TableInfo.Column>(3);
        _columnsSales.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("timestampMillis", new TableInfo.Column("timestampMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("totalAmount", new TableInfo.Column("totalAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSales = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSales = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSales = new TableInfo("sales", _columnsSales, _foreignKeysSales, _indicesSales);
        final TableInfo _existingSales = TableInfo.read(db, "sales");
        if (!_infoSales.equals(_existingSales)) {
          return new RoomOpenHelper.ValidationResult(false, "sales(com.hanouti.app.data.SaleTransaction).\n"
                  + " Expected:\n" + _infoSales + "\n"
                  + " Found:\n" + _existingSales);
        }
        final HashMap<String, TableInfo.Column> _columnsSaleItems = new HashMap<String, TableInfo.Column>(6);
        _columnsSaleItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("transactionId", new TableInfo.Column("transactionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("productBarcode", new TableInfo.Column("productBarcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("productName", new TableInfo.Column("productName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("priceAtSale", new TableInfo.Column("priceAtSale", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSaleItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSaleItems.add(new TableInfo.ForeignKey("sales", "CASCADE", "NO ACTION", Arrays.asList("transactionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSaleItems = new HashSet<TableInfo.Index>(2);
        _indicesSaleItems.add(new TableInfo.Index("index_sale_items_transactionId", false, Arrays.asList("transactionId"), Arrays.asList("ASC")));
        _indicesSaleItems.add(new TableInfo.Index("index_sale_items_productBarcode", false, Arrays.asList("productBarcode"), Arrays.asList("ASC")));
        final TableInfo _infoSaleItems = new TableInfo("sale_items", _columnsSaleItems, _foreignKeysSaleItems, _indicesSaleItems);
        final TableInfo _existingSaleItems = TableInfo.read(db, "sale_items");
        if (!_infoSaleItems.equals(_existingSaleItems)) {
          return new RoomOpenHelper.ValidationResult(false, "sale_items(com.hanouti.app.data.SaleItem).\n"
                  + " Expected:\n" + _infoSaleItems + "\n"
                  + " Found:\n" + _existingSaleItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6efcf6590c551df767259d762f5f5a68", "7bc3744c6a92ad892cad4f84c15748e7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "clients","credit_entries","supplier_bails","products","sales","sale_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `clients`");
      _db.execSQL("DELETE FROM `credit_entries`");
      _db.execSQL("DELETE FROM `supplier_bails`");
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `sales`");
      _db.execSQL("DELETE FROM `sale_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ClientDao.class, ClientDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CreditEntryDao.class, CreditEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SupplierBailDao.class, SupplierBailDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SalesDao.class, SalesDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ClientDao clientDao() {
    if (_clientDao != null) {
      return _clientDao;
    } else {
      synchronized(this) {
        if(_clientDao == null) {
          _clientDao = new ClientDao_Impl(this);
        }
        return _clientDao;
      }
    }
  }

  @Override
  public CreditEntryDao creditEntryDao() {
    if (_creditEntryDao != null) {
      return _creditEntryDao;
    } else {
      synchronized(this) {
        if(_creditEntryDao == null) {
          _creditEntryDao = new CreditEntryDao_Impl(this);
        }
        return _creditEntryDao;
      }
    }
  }

  @Override
  public SupplierBailDao supplierBailDao() {
    if (_supplierBailDao != null) {
      return _supplierBailDao;
    } else {
      synchronized(this) {
        if(_supplierBailDao == null) {
          _supplierBailDao = new SupplierBailDao_Impl(this);
        }
        return _supplierBailDao;
      }
    }
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public SalesDao salesDao() {
    if (_salesDao != null) {
      return _salesDao;
    } else {
      synchronized(this) {
        if(_salesDao == null) {
          _salesDao = new SalesDao_Impl(this);
        }
        return _salesDao;
      }
    }
  }
}
