package com.cwp.chart.widget;

import java.util.Date;

import com.cwp.fragment.FragmentPage3;
import com.cwp.cmoneycharge.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @description�Զ���������ʢ��listview,gridview,scrollview
 * @author Ԭ����
 * @date 2014-7-21 ����5:56:00
 */
public class PullToRefreshView extends LinearLayout {
	private static final String TAG = "PullToRefreshView";
	// ˢ��״��
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// ����״��
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	private boolean enablePullTorefresh = true;
	private boolean enablePullLoadMoreDataStatus = true;
	private int mLastMotionY;
	private boolean mLock;
	/**
	 * ͷ��view
	 */
	private View mHeaderView;
	/**
	 * �ײ�view
	 */
	private View mFooterView;
	private AdapterView<?> mAdapterView;
	private ScrollView mScrollView;
	/**
	 * ͷ��view�ĸ�
	 */
	private int mHeaderViewHeight;
	/**
	 * �ײ�view�ĸ�
	 */
	private int mFooterViewHeight;
	/**
	 * ͷ����ͷ
	 */
	private ImageView mHeaderImageView;
	/**
	 * �ײ���ͷ
	 */
	private ImageView mFooterImageView;
	/**
	 * ͷ������
	 */
	private TextView mHeaderTextView;
	/**
	 * �ײ�����
	 */
	private TextView mFooterTextView;
	/**
	 * ͷ��ˢ��ʱ��
	 */
	private TextView mHeaderUpdateTextView;
	/**
	 * �ײ�ˢ��ʱ��
	 */
	private TextView mFooterUpdateTextView;
	/**
	 * ͷ�����Ȱ�
	 */
	private ProgressBar mHeaderProgressBar;
	/**
	 * �ײ����Ȱ�
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * layout inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * ͷ��view�ĵ�ǰ״��
	 */
	private int mHeaderState;
	/**
	 * �ײ�view�ĵ�ǰ״��
	 */
	private int mFooterState;
	/**
	 * ����״��
	 */
	private int mPullState;
	/**
	 * ��Ϊ���µļ��B�ı��ͷ����
	 */
	private RotateAnimation mFlipAnimation;
	/**
	 * ��Ϊ����ļ��B��ת
	 */
	private RotateAnimation mReverseFlipAnimation;
	/**
	 * �ײ�view��ˢ�¼���
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * ͷ��view��ˢ�¼���
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	/**
	 * �c?����ʱ��
	 */
	private String mLastUpdateTime;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	private void init() {
		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());
		// header view �ڴ����,��֤�ǵ�د?��ӵ�linearlayout�����϶�
		addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);

		mHeaderImageView = (ImageView) mHeaderView
				.findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_text);
		mHeaderUpdateTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_updated_at);
		// mHeaderProgressBar = (ProgressBar) mHeaderView
		// .findViewById(R.id.pull_to_refresh_progress);
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		// ����topMargin�ĐN?����header View�߶�,�����������ږc?��
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);

	}

	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_load_text);
		// mFooterProgressBar = (ProgressBar) mFooterView
		// .findViewById(R.id.pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//������getHeight()==0,����onInterceptTouchEvent()������getHeight()�Ѿ��АN?���ٕp;
		// getHeight()��?ʱ�N?�����Լ�?�о�һد
		// ���������Բ���?��ֱ������ֻҪAdapterView�ĸ߶���MATCH_PARENT,��ôfooter view�ͻᱻ��ӵ��c?,����޽
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view �ڴ���ӱ�֤��ӵ�linearlayout�еĖc?
		addFooterView();
		initContentAdapterView();
	}

	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			Log.e(TAG, "��view�ĸ���С��----------------");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			Log.e(TAG, "viewΪ��-------------------");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��������down�¼�,��¼y����
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 ����������< 0����������
			int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/*
	 * �����onInterceptTouchEvent()������û��������onInterceptTouchEvent()����دreturn
	 * false)����PullToRefreshView ����View�����q����������ķ��������q����PullToRefreshView�Լ������q
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent�Ѿ���¼
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {
				// PullToRefreshViewִ������
				// Log.i(TAG, " pull down!parent view move!");
				headerPrepareToRefresh(deltaY);
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (mPullState == PULL_UP_STATE) {
				// PullToRefreshViewִ������
				// Log.i(TAG, "pull up!parent view move!");
				footerPrepareToRefresh(deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE) {
				if (topMargin >= 0) {
					// ��?ˢ��
					headerRefreshing();
				} else {
					// ��û��ִ��ˢ�£���������
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE) {
				if (Math.abs(topMargin) >= mHeaderViewHeight
						+ mFooterViewHeight) {
					// ��?ִ��footer ˢ��
					footerRefreshing();
				} else {
					// ��û��ִ��ˢ�£���������
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * �Ƿ�Ӧ�õ��˸�View,��PullToRefreshView����
	 * 
	 * @param deltaY
	 *            , deltaY > 0 ����������< 0����������
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// ����ListView��GridView
		if (mAdapterView != null) {
			// ��view(ListView or GridView)���������
			if (deltaY > 0) {
				// �ж��Ƿ��������ˢ�²���
				if (!enablePullTorefresh) {
					return false;
				}
				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// ���mAdapterView��û�����ݲ�����
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 11) {// ����֮ǰ�m�����ж�,�����ڲ��ƻ�û�ҵ�ԭ��
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < 0) {
				// �ж��Ƿ�����������ظ������
				if (!enablePullLoadMoreDataStatus) {
					return false;
				}
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null) {
					// ���mAdapterView��û�����ݲ�����
					return false;
				}
				// �c?د?��view��BottomС�ڸ�View�ĸ߶�˵��mAdapterView������û��������view,
				// ���ڸ�View�ĸ߶�˵��mAdapterView�Ѿ����������
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
								.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// ����ScrollView
		if (mScrollView != null) {
			// ��scroll view���������
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& child.getMeasuredHeight() <= getHeight()
							+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header ׼��ˢ��,��ָ�ƶ�����,��û������
	 * 
	 * @param deltaY
	 *            ,��ָ�����ľ�d
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);

		// ��header view��topMargin>=0ʱ��˵���Ѿ���ȫ��ʾ�������޸�header view ����ʾ״��
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView.setText("�ſ��л���"
					+ Integer.toString(FragmentPage3.getyear() + 1) + "����ˮ");
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// �϶�ʱû������
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			// mHeaderImageView.
			mHeaderTextView.setText("�����л���"
					+ Integer.toString(FragmentPage3.getyear() + 1) + "����ˮ");
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * footer ׼��ˢ��,��ָ�ƶ�����,��û�������ƶ�footer view�߶�ͬ�����ƶ�header view
	 * �߶���һ��������ͨ���޸�header view��topmargin����?����
	 * 
	 * @param deltaY
	 *            ,��ָ�����ľ�d
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// ���header view topMargin �ľ��Լ�?�ڻ����header + footer �ĸߎ�
		// ˵��footer view ��ȫ��ʾ�����ˣ��޸�footer view ����ʾ״��
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView.setText("�ſ��л���"
					+ Integer.toString(FragmentPage3.getyear() - 1) + "����ˮ");
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText("�����л���"
					+ Integer.toString(FragmentPage3.getyear() - 1) + "����ˮ");
			mFooterState = PULL_TO_REFRESH;
		}
	}

	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// �����������د?����,��Ϊ��ǰ������Ȼ���ͷ���ֱָ������,�������ˢ�¸�������,��л����yufengzungzhe��ָ��
		// ��ʾ�������������һ�ξ�dȻ��ֱ������
		if (deltaY > 0 && mPullState == PULL_UP_STATE
				&& Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// ͬ���]��������د?����,������ָ���������ʱد7��bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE
				&& Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	public void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		// mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		// mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
				.setText(R.string.pull_to_refresh_footer_refreshing_label);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * ����header view ��topMargin����
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view ��ɸ��º�ָ���ʼ״��
	 * 
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
		// mHeaderProgressBar.setVisibility(View.GONE);
		mHeaderState = PULL_TO_REFRESH;
		setLastUpdated("�c?����:" + new Date().toLocaleString());
	}

	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view ��ɸ��º�ָ���ʼ״��
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		// mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * footer view ��ɸ��º�ָ���ʼ״��
	 */
	public void onFooterRefreshComplete(int size) {
		if (size > 0) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
		}
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		// mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	// /**
	// * lock
	// */
	// private void lock() {
	// mLock = true;
	// }
	//
	// /**
	// * unlock
	// *
	// private void unlock() {
	// mLock = false;
	// }

	/**
	 * @description
	 * @param headerRefreshListener
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}

	public boolean isEnablePullTorefresh() {
		return enablePullTorefresh;
	}

	public void setEnablePullTorefresh(boolean enablePullTorefresh) {
		this.enablePullTorefresh = enablePullTorefresh;
	}

	public boolean isEnablePullLoadMoreDataStatus() {
		return enablePullLoadMoreDataStatus;
	}

	public void setEnablePullLoadMoreDataStatus(
			boolean enablePullLoadMoreDataStatus) {
		this.enablePullLoadMoreDataStatus = enablePullLoadMoreDataStatus;
	}
}
