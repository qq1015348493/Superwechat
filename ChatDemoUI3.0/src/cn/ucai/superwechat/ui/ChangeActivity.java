package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.Wallet;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by User on 2016/12/15.
 */

public class ChangeActivity extends BaseActivity {
    @BindView(R.id.tv_change_balance)
    TextView tvChangeBalance;
    @BindView(R.id.target_layout)
    LinearLayout targetLayout;
    private View loadingView;
    String change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_fragment_change);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadingView = LayoutInflater.from(this).inflate(R.layout.rp_loading,targetLayout , false);
        targetLayout.addView(loadingView);
        change = SuperwechatHelper.getInstance().getCurrentuserChange();
        tvChangeBalance.setText(change);
        NetDao.GetBalance(this, EMClient.getInstance().getCurrentUser(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Wallet.class);
                    if (result != null && result.isRetMsg()) {
                        Wallet wallet = (Wallet) result.getRetData();
                        SuperwechatHelper.getInstance().setCurrentuserChange(wallet.getBalance().toString());
                        tvChangeBalance.setText("¥" + Float.valueOf(wallet.getBalance()) + "");
                        loadingView.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.tv_change_recharge, R.id.tv_change_withdraw, R.id.tv_change_details, R.id.tv_my_bankcard, R.id.tv_my_red_packet_records, R.id.tv_verify_identity, R.id.tv_forget_pay_pwd, R.id.tv_common_problem})
    public void onClick(View view) {
        switch (view.getId()) {
            //充值
            case R.id.tv_change_recharge:
                break;
            //提现
            case R.id.tv_change_withdraw:
                break;
            //明细
            case R.id.tv_change_details:
                break;
            //银行卡
            case R.id.tv_my_bankcard:
                break;
            //红包
            case R.id.tv_my_red_packet_records:
                break;
            //未验证
            case R.id.tv_verify_identity:
                break;
            //忘记密码
            case R.id.tv_forget_pay_pwd:
                break;
            //问题
            case R.id.tv_common_problem:
                break;
        }
    }
}


