package com.example.centerprimesampletronsdk;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityCreateWalletBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    ActivityCreateWalletBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);

        binding.createWallet.setOnClickListener(v -> {

            if(!TextUtils.isEmpty(binding.password.getText().toString()) && !TextUtils.isEmpty(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().trim().length() >= 6
                    && binding.confirmPassword.getText().toString().trim().length() >= 6) {
                /**
                 * Using this createWallet function user can create a wallet.
                 *
                 * @param password - must be provided password to wallet address
                 * @param Context - activity context
                 *
                 * @return walletAddress
                 */
                progressDialog = ProgressDialog.show(CreateWalletActivity.this, "",
                        "Creating TRX wallet address...", true);

            tronWalletManager.createWallet(binding.password.getText().toString(), this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(walletAddress -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        progressDialog.dismiss();
                        binding.address.setText(walletAddress.getAddress());
                        binding.copy.setVisibility(View.VISIBLE);
                        System.out.println(walletAddress);
                    }, error -> {
                        /**
                         * if function fails error can be catched in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    });
            } else {
                Toast.makeText(this, "请正确输入密码", Toast.LENGTH_SHORT).show();
            }
        });


        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.address.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪切板！", Toast.LENGTH_SHORT).show();
        });

    }
}
