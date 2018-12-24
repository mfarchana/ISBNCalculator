package tech.farchan.isbncalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;

    EditText editAngka1, editAngka2;
    TextView tvHasil;
    TextView tvJumlah;
    TextView tvDigit;
    TextView tvValid;
    Button validasiisbn10, validasiisbn13, konversi, reset;

    String finalWord = "";
    String jml = "";
    String word;
    int[] wordValu = new int[100];

    private Button scanBtn;
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);

        bgapp.animate().translationY(-900).setDuration(1800).setStartDelay(1300);
        clover.animate().alpha(0).setDuration(1200).setStartDelay(1200);
        textsplash.animate().translationY(1400).alpha(0).setDuration(1800).setStartDelay(1300);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);


        scanBtn = (Button)findViewById(R.id.BtnScan);
        scanBtn.setOnClickListener((View.OnClickListener) this);

        editAngka1 = findViewById(R.id.editText1);
        editAngka2 = findViewById(R.id.editText2);
        tvHasil = findViewById(R.id.textView2);
        tvJumlah = findViewById(R.id.textView3);
        tvDigit = findViewById(R.id.textView4);
        tvValid = findViewById(R.id.textView5);
        validasiisbn10 = findViewById(R.id.button1);
        konversi = findViewById(R.id.button2);
        validasiisbn13 = findViewById(R.id.button3);

        validasiisbn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidasiISBN10();
            }
        });
        validasiisbn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidasiISBN13();
            }
        });
        konversi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Konversi();
            }
        });


    }


    public void onClick(View v){
        // inisialisasi IntentIntegrator(scanQR)
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else if(result.getContents().length() == 13){
                String scanContent = result.getContents();
                editAngka2.setText(scanContent);
            }else{
                String scanContent = result.getContents();
                editAngka1.setText(scanContent);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @SuppressLint("SetTextI18n")
    public void ValidasiISBN10() {
        String word = editAngka1.getText().toString();

        for (int i = 0; i < word.length(); i++) {
            wordValu[i] = (int) word.charAt(i) - 48;//Misal 0 = 48, maka 0 = 48 - 48 : 0 (Sesuai dengan index)
        }

        for (int j = 0; j < word.length(); j++) {
            finalWord = finalWord +
                    (char) (((wordValu[j]) + 48));
        }

        tvHasil.setText(String.valueOf(finalWord));
        finalWord = "";

        //Cek Validitas
        int count = 0;
        for (int j = 1; j <= 10; j++) {
            count = count + (j * wordValu[j - 1]);
        }

        //Cek Digit
        if (count % 11 == 0) {
            int sum = 0;
            for (int i = 1; i <= 9; i++) {
                sum += i * wordValu[i - 1];
            }
            tvJumlah.setText(count + " % 11 = " + count % 11);
            tvDigit.setText(sum + " % 11 = " + sum % 11);
            tvValid.setText("Selamat ISBN VALID!");
            jml = "";
        } else {
            int sum = 0;
            for (int i = 1; i <= 9; i++) {
                sum += i * wordValu[i - 1];
            }
            tvJumlah.setText(count + " % 11 = " + count % 11);
            tvDigit.setText(sum + " % 11 = " + sum % 11);
            tvValid.setText("Maaf, ISBN tidak VALID");
            jml = "";
        }
    }

    public void ValidasiISBN13() {
        String word = editAngka2.getText().toString();

        for (int i = 0; i < word.length(); i++) {
            wordValu[i] = (int) word.charAt(i) - 48;//Misal 0 = 48, maka 0 = 48 - 48 : 0 (Sesuai dengan index)
        }

        for (int j = 0; j < word.length(); j++) {
            finalWord = finalWord +
                    (char) (((wordValu[j]) + 48));
        }

        tvHasil.setText(String.valueOf(finalWord));
        finalWord = "";

        //Cek Validitas
        int count = 0;
        for (int j = 0; j < 13; j += 2) {
            count = count + (1 * wordValu[j]) + (3 * wordValu[j + 1]);
        }

        //Cek Digit
        if (count % 10 == 0) {
            int sum = 0;
            for (int i = 0; i < 12; i += 2) {
                sum = sum + (1 * wordValu[i]) + (3 * wordValu[i + 1]);
            }
            tvJumlah.setText(count + " % 10 = " + count % 10);
            tvDigit.setText(sum + " % 10 = " + sum % 10 + "\n 10 - " + (sum % 10) + " = " + (10 - (sum % 10)));
            tvValid.setText("Selamat ISBN VALID!");
            jml = "";
        } else {
            int sum = 0;
            for (int i = 0; i < 12; i += 2) {
                sum = sum + (1 * wordValu[i]) + (3 * wordValu[i + 1]);
            }
            tvJumlah.setText(count + " % 10 = " + count % 10);
            tvDigit.setText(sum + " % 10 = " + sum % 10 + "\n 10 - " + (sum % 10) + " = " + (10 - (sum % 10)));
            tvValid.setText("Maaf, ISBN tidak VALID");
            jml = "";
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void Konversi() {
        String word = editAngka1.getText().toString();
        for (int i = 0; i < word.length(); i++) {
            wordValu[i] = (int) word.charAt(i) - 48;//Misal 0 = 48, maka 0 = 48 - 48 : 0 (Sesuai dengan index)
        }

        for (int j = 0; j < word.length(); j++) {
            finalWord = finalWord +
                    (char) (((wordValu[j]) + 48));
        }

        //Penjumlahan Elemen
        int jmlawal = 38; //9+7+8
        int jumlah1 = 0;
        int jumlah2 = 0;
        for (int i = 0; i <= 8; i++) {
            if (i % 2 == 0 || i == 0) {
                jumlah1 += (3 * wordValu[i]);
            } else {
                jumlah2 += (1 * wordValu[i]);
            }
        }

        int hasil = jumlah1 + jumlah2 + jmlawal;

        tvHasil.setText(String.format(getString(R.string.outConvert), wordValu[0], wordValu[1], wordValu[2],
                wordValu[3], wordValu[4], wordValu[5], wordValu[6], wordValu[7], wordValu[8], 10 - (hasil % 10)));
        tvJumlah.setText(hasil+"");
        tvDigit.setText(hasil + " % 10 = " + hasil % 10 + "\n 10 - " + (hasil % 10) + " = " + (10 - (hasil % 10)));
        tvValid.setText("Konversi Berhasil!");
        finalWord = "";
    }
}
