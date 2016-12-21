package cn.ucai.superwechat.ui;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_fragment_change);
        ButterKnife.bind(this);
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        loadingView = LayoutInflater.from(this).inflate(R.layout.rp_loading, targetLayout, false);
        targetLayout.addView(loadingView);
        change = SuperwechatHelper.getInstance().getCurrentuserChange();
        tvChangeBalance.setText(change);
        GetBalance();

    }

    private void GetBalance() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("输入充值金额");
                final EditText textView = new EditText(this);
                builder.setView(textView);
                final String rmb = textView.getText().toString();
                builder.setPositiveButton("确定充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReChange(rmb);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
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

    private void ReChange(final String rmb) {
        NetDao.ReChange(this, EMClient.getInstance().getCurrentUser(), rmb, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Wallet.class);
                    if (result != null && result.isRetMsg()) {
                        Wallet wallet = (Wallet) result.getRetData();
                        if(wallet!=null){
                            initData();
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Change Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


