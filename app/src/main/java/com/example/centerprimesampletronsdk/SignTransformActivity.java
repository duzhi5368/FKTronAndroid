package com.example.centerprimesampletronsdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivitySignTrx20TransformBinding;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignTransformActivity extends AppCompatActivity {
    ActivitySignTrx20TransformBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_trx20_transform);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        tronWalletManager.init(this);
        binding.signBtn.setOnClickListener(v -> {
            binding.signBtn.setEnabled(false);

            if(!TextUtils.isEmpty(binding.unsignedData.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.address2.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String unsignedData = binding.unsignedData.getText().toString();
                String address = binding.address2.getText().toString();
                String password = binding.password.getText().toString();

                tronWalletManager.signTRX20Transform(this, unsignedData, address, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(signedData -> {
                            binding.signedData.setText(signedData);
                            binding.copy.setVisibility((View.VISIBLE));
                            binding.signBtn.setEnabled(true);
                        }, error -> {
                            binding.signedData.setText(error.getMessage() + ". 请检查输入项是否正确!");
                            error.printStackTrace();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.signBtn.setEnabled(true);
                        });
            } else {
                Toast.makeText(this, "请检查输入项是否正确!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.signedData.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪切板!", Toast.LENGTH_SHORT).show();
        });
    }
}
