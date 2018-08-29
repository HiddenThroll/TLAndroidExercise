package com.tanlong.exercise.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.database.DbOpenHelper;

/**
 * @author 龙
 */
public class BookProvider extends ContentProvider {

    public static final String AUTHORITIES = "com.tanlong.exercise.book.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/user");

    public static final int BOOK_URI_CODE = 1;
    public static final int USER_URI_CODE = 2;

    private SQLiteDatabase mDb;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //关联 URI 和 URI CODE
    static {
        uriMatcher.addURI(AUTHORITIES, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITIES, "user", USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        Logger.e("BookProvider onCreate thread is " + Thread.currentThread().getName());
        mDb = new DbOpenHelper(getContext()).getWritableDatabase();
        return true;
    }

    private String getTableName(Uri uri) {
        String tableName = "";
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Logger.e("BookProvider query thread is " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Unsupport Uri: " + uri);
        }
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Unsupport Uri: " + uri);
        }
        mDb.insert(table, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
