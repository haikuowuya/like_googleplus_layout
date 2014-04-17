package com.roboo.like.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/** 我的位置 */
public class LocationActivity extends BaseActivity
{
	public LocationClient mLocationClient = null;
	private MapView mMapView;
	/** 百度地图管理器，需要在setContentView之前init */
	private BMapManager mBMapManager = null;

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
		setContentView(R.layout.activity_location);
		initView();
		initMapView();
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
		super.onDestroy();
	}

	public void initView()
	{
		mMapView = (MapView) findViewById(R.id.mv_mapview);
	}

	private void initMapView()
	{
		mMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(12);// 设置地图zoom级别
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
			if (location == null)
			{
				return;
			}
			StringBuffer stringBuffer = new StringBuffer(256);
			stringBuffer.append("time : ");
			stringBuffer.append(location.getTime());
			stringBuffer.append("\nerror code : ");
			stringBuffer.append(location.getLocType());
			stringBuffer.append("\nlatitude : ");
			stringBuffer.append(location.getLatitude());
			stringBuffer.append("\nlontitude : ");
			stringBuffer.append(location.getLongitude());
			stringBuffer.append("\nradius : ");
			stringBuffer.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation)
			{
				stringBuffer.append("\nspeed : ");
				stringBuffer.append(location.getSpeed());
				stringBuffer.append("\nsatellite : ");
				stringBuffer.append(location.getSatelliteNumber());
			}
			else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				stringBuffer.append("\naddr : ");
				stringBuffer.append(location.getAddrStr());
			}
			System.out.println("定位结果   = " + stringBuffer.toString());
		}

		@Override
		public void onReceivePoi(BDLocation location)
		{

		}

	}
}
