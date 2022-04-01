package com.example.centerprimesampletronsdk;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityExportPrivateKeyBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExportPrivateKeyActivity extends AppCompatActivity {
    ActivityExportPrivateKeyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_export_private_key);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(this);

        binding.button.setOnClickListener(v -> {
/**
 * Using this getKeyStore function user can get keyStore of provided walletAddress.
 *
 * @param WalletAddress - wallet address which user want to get key store
 * @param Context - activity context
 *
 * @return if the function is completed successfully returns keyStore JSON file or error name
 */
            String walletAddress = "";
            if(!TextUtils.isEmpty(binding.address.getText().toString())) {
                walletAddress = binding.address.getText().toString();
            } else {
                Toast.makeText(this, "输入钱包地址", Toast.LENGTH_SHORT).show();
            }

            String password = "";
            if(!TextUtils.isEmpty(binding.password.getText().toString())
                    && binding.password.getText().toString().trim().length() >= 6) {
                password = binding.password.getText().toString();
            }else {
                Toast.makeText(this, "密码不符合规则", Toast.LENGTH_SHORT).show();
            }

            tronWalletManager.exportPrivateKey(walletAddress, password,this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(privateKey -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        binding.privateKey.setVisibility(View.VISIBLE);
                        binding.copy.setVisibility(View.VISIBLE);
                        binding.privateKey.setText(privateKey);
                        hideKeyboard(this);


                    }, error -> {
                        /**
                         * if function fails error can be catched in this block
                         */
                        Toast.makeText(this, "请输入有效钱包地址", Toast.LENGTH_SHORT).show();
                    });
        });

        binding.copy.setOnClickListener(v ->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.privateKey.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪切板!", Toast.LENGTH_SHORT).show();
        });
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
