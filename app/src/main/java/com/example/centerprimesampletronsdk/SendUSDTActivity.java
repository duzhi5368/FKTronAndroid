package com.example.centerprimesampletronsdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivitySendTrx20TokenBinding;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SendUSDTActivity extends AppCompatActivity {
    ActivitySendTrx20TokenBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_trx20_token);
        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);

        binding.sendERCToken.setOnClickListener(v -> {
            binding.sendERCToken.setEnabled(false);
            if(!TextUtils.isEmpty(binding.address.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.ethAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String walletAddress = binding.address.getText().toString();
                String password = binding.password.getText().toString();

                BigDecimal usdtAmount = new BigDecimal(binding.ethAmount.getText().toString().trim());
                BigDecimal gasLimit = new BigDecimal(binding.gasLimit.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();

                String usdtContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
                tronWalletManager.sendTRX20(this, walletAddress, password, receiverAddress, usdtContractAddress, usdtAmount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(usdt -> {
                            binding.result.setText("交易Hash: " + usdt);
                            Toast.makeText(this, "已发送USDT : " + usdtAmount.toString(), Toast.LENGTH_SHORT).show();
                            binding.sendERCToken.setEnabled(true);
                        }, error -> {
                            binding.result.setText(error.getMessage() + ". 请检查输入项是否正确!");

                            error.printStackTrace();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.sendERCToken.setEnabled(true);
                        });
            }

        });
    }
}
