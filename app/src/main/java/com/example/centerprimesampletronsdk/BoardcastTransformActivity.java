package com.example.centerprimesampletronsdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityBoardcastTrx20TransformBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BoardcastTransformActivity extends AppCompatActivity {
    ActivityBoardcastTrx20TransformBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardcast_trx20_transform);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        tronWalletManager.init(this);
        binding.boardcastBtn.setOnClickListener(v -> {
            binding.boardcastBtn.setEnabled(false);

            if(!TextUtils.isEmpty(binding.keystoreT.getText().toString().trim())) {

                String txData = binding.keystoreT.getText().toString();

                tronWalletManager.broadcastTRC20Transform(txData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(r -> {
                            if(r) {
                                binding.boardcastResult.setText("公告完成");
                                binding.boardcastBtn.setEnabled(true);
                            } else {
                                binding.boardcastResult.setText("公告出现异常失败");
                                Toast.makeText(this, "公告出现异常失败", Toast.LENGTH_SHORT).show();
                                binding.boardcastBtn.setEnabled(true);
                            }
                        }, error -> {
                            binding.boardcastResult.setText(error.getMessage() + ". 请检查输入项是否正确!");
                            error.printStackTrace();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.boardcastBtn.setEnabled(true);
                        });
            } else {
                Toast.makeText(this, "请检查输入项是否正确!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
