/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ucai.superwechat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.data.LiveRoom;
import cn.ucai.superwechat.utils.Clazz2;

public class PublicChatRoomsActivity extends BaseActivity {
    private ProgressBar pb;
    private RecyclerView listView;

    private List<EMChatRoom> chatRoomList;
    private boolean isLoading;
    private boolean isFirstLoading = true;
    private boolean hasMoreData = true;
    private String cursor;
    private final int pagesize = 50;
    private LinearLayout footLoadingLayout;
    private ProgressBar footLoadingPB;
    private TextView footLoadingText;
    private EditText etSearch;
    private ImageButton ibClean;
    private List<EMChatRoom> rooms;
    PhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_public_groups);
        etSearch = (EditText) findViewById(R.id.query);
        ibClean = (ImageButton) findViewById(R.id.search_clear);
        etSearch.setHint(R.string.search);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        listView = (RecyclerView) findViewById(R.id.recycleview);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.chat_room));
        chatRoomList = new ArrayList<EMChatRoom>();
        rooms = new ArrayList<EMChatRoom>();

//		View footView = getLayoutInflater().inflate(R.layout.em_listview_footer_view, listView, false);
//        footLoadingLayout = (LinearLayout) footView.findViewById(R.id.loading_layout);
//        footLoadingPB = (ProgressBar)footView.findViewById(R.id.loading_bar);
//        footLoadingText = (TextView) footView.findViewById(R.id.loading_text);
//        footLoadingLayout.setVisibility(View.GONE);
//
//        etSearch.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			    if (mAdapter != null) {
//			        mAdapter.getFilter().filter(s);
//			    }
//				if(s.length()>0){
//					ibClean.setVisibility(View.VISIBLE);
//				}else{
//					ibClean.setVisibility(View.INVISIBLE);
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});

        ibClean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.getText().clear();
                hideSoftKeyboard();
            }
        });

        loadAndShowData();

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(new EMChatRoomChangeListener() {
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                chatRoomList.clear();
                if (mAdapter != null) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                                loadAndShowData();
                            }
                        }

                    });
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                                       String participant) {

            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                                       String participant) {
            }

        });

//        listView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final EMChatRoom room = adapter.getItem(position);
//                startActivity(new Intent(PublicChatRoomsActivity.this, ChatActivity.class).putExtra("chatType", 3).
//                		putExtra("userId", room.getId()));
//
//            }
//        });

    }

    private void setPullUpListener() {
        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                loadAndShowData();
            }
        });
    }

    private void loadAndShowData() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    isLoading = true;
                    final EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(pagesize, cursor);
                    //get chat room list
                    final List<EMChatRoom> chatRooms = result.getData();
                    runOnUiThread(new Runnable() {

                        public void run() {
                            chatRoomList.addAll(chatRooms);
                            if (chatRooms.size() != 0) {
                                cursor = result.getCursor();
                            }
                            if (isFirstLoading) {
                                pb.setVisibility(View.INVISIBLE);
                                isFirstLoading = false;
                                mAdapter = new PhotoAdapter(PublicChatRoomsActivity.this, Clazz2.EMChatRoom2LiveRoom(chatRoomList));
                                listView.setAdapter(mAdapter);
                                rooms.addAll(chatRooms);
                            } else {
                                if (chatRooms.size() < pagesize) {
                                    hasMoreData = false;
                                    footLoadingLayout.setVisibility(View.VISIBLE);
                                    footLoadingPB.setVisibility(View.GONE);
                                    footLoadingText.setText(getResources().getString(R.string.no_more_messages));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            isLoading = false;
                            pb.setVisibility(View.INVISIBLE);
                            footLoadingLayout.setVisibility(View.GONE);
                            Toast.makeText(PublicChatRoomsActivity.this, getResources().getString(R.string.failed_to_load_data), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void search(View view) {
    }

    /**
     * adapter
     *
     */
//	private class ChatRoomAdapter extends ArrayAdapter<EMChatRoom> {
//
//		private LayoutInflater inflater;
//		private RoomFilter filter;
//
//		public ChatRoomAdapter(Context context, int res, List<EMChatRoom> rooms) {
//			super(context, res, rooms);
//			this.inflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
//			}
//
//			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getName());
//
//			return convertView;
//		}
//
//		@Override
//		public Filter getFilter(){
//			if(filter == null){
//				filter = new RoomFilter();
//			}
//			return filter;
//		}
//
//		private class RoomFilter extends Filter{
//
//			@Override
//			protected FilterResults performFiltering(CharSequence constraint) {
//				FilterResults results = new FilterResults();
//
//				if(constraint == null || constraint.length() == 0){
//					results.values = rooms;
//					results.count = rooms.size();
//				}else{
//					List<EMChatRoom> roomss = new ArrayList<EMChatRoom>();
//					for(EMChatRoom chatRoom : rooms){
//						if(chatRoom.getName().contains(constraint)){
//							roomss.add(chatRoom);
//						}
//					}
//					results.values = roomss;
//					results.count = roomss.size();
//				}
//				return results;
//			}
//
//			@SuppressWarnings("unchecked")
//			@Override
//			protected void publishResults(CharSequence constraint, FilterResults results) {
//				chatRoomList.clear();
//				chatRoomList.addAll((List<EMChatRoom>)results.values);
//				notifyDataSetChanged();
//			}
//
//		}
//	}
    public void back(View view) {
        finish();
    }

    static class PhotoAdapter extends RecyclerView.Adapter{
        private final List<LiveRoom> liveRoomList;
        private final Context context;

        boolean isMore;

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean ismore) {
            isMore = ismore;
            notifyDataSetChanged();
        }

        private int getFootString() {
            return isMore ? R.string.loading_more : R.string.load_more_end;
        }

        public PhotoAdapter(Context context, List<LiveRoom> liveRoomList) {
            this.liveRoomList = liveRoomList;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (viewType == Constant.ACTION_TYPE_FOOTER) {
                holder = new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.em_listview_footer_view, parent, false));
            } else {
                holder = new PhotoViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.layout_livelist_item, parent, false));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position)==Constant.ACTION_TYPE_FOOTER){
                FooterViewHolder vh = (FooterViewHolder) holder;
                vh.loadingText.setText(getFootString());
                vh.loadingBar.setVisibility(isMore?View.VISIBLE:View.GONE);
            }else {
                final PhotoViewHolder vh = (PhotoViewHolder) holder;
                LiveRoom liveRoom = liveRoomList.get(position);
                vh.anchor.setText(liveRoom.getName());
                vh.audienceNum.setText(liveRoom.getAudienceNum()+"人");
                EaseUserUtils.setCover(context, liveRoom.getCover(), vh.imageView);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final int position = vh.getAdapterPosition();
//                        if (position == RecyclerView.NO_POSITION) return;
////                        LiveRoom room = Clazz2.EMChatRoom2LiveRoom(liveRoomList.get(position));
//                        Intent intent;
//                        if (EMClient.getInstance().getCurrentUser() == room.getAnchorId()) {
//                            intent = new Intent(context, StartLiveActivity.class);
//                        } else {
//                            intent = new Intent(context, LiveDetailsActivity.class);
//                        }
//                        intent.putExtra("liveroom", room);
//                        context.startActivity(intent);
//                    }
//                });
            }

        }

        @Override
        public int getItemCount() {
            return liveRoomList.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == getItemCount()-1){
                return Constant.ACTION_TYPE_FOOTER;
            }else {
                return Constant.ACTION_TYPE_ITEM;
            }
        }

        static class FooterViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.loading_bar)
            ProgressBar loadingBar;
            @BindView(R.id.loading_text)
            TextView loadingText;
            @BindView(R.id.loading_layout)
            LinearLayout loadingLayout;

            FooterViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
        static class PhotoViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.photo)
            ImageView imageView;
            @BindView(R.id.author)
            TextView anchor;
            @BindView(R.id.audience_num)
            TextView audienceNum;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
