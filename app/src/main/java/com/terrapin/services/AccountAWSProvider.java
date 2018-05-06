package com.terrapin.services;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.models.nosql.AccountDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.terrapin.data.AccountContentContract;

import java.util.Iterator;
import java.util.List;


public class AWSProvider extends ContentProvider{

    private static AWSProvider instance = null;
    private Context context;
    private AWSConfiguration awsConfiguration;
    private PinpointManager pinpointManager;

    // Declare DynamoDBMapper and AmazonDynamoDBClient private variables
    // to support data access methods
    private AmazonDynamoDBClient dbClient = null;
    private DynamoDBMapper dbMapper = null;

    public static AWSProvider getInstance() {
        return instance;
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new AWSProvider(context);
        }
    }

    public AWSProvider()
    {

    }
    private AWSProvider(Context context) {
        this.context = context;
        this.awsConfiguration = new AWSConfiguration(context);

        IdentityManager identityManager = new IdentityManager(context, awsConfiguration);
        IdentityManager.setDefaultIdentityManager(identityManager);
    }

    public Context getThisContext() {
        return this.context;
    }


    public AWSConfiguration getConfiguration() {
        return this.awsConfiguration;
    }

    public IdentityManager getIdentityManager() {
        return IdentityManager.getDefaultIdentityManager();
    }

    public PinpointManager getPinpointManager() {
        if (pinpointManager == null) {
            final AWSCredentialsProvider cp = getIdentityManager().getCredentialsProvider();
            PinpointConfiguration config = new PinpointConfiguration(
                    getThisContext(), cp, getConfiguration());
            pinpointManager = new PinpointManager(config);
        }
        return pinpointManager;
    }

    public DynamoDBMapper getDynamoDBMapper() {
        if (dbMapper == null) {
            final AWSCredentialsProvider cp = getIdentityManager().getCredentialsProvider();
            dbClient = new AmazonDynamoDBClient(cp);
            dbMapper = DynamoDBMapper.builder()
                    .awsConfiguration(getConfiguration())
                    .dynamoDBClient(dbClient)
                    .build();
        }
        return dbMapper;
    }


    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final AccountDO newAccount = toAccountDO(values);
                dbMapper.save(newAccount);
                Uri item = AccountContentContract.Account.uriBuilder(newAccount.getUserId());
                notifyAllListeners(item);
                return item;


    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;

        DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
        final AccountDO note = new AccountDO();
        note.setUserId(uri.getLastPathSegment());
        note.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        dbMapper.delete(note);
        rows = 1;

        if (rows > 0) {
            notifyAllListeners(uri);
        }
        return rows;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;


                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final AccountDO updatedNote = toAccountDO(values);
                dbMapper.save(updatedNote);
                rows = 1;
        if (rows > 0) {
            notifyAllListeners(uri);
        }
        return rows;
    }

    @Nullable
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String userName,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {

        DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
        MatrixCursor cursor = new MatrixCursor(AccountContentContract.Account.PROJECTION_ALL);

        AccountDO template = new AccountDO();
        template.setUserId(userName);
        // Now create a query expression that is based on the template record.
        DynamoDBQueryExpression<AccountDO> queryExpression;
        queryExpression = new DynamoDBQueryExpression<AccountDO>()
                .withHashKeyValues(template);
        // Finally, do the query with that query expression.
        List<AccountDO> result = dbMapper.query(AccountDO.class, queryExpression);
        Iterator<AccountDO> iterator = result.iterator();
        while (iterator.hasNext()) {
            final AccountDO account = iterator.next();
            Object[] columnValues = fromAccountDO(account);
            cursor.addRow(columnValues); }

        cursor.setNotificationUri(getThisContext().getContentResolver(), uri);
        return cursor;


    }

    public Cursor scanAccounts(Uri uri)
    {
        DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
        MatrixCursor cursor = new MatrixCursor(AccountContentContract.Account.PROJECTION_ALL);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<AccountDO> result = dbMapper.scan(AccountDO.class, scanExpression);
        Iterator<AccountDO> iterator = result.iterator();
        while(iterator.hasNext())
        {
            final AccountDO account = iterator.next();
            Object[] columnValues = fromAccountDO(account);
            cursor.addRow(columnValues);
        }

        cursor.setNotificationUri(getThisContext().getContentResolver(), uri);
        return cursor;
    }

    private AccountDO toAccountDO(ContentValues values) {
        final AccountDO account = new AccountDO();

        account.setUserId(values.getAsString(AccountContentContract.Account._ID));
        account.setUsername(values.getAsString(AccountContentContract.Account.USERNAME));
        account.setEmailAddress(values.getAsString(AccountContentContract.Account.EMAILADDRESS));
        account.setFirstName(values.getAsString(AccountContentContract.Account.FIRST_NAME));
        account.setLastName(values.getAsString(AccountContentContract.Account.LAST_NAME));
        account.setEmailOptin(values.getAsBoolean(AccountContentContract.Account.EMAIL_OPTIN));
        return account;
    }

    private Object[] fromAccountDO(AccountDO account) {
        String[] fields = AccountContentContract.Account.PROJECTION_ALL;
        Object[] r = new Object[fields.length];
        for (int i = 0 ; i < fields.length ; i++) {
            if (fields[i].equals(AccountContentContract.Account._ID)) {
                r[i] = account.getUserId();
            } else if (fields[i].equals(AccountContentContract.Account.USERNAME)) {
                r[i] = account.getUsername();
            } else if (fields[i].equals(AccountContentContract.Account.EMAILADDRESS)) {
                r[i] = account.getEmailAddress();
            } else if (fields[i].equals(AccountContentContract.Account.FIRST_NAME)) {
                r[i] = account.getFirstName();
            } else if (fields[i].equals(AccountContentContract.Account.LAST_NAME)) {
                r[i] = account.getLastName();
            } else if (fields[i].equals(AccountContentContract.Account.EMAIL_OPTIN))
            {
                r[i] = account.getEmailOptin();
            }
            else {
                r[i] = new Integer(0);
            }
        }
        return r;
    }

    /**
     * Notify all listeners that the specified URI has changed
     * @param uri the URI that changed
     */
    private void notifyAllListeners(Uri uri) {
        ContentResolver resolver = getThisContext().getContentResolver();
        if (resolver != null) {
            resolver.notifyChange(uri, null);
        }
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return AccountContentContract.Account.CONTENT_ITEM_TYPE;

    }

    public boolean onCreate() {
       // databaseHelper = new DatabaseHelper(getThisContext());
        return true;
    }

}