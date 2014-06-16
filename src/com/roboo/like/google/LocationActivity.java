package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.roboo.like.google.utils.CardToastUtils;

/** 我的位置 */
public class LocationActivity extends BaseActivity
{
	/** 在显示定位结果时等待时间 */
	private static final long DELAY_IN_MILLIS_SHOW_LOCATION_RESULT = 2000L;
	private static final int LOCATION_BY_GPS_CODE = 61;// BDLocation.TypeGpsLocation
	private static final int LOCATION_BY_NETWORK_CODE = 161;// BDLocation.TypeNetWorkLocation
	private static final double LOCATION_FAILURED_LONGITUDE_LATITUDE = 4.9E-324;
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
	private boolean mFlag = true;
	private boolean mPopupShowingFlag = false;
	private LocationData mLocationData = new LocationData();
	private LocationOverlay mLocationOverlay;
	private TextView mPopupTextView;
	private PopupOverlay mPopupOverlay;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (mFlag)
			{
				mFlag = !mFlag;
				new CardToastUtils(LocationActivity.this).setMessageTextColor(Color.RED).showAndAutoDismiss("定位成功");
			}
			GeoPoint point = new GeoPoint((int) (mLocationData.latitude * 1E6), (int) (mLocationData.longitude * 1E6));
			// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
			mMapView.getController().setCenter(point);// 设置地图中心点
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
		mBMapManager = new BMapManager(getApplicationContext());
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListenerImpl());
		mBMapManager.init(new MKGeneralListenerImpl());

		super.onCreate(savedInstanceState);
		customActionBar();
		setContentView(R.layout.activity_location);// TODO
		initView();
		initMapView();
		initLocationClient();
		startRequestLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_location:
			Toast.makeText(this, "定位", Toast.LENGTH_SHORT).show();
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
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
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

	@Override
	protected void onPause()
	{
		super.onPause();
		mMapView.onPause();
		if (mBMapManager != null)
		{
			mBMapManager.stop();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMapView.destroy();
		if (mBMapManager != null)
		{
			mBMapManager.destroy();
			mBMapManager = null;
		}
	}

	public void initView()
	{
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
		MapController mMapController = mMapView.getController();
		mMapController.enableClick(true);// 设置可点击
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		// GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 *
		// 1E6));
		// // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		// mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(19);// 设置地图zoom级别
		mLocationOverlay = new LocationOverlay(mMapView);// 创建定位图层
		mLocationOverlay.enableCompass(); // 打开指南针
		mLocationOverlay.setData(mLocationData);// 设置我的位置信息
		mMapView.getOverlays().add(mLocationOverlay);// 添加定位图层
		// 修改定位数据后刷新图层生效
		mMapView.refresh();
	}

	private void customActionBar()
	{
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("我的位置");
		mActionBar.setLogo(R.drawable.ic_abs_location_up);
	}

	private class MKGeneralListenerImpl implements MKGeneralListener
	{
		public void onGetNetworkState(int arg0)
		{

		}

		public void onGetPermissionState(int arg0)
		{

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
				// 定位方式[61:GPS 161:NETWORK]
				int statusCode = location.getLocType();
				double longitude = location.getLongitude();// 经度
				double latitude = location.getLatitude();// 纬度
				float radius = location.getRadius();// 精度半径
				String address = location.getAddrStr();// 网络定位时才有数据，地址信息
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
				mHandler.sendEmptyMessageDelayed(0, DELAY_IN_MILLIS_SHOW_LOCATION_RESULT);
				if (!TextUtils.isEmpty(address))
				{
					Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
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