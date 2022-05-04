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
import com.example.centerprimesampletronsdk.databinding.ActivityCreateTrx20TransformBinding;
import com.example.centerprimesampletronsdk.databinding.ActivityCreateWalletBinding;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateTransformActivity extends AppCompatActivity {
    ActivityCreateTrx20TransformBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_trx20_transform);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);
        binding.createTransform.setOnClickListener(v -> {
            binding.createTransform.setEnabled(false);
            if(!TextUtils.isEmpty(binding.address.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.ethAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password3.getText().toString().trim())) {

                String walletAddress = binding.address.getText().toString();
                String password = binding.password3.getText().toString();

                BigDecimal usdtAmount = new BigDecimal(binding.ethAmount.getText().toString().trim());
                BigDecimal gasLimit = new BigDecimal(binding.gasLimit.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();

                String usdtContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
                tronWalletManager.createTRX20Transform(this, walletAddress, password, receiverAddress, usdtContractAddress, usdtAmount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(transform -> {
                            binding.result.setText(transform);
                            binding.copy2.setVisibility((View.VISIBLE));
                            binding.createTransform.setEnabled(true);
                        }, error -> {
                            binding.result.setText(error.getMessage() + ". 请检查输入项是否正确!");
                            error.printStackTrace();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.createTransform.setEnabled(true);
                        });
            } else {
                Toast.makeText(this, "请检查输入项是否正确!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.copy2.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.result.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪切板!", Toast.LENGTH_SHORT).show();
        });
    }
}
