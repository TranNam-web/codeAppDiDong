package com.manager.doandidong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.doandidong.Interface.ItemClickListener;
import com.manager.doandidong.R;
import com.manager.doandidong.model.DonHang;
import com.manager.doandidong.model.EventBus.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private  RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    List<DonHang> listdonhang;
    Context context ;


    public DonHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder .txtdonhang.setText("Đơn hàng: "+donHang.getId() );
        holder.diachi.setText("Địa chỉ:" + donHang.getDiachi());
        holder.username.setText("Người đặt:" + donHang.getUsername());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tongtien.setText("Tổng tiền:"+decimalFormat.format(Double.parseDouble(donHang.getTongtien()))+"VNĐ");
        holder.trangthai.setText(trangThaiDon(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adpater chitiet
        ChitietAdapter chitietAdapter = new ChitietAdapter(context,donHang.getItem());
        holder.reChitiet.setAdapter(chitietAdapter);
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setRecycledViewPool(viewPool);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            }
        });
    }
    private  String trangThaiDon(int status){
        String result = "";
        switch (status){
            case  0:
                result = "Đơn hàng đang được xử lý";
                break;
            case 1:
                result = "Đơn hàng đã được chấp nhận";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Giao hàng thành công";
                break;
            case 4:
                result = "Đơn hàng đã huỷ";
                break;


        }


        return  result;
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang,trangthai,diachi,username,tongtien;
        RecyclerView reChitiet;
        ItemClickListener listener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            trangthai = itemView.findViewById(R.id.tinhtrang);
            tongtien = itemView.findViewById(R.id.tongtien_donhang);
            diachi = itemView.findViewById(R.id.diachi_donhang);
            username = itemView.findViewById(R.id.user_donhang);
            reChitiet = itemView.findViewById(R.id.recycleview_chitiet);
            itemView.setOnLongClickListener(this);


        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view,getAdapterPosition(),true);
            return false;
        }
    }
}
