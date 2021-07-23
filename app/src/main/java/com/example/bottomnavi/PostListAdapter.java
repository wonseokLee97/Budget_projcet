package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//public class PostListAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
//    private Context context;
//    private final List<PostListItem> mDatalist;
//
//    public interface MyRecyclerViewClickListener{
//        void onItemClicked(int position);
//        void onShareButtonClicked(int position);
//        void onLearnMoreButtonClicked(int position);
//    }
//
//    private RecyclerAdapter.MyRecyclerViewClickListener mListener;
//
//    public void setOnClickListener(RecyclerAdapter.MyRecyclerViewClickListener listener){
//        mListener = listener;
//    }
//
//    public PostListAdapter(List<PostListItem> dataList){
//        mDatalist = dataList;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.postlist_item, parent, false);
//
//        context = parent.getContext();
//
//        return new RecyclerAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, final int position) {
//        PostListItem item = mDatalist.get(position);
//        holder.title.setText(item.getTitle());
////        holder.contents.setText(item.getContents());
////        holder.name.setText(item.getName());
//
//        final int pos = position;
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v) {
//                PostListItem item = mDatalist.get(position);
//                Intent intent = new Intent(context,PostListActivity.class);
//                intent.putExtra("id", item.getId());
//                intent.putExtra("member_id", item.getMember_id());
//                intent.putExtra("title", item.getTitle());
//                context.startActivity(intent);
////                    mListener.onItemClicked(pos);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDatalist.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder{
//        TextView title;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.Title_text_item);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//    }
//}

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{
    private Context context;
    private final List<PostListItem> mDatalist;

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onShareButtonClicked(int position);
        void onLearnMoreButtonClicked(int position);
    }

    private PostListAdapter.MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }

    public PostListAdapter(List<PostListItem> mDatalist) {
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postlist_item, parent, false);

        context = parent.getContext();

        return new PostListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder holder, final int position) {
        PostListItem item = mDatalist.get(position);
        holder.title.setText(item.getTitle());
        holder.contents.setText(item.getContent());
        holder.post_time.setText(item.getPost_time());
        holder.like_num.setText("" + item.getLike_num());
        holder.nick_name.setText(item.getNick_name());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url2 = "http://test.danggeun.ga/comment?post_id=" + item.getId();
                ((PostListActivity)context).request2(url2);
                Log.d("CountNum", String.valueOf(((PostListActivity)context).count));
                ((PostListActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.comment_num.setText("[ " +String.valueOf(((PostListActivity)context).count) + " ]");

                    }
                });
            }
        }).start();
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                PostListItem item = mDatalist.get(position);
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(item.getId()));
                intent.putExtra("member_id", String.valueOf(item.getMember_id()));
                intent.putExtra("title", String.valueOf(item.getTitle()));
                context.startActivity(intent);
//                    mListener.onItemClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView contents;
        TextView post_time;
        TextView like_num;
        TextView comment_num;
        TextView nick_name;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.Title_text_item);
            contents = view.findViewById(R.id.Contents_text_item);
            post_time = view.findViewById(R.id.Post_time_text_item);
            like_num = view.findViewById(R.id.Like_num_text_item);
            comment_num = view.findViewById(R.id.comment_num);
            nick_name = view.findViewById(R.id.nick_name_);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}