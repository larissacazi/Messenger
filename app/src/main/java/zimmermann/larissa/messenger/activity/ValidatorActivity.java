package zimmermann.larissa.messenger.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import zimmermann.larissa.messenger.R;
import zimmermann.larissa.messenger.utils.Constants;
import zimmermann.larissa.messenger.utils.Preferences;

public class ValidatorActivity extends AppCompatActivity {

    private EditText codeNumber;
    private Button validatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        codeNumber = (EditText)findViewById(R.id.codeNumberID);
        validatorButton = (Button)findViewById(R.id.validatorButtonID);

        //Add mask
        addMaskToEditText();

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
}
