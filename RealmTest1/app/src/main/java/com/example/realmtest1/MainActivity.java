package com.example.realmtest1;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.realmtest1.model.Esempio;
import com.example.realmtest1.model.Esempio1;

public class MainActivity extends AppCompatActivity {

    //Interface elements
    TextView txtLog;
    EditText editNome, editID;
    Realm realm;
    SyncConfiguration config;

    //Authentication data
    private static final String INSTANCE_ADDRESS = "exercisesdb.us1a.cloud.realm.io";
    public static final String AUTH_URL = "https://" + INSTANCE_ADDRESS + "/auth";
    public static final String REALM_URL = "realms://" + INSTANCE_ADDRESS + "/ProgettoS3_v1";
    public static final String username = "Gabriele";
    public static final String password = "password";
    public static final boolean createUser = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLog = findViewById(R.id.txtLog);
        editNome = findViewById(R.id.editNome);
        editID = findViewById(R.id.editID);

        //Realm initialization
        Realm.init(this);

        //Login
        SyncCredentials credentials = SyncCredentials.usernamePassword(username, password, createUser);
        SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(SyncUser user) {

                // Create the configuration
                user = SyncUser.current();
                String url = REALM_URL;
                config = user.createConfiguration(url).fullSynchronization().build();

                // Open the remote Realm
                realm = Realm.getInstance(config);

                //This log instruction is useful to debug
                Log.i("Login status: ", "Successful");
            }

            @Override
            public void onError(ObjectServerError error) {
                Log.e("Login error", error.toString());
            }
        });

    }

    public void Inserimento(View view){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Esempio1 esempio1 = realm.createObject(Esempio1.class, editID.getText().toString());
                esempio1.setNome(editNome.getText().toString());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                txtLog.setText(txtLog.getText().toString() + "\nInserimento: " + editID.getText().toString() + "-" + editNome.getText().toString());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                txtLog.setText(txtLog.getText().toString() + "\nInserimento fallito: " + error.getMessage());
            }
        });

    }
}
