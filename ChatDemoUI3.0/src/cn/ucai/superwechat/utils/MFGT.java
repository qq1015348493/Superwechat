package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.easeui.domain.User;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.data.LiveRoom;
import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.AddFriendActivity;
import cn.ucai.superwechat.ui.ChatActivity;
import cn.ucai.superwechat.ui.ContactListFragment;
import cn.ucai.superwechat.ui.FriendProfileActivity;
import cn.ucai.superwechat.ui.LiveDetailsActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.StartLiveActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;


public class MFGT { public static void finish(Activity activity){ activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }
    public static void startActivityforResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoLogin(Activity context) {
        startActivity(context, LoginActivity.class);
    }

    public static void gotoRegister(Activity context) {
        startActivity(context, RegisterActivity.class);
    }
    public static void gotoSettings(Activity context) {
        startActivity(context, SettingsActivity.class);
    }
    public static void gotoUserProfile(Activity context) {
        startActivity(context, UserProfileActivity.class);
    }
    public static void gotoAddContact(Activity context) {
        startActivity(context, AddContactActivity.class);
    }

    public static void gotoFriendProfile(Activity context, String username){
        Intent intent = new Intent();
        intent.putExtra(I.User.USER_NAME,username);
        intent.setClass(context, FriendProfileActivity.class);
        startActivity(context,intent);
    }
    public static void gotoAddFriend(Activity context, User user){
        Intent intent = new Intent();
        intent.putExtra(I.User.USER_NAME,user);
        intent.setClass(context, AddFriendActivity.class);
        startActivity(context,intent);
    }

    public static void gotoChat(Activity context, String mUserName) {
        Intent intent = new Intent();
        intent.putExtra("userId",mUserName);
        intent.setClass(context, ChatActivity.class);
        startActivity(context,intent);
    }
    public static void gotoStartLive(Context context, LiveRoom liveRoom) {
        Intent intent = new Intent();
        intent.putExtra("liveroom",liveRoom);
        intent.setClass(context,StartLiveActivity.class);
        context.startActivity(intent);
    }

    public static void gotoLiveDetails(Context context,LiveRoom liveRoom) {
        Intent intent = new Intent();
        intent.putExtra("liveroom",liveRoom);
        intent.setClass(context,LiveDetailsActivity.class);
        context.startActivity(intent);
    }
}
