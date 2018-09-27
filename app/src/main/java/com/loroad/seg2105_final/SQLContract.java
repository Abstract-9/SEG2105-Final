package com.loroad.seg2105_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


/**
 * This class contains the contract for interaction with the SQL database. It
 * also contains a helper that allows the app to easily read and write data.
 *
 * @author Logan
 *
 * @see AdminUser
 * @see HomeUser
 * @see ServiceUser
 * @see Service
 * @see AppDBHelper
 */
public class SQLContract {

    /**
     * Private constructor, so it is not accidentally instantiated
     */
    private SQLContract(){}

    //The following section contains Classes that act as containers for the
    //SQLite database schema currently being used.

    /**
     * This class contains the constants required for interaction with the Admins database table
     */
    public static class AdminUser implements BaseColumns{
        public static final String TABLE_NAME = "Admins";
        public static final String TABLE_COLUMN_USER = "username";
        public static final String TABLE_COLUMN_PASSWORD = "password";
    }

    /**
     * This class contains the constants required for interaction with the HomeOwners database table
     */
    public static class HomeUser implements  BaseColumns{
        public static final String TABLE_NAME = "HomeOwners";
        public static final String TABLE_COLUMN_USER = "username";
        public static final String TABLE_COLUMN_PASSWORD = "password";
        public static final String TABLE_COLUMN_NAME = "name";
        public static final String TABLE_COLUMN_EMAIL = "email";
        public static final String TABLE_COLUMN_PHONE = "phone";
        public static final String TABLE_COLUMN_ADDRESS = "address";



    }

    /**
     * This class contains the constants required for interaction with the ServiceProviders database table
     */
    public static class ServiceUser implements  BaseColumns{
        public static final String TABLE_NAME = "ServiceProviders";
        public static final String TABLE_COLUMN_USER = "username";
        public static final String TABLE_COLUMN_PASSWORD = "password";
        public static final String TABLE_COLUMN_NAME = "name";
        public static final String TABLE_COLUMN_EMAIL = "email";
        public static final String TABLE_COLUMN_PHONE = "phone";
        public static final String TABLE_COLUMN_ADDRESS = "address";
    }

    /**
     * This class contains the constants required for interaction with the Services database table
     */
    public static class Service implements BaseColumns{
        public static final String TABLE_NAME = "Services";
        public static final String TABLE_COLUMN_NAME = "name";
        public static final String TABLE_COLUMN_RATE = "rate";
    }

    //The following section contains construction strings for the
    //SQLite database schema.


    /**
     * The constructor string for the Admins SQLite table
     */
    public final String SQL_CREATE_ADMINS =
            "CREATE TABLE " + AdminUser.TABLE_NAME + "(" +
                    AdminUser.TABLE_COLUMN_USER + " TEXT, " +
                    AdminUser.TABLE_COLUMN_PASSWORD + " TEXT)";

    /**
     * The constructor string for the HomeOwners SQLite table
     */
    public final String SQL_CREATE_HOMES =
            "CREATE TABLE " + HomeUser.TABLE_NAME + "(" +
                    HomeUser.TABLE_COLUMN_USER + " TEXT, " +
                    HomeUser.TABLE_COLUMN_PASSWORD + " TEXT, " +
                    HomeUser.TABLE_COLUMN_NAME + " TEXT, " +
                    HomeUser.TABLE_COLUMN_EMAIL + " TEXT, " +
                    HomeUser.TABLE_COLUMN_PHONE + " TEXT, " +
                    HomeUser.TABLE_COLUMN_ADDRESS + " TEXT)";


    /**
     * The constructor string for the ServiceProviders SQLite table
     */
    public final String SQL_CREATE_SERVICE_PROVIDERS =
            "CREATE TABLE " + ServiceUser.TABLE_NAME + "(" +
                    ServiceUser.TABLE_COLUMN_USER + " TEXT, " +
                    ServiceUser.TABLE_COLUMN_PASSWORD + " TEXT, " +
                    ServiceUser.TABLE_COLUMN_NAME + " TEXT, " +
                    ServiceUser.TABLE_COLUMN_EMAIL + " TEXT, " +
                    ServiceUser.TABLE_COLUMN_PHONE + " TEXT, " +
                    ServiceUser.TABLE_COLUMN_ADDRESS + " TEXT)";


    /**
     * The constructor string for the Services SQLite table
     */
    public final String SQL_CREATE_SERVICES =
            "CREATE TABLE " + Service.TABLE_NAME + "(" +
                    Service.TABLE_COLUMN_NAME + " TEXT, " +
                    Service.TABLE_COLUMN_RATE + " TEXT)";

    /**
     * This class is the helper that allows the app to easily interact with the database.
     * SQLite interactions from the app are restricted to this class to make the interactions
     * simple, and to avoid errors. It is part of the SQL Contract.
     *
     * @author Logan
     *
     * @see SQLContract
     */
    public class AppDBHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "app.db";

        public AppDBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ADMINS);
            db.execSQL(SQL_CREATE_HOMES);
            db.execSQL(SQL_CREATE_SERVICE_PROVIDERS);
            db.execSQL(SQL_CREATE_SERVICES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //TODO maybe do something here
        }
    }
}
