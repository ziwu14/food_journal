package com.example.foodJournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodJournal.R;

import java.util.ArrayList;

public class DangerFoodAdapter extends RecyclerView.Adapter<DangerFoodAdapter.DangerFoodViewHolder> {

    private Context mContext;//to tell which activity
    private ArrayList<String[]> mDataList;//to tell data source; here 0: name, 1: amount

    public interface OnItemClickListener {
        void onItemDeleteClick(int position);//this method will be stored in the external when we pass the external OnItemClickListen from external to internal
    }
    private OnItemClickListener mListerner;//listener for single item
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }
    ////--------------------------constructor-----------------------------------------
    public DangerFoodAdapter(Context context, ArrayList<String[]> dataList){
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public DangerFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_danger_food, parent, false);
        return new DangerFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DangerFoodViewHolder holder, int position) {
        if (mDataList.size() == 0) {
            return;
        }

        String[] itemInfo = mDataList.get(position);

        holder.name.setText(itemInfo[0]);
    }

    //# of items in the cursor
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public String queryName(int position) {
        return mDataList.get(position)[0];
    }

    public void addData(String[] Data) {
        mDataList.add(0,Data);
        notifyItemInserted(0);

    }

    public void removeData(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void swapDataList(ArrayList<String[]> newDataList) {
        mDataList = newDataList;
        notifyDataSetChanged();
    }



    ////----------------------------Customize ViewHolder -----------------------------------
    public class DangerFoodViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageButton delBtn;

        public DangerFoodViewHolder(View itemView) {
            super(itemView);//extend RecyclerView.ViewHolder

            name = itemView.findViewById(R.id.item_danger_food_name);


            delBtn = itemView.findViewById(R.id.item_danger_food_deleteBtn);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListerner.onItemDeleteClick(position);
                        removeData(position);
                    }
                }
            });

        }
    }
}
