package cn.ucai.superwechat.utils;

import com.hyphenate.chat.EMChatRoom;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.superwechat.data.LiveRoom;

/**
 * Created by User on 2016/12/12.
 */

public class Clazz2 {
    public static List<LiveRoom> EMChatRoom2LiveRoom(List<EMChatRoom> chatRooms){
        List<LiveRoom> roomlist = new ArrayList<>();
        for(EMChatRoom chatRoom : chatRooms){
            LiveRoom room = new LiveRoom();
            room.setName(chatRoom.getName());
            room.setAudienceNum(chatRoom.getAffiliationsCount());
            room.setId(chatRoom.getId());
            room.setChatroomId(chatRoom.getId());
            room.setCover(chatRoom.getId());
            room.setAnchorId(chatRoom.getOwner());
            roomlist.add(room);
        }
        return roomlist;
    }

}
