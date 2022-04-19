package com.example.centerprimesampletronsdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.centerprimesampletronsdk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.createBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CreateWalletActivity.class));
        });
        binding.checkBalance.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CheckBalanceActivity.class));
        });
        binding.exportKeystore.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExportKeyStoreActivity.class));
        });
        binding.exportPrivateKeyBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExportPrivateKeyActivity.class));
        });
        binding.importBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ImportWalletFromKeyStoreActivity.class));
        });
        binding.importPrivateKeyBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ImportWalletFromPrivateKeyActivity.class));
        });
        binding.checkERCTokenkBalance.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CheckTrx20TokenBalanceActivity.class));
        });
        binding.sendTrx.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SendTrxActivity.class));
        });
        binding.sendUSDT.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SendUSDTActivity.class));
        });

        // Create mutilsig
        // create a transform
        // Sign a transform - signTransaction / signTransactionByApi2
        // Boardcast a transform  - broadcastTransaction

    }
}