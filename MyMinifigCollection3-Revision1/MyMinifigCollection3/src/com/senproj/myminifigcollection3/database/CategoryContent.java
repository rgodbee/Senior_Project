package com.senproj.myminifigcollection3.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.senproj.myminifigcollection3.database.DatabaseHelper;
import com.senproj.myminifigcollection3.database.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class CategoryContent {


	/**
	 * Arrays of category items.
	 */
	public static List<MainCategoryItem> MAIN_ITEMS = new ArrayList<MainCategoryItem>();
	public static List<PrimarySubItem> PSUB_ITEMS = new ArrayList<PrimarySubItem>();
	public static List<SecondarySubItem> SSUB_ITEMS = new ArrayList<SecondarySubItem>();
	public static List<TertiarySubItem> TSUB_ITEMS = new ArrayList<TertiarySubItem>();

	/**
	 * Maps of category items, by ID.
	 */
	public static Map<String, MainCategoryItem> MAIN_ITEM_MAP = new HashMap<String, MainCategoryItem>();
	public static Map<String, PrimarySubItem> PSUB_ITEM_MAP = new HashMap<String, PrimarySubItem>();
	public static Map<String, SecondarySubItem> SSUB_ITEM_MAP = new HashMap<String, SecondarySubItem>();
	public static Map<String, TertiarySubItem> TSUB_ITEM_MAP = new HashMap<String, TertiarySubItem>();


	/**********************handle Main Category Calls****************************/

	public static void setMainCategoryContext(Context c) {
		
		MAIN_ITEMS.clear();
		MAIN_ITEM_MAP.clear();
		DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
		try {   //used this for testing	           
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
		String[] cols = new String[] {Database.Categories._ID, Database.Categories.CATEGORY_ID, Database.Categories.MAIN_CATEGORY,
				 Database.Categories.LOCATION, Database.Categories.HAS_P_SUB};
		//Cursor cur = myDbHelper.queryMain("tblMainCategories", null, null, null, null, null, null); // database query
		Cursor mainCur = myDbHelper.queryCategory(true, Database.Categories.TABLE_NAME, cols, null, null,
				Database.Categories.MAIN_CATEGORY, null, Database.Categories.MAIN_CATEGORY, null);
		if (mainCur.moveToFirst()) {
			do {
				MainCategoryItem item = new MainCategoryItem(mainCur.getString(0), mainCur.getString(1), mainCur.getString(2),
						mainCur.getString(3), myDbHelper.queryMainCount(mainCur.getString(3)), mainCur.getString(4));
				addItem(item);
			} while (mainCur.moveToNext());
		}
		
		myDbHelper.close();
	}


	private static void addItem(MainCategoryItem item) {
		MAIN_ITEMS.add(item);
		MAIN_ITEM_MAP.put(item.id, item);
	}

	/**
	 * A Main Category listing item representing a main category detail item.
	 */
	public static class MainCategoryItem {
		public String id;
		public String categoryID;
		public String categoryName;
		public String foundInTable;
		public String count;
		public String hasPSub;

		public MainCategoryItem(String id, String categoryID, String categoryName, String foundInTable, String count, String hasPSub) {
			this.id = id;
			this.categoryID = categoryID;
			this.categoryName = categoryName;
			this.foundInTable = foundInTable;
			this.count = count;
			this.hasPSub = hasPSub;
		}
		@Override
		public String toString() {
			String output = categoryName + ": (" + count + " items)";              //hasPSub; //set this to desired output before pub
			return output;
		}
	}
		
		
		
		/********************handle Primary Sub Category calls**************************/
		
		private static void addPrimarySubItem(PrimarySubItem item) {
			PSUB_ITEMS.add(item);
			PSUB_ITEM_MAP.put(item.id, item);
		}
		
		public static void setPrimarySubContext(Context c, String mainCat) {
			PSUB_ITEMS.clear();
			PSUB_ITEM_MAP.clear();
			
			DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
			try {   //used this for testing	           
				myDbHelper.openDataBase();
			}catch(SQLException sqle){
				throw sqle;
			}
			
			String[] cols = new String[] {Database.Categories._ID, Database.Categories.CATEGORY_ID, Database.Categories.MAIN_CATEGORY,
					Database.Categories.PRIMARY_SUB, Database.Categories.LOCATION, Database.Categories.HAS_S_SUB};
			String selection = Database.Categories.MAIN_CATEGORY + "=? and " + Database.Categories.PRIMARY_SUB + " IS NOT NULL";
			String[] selectionArgs = new String[] {mainCat};
			Cursor pCur = myDbHelper.queryCategory(true, Database.Categories.TABLE_NAME, cols, selection, selectionArgs,
					Database.Categories.PRIMARY_SUB, null, Database.Categories.PRIMARY_SUB, null); // database query
			if (pCur.moveToFirst()) {
			//add View All option
			String viewalltext = "View All of " + mainCat + " including subs";
			PrimarySubItem viewallitem = new PrimarySubItem("8888", viewalltext, pCur.getString(2), pCur.getString(3), pCur.getString(4), "0");
			addPrimarySubItem(viewallitem);
			//add View only option
			String viewonlytext = "View Only from  " + mainCat + " with no subs";
			PrimarySubItem viewonlyitem = new PrimarySubItem("9999", viewonlytext, pCur.getString(2), null, pCur.getString(4), "0");
			addPrimarySubItem(viewonlyitem);
			
			
				do {
					PrimarySubItem subitem = new PrimarySubItem(pCur.getString(0), pCur.getString(1), pCur.getString(2),
							pCur.getString(3), pCur.getString(4), pCur.getString(5));
					addPrimarySubItem(subitem);
				} while (pCur.moveToNext());
			}
			
						
			myDbHelper.close();
		}
		
		/**
		 * A PrimarySubItem representing a primary sub category of a main category.
		 */
		public static class PrimarySubItem {
			public String id;
			public String categoryID;
			public String categoryName;
			public String primarySub;
			public String foundInTable;
			public String hasSSub;

			public PrimarySubItem(String id, String categoryID, String categoryName, String primary, String foundInTable, String hasSSub) {
				this.id = id;
				this.categoryID = categoryID;
				this.categoryName = categoryName;
				this.primarySub = primary;
				this.foundInTable = foundInTable;
				this.hasSSub = hasSSub;
			}

		@Override
		public String toString() {
			if(id == "8888")
				return categoryID; //used to store viewalltext for this item only
			else if (id == "9999")
				return categoryID; //used to store viewonlytext for this item only
			else
				return "Sub: " + primarySub + ": " + hasSSub;
		}
	}
	
		/********************handle Secondary Sub Category calls**************************/
		
		private static void addSecondarySubItem(SecondarySubItem item) {
			SSUB_ITEMS.add(item);
			SSUB_ITEM_MAP.put(item.id, item);
		}
		
		public static void setSecondarySubContext(Context c, String mainCat, String priCat) {
			SSUB_ITEMS.clear();
			SSUB_ITEM_MAP.clear();
			DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
			try {   //used this for testing	           
				myDbHelper.openDataBase();
			}catch(SQLException sqle){
				throw sqle;
			}
			
			String[] cols = new String[] {Database.Categories._ID, Database.Categories.CATEGORY_ID, Database.Categories.MAIN_CATEGORY,
					Database.Categories.PRIMARY_SUB, Database.Categories.SECONDARY_SUB, Database.Categories.LOCATION, Database.Categories.HAS_T_SUB};
			String selection = Database.Categories.MAIN_CATEGORY + "=? and " + Database.Categories.PRIMARY_SUB + "=? and " +
					Database.Categories.SECONDARY_SUB + " IS NOT NULL";
			String[] selectionArgs = new String[] {mainCat, priCat};
			Cursor sCur = myDbHelper.queryCategory(true, Database.Categories.TABLE_NAME, cols, selection, selectionArgs,
					Database.Categories.SECONDARY_SUB, null, Database.Categories.SECONDARY_SUB, null); // database query			

			if (sCur.moveToFirst()) {
				//add View All option
				String viewalltext = "View All of " + mainCat + " : " + priCat + " including subs";
				SecondarySubItem viewallitem = new SecondarySubItem("8888", viewalltext, sCur.getString(2), sCur.getString(3), sCur.getString(4), sCur.getString(5), "0");
				addSecondarySubItem(viewallitem);
				//add View only option
				String viewonlytext = "View Only from  " + mainCat + " : " + priCat + " with no subs";
				SecondarySubItem viewonlyitem = new SecondarySubItem("9999", viewonlytext, sCur.getString(2), sCur.getString(3), sCur.getString(4), sCur.getString(5), "0");
				addSecondarySubItem(viewonlyitem);
				do {
					SecondarySubItem secsubitem = new SecondarySubItem(sCur.getString(0), sCur.getString(1), sCur.getString(2),
							sCur.getString(3), sCur.getString(4), sCur.getString(5), sCur.getString(6));
					addSecondarySubItem(secsubitem);
				} while (sCur.moveToNext());
			}
			
						
			myDbHelper.close();
		}
		
		/**
		 * A SecondarySubItem representing a secondary sub category of a main category/primary sub category.
		 */
		public static class SecondarySubItem {
			public String id;
			public String categoryID;
			public String categoryName;
			public String primarySub;
			public String secondarySub;
			public String foundInTable;
			public String hasTSub;

			public SecondarySubItem(String id, String categoryID, String categoryName, String primary, String secondary, String foundInTable, String hasTSub) {
				this.id = id;
				this.categoryID = categoryID;
				this.categoryName = categoryName;
				this.primarySub = primary;
				this.secondarySub = secondary;
				this.foundInTable = foundInTable;
				this.hasTSub = hasTSub;
			}

			@Override
			public String toString() {
				if(id == "8888")
					return categoryID; //used to store viewalltext for this item only
				else if (id == "9999")
					return categoryID; //used to store viewonlytext for this item only
				else
					return "Sub: " + secondarySub + ": " + hasTSub;
			}
	}
		
		
		/********************handle Tertiary Sub Category calls**************************/
		
		private static void addTertiarySubItem(TertiarySubItem item) {
			TSUB_ITEMS.add(item);
			TSUB_ITEM_MAP.put(item.id, item);
		}
		
		public static void setTertiarySubContext(Context c, String mainCat, String priCat, String secCat) {
			TSUB_ITEMS.clear();
			TSUB_ITEM_MAP.clear();
			DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
			try {   //used this for testing	           
				myDbHelper.openDataBase();
			}catch(SQLException sqle){
				throw sqle;
			}
			
			String[] cols = new String[] {Database.Categories._ID, Database.Categories.CATEGORY_ID, Database.Categories.MAIN_CATEGORY,
					Database.Categories.TERTIARY_SUB, Database.Categories.LOCATION};
			String selection = Database.Categories.MAIN_CATEGORY + "=? and " + Database.Categories.SECONDARY_SUB + "=? and " + Database.Categories.TERTIARY_SUB + " IS NOT NULL";
			String[] selectionArgs = new String[] {mainCat, secCat};
			Cursor cur = myDbHelper.queryCategory(true, Database.Categories.TABLE_NAME, cols, selection, selectionArgs,
					Database.Categories.TERTIARY_SUB, null, Database.Categories.TERTIARY_SUB, null); // database query
						
			if (cur.moveToFirst()) {
				//add View All option
				String viewalltext = "View All of " + mainCat + " : " + priCat + " : " + secCat + "including subs";
				TertiarySubItem viewallitem = new TertiarySubItem("8888", viewalltext, cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6));
				addTertiarySubItem(viewallitem);
				//add View only option
				String viewonlytext = "View Only from  " + mainCat + " : " + priCat + " : " + secCat + "with no subs";
				TertiarySubItem viewonlyitem = new TertiarySubItem("9999", viewonlytext, cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6));
				addTertiarySubItem(viewonlyitem);
				do {
					TertiarySubItem tertsubitem = new TertiarySubItem(cur.getString(0), cur.getString(1), cur.getString(2),
							cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6));
					addTertiarySubItem(tertsubitem);
				} while (cur.moveToNext());
			}
			
						
			myDbHelper.close();
		}
		
		/**
		 * A PrimarySubItem representing a primary sub category of a main category.
		 */
		public static class TertiarySubItem {
			public String id;
			public String categoryID;
			public String categoryName;
			public String primarySub;
			public String secondarySub;
			public String tertiarySub;
			public String foundInTable;

			public TertiarySubItem(String id, String categoryID, String categoryName, String primary,
					String secondary, String tertiary, String foundInTable) {
				this.id = id;
				this.categoryID = categoryID;
				this.categoryName = categoryName;
				this.primarySub = primary;
				this.secondarySub = secondary;
				this.tertiarySub = tertiary;
				this.foundInTable = foundInTable;
			}

		@Override
		public String toString() {
			return tertiarySub;
		}
	}
}

