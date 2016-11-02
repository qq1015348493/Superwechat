package cn.ucai.superwechat.utils;

import android.widget.Toast;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperwechatApplication;

/**
 * Created by clawpo on 16/9/20.
 */
public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(SuperwechatApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(SuperwechatApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(SuperwechatApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(SuperwechatApplication.getInstance().getString(rId));
    }
    public static void showLongResultMsg(int msg){
        showLongToast(getMsgString(msg));
    }
    public static void showShortResultMsg(int msg){
        showShortToast(getMsgString(msg));
    }
    private static int getMsgString(int msg){
        int resId = 0/*R.string.msg_1*/;
        if(msg>0){
            resId = SuperwechatApplication.getInstance().getResources()
                    .getIdentifier(I.MSG_PREFIX_MSG+msg,"string",
                            SuperwechatApplication.getInstance().getPackageName());
        }
        return resId;
    }

    public static String getWeChatNoString(){
        return SuperwechatApplication.getInstance().getString(R.string.userinfo_txt_wechat_no);
    }

    public static String getAddContactPrefixString(){
        return SuperwechatApplication.getInstance().getString(R.string.addcontact_send_msg_prefix);
    }
}
