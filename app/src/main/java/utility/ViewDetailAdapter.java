package utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.navi.R;
import vo.DetailListVO;

import java.util.ArrayList;

/**
 * Created by 95016056 on 2017-05-30.
 */

public class ViewDetailAdapter extends BaseAdapter {
    private ArrayList<DetailListVO> list = new ArrayList<DetailListVO>();

    public ViewDetailAdapter(){

    }

    public void removeAll(){
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DetailListVO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detail_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView seq = (TextView) convertView.findViewById(R.id.seq) ;
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.MapAddress);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        DetailListVO listViewItem = list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        seq.setText(listViewItem.getSeq());
        name.setText(listViewItem.getName());
        address.setText(listViewItem.getDesc());

        return convertView;
    }

    public void addItem(int seq, String name, String road) {
        DetailListVO item = new DetailListVO();

        item.setSeq(seq);
        item.setName(name);
        item.setDesc(road);

        list.add(item);
    }
}


