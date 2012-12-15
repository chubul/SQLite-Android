public class SqliteManager {
  public static final int VERSI_DATABASE= 1;
	public static final String NAMA_DATABASE = "dbCrudSqlite";
	public static final String NAMA_TABEL = "tbAgenda";

	public static final String FIELD_ID = "_id";
	public static final int POSISI_ID = 0;
	public static final String FIELD_JUDUL = "judul";
	public static final int POSISI_JUDUL = 1;
	public static final String FIELD_DESKRIPSI = "deskripsi";
	public static final int POSISI_DESKRIPSI = 2;
	public static final String FIELD_WAKTU = "waktu";
	public static final int POSISI_WAKTU = 3;

	public static final String[] FIELD_TABEL ={ SqliteManager.FIELD_ID, SqliteManager.FIELD_JUDUL, SqliteManager.FIELD_DESKRIPSI, SqliteManager.FIELD_WAKTU };

	private Context crudContext;
	private SQLiteDatabase crudDatabase;
	private SqliteManagerHelper crudHelper;

	private static class SqliteManagerHelper extends SQLiteOpenHelper {
		private static final String BUAT_TABEL =
			"create table " + NAMA_TABEL + " (" +
			SqliteManager.FIELD_ID + " integer primary key autoincrement, " +
			SqliteManager.FIELD_JUDUL + " text not null, " +
			SqliteManager.FIELD_DESKRIPSI + " text not null," +
			SqliteManager.FIELD_WAKTU + " text not null " +
			");";

		public SqliteManagerHelper(Context context) {
			super(context, NAMA_DATABASE, null, VERSI_DATABASE);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(BUAT_TABEL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
	}

	public SqliteManager(Context context) {
		crudContext = context;
	}

	public void bukaKoneksi() throws SQLException {
		crudHelper = new SqliteManagerHelper(crudContext);
		crudDatabase = crudHelper.getWritableDatabase();
	}

	public void tutupKoneksi() {
		crudHelper.close();
		crudHelper = null;
		crudDatabase = null;
	}

	public long insertData(ContentValues values) {
		return crudDatabase.insert(NAMA_TABEL, null, values);
	}

	public boolean updateData(long rowId, ContentValues values) {
		return crudDatabase.update(NAMA_TABEL, values,
				SqliteManager.FIELD_ID + "=" + rowId, null) > 0;
	}

	public boolean hapusData(long rowId) {
		return crudDatabase.delete(NAMA_TABEL,
				SqliteManager.FIELD_ID + "=" + rowId, null) > 0;
	}

	public Cursor bacaData() {
		return crudDatabase.query(NAMA_TABEL,FIELD_TABEL,null, null, null, null,SqliteManager.FIELD_JUDUL + " DESC");
	}

	public Cursor bacaDataTerseleksi(long rowId) throws SQLException {
		Cursor cursor = crudDatabase.query(true, NAMA_TABEL,FIELD_TABEL,FIELD_ID + "=" + rowId,null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public ContentValues ambilData(String tempat, String lat, String lng) {
		ContentValues values = new ContentValues();
		values.put(SqliteManager.FIELD_JUDUL, tempat);
		values.put(SqliteManager.FIELD_DESKRIPSI, lat);
		values.put(SqliteManager.FIELD_WAKTU, lng);
		return values;
	}
}
