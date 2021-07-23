package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context context;
    private Context mContext;
    private int comment = 0;
    private List<CommentListItem> mDatalist;
    private String url6;
    private String url1;


    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onShareButtonClicked(int position);
        void onLearnMoreButtonClicked(int position);
    }

    private CommentListAdapter.MyRecyclerViewClickListener mListener;


    public void setmDatalist(List<CommentListItem> mDatalist){
        this.mDatalist = mDatalist;
    }

    public int getComment(){
        return comment;
    }

    public void setOnClickListener(CommentListAdapter.MyRecyclerViewClickListener listener){
        mListener = listener;
    }
    public CommentListAdapter() {

    }
    public CommentListAdapter(Context context){
        this.mContext = context;
    }
    public CommentListAdapter(List<CommentListItem> mDatalist) {
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentlist_item, parent, false);

        context = parent.getContext();

        return new CommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, final int position) {
        CommentListItem item = mDatalist.get(position);
        holder.name.setText(item.getNick_name());
        holder.contents.setText(item.getContent());
        holder.post_time.setText(item.getTime());
        holder.like_num.setText(""+ item.getLike_num());
        Log.d("어찌된일1", "어찌된일1");
        if(item.getDepth() == 1){
            if(position != 0){
                Log.d("어찌된일2", "어찌된일2");
                holder.cardView.setCardBackgroundColor(Color.GRAY);
                holder.button.setVisibility(View.INVISIBLE);
            }
        }
        if(item.getMember_id() != ((PostDetailActivity)mContext).login_member_id){
            holder.delete_comment_button.setVisibility(View.INVISIBLE);
        }
        final int pos = position;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentListItem item = mDatalist.get(position);
                comment = 1;
//                Toast.makeText(context, String.valueOf(item.getId()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PostDetailActivity.class);
//                intent.putExtra("id", String.valueOf(item.getId()));
//                context.startActivity(intent);
                ((PostDetailActivity)mContext).test();
                ((PostDetailActivity)mContext).comment = 1;
                ((PostDetailActivity)mContext).commentNumber = item.getId();
                ((PostDetailActivity)mContext).request("aa");
                Log.d("Hi", String.valueOf(item.getId()));
            }
        });
        holder.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentListItem item = mDatalist.get(position);
                comment = 1;

//                Toast.makeText(context, String.valueOf(item.getId()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PostDetailActivity.class);
//                intent.putExtra("id", String.valueOf(item.getId()));
//                context.startActivity(intent);
                ((PostDetailActivity)mContext).comment = 1;
                ((PostDetailActivity)mContext).commentNumber = item.getId();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = ((PostDetailActivity)mContext).getIntent();
                        url6 = "http://test.danggeun.ga/comment/" + item.getId() + "/like";
                        String url7 = "http://test.danggeun.ga/comment2/" + item.getId() + "/like";
                        url1 = "http://test.danggeun.ga/comment?post_id=" + intent.getStringExtra("id");
                        if(item.getDepth()==0){
                            ((PostDetailActivity)mContext).request8(url6);
                            ((PostDetailActivity)mContext).request2(url1);
                        }
                        else if(item.getDepth() ==1){
                            ((PostDetailActivity)mContext).request8(url7);
                            ((PostDetailActivity)mContext).request2(url1);
                        }

                        Log.d("url6",url6+"");

                        Log.d("url1",url1+"");

                    }
                }).start();
                Log.d("Hi", String.valueOf(item.getId()));
            }
        });
        holder.delete_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentListItem item = mDatalist.get(position);
                comment = 1;

//                Toast.makeText(context, String.valueOf(item.getId()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PostDetailActivity.class);
//                intent.putExtra("id", String.valueOf(item.getId()));
//                context.startActivity(intent);
                ((PostDetailActivity)mContext).comment = 1;
                ((PostDetailActivity)mContext).commentNumber = item.getId();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = ((PostDetailActivity)mContext).getIntent();
                        url6 = "http://test.danggeun.ga/comment/" + item.getId();
                        String url7 = "http://test.danggeun.ga/comment2/" + item.getId();
                        url1 = "http://test.danggeun.ga/comment?post_id=" + intent.getStringExtra("id");
                        if(item.getDepth()==0){
                            ((PostDetailActivity)mContext).request9(url6);
                            ((PostDetailActivity)mContext).request2(url1);
//                            for(int i = 0; i < getItemCount(); i++){
//                                if(((PostDetailActivity)mContext).member_id2[i] != ((PostDetailActivity)mContext).login_member_id){
//                                    Log.d("heeeei2", String.valueOf(((PostDetailActivity)mContext).member_id2[i]));
//                                    Log.d("heeeei2", String.valueOf(((PostDetailActivity)mContext).login_member_id));
//                                    ((PostDetailActivity)mContext).delete_comment_button.setVisibility(View.INVISIBLE);
//                                }
//                                else{Log.d("hi2", String.valueOf(((PostDetailActivity)mContext).member_id2[i]));
//                                    Log.d("hi2", String.valueOf(((PostDetailActivity)mContext).login_member_id));
//                                    ((PostDetailActivity)mContext).delete_comment_button.setVisibility(View.VISIBLE);
//                                }
//                            }
                        }
                        else if(item.getDepth() ==1){
                            ((PostDetailActivity)mContext).request9(url7);
                            ((PostDetailActivity)mContext).request2(url1);
//                            for(int i = 0; i < getItemCount(); i++){
//                                if(((PostDetailActivity)mContext).member_id3[i] != ((PostDetailActivity)mContext).login_member_id){
//                                    Log.d("heeeei2", String.valueOf(((PostDetailActivity)mContext).member_id3[i]));
//                                    Log.d("heeeei2", String.valueOf(((PostDetailActivity)mContext).login_member_id));
//                                    ((PostDetailActivity)mContext).delete_comment_button.setVisibility(View.INVISIBLE);
//                                }
//                                else{Log.d("hi2", String.valueOf(((PostDetailActivity)mContext).member_id3[i]));
//                                    Log.d("hi2", String.valueOf(((PostDetailActivity)mContext).login_member_id));
//                                    ((PostDetailActivity)mContext).delete_comment_button.setVisibility(View.VISIBLE);
//                                }
//                            }
                        }
                    }
                }).start();
                Log.d("Hi", String.valueOf(item.getId()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
//                CommentListItem item = mDatalist.get(position);
//                Intent intent = new Intent(context,PostDetailActivity.class);
//                intent.putExtra("id", String.valueOf(item.getId()));
//                intent.putExtra("member_id", String.valueOf(item.getMember_id()));
//                intent.putExtra("title", String.valueOf(item.getTitle()));
//                context.startActivity(intent);
//                    mListener.onItemClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView contents;
        TextView post_time;
        TextView like_num;
        CardView cardView;
        Button button;
        Button button4;
        Button delete_comment_button;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            name = view.findViewById(R.id.Name_text_item);
            contents = view.findViewById(R.id.Contents_text_item);
            post_time = view.findViewById(R.id.Post_time_text_item);
            like_num = view.findViewById(R.id.Like_num_text_item);
            button = view.findViewById(R.id.button);
            button4 = view.findViewById(R.id.button4);
            delete_comment_button = view.findViewById(R.id.delete_comment_button);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
