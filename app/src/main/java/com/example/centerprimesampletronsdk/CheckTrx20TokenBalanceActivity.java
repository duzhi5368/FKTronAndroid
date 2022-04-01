package com.example.centerprimesampletronsdk;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityTrx20TokenBalanceBindingImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckTrx20TokenBalanceActivity extends AppCompatActivity {
    ActivityTrx20TokenBalanceBindingImpl binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trx20_token_balance);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);
        binding.checkBtn.setOnClickListener(v -> {
            /**
             * Using this getTokenBalance function you can check balance of provided walletAddress with smart contract.
             *
             * @param walletAddress - which user want to check it's balance
             * @param contractAddress - contract address of token
             *
             * @return balance
             */
            String walletAddress = binding.address.getText().toString().trim();
            String trx20TokenContractAddress = binding.contractAddress.getText().toString().trim();
          //  String trx20TokenContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
            tronWalletManager.getTokenTRX20Balance(walletAddress, trx20TokenContractAddress, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        balance = balance.divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP);
                        binding.balanceTxt.setText("USDT余额为 :" + balance.toString());
                        Toast.makeText(this, "账户余额 : " + balance, Toast.LENGTH_SHORT).show();

                    }, error -> {
                        /**
                         * if function fails error can be catched in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
