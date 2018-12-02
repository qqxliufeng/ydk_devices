package cn.eas.national.deviceapisample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Czl on 2017/8/21.
 */

public class DeviceModuleAdapter extends ArrayAdapter {
    private Context context;
    private int resourceId;
    private List<DeviceModule> list;

    public DeviceModuleAdapter(Context context, int resourceId, List<DeviceModule> list) {
        super(context, resourceId, list);
        this.context = context;
        this.resourceId = resourceId;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            TextView tvName = (TextView) convertView.findViewById(cn.eas.national.deviceapisample.R.id.ItemText);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvName = tvName;
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        TextView tvName = viewHolder.tvName;
        tvName.setText(list.get(position).name);
        return convertView;
    }
}

class ViewHolder {
    public TextView tvName;
}