package cn.ucai.superwechat.data;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.io.File;
import java.util.ArrayList;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperwechatHelper;
import cn.ucai.superwechat.bean.Gift;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MD5;


/**
 * Created by Administrator on 2016/10/17.
 */

public class NetDao {
    public static void Login(Context context,String name, String password, OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,String.valueOf(name))
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }

    public static void register(Context context, String user, String nick, String password, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,user)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }

    public static void unregister(Context context, String user, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,user)
                .targetClass(Result.class)
                .execute(listener);
    }

    public static void updateNick(Context context,String muserName, String input,OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,muserName)
                .addParam(I.User.NICK,input)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void updateAvatar(Context context,String username, File file,OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,username)
                .addParam(I.AVATAR_TYPE,"user_avatar")
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void syncUser(Context context,String username,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void searchUser(Context context,String username,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void addContact(Context context,String username,String cusername,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,cusername)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void delContact(Context context,String username,String cusername,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,cusername)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void loadContact(Context context,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME, EMClient.getInstance().getCurrentUser())
                .targetClass(String.class)
                .execute(listener);
    }

    public static void createGroup(Context context, EMGroup emGroup,File file,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,emGroup.getGroupId())
                .addParam(I.Group.NAME,emGroup.getGroupName())
                .addParam(I.Group.DESCRIPTION,emGroup.getDescription())
                .addParam(I.Group.OWNER, emGroup.getOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(emGroup.isPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(emGroup.isAllowInvites()))
                .targetClass(String.class)
                .addFile2(file)
                .post()
                .execute(listener);
    }
    public static void createGroup(Context context, EMGroup emGroup,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,emGroup.getGroupId())
                .addParam(I.Group.NAME,emGroup.getGroupName())
                .addParam(I.Group.DESCRIPTION,emGroup.getDescription())
                .addParam(I.Group.OWNER, emGroup.getOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(emGroup.isPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(emGroup.isAllowInvites()))
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void addGroupMembers(Context context, EMGroup emGroup,OkHttpUtils.OnCompleteListener<String> listener){
        String memberArr="";
        for(String m:emGroup.getMembers()){
            if(!m.equals(SuperwechatHelper.getInstance().getCurrentUsernName())){
                memberArr+= m+",";
            }
            memberArr = memberArr.substring(0,memberArr.length()-1);
            L.e("addGroupMembers","memberArr="+memberArr);
            OkHttpUtils<String> utils = new OkHttpUtils<>(context);
            utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
                    .addParam(I.Member.GROUP_HX_ID,emGroup.getGroupId())
                    .addParam(I.Member.USER_NAME,memberArr)
                    .targetClass(String.class)
                    .execute(listener);
        }
    }
    public static void getAllGifts(Context context, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ALL_GIFTS)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void GivingGifts(Context context,String username,String anchor,int giftId,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_GIVING_GIFT)
                .addParam(I.Gift.USERNAME,username)
                .addParam(I.Gift.ANCHOR,anchor)
                .addParam(I.Gift.Gid,Integer.toString(giftId))
                .addParam(I.Gift.GIFT_NUM,"1")
                .targetClass(String.class)
                .execute(listener);
    }

    public static void ReChange(Context context,String username,int rmb,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_RECHARGE)
                .addParam(I.Live.USERNAME,username)
                .addParam(I.Live.RMB,rmb+"")
                .targetClass(String.class)
                .execute(listener);
    }

    public static void GetBalance(Context context,String username,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_BALANCE)
                .addParam(I.Live.USERNAME,username)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void ReChange(Context context,String username,String rmb,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_BALANCE)
                .addParam(I.Live.USERNAME,username)
                .addParam(I.Live.RMB,rmb)
                .targetClass(String.class)
                .execute(listener);
    }
}
