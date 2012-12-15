public class CrudSqliteActivity extends ListActivity {
  private SqliteManager sqliteDB;
	private SimpleCursorAdapter mCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        sqliteDB = new SqliteManager(this);
        sqliteDB.bukaKoneksi();

		Cursor cursor = sqliteDB.bacaData();

		startManagingCursor(cursor);

		String[] awal = new String[] { SqliteManager.FIELD_JUDUL };
		int[] tujuan = new int[] { R.id.rowtext };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.baris, cursor, awal, tujuan);

		mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex == SqliteManager.POSISI_ID) {
					TextView textView = (TextView) view;
						textView.setText("");
					return true;
			    }
			    return false;
			}
			});

		setListAdapter(mCursorAdapter);
		registerForContextMenu(getListView());

    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.tambah:
        		Intent intent = new Intent(this, SimpanActivity.class);
        		intent.putExtra("judul", "");
        		intent.putExtra("deskripsi", "");
        		intent.putExtra("waktu", "");
        		startActivity(intent);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.menu_edit:
				startDetail(info.id, false);
				return true;

			case R.id.menu_delete:
				hapus(info.id);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

    public void hapus(long rowId) {
    	sqliteDB.hapusData(rowId);
		mCursorAdapter.getCursor().requery();
    }

	public static final String EXTRA_ROWID = "rowid";

	@Override
	protected void onListItemClick(ListView l, View v, int position, long rowId) {
		super.onListItemClick(l, v, position, rowId);
		tampilTempatTerseleksi(rowId);

	}

	public void tampilTempatTerseleksi(Long mRowId) {
		Cursor cursor = sqliteDB.bacaDataTerseleksi(mRowId);
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra("judul", cursor.getString(SqliteManager.POSISI_JUDUL));
		intent.putExtra("deskripsi", cursor.getString(SqliteManager.POSISI_DESKRIPSI));
		intent.putExtra("waktu", cursor.getString(SqliteManager.POSISI_WAKTU));
		startActivity(intent);

	}

	public void startDetail(long rowId, boolean baru) {
		Intent intent = new Intent(this, SimpanActivity.class);
		if (!baru) {
			intent.putExtra(EXTRA_ROWID, rowId);
		}
		startActivity(intent);
	}
}
