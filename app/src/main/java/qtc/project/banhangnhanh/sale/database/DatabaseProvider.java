package qtc.project.banhangnhanh.sale.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import qtc.project.banhangnhanh.sale.model.ListOrderModel;

public class DatabaseProvider extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "POS";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "pos";

    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_NAME = "nameProduct";
    private static final String KEY_QUANTITY = "quantityProduct";
    private static final String KEY_PRICE = "priceProduct";
    private static final String KEY_INVENTORY = "inventory";
    private static final String KEY_TOTAL_STORAGE = "totalStore";

    public DatabaseProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_students_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT,%s TEXT,%s TEXT,%s TEXT)", TABLE_NAME, KEY_ID, KEY_CUSTOMER_ID, KEY_NAME, KEY_QUANTITY, KEY_PRICE, KEY_INVENTORY,KEY_TOTAL_STORAGE);
        db.execSQL(create_students_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);

        onCreate(db);
    }

        public void addNotes(ListOrderModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, model.getId());
        values.put(KEY_CUSTOMER_ID, model.getCustomer_id());
        values.put(KEY_NAME, model.getNameProduct());
        values.put(KEY_QUANTITY, model.getQuantityProduct());
        values.put(KEY_PRICE, model.getPriceProduct());
        values.put(KEY_INVENTORY, model.getInventory());
        values.put(KEY_TOTAL_STORAGE, model.getTotalStore());
        //inserting new row
        db.insert(TABLE_NAME, null, values);
        //close database connection
        db.close();
    }

    public int updateRecord(ListOrderModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, model.getQuantityProduct());
        values.put(KEY_PRICE, model.getPriceProduct());

        //update row
        return db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
    }

    public void updateNote(String quantity, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, quantity);
        //updating row
        String query = "UPDATE " + TABLE_NAME + " SET " + KEY_QUANTITY + "=" + quantity + " WHERE " + KEY_ID + "=" + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
    }

    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    public void deleteRecord(ListOrderModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(model.getId())});
        db.close();
    }

    public boolean deleteRow(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{name}) > 0;
    }


    //get the all notes
    public ArrayList<ListOrderModel> getNotes() {
        ArrayList<ListOrderModel> arrayList = new ArrayList<>();
        // select all query
        String select_query = "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListOrderModel noteModel = new ListOrderModel();
                noteModel.setId(cursor.getString(0));
                noteModel.setCustomer_id(cursor.getString(1));
                noteModel.setNameProduct(cursor.getString(2));
                noteModel.setQuantityProduct(cursor.getString(3));
                noteModel.setPriceProduct(cursor.getString(4));
                noteModel.setInventory(cursor.getString(5));
                arrayList.add(noteModel);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
