package com.example.bottomnavi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private Context context;
    private final  List<Carditem> mDatalist;

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onShareButtonClicked(int position);
        void onLearnMoreButtonClicked(int position);
    }

    private MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }



    public RecyclerAdapter(List<Carditem> dataList){
        mDatalist = dataList;
    }


    @NonNull
    @Override

    // 뷰 홀더가 생성되었을 때
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bulletin_item, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);

        // 연결할 레이아웃 설정
    }

    @Override
    // 뷰와 뷰 홀더가 묶였을 때
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Carditem item = mDatalist.get(position);
        holder.title.setText(item.getTitle());
//        holder.contents.setText(item.getContents());
//        holder.name.setText(item.getName());


            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v) {
                    Carditem item = mDatalist.get(position);
                    Intent intent = new Intent(context, PostListActivity.class);
                    intent.putExtra("id", String.valueOf(item.getId()));
                    intent.putExtra("title", String.valueOf(item.getTitle()));
                    intent.putExtra("member_id", String.valueOf(item.getMember_id()));
                    context.startActivity(intent);
//                    mListener.onItemClicked(pos);
                }
            });
        }

    @Override

    // 목록의 아이템 수
    public int getItemCount() {
        return mDatalist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title_text_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}

