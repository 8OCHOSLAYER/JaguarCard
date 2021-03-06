package com.example.beta22;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements Listener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEtMessage;
    private EditText mEtMessage2;
    private EditText mEtMatricula;
    private EditText mEtsemestre;
    private EditText mEtTipoSangre;
    private EditText mEtContacto;
    private EditText mEtTelefonoContacto;
    private EditText mEtredSocial;






    private Button mBtWrite;
    private Button mBtRead;
    private Spinner spi;

    private NFCWriteFragment mNfcWriteFragment;
    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initNFC();




    }


    private void initViews() {
/*
* Seccion donde creamos las nuevas variables para obtener la informacion capturada por el usuario
* */

        mEtMessage = (EditText) findViewById(R.id.et_message);
        mEtMessage2 = (EditText) findViewById(R.id.et_mensaje2);
        mEtMatricula=(EditText) findViewById(R.id.et_Matricula);
        mEtsemestre=(EditText) findViewById(R.id.et_semestre);
        mEtTipoSangre=(EditText) findViewById(R.id.et_TipoSangre);
        mEtContacto=(EditText) findViewById(R.id.et_Contacto);
        mEtTelefonoContacto=(EditText) findViewById(R.id.et_TelefonoContacto);
        mEtredSocial=(EditText) findViewById(R.id.et_redSocial);



        mBtWrite = (Button) findViewById(R.id.btn_write);
        mBtRead = (Button) findViewById(R.id.btn_read);

        mBtWrite.setOnClickListener(view -> showWriteFragment());
        mBtRead.setOnClickListener(view -> showReadFragment());
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showWriteFragment() {

        isWrite = true;

        mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = NFCWriteFragment.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(),NFCWriteFragment.TAG);

    }

    private void showReadFragment() {

        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);

    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {

                    String messageToWrite = mEtMessage.getText().toString();
                    String messageToWrite2 = mEtMessage2.getText().toString();
                    String messageToWrite3 =  mEtMatricula.getText().toString();
                    String messageToWrite4 = mEtsemestre.getText().toString();
                    String messageToWrite5 = mEtTipoSangre.getText().toString();
                    String messageToWrite6 = mEtContacto.getText().toString();
                    String messageToWrite7 = mEtTelefonoContacto.getText().toString();
                    String messageToWrite8 = mEtredSocial.getText().toString();

                    String datocompleto ="INSTITUTO TECONOLOGICO DE CHAMPOTON"+"\n"+"Jaguar Card"+"\n"+"Carrera: "+messageToWrite+"\r\n"+"Nombre Alumno: "+messageToWrite2+"\n"+"Matricula: "+messageToWrite3+"\n"+"Semestre: "+messageToWrite4+"\n"+"Tipo de Sangre"+messageToWrite5+"\n"+"Nombre Contacto: "+messageToWrite6+"\n"+"Telefono Contacto: "+messageToWrite7+"\n"+"RedSocial: "+messageToWrite8;




                    mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                    mNfcWriteFragment.onNfcDetected(ndef,datocompleto);


                } else {

                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }
        }
    }

}