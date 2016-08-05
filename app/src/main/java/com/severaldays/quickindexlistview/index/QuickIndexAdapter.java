package com.severaldays.quickindexlistview.index;

import java.util.HashMap;
import java.util.List;

import com.severaldays.quickindexlistview.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *
 * @author LingJian•He
 * created at 16/8/5
 *
 */
public class QuickIndexAdapter extends BaseAdapter implements Indexer {

    private final LayoutInflater layoutInflater;
    private List<QuickIndexItem> quickIndexItems;
    private HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

    public QuickIndexAdapter(Context context, List<QuickIndexItem> cities) {
        quickIndexItems = cities;
        layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < quickIndexItems.size(); i++) {
            QuickIndexItem quickIndexItem = quickIndexItems.get(i);
            String quickIndexItemId = quickIndexItem.getIndex();
            if (quickIndexItemId == null || "".equals(quickIndexItemId)) continue;
            String section = quickIndexItemId.toUpperCase().substring(0, 1);
            if (!indexMap.containsKey(section)) {
                indexMap.put(section, i);
            }
        }
    }

    public int getCount() {
        return quickIndexItems.size();
    }

    public Object getItem(int position) {
        return quickIndexItems.get(position);
    }

    public long getItemId(int position) {
        return quickIndexItems.get(position).getIndex().hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.quick_index_item, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_index_name);
            holder.tvSection = (TextView) convertView.findViewById(R.id.tv_index_section);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuickIndexItem quickIndexItem = quickIndexItems.get(position);
        holder.tvName.setText(quickIndexItem.getName());

        String id = quickIndexItem.getIndex();
        char idFirstChar = id.toUpperCase().charAt(0);
        if (position == 0) {
            setIndex(holder.tvSection, String.valueOf(idFirstChar));
        } else {
            String preLabel = quickIndexItems.get(position - 1).getIndex();
            char preFirstChar = preLabel.toUpperCase().charAt(0);
            if (idFirstChar != preFirstChar) {
                setIndex(holder.tvSection, String.valueOf(idFirstChar));
            } else {
                holder.tvSection.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private void setIndex(TextView section, String str) {
        section.setVisibility(View.VISIBLE);
        if ("#".equals(str)) section.setText("当前城市");
        else section.setText(str);
    }

    @Override
    public int getStartPositionOfSection(String section) {
        if (indexMap.containsKey(section))
            return indexMap.get(section);
        return -1;
    }


    class ViewHolder {
        TextView tvName;
        TextView tvSection;
    }
}
