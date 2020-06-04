package com.app.test.expandablelist;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.expandablelist.ExpandableListViewActivity.CourseDto;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<CourseDto> list;

	public ExpandableAdapter(Context context, List<CourseDto> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).nameList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).nameList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder h = null;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_expandable_group,
					null);
			h.groupText = (TextView) convertView.findViewById(R.id.groupText);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		h.groupText.setText(list.get(groupPosition).name);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder h = null;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_expandable_child,
					null);
			h.childText = (TextView) convertView.findViewById(R.id.childText);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		h.childText
				.setText(list.get(groupPosition).nameList.get(childPosition));
		return convertView;
	}

	// 子类是否点击了
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class ViewHolder {
		private TextView groupText;
		private TextView childText;
	}

}
