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

public class MinifigureContent {

	/**
	 * Arrays of Minifigure items.
	 */
	public static List<MinifigListItem> MINI_LIST_ITEMS = new ArrayList<MinifigListItem>();
	public static List<MinifigDetailItem> MINI_DETAIL_ITEM = new ArrayList<MinifigDetailItem>();

	/**
	 * Maps of Minifigure items, by ID.
	 */
	public static Map<String, MinifigListItem> MINI_LIST_ITEMS_MAP = new HashMap<String, MinifigListItem>();
	public static Map<String, MinifigDetailItem> MINI_DETAIL_ITEM_MAP = new HashMap<String, MinifigDetailItem>();


	/**********************handle Minifigure listing Calls****************************/


	public static void setPrimaryMinifigureListContext(Context c, String parentCat, String table, String level) {
		MINI_LIST_ITEMS.clear();
		MINI_LIST_ITEMS_MAP.clear();
		DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
		try {   //used this for testing	           
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}

		/*************************************   GET MAIN LEVEL FIG LISTINGS   *******/
		if(level.equals("Main")){
			//columns to SELECT
			String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
					Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
			Cursor cur = myDbHelper.queryMinifig(table, cols, null, null, null, null, null); // database query

			if (cur.moveToFirst()) {
				do {
					MinifigListItem figitem = new MinifigListItem(cur.getString(0), cur.getString(1),
							cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5));
					addMinifigureItem(figitem);
				} while (cur.moveToNext());
			}

			/*************************************   GET PRIMARY SUB LEVEL FIG LISTINGS   *******/
		} else if(level.equals("Primary")){
			//columns to SELECT
			String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
					Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
			//WHERE selection
			String selection = Database.Minifigures.SUB_CATEGORIES + " IS NOT NULL";
			Cursor cur2 = myDbHelper.queryMinifig(table, cols, selection, null, null, null, Database.Minifigures.DEFAULT_SORT_ORDER); // database query

			if (cur2.moveToFirst()) {
				do {
					//get array of split substring(pipe delimited)
					String[] subs = cur2.getString(4).split("\\|");
					//cycle through possible scenarios of split results
					if(subs.length >= 1 && subs[0].trim().equals(parentCat)) {

						MinifigListItem figitem = new MinifigListItem(cur2.getString(0), cur2.getString(1),
								cur2.getString(2), cur2.getString(3), cur2.getString(4), cur2.getString(5));
						addMinifigureItem(figitem);
					}
				} while (cur2.moveToNext());
			}

			/*************************************   GET SECONDARY SUB LEVEL FIG LISTINGS   *******/
		} else if(level.equals("Secondary")){
			//columns to SELECT
			String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
					Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
			//WHERE selection
			String selection = Database.Minifigures.SUB_CATEGORIES + " IS NOT NULL";
			Cursor cur3 = myDbHelper.queryMinifig(table, cols, selection, null, null, null, Database.Minifigures.DEFAULT_SORT_ORDER); // database query

			if (cur3.moveToFirst()) {
				do {
					//get array of split substring(pipe delimited)
					String[] subs = cur3.getString(4).split("\\|");
					//cycle through possible scenarios of split results
					if(subs.length >= 2 && subs[1].trim().equals(parentCat)) {

						MinifigListItem figitem = new MinifigListItem(cur3.getString(0), cur3.getString(1),
								cur3.getString(2), cur3.getString(3), cur3.getString(4), cur3.getString(5));
						addMinifigureItem(figitem);
					}
				} while (cur3.moveToNext());
			}

			/*************************************   GET TERTIARY SUB LEVEL FIG LISTINGS   *******/
		} else if(level.equals("Tertiary")){
			//columns to SELECT
			String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
					Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
			//WHERE selection
			String selection = Database.Minifigures.SUB_CATEGORIES + " IS NOT NULL";
			Cursor cur4 = myDbHelper.queryMinifig(table, cols, selection, null, null, null, Database.Minifigures.DEFAULT_SORT_ORDER); // database query

			if (cur4.moveToFirst()) {
				do {
					//get array of split substring(pipe delimited)
					String[] subs = cur4.getString(4).split("\\|");
					//cycle through possible scenarios of split results
					if(subs.length >= 3 && subs[2].trim().equals(parentCat)) {

						MinifigListItem figitem = new MinifigListItem(cur4.getString(0), cur4.getString(1),
								cur4.getString(2), cur4.getString(3), cur4.getString(4), cur4.getString(5));
						addMinifigureItem(figitem);
					}
				} while (cur4.moveToNext());
			}

		}


		myDbHelper.close();

	}
	
	public static void setAllMinifigureListContext(Context c, String parentCat, String table, String level) {
		MINI_LIST_ITEMS.clear();
		MINI_LIST_ITEMS_MAP.clear();
		DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
		try {   //used this for testing	           
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}

		//columns to SELECT
		String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
				Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
		Cursor allCur = myDbHelper.queryMinifig(table, cols, null, null, null, null, null); // database query

		if (allCur.moveToFirst()) {
			do {
				MinifigListItem figitem = new MinifigListItem(allCur.getString(0), allCur.getString(1),
						allCur.getString(2), allCur.getString(3), allCur.getString(4), allCur.getString(5));
				addMinifigureItem(figitem);
			} while (allCur.moveToNext());
		}
		myDbHelper.close();
	}
	
	public static void setOnlyMinifigureListContext(Context c, String parentCat, String table, String level) {
		MINI_LIST_ITEMS.clear();
		MINI_LIST_ITEMS_MAP.clear();
		DatabaseHelper myDbHelper = new DatabaseHelper(c); // SQLiteOpenHelper + SQLiteDatabase manager
		try {   //used this for testing	           
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}

		//columns to SELECT
		String[] cols = new String[] {Database.Minifigures._ID, Database.Minifigures.FIG_ID, Database.Minifigures.BRICKLINK_ID,
				Database.Minifigures.DESCRIPTION, Database.Minifigures.SUB_CATEGORIES, Database.Minifigures.IN_COLLECTION};
		//WHERE selection
		//String selection = Database.Minifigures.SUB_CATEGORIES + " IS NULL OR " + Database.Minifigures.SUB_CATEGORIES + "=?";
		//String[] selectionArgs = new String[] {""};
		Cursor onlyCur = myDbHelper.queryMinifig(table, cols, null, null, null, null, null); // database query

		if (onlyCur.moveToFirst()) {
			do {
				String[] subs = onlyCur.getString(4).split("\\|");
				if(subs.length >= 1 && subs[0].trim().equals("null")) {
				//if(cur.getString(4) == "null" || cur.getString(4) == "" || cur.isNull(4)){
				MinifigListItem figitem = new MinifigListItem(onlyCur.getString(0), onlyCur.getString(1),
						onlyCur.getString(2), onlyCur.getString(3), onlyCur.getString(4), onlyCur.getString(5));
				addMinifigureItem(figitem);
				}
			} while (onlyCur.moveToNext());
		}
		myDbHelper.close();
	}

	private static void addMinifigureItem(MinifigListItem item) {
		MINI_LIST_ITEMS.add(item);
		MINI_LIST_ITEMS_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class MinifigListItem {
		public String id;
		public String figID;
		public String blfigID;
		public String description;
		public String subCategories;
		public String inCollection;

		public MinifigListItem(String id, String figID, String blfigID, String description,
				String subCategories, String inCollection) {
			this.id = id;
			this.figID = figID;
			this.blfigID = blfigID;
			this.description = description;
			this.subCategories = subCategories;
			this.inCollection = inCollection;

		}
		@Override
		public String toString() {
			String output = " " + figID + ": " + subCategories;                //description;
			return output;
		}
	}

	/**********************handle Minifigure detail Calls****************************/


	private static void addItem(MinifigDetailItem item) {
		MINI_DETAIL_ITEM.add(item);
		MINI_DETAIL_ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class MinifigDetailItem {  ///***********temporary placeholder, change all data
		public String id;
		public String figID;
		public String blfigID;
		public String description;
		public String categoryID;
		public String categoryName;
		public String subCategories;
		public String prodYear;
		public String inCollection;

		public MinifigDetailItem(String id, String figID, String blfigID, String description,
				String categoryID, String categoryName, String subCategories, String prodYear, String inCollection) {
			this.id = id;
			this.figID = figID;
			this.blfigID = blfigID;
			this.description = description;
			this.categoryID = categoryID;
			this.categoryName = categoryName;
			this.subCategories = subCategories;
			this.prodYear = prodYear;
			this.inCollection = inCollection;
		}
		@Override
		public String toString() {
			String output = " " + figID + ": " + description;
			return output;
		}
	}

}
