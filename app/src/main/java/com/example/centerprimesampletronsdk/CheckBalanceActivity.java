package com.example.centerprimesampletronsdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityCheckBalanceBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.math.RoundingMode;

public class CheckBalanceActivity extends AppCompatActivity {
    ActivityCheckBalanceBinding balanceBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balanceBinding = DataBindingUtil.setContentView(this, R.layout.activity_check_balance);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);
        balanceBinding.checkBtn.setOnClickListener(v -> {
            /**
             * Using this getBalanceTrx function you can check balance of provided walletAddress.
             *
             * @param walletAddress - which user want to check it's balance
             * @param context - activity context
             *
             * @return if the function completes successfully returns balance of provided wallet address or returns error name
             */
            String address = balanceBinding.address.getText().toString();

            tronWalletManager.getBalanceTrx(address, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        balance = balance.setScale(2, RoundingMode.HALF_UP);
                        balanceBinding.balanceTxt.setText("TRX余额为: " + balance.toString());
                        balanceBinding.balanceTxt.setVisibility(View.VISIBLE);

                    }, error -> {
                        /**
                         * if function fails error can be caught in this block
                         */
                        balanceBinding.balanceTxt.setVisibility(View.INVISIBLE);
                        System.out.println(error.getMessage());
                        Toast.makeText(this,  error.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        });
    }
}
