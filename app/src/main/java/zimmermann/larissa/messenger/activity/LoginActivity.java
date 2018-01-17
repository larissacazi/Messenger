package zimmermann.larissa.messenger.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import zimmermann.larissa.messenger.R;

public class LoginActivity extends AppCompatActivity {

    private EditText countryCode;
    private EditText localCode;
    private EditText telephoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCode = (EditText)findViewById(R.id.countryCodeID);
        localCode = (EditText)findViewById(R.id.localCodeID);
        telephoneNumber = (EditText)findViewById(R.id.telNumberID);

        //Add all masks
        addMaskToEditText();
    }

    private void addMaskToEditText() {
        SimpleMaskFormatter countryCodeSMF = new SimpleMaskFormatter("+NN");
        MaskTextWatcher countryCodeMTW = new MaskTextWatcher(countryCode, countryCodeSMF);
        countryCode.addTextChangedListener(countryCodeMTW);

        SimpleMaskFormatter localCodeSMF = new SimpleMaskFormatter("NN");
        MaskTextWatcher localCodeMTW = new MaskTextWatcher(localCode, localCodeSMF);
        localCode.addTextChangedListener(localCodeMTW);

        SimpleMaskFormatter telephoneSMF = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher telephoneMTW = new MaskTextWatcher(telephoneNumber, telephoneSMF);
        telephoneNumber.addTextChangedListener(telephoneMTW);
    }
}
