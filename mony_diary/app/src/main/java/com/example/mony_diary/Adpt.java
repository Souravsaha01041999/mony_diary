package com.example.mony_diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adpt extends RecyclerView.Adapter<Adpt.MyHolder> {
    interface UpdateData
    {
        void onUpdate(String uid,String id,String title,String price,String date,ImageView img);
    }
    List<ListData> data;
    Context context;
    UpdateData ud;

    Adpt(Context c) {
        this.context = c;
        data = new ArrayList<>();
    }
    void setUpdateListener(UpdateData ud)
    {
        this.ud=ud;
    }

    void add(ListData ld)
    {
        this.data.add(ld);
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.dsg,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice());
        holder.date.setText(data.get(position).getDate());

        if (data.get(position).getStatus()=='n')
            holder.reupload.setVisibility(View.VISIBLE);

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getStatus()=='n')
                {
                    ud.onUpdate(data.get(position).getUid(),
                            data.get(position).getId(),
                            data.get(position).getTitle(),
                            data.get(position).getPrice(),
                            data.get(position).getDate(),
                            holder.reupload
                            );
                }
                else
                {
                    context.startActivity(new Intent(context,update.class)
                            .putExtra("id",data.get(position).getId())
                            .putExtra("title",data.get(position).getTitle())
                            .putExtra("price",data.get(position).getPrice())
                    );
                }
            }
        });

    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView title,date,price;
        ImageView reupload;
        RelativeLayout rl;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.fortitle);
            date=itemView.findViewById(R.id.fordate);
            price=itemView.findViewById(R.id.forprice);
            rl=itemView.findViewById(R.id.forclick);
            reupload=itemView.findViewById(R.id.reupload);

        }
    }
}
