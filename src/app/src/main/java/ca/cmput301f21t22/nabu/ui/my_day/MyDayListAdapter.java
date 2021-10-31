package ca.cmput301f21t22.nabu.ui.my_day;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examplecitylist.R;

import java.util.List;

public class MyDayListAdapter extends BaseAdapter implements View.OnClickListener {

    //上下文
    private Context context;
    //数据项
    private List<String> data;
    public MyDayListAdapter(List<String> data){
        this.data = data;
    }
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(context == null)
            context = viewGroup.getContext();
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mTv = (TextView)view.findViewById(R.id.mTv);
            viewHolder.CheckBox1 = (Button)view.findViewById(R.id.checkBox1);
            viewHolder.CheckBox2 = (Button)view.findViewById(R.id.checkBox2);
            viewHolder.CheckBox3 = (Button)view.findViewById(R.id.checkBox3);
            viewHolder.CheckBox4 = (Button)view.findViewById(R.id.checkBox4);
            viewHolder.CheckBox5 = (Button)view.findViewById(R.id.checkBox5);
            viewHolder.CheckBox6 = (Button)view.findViewById(R.id.checkBo6);
            viewHolder.CheckBox7 = (Button)view.findViewById(R.id.checkBox7);
            view.setTag(viewHolder);
        }
        //获取viewHolder实例
        viewHolder = (ViewHolder)view.getTag();
        //设置数据
        viewHolder.mTv.setText(data.get(i));
        //设置监听事件
        viewHolder.mTv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mTv:
                Log.d("tag", "Tv_onClick: " + "view = " + view);
                Toast.makeText(context,"text",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    static class ViewHolder{
        TextView mTv;
        Button CheckBox1;
        Button CheckBox2;
        Button CheckBox3;
        Button CheckBox4;
        Button CheckBox5;
        Button CheckBox6;
        Button CheckBox7;

    }

}

