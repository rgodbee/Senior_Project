package com.senproj.myminifigcollection3;

import com.senproj.myminifigcollection3.database.CategoryContent;
import com.senproj.myminifigcollection3.database.MinifigureContent;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Build;

public class PriSubCatActivity extends ActionBarActivity {

	/**
	 * The argument representing the selected Main Category Item ID that this Activity represents
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_SELECTED_MAIN = "selectedMainCategory";  // selected main category

	ListView listviewprisubs;
	//ListView listviewminis;

	//an instance for item selected of the selected Main Category MAIN_ITEM
	private CategoryContent.PrimarySubItem mItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pri_sub_cat);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

		//retrieve extra data from intent
		Intent extras = getIntent();
		if (extras == null) {
			return;
		}
		// get data via the key
		String mainCat = extras.getStringExtra(ARG_SELECTED_MAIN);
		if (mainCat != null) {
			//call to initialize CategoryContent ITEMS (Main Category List) from database
			CategoryContent.setPrimarySubContext(this, mainCat);
		}

		// Get ListView object from xml
		listviewprisubs = (ListView) findViewById(R.id.prisubcategorylist);

		/***** set list adapters*********************/
		// Primary Sub Category List adapter.
		final ArrayAdapter<CategoryContent.PrimarySubItem> catadapter = new ArrayAdapter<CategoryContent.PrimarySubItem>(this,
				android.R.layout.simple_list_item_activated_1, android.R.id.text1, CategoryContent.PSUB_ITEMS);
		listviewprisubs.setAdapter(catadapter);
		
		/***** set onItemClickListeners for listviews*********************/
		
		listviewprisubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				String selectedId = Long.toString(id);
				//get Main CategoryID from passed id Main_Item_Map
				mItem = (CategoryContent.PrimarySubItem) catadapter.getItem(position);
				//start the primary sub category activity for the selected item ID
				int sub = Integer.valueOf(mItem.hasSSub);
				switch (sub) {
				case 0:
					MinifigureContent.MINI_LIST_ITEMS.clear();
					MinifigureContent.MINI_LIST_ITEMS_MAP.clear();
					//no sub categories
					Intent figListIntent = new Intent(PriSubCatActivity.this, FigList.class);
					figListIntent.putExtra(FigList.ARG_ITEM_ID, selectedId);
					figListIntent.putExtra(FigList.ARG_ITEM_LEVEL, "Primary");
					figListIntent.putExtra(FigList.ARG_SELECTED_MAIN, mItem.categoryName);
					figListIntent.putExtra(FigList.ARG_SELECTED_PRI, mItem.primarySub);
					figListIntent.putExtra(FigList.ARG_SEARCH_TABLE, mItem.foundInTable);
					figListIntent.putExtra(FigList.ARG_ITEM_INDEX, mItem.id);
					startActivity(figListIntent);      
					//finish();
					break;

				case 1:
					Intent secsubIntent = new Intent(PriSubCatActivity.this, SecSubCatActivity.class);
					secsubIntent.putExtra(SecSubCatActivity.ARG_ITEM_ID, selectedId);
					secsubIntent.putExtra(SecSubCatActivity.ARG_SELECTED_MAIN, mItem.categoryName);
					secsubIntent.putExtra(SecSubCatActivity.ARG_SELECTED_PRI, mItem.primarySub);
					startActivity(secsubIntent);      
					//finish();
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pri_sub_cat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_pri_sub_cat,
					container, false);
			return rootView;
		}
	}
}
