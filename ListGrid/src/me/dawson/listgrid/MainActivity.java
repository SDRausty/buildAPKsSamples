package me.dawson.listgrid;

import me.dawson.listgrid.view.ListGrid;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
	public static final String TAG = "ListGrid";

	private ListGrid lgDemo;

	private ListAdapter adapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return 13;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (!(convertView instanceof LinearLayout)) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.lg_item, parent, false);
			}

			TextView tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title);
			tvTitle.setText("Title" + position);

			TextView tvSummary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			tvSummary.setText("Summary" + position);

			return convertView;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lgDemo = (ListGrid) findViewById(R.id.lg_demo);
		lgDemo.setRowCount(2);
		lgDemo.setAdapter(adapter);
		lgDemo.setOnItemClickListener(this);
		TextView v = new TextView(this);
		v.setText("ListGrid Header!");
		lgDemo.addHeaderView(v);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "onItemClick " + position);
	}

}
