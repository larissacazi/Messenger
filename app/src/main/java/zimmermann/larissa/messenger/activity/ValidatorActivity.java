package zimmermann.larissa.messenger.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.HashMap;

import zimmermann.larissa.messenger.R;
import zimmermann.larissa.messenger.utils.Constants;
import zimmermann.larissa.messenger.utils.Preferences;

public class ValidatorActivity extends AppCompatActivity {

    private static final String TAG = "ValidatorActivity";

    private EditText codeNumber;
    private Button validatorButton;

    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        codeNumber = (EditText)findViewById(R.id.codeNumberID);
        validatorButton = (Button)findViewById(R.id.validatorButtonID);

        //Add mask
        addMaskToEditText();

        validateCodeNumberAutomatically();

        //Check Validator Button
        validatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateCodeNumber()) {
                    Toast.makeText(ValidatorActivity.this, "Token validado!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ValidatorActivity.this, "Token não validado!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addMaskToEditText() {
        SimpleMaskFormatter codeNumberSMF = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher codeNumberMTW = new MaskTextWatcher(codeNumber, codeNumberSMF);
        codeNumber.addTextChangedListener(codeNumberMTW);
    }

    private boolean validateCodeNumber() {
        Preferences preferences = new Preferences(getApplicationContext());
        HashMap<String, String> userData = preferences.getUserPreferences();

        String savedToken = userData.get(Constants.TOKEN);
        String receivedToken = codeNumber.getText().toString();

        if(savedToken.equals(receivedToken)) return true;

        return false;
    }

    private void validateCodeNumberAutomatically() {

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SmsManager smsManager = SmsManager.getDefault();
            String appSmsToken = null;
            appSmsToken = smsManager.createAppSpecificSmsToken(createSmsTokenPendingIntent());
            codeNumber.setText(appSmsToken);
            Log.i(TAG, "OREO VERSION:: sms token " + appSmsToken);
        }
        else {
            Log.i(TAG, "Another version");
            smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
                @Override
                public void onSmsCatch(String message) {
                    Log.i(TAG, "Inside: " + message);
                    String token = parseCodeNumber(message);//Parse verification code
                    codeNumber.setText(token);//set code in edit text
                    //then you can send verification code to server
                }
            });
        }
    }

    private String parseCodeNumber(String message) {
        Log.i(TAG, "Message: " + message);
        String token = message.replace(Constants.MESSENGER_MESSAGE, "");
        Log.i(TAG, "Token received: " + token);
        return token;
    }

    private PendingIntent createSmsTokenPendingIntent() {
        return PendingIntent.getActivity(this, 1234,
                new Intent(this, MainActivity.class), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
