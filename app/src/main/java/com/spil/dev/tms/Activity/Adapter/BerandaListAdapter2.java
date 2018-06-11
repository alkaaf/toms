//package com.andresual.dev.tms.Activity.Adapter;
//
//import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.andresual.dev.tms.Activity.Model.JobOrderModel;
//import com.andresual.dev.tms.R;
//
//import java.util.List;
//
///**
// * Created by andresual on 2/1/2018.
// */
//
//public class BerandaListAdapter2 extends ArrayAdapter<JobOrderModel> {
//
//    private Activity context;
//    private List<JobOrderModel> jobOrderModelList;
//
//    public BerandaListAdapter2(Activity context, List<JobOrderModel> jobOrderModelList) {
//        super(context, R.layout.list_beranda, jobOrderModelList);
//        this.context = context;
//        this.jobOrderModelList = jobOrderModelList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.list_beranda, null, true);
//
//        TextView tvName = (TextView) listViewItem.findViewById(R.id.tv_nama);
//        JobOrderModel jobOrderModel = jobOrderModelList.get(position);
//
//        tvName.setText(jobOrderModel.getJobDescription());
//
//        return listViewItem;
//    }
//}
