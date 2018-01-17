package zimmermann.larissa.messenger.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import zimmermann.larissa.messenger.R;
import zimmermann.larissa.messenger.utils.Constants;
import zimmermann.larissa.messenger.utils.Permission;
import zimmermann.larissa.messenger.utils.Preferences;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private static final String MESSENGER_MESSAGE = "Messenger - Código de Confirmação: ";

    private String[] PERMISSIONS = new String[]{
            android.Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private EditText userNameEditText;
    private EditText countryCodeEditText;
    private EditText localCodeEditText;
    private EditText telephoneNumberEditText;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check Permissions
        Permission.checkPermissions(LoginActivity.this, PERMISSIONS, REQUEST_CODE);

        //IDs
        userNameEditText = (EditText)findViewById(R.id.userNameID);
        countryCodeEditText = (EditText)findViewById(R.id.countryCodeID);
        localCodeEditText = (EditText)findViewById(R.id.localCodeID);
        telephoneNumberEditText = (EditText)findViewById(R.id.telNumberID);
        registerButton = (Button)findViewById(R.id.registerButtonID);

        //Add all masks
        addMaskToEditText();

        //Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String countryCode = countryCodeEditText.getText().toString();
                String localCode = localCodeEditText.getText().toString();
                String telephoneNumber = telephoneNumberEditText.getText().toString();

                String telephone = countryCode + localCode + telephoneNumber.replace("-", "");

                //Generate Token
                String token = generateToken();

                //Save USER data
                Preferences preferences = new Preferences(getApplicationContext());
                preferences.saveUserPreferences(userName, telephone, token);

                //Send SMS
                if(sendSMS(telephone, MESSENGER_MESSAGE + token)) {
                    Log.i(TAG, "SMS successfully sent!");
                }
                else {
                    Log.i(TAG, "SMS NOT sent!");
                }

                //Get USER data
                /*HashMap<String, String> userData = preferences.getUserPreferences();
                Log.i(TAG, "Username: " + userData.get(Constants.USER_NAME) + " Telephone: "
                + userData.get(Constants.USER_TELEPHONE) + " TOKEN: " + userData.get(Constants.TOKEN));*/
            }
        });
    }

    private void addMaskToEditText() {
        SimpleMaskFormatter countryCodeSMF = new SimpleMaskFormatter("+NN");
        MaskTextWatcher countryCodeMTW = new MaskTextWatcher(countryCodeEditText, countryCodeSMF);
        countryCodeEditText.addTextChangedListener(countryCodeMTW);

        SimpleMaskFormatter localCodeSMF = new SimpleMaskFormatter("NN");
        MaskTextWatcher localCodeMTW = new MaskTextWatcher(localCodeEditText, localCodeSMF);
        localCodeEditText.addTextChangedListener(localCodeMTW);

        SimpleMaskFormatter telephoneSMF = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher telephoneMTW = new MaskTextWatcher(telephoneNumberEditText, telephoneSMF);
        telephoneNumberEditText.addTextChangedListener(telephoneMTW);
    }

    private String generateToken() {
        Random random = new Random();
        //Generate number between 1000 and 9999
        int randomNumber = random.nextInt(8999) + 1000;
        return String.valueOf(randomNumber);
    }

    private boolean sendSMS(String telephone, String message) {
        try{
            //Get SmsManager instance
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telephone, null, message, null, null);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int result : grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                alertNecessaryPermission();
            }
        }
    }

    private void alertNecessaryPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão Negada");
        builder.setMessage("Para utilizar esse aplicativo, é necessário aceitar as permissões.");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
