package com.roboo.like.google;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKRouteAddrResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.roboo.like.google.utils.CardToastUtils;

/** 我的位置 */
public class LocationActivity extends BaseLayoutActivity
{
	/** 在显示定位结果时等待时间 */
	private static final long DELAY_IN_MILLIS_SHOW_LOCATION_RESULT = 500L;
	private static final int LOCATION_BY_GPS_CODE = 61;// BDLocation.TypeGpsLocation
	private static final int LOCATION_BY_NETWORK_CODE = 161;// BDLocation.TypeNetWorkLocation
	private static final double LOCATION_FAILURED_LONGITUDE_LATITUDE = 4.9E-324;
	private static final String[] COLUMNS = { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, };

	private static final double SUZHOU_LONGITUDE = 120.676459;
	private static final double SUZHOU_LATITUDE = 31.300916;
	public LocationClient mLocationClient = null;
	private MapView mMapView;
	/** 百度地图管理器，需要在setContentView之前init */
	private BMapManager mBMapManager = null;
	// /** 定位成功后获取的经度 */
	// private double mLocationLongitude;
	// /** 定位成功后获取的纬度 */
	// private double mLocationLatitude;
	private Button mBtnDetail;
	private boolean mPopupShowingFlag = false;
	private LocationData mLocationData = new LocationData();
	private TextView mPopupTextView;
	private PopupOverlay mPopupOverlay;
	private GeoPoint mGeoPoint;
	private MapController mMapController;
	/** 线路图层 */
	private RouteOverlay mRouteOverlay;
	/** 我的位置图层 */
	private LocationOverlay mLocationOverlay;
	/** 点击位置图层 */
	private LocationOverlay mClickLocationOverlay;
	/** 目的地信息 */
	private MKPoiInfo mMKInfo;
	private MKSearch mMKSearch;
	// 将路线数据保存给全局变量
	private MKRoute mRoute;
	private SearchView mSearchView;
	private SuggestionsAdapter mSuggestAdapter = null;
	private MatrixCursor mCursorData;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (null == mBMapManager)
			{
				return;
			}
			new CardToastUtils(LocationActivity.this).setMessageTextColor(Color.RED).showAndAutoDismiss("定位成功   " + msg.obj);
			mGeoPoint = new GeoPoint((int) (mLocationData.latitude * 1E6), (int) (mLocationData.longitude * 1E6));
			// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
			mMapController.setCenter(mGeoPoint);// 设置地图中心点
			mMapController.animateTo(mGeoPoint);
			mMapController.setZoom(19);
			mLocationOverlay.setData(mLocationData);// 设置我的位置信息
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			mLocationClient.stop();
		};
	};

	/** 跳转到我的位置界面 */
	public static void actionLocation(Activity activity)
	{
		Intent intent = new Intent(activity, LocationActivity.class);
		activity.startActivity(intent);
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		GoogleApplication app = (GoogleApplication) this.getApplication();
		if (app.mBMapManager == null)
		{
			app.mBMapManager = new BMapManager(getApplicationContext());
			app.mBMapManager.init(new GoogleApplication.MyGeneralListener());
		}
		mBMapManager = app.mBMapManager;
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListenerImpl());
		// 默认经纬度为苏州
		mLocationData.latitude = SUZHOU_LATITUDE;
		mLocationData.longitude = SUZHOU_LONGITUDE;
		mGeoPoint = new GeoPoint((int) (mLocationData.latitude * 1E6), (int) (mLocationData.longitude * 1E6));
		super.onCreate(savedInstanceState);
		customActionBar();
		setContentView(R.layout.activity_location);// TODO
		initView();
		initMapView();
		initLocationClient();
		initMKSearch();
		startRequestLocation();
		setListener();
		mMapController.animateTo(mGeoPoint);

	}

	private void setListener()
	{
		mBtnDetail.setOnClickListener(new OnClickListenerImpl());
	}

	private void initMKSearch()
	{
		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapManager, new MKSearchListenerImpl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_location, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) searchItem.getActionView();
		mSearchView.setQueryHint("输入关键字");
		mSearchView.setSubmitButtonEnabled(false);// 不显示搜索提交按钮
		modifySearchView();
		SearchViewListenerImpl searchViewListenerImpl = new SearchViewListenerImpl();
		mSearchView.setOnQueryTextListener(searchViewListenerImpl);
		mSearchView.setOnCloseListener(searchViewListenerImpl);
		mSearchView.setOnSuggestionListener(searchViewListenerImpl);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		String searchKey = "美食";
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_search:// 搜索
			return true;
		case R.id.menu_location:
			Toast.makeText(this, "定位", Toast.LENGTH_SHORT).show();
			startRequestLocation();
			return true;
			// return true;
		case R.id.menu_food:// 美食
		case R.id.menu_hotel:// 酒店
		case R.id.menu_market:// 超市
		case R.id.menu_cinema:// 影院

			searchKey = item.getTitle().toString();
			String city = mPreferences.getString(PREF_LOACTION_CITY, DEFAULT_CITY);
			mMKSearch.poiSearchInCity(city, searchKey);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 请求定位 */
	private void startRequestLocation()
	{
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	private void initLocationClient()
	{
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		// option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mMapView.onResume();
		if (null != mBMapManager)
		{
			mBMapManager.start();
		}
	}

	protected void onPause()
	{
		super.onPause();
		mMapView.onPause();
		if (mBMapManager != null)
		{
			mBMapManager.stop();
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
		mMapView.destroy();
		if (mBMapManager != null)
		{
			mBMapManager.destroy();
			mBMapManager = null;
			GoogleApplication app = (GoogleApplication) this.getApplication();
			app.mBMapManager = null;
		}
	}

	public void initView()
	{
		mBtnDetail = (Button) findViewById(R.id.btn_detail);
		mMapView = (MapView) findViewById(R.id.mv_mapview);
		mMapView.setOnTouchListener(getOnTouchListener());
		mPopupTextView = new TextView(this);
		mPopupTextView.setTextColor(Color.RED);
		mPopupTextView.setPadding(50, 50, 50, 50);
		mPopupTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.f);
		mPopupTextView.setGravity(Gravity.CENTER);
		mPopupOverlay = new PopupOverlay(mMapView, new PopupClickListenerImpl());
	}

	private void initMapView()
	{
		mMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		mMapController = mMapView.getController();
		mMapController.enableClick(true);// 设置可点击
		mMapController.setZoom(19);// 设置地图zoom级别最高级别
		mMapController.enableClick(true);

		mLocationOverlay = new LocationOverlay(mMapView);// 创建定位图层
		mClickLocationOverlay = new LocationOverlay(mMapView);// 创建用户点击位置图层
		mLocationOverlay.enableCompass(); // 打开指南针
		mLocationOverlay.setData(mLocationData);// 设置我的位置信息
		mMapView.getOverlays().add(mLocationOverlay);// 添加定位图层
		mMapView.setTraffic(true);
		mMapView.setBuiltInZoomControls(false);
		mMapView.showScaleControl(true);
		// 修改定位数据后刷新图层生效
		mMapView.refresh();
		mMapView.regMapViewListener(mBMapManager, new MKMapViewListenerImpl());
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("我的位置");
		mActionBar.setLogo(R.drawable.ic_abs_location_up);
	}

	/** 规划线路 */
	public void routeDrive(MKPoiInfo info)
	{
		String city = mPreferences.getString(PREF_LOACTION_CITY, DEFAULT_CITY);
		String address = mPreferences.getString(PREF_LOACTION_ADDRESS, DEFAULT_ADDRESS);
		if (null != mRouteOverlay)
		{
			// 移除上次规划的线路
			mMapView.getOverlays().remove(mRouteOverlay);
		}
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode startNode = new MKPlanNode();
		startNode.pt = mGeoPoint;
		startNode.name = address;
		MKPlanNode endNode = new MKPlanNode();
		endNode.name = info.address;
		endNode.pt = info.pt;
		mMKSearch.drivingSearch(city, startNode, city, endNode);
	}

	/** TODO 修改SearchView属性 */
	private void modifySearchView()
	{
		try
		{
			Field field = SearchView.class.getDeclaredField("mSubmitButton");
			field.setAccessible(true);
			Object object = field.get(mSearchView);
			if (object instanceof ImageView)
			{
				ImageView tmpImageView = (ImageView) object;
				tmpImageView.setImageResource(R.drawable.ic_menu_search);
				field.set(object, tmpImageView);
			}

			field = SearchView.class.getDeclaredField("mSearchButton");
			field.setAccessible(true);
			object = field.get(mSearchView);
			if (object instanceof ImageView)
			{
				ImageView tmpImageView = (ImageView) object;
				tmpImageView.setImageResource(R.drawable.ic_menu_search);
				field.set(object, tmpImageView);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private class BDLocationListenerImpl implements BDLocationListener
	{
		public void onReceiveLocation(BDLocation location)
		{
			if (null != location)
			{
				StringBuffer stringBuffer = new StringBuffer(256);
				String locationTime = location.getTime();// 定位时间
				int statusCode = location.getLocType();// 定位方式[61:GPS 161:NETWORK]
				double longitude = location.getLongitude();// 经度
				double latitude = location.getLatitude();// 纬度
				float radius = location.getRadius();// 精度半径
				String address = location.getAddrStr();// 网络定位时才有数据，地址信息
				String city = location.getCity();
				float speed = location.getSpeed();// GPS定位时才有数据
				int satellite = location.getSatelliteNumber();// GPS定位时才有数据
				if (longitude == LOCATION_FAILURED_LONGITUDE_LATITUDE)
				{
					longitude = SUZHOU_LONGITUDE;
					latitude = SUZHOU_LATITUDE;
				}
				mLocationData.longitude = longitude;
				mLocationData.latitude = latitude;
				mLocationData.satellitesNum = satellite;
				mLocationData.speed = speed;

				stringBuffer.append("\nlocationTime[定位时间] = " + locationTime);
				stringBuffer.append("\nstatusCode[定位方式[61:GPS 161:NETWORK]] = " + statusCode);
				stringBuffer.append("\nlongitude[经度] = " + longitude);
				stringBuffer.append("\nlatitude[纬度] = " + latitude);
				stringBuffer.append("\nradius[精度半径] = " + radius);
				if (location.getLocType() == LOCATION_BY_GPS_CODE)
				{
					stringBuffer.append("\nspeed = " + speed);
					stringBuffer.append("\nsatellite = " + satellite);
				}
				else if (location.getLocType() == LOCATION_BY_NETWORK_CODE)
				{
					stringBuffer.append("\naddress[地址信息] = " + address);
				}
				System.out.println("定位结果   = " + stringBuffer.toString());
				if (!TextUtils.isEmpty(address))
				{
					Message message = mHandler.obtainMessage();
					message.obj = address;
					mHandler.sendMessageDelayed(message, DELAY_IN_MILLIS_SHOW_LOCATION_RESULT);
					mPreferences.edit().putString(PREF_LOACTION_ADDRESS, address).commit();
				}
				if (!TextUtils.isEmpty(city))
				{
					mPreferences.edit().putString(PREF_LOACTION_CITY, city).commit();
				}
			}

		}

		@Override
		public void onReceivePoi(BDLocation location)
		{

		}
	}

	private class PopupClickListenerImpl implements PopupClickListener
	{

		public void onClickedPopup(int arg0)
		{

		}

	}

	private class LocationOverlay extends MyLocationOverlay
	{

		public LocationOverlay(MapView mapView)
		{
			super(mapView);
			setMarker(getResources().getDrawable(R.drawable.ic_geo));
			// 修改图层，需要刷新MapView生效
			mapView.refresh();
		}

		protected boolean dispatchTap()
		{
			// 处理点击事件,弹出泡泡
			if (null != mPopupTextView)
			{
				mPopupTextView.setBackgroundResource(R.drawable.ic_popup);
				mPopupTextView.setText("我的位置");
				mPopupShowingFlag = true;
				mPopupOverlay.showPopup(getBitmapFromView(mPopupTextView), new GeoPoint((int) (mLocationData.latitude * 1e6), (int) (mLocationData.longitude * 1e6)), 8);
			}
			return true;
		}

		public Bitmap getBitmapFromView(View view)
		{
			view.destroyDrawingCache();
			view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			view.setDrawingCacheEnabled(true);
			Bitmap bitmap = view.getDrawingCache(true);
			return bitmap;
		}
	}

	private OnTouchListener getOnTouchListener()
	{
		return new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				// 消隐泡泡
				if (mPopupOverlay != null && mPopupShowingFlag && event.getAction() == MotionEvent.ACTION_UP)
				{
					mPopupOverlay.hidePop();
					mPopupShowingFlag = false;
					return true;
				}
				return false;
			}
		};
	}

	/** TODO 百度搜索 处理回调事件 */
	class MKSearchListenerImpl implements MKSearchListener
	{
		public void onGetAddrResult(MKAddrInfo arg0, int arg1)
		{}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1)
		{

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error)
		{
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR)
			{
				return;
			}
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null)
			{
				Toast.makeText(LocationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
				return;
			}
			// 将路线数据保存给全局变量
			mRoute = res.getPlan(0).getRoute(0);
			getRouteInfo(res);
			mRouteOverlay = new RouteOverlay(LocationActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
			mRouteOverlay.setData(mRoute);
			// 清除其他图层
			// mMapView.getOverlays().clear();
			// 添加定位地点图层
			// mMapView.getOverlays().add(mLocationOverlay);
			// 添加路线图层
			mMapView.getOverlays().add(mRouteOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(mRouteOverlay.getLatSpanE6(), mRouteOverlay.getLonSpanE6());
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 重置路线节点索引，节点浏览时使用
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1)
		{

		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int type, int error)
		{
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null)
			{
				Toast.makeText(LocationActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
				return;
			}
			// 将地图移动到第一个POI中心点
			if (res.getCurrentNumPois() > 0)
			{
				// 将poi结果显示到地图上
				MyPoiOverlay poiOverlay = new MyPoiOverlay(LocationActivity.this, mMapView, mMKSearch);
				poiOverlay.setData(res.getAllPoi());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(mLocationOverlay);
				mMapView.getOverlays().add(poiOverlay);
				mMapView.refresh();
				// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
				for (MKPoiInfo info : res.getAllPoi())
				{
					if (info.pt != null)
					{
						mMapView.getController().animateTo(info.pt);
						break;
					}
				}
			}
			else if (res.getCityListNum() > 0)
			{
				// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
				String strInfo = "在";
				for (int i = 0; i < res.getCityListNum(); i++)
				{
					strInfo += res.getCityListInfo(i).city;
					strInfo += ",";
				}
				strInfo += "找到结果";
				Toast.makeText(LocationActivity.this, strInfo, Toast.LENGTH_LONG).show();
			}

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2)
		{

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res, int arg1)
		{
			if (res == null || res.getAllSuggestions() == null)
			{
				return;
			}
			mCursorData = new MatrixCursor(COLUMNS);
			for (int i = 0; i < res.getAllSuggestions().size(); i++)
			{
				MKSuggestionInfo info = res.getAllSuggestions().get(i);
				if (info.key != null)
				{
					System.out.println("key = " + info.key);
					if (i > 5)
						continue;
					mCursorData.addRow(new String[] { (1 + i) + "", info.key });
				}
			}
			mSuggestAdapter = new SuggestionsAdapter(LocationActivity.this, mCursorData);
			mSearchView.setSuggestionsAdapter(mSuggestAdapter);
			mSuggestAdapter.notifyDataSetChanged();
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1)
		{

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1)
		{

		}

	}

	class MyPoiOverlay extends PoiOverlay
	{
		MKSearch mSearch;
		private LocationActivity mActivity;

		public MyPoiOverlay(LocationActivity activity, MapView mapView, MKSearch search)
		{
			super(activity, mapView);
			mSearch = search;
			mActivity = activity;

		}

		@Override
		protected boolean onTap(int i)
		{
			super.onTap(i);
			mMKInfo = getPoi(i);
			StringBuffer message = new StringBuffer();
			message.append("城市：" + mMKInfo.city);
			message.append("\n名称：" + mMKInfo.name);
			message.append("\n地址：" + mMKInfo.address);
			message.append("\n电话号码：" + mMKInfo.phoneNum);
			final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).setMessage(message).setTitle("位置详情").setPositiveButton("查看线路", getOnClickListener(mMKInfo)).setNegativeButton("取消", null).create();
			alertDialog.show();
			return true;
		}

		private OnClickListener getOnClickListener(final MKPoiInfo info)
		{
			return new OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
				{
					routeDrive(info);
				}
			};
		}
	}

	private class MKMapViewListenerImpl implements MKMapViewListener
	{
		public void onClickMapPoi(MapPoi poi)
		{
			if (null != poi)
			{
				mGeoPoint = new GeoPoint(poi.geoPt.getLatitudeE6(), poi.geoPt.getLongitudeE6());
				LocationData clickLocationData = new LocationData();
				clickLocationData.latitude = poi.geoPt.getLatitudeE6() / 1000000.0;
				clickLocationData.longitude = poi.geoPt.getLongitudeE6() / 1000000.0;
				mClickLocationOverlay.setData(clickLocationData);

				mClickLocationOverlay.setMarker(getResources().getDrawable(R.drawable.ic_nav_turn_start_s));
				mMapView.getOverlays().remove(mClickLocationOverlay);
				mMapView.getOverlays().add(mClickLocationOverlay);
				mMapView.refresh();
				Toast.makeText(LocationActivity.this, poi.strText, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onGetCurrentMap(Bitmap arg0)
		{

		}

		@Override
		public void onMapAnimationFinish()
		{

		}

		@Override
		public void onMapLoadFinish()
		{

		}

		@Override
		public void onMapMoveFinish()
		{

		}

	}

	private class OnClickListenerImpl implements android.view.View.OnClickListener
	{

		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btn_detail:
				if (null != mRoute)
				{
					String startAddress = mPreferences.getString(PREF_LOACTION_ADDRESS, DEFAULT_ADDRESS);
					String endAddress = mMKInfo.address;
					ArrayList<String> routeList = new ArrayList<String>();
					routeList.add(startAddress);
					int steps = mRoute.getNumSteps();
					for (int i = 0; i < steps; i++)
					{
						routeList.add(mRoute.getStep(i).getContent());
						System.out.println(mRoute.getStep(i).getContent());
					}
					routeList.add(endAddress);
					RouteActivity.actionRoute(LocationActivity.this, routeList);
				}
				else
				{

				}
				break;
			default:
				break;
			}
		}
	}

	/** TODO 查看路线信息 */
	private void getRouteInfo(MKDrivingRouteResult res)
	{
		MKRouteAddrResult addressResult = res.getAddrResult();
		int steps = mRoute.getNumSteps();
		for (int i = 0; i < steps; i++)
		{
			System.out.println(mRoute.getStep(i).getContent());
		}
		System.out.println("taxi价格  = " + res.getTaxiPrice());
		System.out.println("目的地 = " + res.getEnd().name);
		System.out.println("起始地 = " + res.getStart().name);
		if (null != addressResult)
		{
			ArrayList<ArrayList<MKPoiInfo>> poiList = addressResult.mWpPoiList;
			for (ArrayList<MKPoiInfo> poi1 : poiList)
			{
				for (MKPoiInfo poi2 : poi1)
				{
					System.out.println("poi2.name = " + poi2.name);
				}
			}
		}
	}

	/** TODO SearchView 点击事件 */
	private class SearchViewListenerImpl implements OnQueryTextListener, OnCloseListener, OnSuggestionListener
	{
		public boolean onQueryTextSubmit(String query)
		{
			return true;
		}

		public boolean onQueryTextChange(String newText)
		{
			String city = mPreferences.getString(PREF_LOACTION_CITY, DEFAULT_CITY);
			mMKSearch.suggestionSearch(newText, city);
			mCursorData = new MatrixCursor(COLUMNS);
			if (null != mSuggestAdapter)
			{
				mSuggestAdapter.changeCursor(mCursorData);
				mSuggestAdapter.notifyDataSetChanged();
			}
			return true;
		}

		@Override
		public boolean onSuggestionSelect(int position)   
		{
			return false;
		}

		@Override
		public boolean onSuggestionClick(int position)
		{
			MatrixCursor cursor = (MatrixCursor) mSuggestAdapter.getItem(position);
			String searchKey = cursor.getString(1);
			String city = mPreferences.getString(PREF_LOACTION_CITY, DEFAULT_CITY);
			mMKSearch.poiSearchInCity(city, searchKey);
			mSearchView.setQuery(searchKey, false);
			return false;
		}

		@Override
		public boolean onClose()
		{
			return false;
		}

	}

	private class SuggestionsAdapter extends CursorAdapter
	{
		public SuggestionsAdapter(Context context, Cursor c)
		{
			super(context, c, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			TextView tv = (TextView) view;
			final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
			tv.setText(cursor.getString(textIndex));
		}
	}

}
// ====================================================================================
// ====================================================================================
// 百度定位返回状态码信息
// ====================================================================================
// ====================================================================================
// 61 ： GPS定位结果
// 62 ： 扫描整合定位依据失败。此时定位结果无效。
// 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
// 65 ： 定位缓存的结果。
// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
// 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
// 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
// 161： 表示网络定位结果
// 162~167： 服务端定位失败
// 502：KEY参数错误
// 505：KEY不存在或者非法
// 601：KEY服务被开发者自己禁用
// 602: KEY Mcode不匹配,意思就是您的ak配置过程中安全码设置有问题，请确保： sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名
// 501-700：KEY验证失败
// ====================================================================================