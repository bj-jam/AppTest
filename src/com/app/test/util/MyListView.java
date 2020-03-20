package com.app.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.app.test.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyListView extends ListView implements OnScrollListener, View.OnClickListener {
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	private final static int RATIO = 3;

	private LinearLayout headView;
	private LinearLayout footView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private LinearLayout myHeadView;
	private FrameLayout progressFrame;
	private LinearLayout toast;

	private ProgressBar footPb;
	private TextView footTv;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private boolean isRecored;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private OnFootClickListener listener;
	private boolean isRefreshable;
	/** 是否改写onMeasure方法 */
	private boolean isOnMeasure = false;
	/** 是否正在加载 */
	private boolean isOnLoad = false;
	// 是否能够加载更多
	private boolean isLoadMore = false;

	/** 提示文字 **/
	public String loosenRefresh = "松开刷新";
	public String dropRefresh = "下拉刷新";
	public String refreshing = "正在刷新…";
	public String nearestUpdate = "最近更新：";

	private SimpleDateFormat format;
	private boolean isMyFragment = false;

	/** 滑动控件滑动状态监听 */
	private OnScrollStateLisenter onScrollStateLisenter;

	public MyListView(Context context) {
		super(context);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		if (isInEditMode()) {
			return;
		}

		format = new SimpleDateFormat("kk:mm", Locale.CHINA);

		setCacheColorHint(context.getResources().getColor(R.color.transparent));

		headView = (LinearLayout) View.inflate(context, R.layout.my_list_view_head, null);
		footView = (LinearLayout) View.inflate(context, R.layout.my_list_view_foot, null);

		addHeaderView(headView);
		addFooterView(footView);

		footView.setVisibility(View.GONE);// 底部默认是隐藏的
		myHeadView = (LinearLayout) headView.findViewById(R.id.myHeadView);
		progressFrame = (FrameLayout) headView.findViewById(R.id.progressFrame);
		toast = (LinearLayout) headView.findViewById(R.id.toast);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();

		// foot
		footPb = (ProgressBar) footView.findViewById(R.id.myListViewFootPb);
		footTv = (TextView) footView.findViewById(R.id.myListViewFootTv);
		footView.setOnClickListener(this);

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		if (onScrollStateLisenter != null) {
			onScrollStateLisenter.onScroll(arg0, firstVisiableItem, arg2, arg3);
		}
		firstItemIndex = firstVisiableItem;
		// if (needCallBack) {
		// svcb.scrollingCallBack(arg0, firstVisiableItem, arg2, arg3);
		// }
	}

	// private ListViewCallBack svcb;
	// private boolean needCallBack = false;
	//
	// public void setScrollingCallBackListenr(ListViewCallBack svcb,
	// boolean needCallBack) {
	// // TODO Auto-generated method stub
	// this.needCallBack = needCallBack;
	// this.svcb = svcb;
	// }

	/**
	 * 自动加载
	 */
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		switch (arg1) {
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动状态
			if (onScrollStateLisenter != null) {
				onScrollStateLisenter.onScrollState(arg1);
			}
			break;
		// 当不滚动时
		case OnScrollListener.SCROLL_STATE_IDLE:
			if (onScrollStateLisenter != null) {
				onScrollStateLisenter.onScrollState(arg1);
			}
			// 判断滚动到底部
			if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)) {
				onClick(footView);
			}
			break;
		}
	}

	/**
	 * 手动调用调用刷新，一般用于刚进入页面自动刷新
	 */
	public void doRefresh() {
		state = REFRESHING;
		lastUpdatedTextView.setText(nearestUpdate + format.format(new Date()));
		changeHeaderViewByState();
		onRefresh();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight)// 更改
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						} else {
						}
					}

					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headContentHeight) {// 更改
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
					}
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			if (!isMyFragment) {
				arrowImageView.setVisibility(View.VISIBLE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(animation);
				tipsTextview.setText(loosenRefresh);
			}
			progressBar.setVisibility(View.GONE);
			break;
		case PULL_To_REFRESH:
			if (!isMyFragment) {
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.VISIBLE);
				if (isBack) {
					isBack = false;
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(reverseAnimation);
					tipsTextview.setText(dropRefresh);
				} else {
					tipsTextview.setText(dropRefresh);
				}
			}
			progressBar.setVisibility(View.GONE);
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);// 更改
			if (!isMyFragment) {
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.GONE);
				tipsTextview.setText(refreshing);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
			}
			progressBar.setVisibility(View.VISIBLE);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			if (!isMyFragment) {
				arrowImageView.clearAnimation();
				arrowImageView.setImageResource(R.drawable.xlistview_arrow);
				tipsTextview.setText(dropRefresh);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
			}
			progressBar.setVisibility(View.GONE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	/**
	 * 是否可刷新
	 * 
	 * @param isRefreshable
	 */
	public void setRefreshable(boolean isRefreshable) {
		this.isRefreshable = isRefreshable;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	/** 表头刷新中 */
	public void onRefreshing() {
		state = REFRESHING;
		lastUpdatedTextView.setText(nearestUpdate + format.format(new Date()));
		changeHeaderViewByState();
	}

	/** 表头复位 */
	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText(nearestUpdate + format.format(new Date()));
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText(nearestUpdate + format.format(new Date()));
		super.setAdapter(adapter);
	}

	/** 表尾事件 */
	public void setOnFootClickListener(OnFootClickListener listener) {
		this.listener = listener;
	}

	public interface OnFootClickListener {
		public void onClick();
	}

	/** 表尾正在加载 */
	public void load() {
		isOnLoad = true;
		setFootVisibility(View.VISIBLE);
		footPb.setVisibility(View.VISIBLE);
		footTv.setText("正在加载...");
	}

	/** 表尾加载更多 */
	public void onLoadComplete() {
		isOnLoad = false;
		isLoadMore = true;// 允许加载更多
		footPb.setVisibility(View.GONE);
		footTv.setText("点击加载更多");
	}

	/** 设置表尾隐藏与显示 */
	public void setFootVisibility(int visibility) {
		footView.setVisibility(visibility);
	}

	/** 去掉表尾 */
	public void moveFootView() {
		if (footView != null) {
			removeFooterView(footView);
		}
	}

	/** 加载到最后一条 */
	public void onLoadFinal() {
		isOnLoad = false;
		isLoadMore = false;// 禁用加载更多
		footPb.setVisibility(View.GONE);
		footTv.setText("已经到底啦！");
	}

	/** 能否点击 */
	public void setFootClick(boolean isclick) {
		footView.setEnabled(isclick);
	}

	/** 设置表尾背景颜色 */
	public void setFootBackground(int color) {
		footView.setBackgroundColor(color);
	}

	public void setFootTextColor(int color) {
		footTv.setTextColor(color);
	}

	/** 表尾点击事件 */
	@Override
	public void onClick(View v) {
		if (!isLoadMore) {
			return;
		}
		load();
		if (listener != null) {
			listener.onClick();
		}
	}

	public boolean getIsOnLoad() {
		return isOnLoad;
	}

	/** 设置是否要重写OnMeasure方法 传true为改写 以用于ScrollView嵌套ListView */
	public void setEnableOnMeasure(boolean isOnMeasure) {
		this.isOnMeasure = isOnMeasure;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isOnMeasure) {
			// MeasureSpec.makeMeasureSpec(int size,int
			// mode),size表示父布局提供给你的大小参数，mode表示规格，有EXACTLY、AT_MOST、UNSPECIFIED；
			// Integer.MAX_VALUE >> 2表示父布局给的参考的大小无限大（listview无边界）
			//MeasureSpec.AT_MOST：表示根据布局的大小来确定listView最终的的高度，也就是有多少内容就显示多少内容
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public interface OnScrollStateLisenter {
		/**
		 * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 滑动中<br>
		 * OnScrollListener.SCROLL_STATE_IDLE 停止滑动
		 * 
		 * @param scrollState
		 */
		public void onScrollState(int scrollState);

		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
	}

	public void setOnScrollStateLisenter(OnScrollStateLisenter onScrollStateLisenter) {
		this.onScrollStateLisenter = onScrollStateLisenter;
	}

	public void changeHeadView() {

		isMyFragment = true;
		myHeadView.setBackgroundColor(Color.parseColor("#00c896"));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		progressFrame.setLayoutParams(layoutParams);
		progressBar.setVisibility(View.GONE);
		arrowImageView.setVisibility(View.GONE);
		toast.setVisibility(View.GONE);
	}
}
