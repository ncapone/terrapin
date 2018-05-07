package com.terrapin;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amazonaws.models.nosql.AccountDO;
import com.terrapin.data.AccountContentContract;
import com.terrapin.services.AccountAWSProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Content Resolver
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView view = (TextView) findViewById(R.id.textView1);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            getAccounts();
        }

    }

    protected AccountDO getAccounts()
    {
        AccountAWSProvider.initialize(getApplicationContext());
        AccountAWSProvider provider = AccountAWSProvider.getInstance();

        Uri itemUri = AccountContentContract.Account.uriBuilder("");
        List<AccountDO> data = provider.scanAccounts(itemUri);

        for(AccountDO acct : data)
        {
            final TextView view = (TextView) findViewById(R.id.textView1);

            view.append(acct.getUsername());

        }
        // Cursor data =

    return null;
    }
}
