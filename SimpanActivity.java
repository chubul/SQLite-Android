public class SimpanActivity extends Activity {
  private SqliteManager sqliteDB;
	private Long id;

	private EditText teks_judul;
	private EditText teks_deskripsi;
	private EditText teks_waktu;
	String judul,deskripsi,waktu;

	public static final String SIMPAN_DATA = "simpan";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.simpan);

		teks_judul = (EditText) findViewById(R.id.edit_judul);
		teks_deskripsi = (EditText) findViewById(R.id.edit_deskripsi);
		teks_waktu = (EditText) findViewById(R.id.edit_waktu);

		id = null;

		if (bundle == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey(CrudSqliteActivity.EXTRA_ROWID)) {
				id = extras.getLong(CrudSqliteActivity.EXTRA_ROWID);
			}
			else
			{
				judul = extras.getString("judul");
				deskripsi = extras.getString("deskripsi");
				waktu = extras.getString("waktu");
				teks_judul.setText(judul);
				teks_deskripsi.setText(deskripsi);
				teks_waktu.setText(waktu);
			}
		}

		sqliteDB = new SqliteManager(this);
		sqliteDB.bukaKoneksi();

		pindahData();

		Button button = (Button) findViewById(R.id.btn_simpan);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				simpan();
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

	private void pindahData() {
		if (id != null) {
			Cursor cursor = sqliteDB.bacaDataTerseleksi(id);
			teks_judul.setText(cursor.getString(SqliteManager.POSISI_JUDUL));
			teks_deskripsi.setText(cursor.getString(SqliteManager.POSISI_DESKRIPSI));
			teks_waktu.setText(cursor.getString(SqliteManager.POSISI_WAKTU));
			cursor.close();
		}
	}

	private void simpan() {
		String judul = teks_judul.getText().toString();
		String deskripsi = teks_deskripsi.getText().toString();
		String waktu = teks_waktu.getText().toString();

		if (id != null) {
			sqliteDB.updateData(id, sqliteDB.ambilData(judul, deskripsi, waktu));
		}
		else {
			id = sqliteDB.insertData(sqliteDB.ambilData(judul, deskripsi, waktu));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SIMPAN_DATA, id);
	}
}
