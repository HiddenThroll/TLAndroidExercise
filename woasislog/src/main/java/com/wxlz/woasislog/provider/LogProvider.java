package com.wxlz.woasislog.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wxlz.woasislog.database.LogDbOpenHelper;

/**
 * @author 龙
 */
public class LogProvider extends ContentProvider {
    private final String TAG = getClass().getSimpleName();

    private static final String AUTHORITY = "com.tanlong.exercise.log.provider";

    public static final Uri LOG_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/log");
    private static final int LOG_URI_CODE = 0;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //关联 URI 和 URI CODE
    static {
        mUriMatcher.addURI(AUTHORITY, "log", LOG_URI_CODE);
    }

    private SQLiteDatabase mDb;

    private String  getTableName(Uri uri) {
        String tableName = "";
        switch (mUriMatcher.match(uri)) {
            case LOG_URI_CODE:
                tableName = LogDbOpenHelper.LOG_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    @Override
    public boolean onCreate() {
        mDb = new LogDbOpenHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("UnSupport URI " + uri);
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("UnSupport URI " + uri);
        }
        mDb.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("UnSupport URI " + uri);
        }
        int count = mDb.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("UnSupport URI " + uri);
        }
        int count = mDb.update(tableName, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
