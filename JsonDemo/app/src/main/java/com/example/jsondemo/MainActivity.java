package com.example.jsondemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private TextView tv_address;

	// �ж��Ƿ�����
	private boolean hasArea = false;

	// ʡ��
	private String[] mProvinceDatas;
	// ����
	private String[] mCitiesDatas;
	// ����
	private String[] mAreaDatas;

	// �б�ѡ���ʡ����
	private String selectedPro = "";
	private String selectedCity = "";
	private String selectedArea = "";

	private Spinner mProvinceSpinner;
	private Spinner mCitySpinner;
	private Spinner mAreaSpinner;

	private ArrayAdapter<String> mProvinceAdapter;
	private ArrayAdapter<String> mCityAdapter;
	private ArrayAdapter<String> mAreaAdapter;

	// �洢ʡ��Ӧ��������
	private Map<String, String[]> mCitiesDataMap = new HashMap<String, String[]>();
	// �洢�ж�Ӧ��������
	private Map<String, String[]> mAreaDataMap = new HashMap<String, String[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BeginJsonCitisData(InitData());

		tv_address = (TextView) findViewById(R.id.tv_address);

		mProvinceSpinner = (Spinner) findViewById(R.id.spinner_pro);
		mCitySpinner = (Spinner) findViewById(R.id.spinner_city);
		mAreaSpinner = (Spinner) findViewById(R.id.spinner_area);

		mProvinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mProvinceDatas);
		mProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mProvinceSpinner.setAdapter(mProvinceAdapter);

		// ʡ��ѡ��
		mProvinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedPro = "";
				selectedPro = (String) parent.getSelectedItem();
				// ����ʡ�ݸ��³���������Ϣ
				updateCity(selectedPro);
				Log.d(TAG, "mProvinceSpinner has excuted !" + "selectedPro is " + selectedPro);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// ��ѡ��
		mCitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedCity = "";
				selectedCity = (String) parent.getSelectedItem();
				updateArea(selectedCity);
				Log.d(TAG, "mCitySpinner has excuted !" + "selectedCity is " + selectedCity);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// ��ѡ��
		mAreaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedArea = "";
				selectedArea = (String) parent.getSelectedItem();
				tv_address.setText("��ѡ��: " + selectedPro + selectedCity + selectedArea);
				Log.d(TAG, "mAreaSpinner has excuted !" + "selectedArea is " + selectedArea);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * ������ ѡ���Ӧ����
	 * 
	 * @Title: updateArea
	 * @param @param
	 *            city
	 * @return void
	 * @throws @date
	 *             [2015��8��21�� ����3:52:17]
	 */
	private void updateArea(String city) {

		String[] areas = mAreaDataMap.get(city);

		// ������
		if (areas != null) {
			// ������
			mAreaSpinner.setVisibility(View.VISIBLE);
			mAreaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas);
			mAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mAreaSpinner.setAdapter(mAreaAdapter);
			mAreaAdapter.notifyDataSetChanged();
			mAreaSpinner.setSelection(0);
		} else {
			tv_address.setText("��ѡ��: " + selectedPro + selectedCity);
			mAreaSpinner.setVisibility(View.GONE);
		}

	}

	/**
	 * ����ʡ�ݸ��³�������
	 * 
	 * @Title: updateCityAndAreaData
	 * @param @param
	 *            pro ʡ��
	 * @return void
	 * @throws @date
	 *             [2015��8��21�� ����3:20:15]
	 */
	private void updateCity(String pro) {

		String[] cities = mCitiesDataMap.get(pro);
		for (int i = 0; i < cities.length; i++) {
			// ������
			mCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
			mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mCitySpinner.setAdapter(mCityAdapter);
			mCityAdapter.notifyDataSetChanged();
			mCitySpinner.setSelection(0);
		}

	}

	/**
	 * ��ʼ������������
	 * 
	 * @Title: BeginJsonCitisData
	 * @param
	 * @return void
	 * @throws @date
	 *             [2015��8��21�� ����10:02:23]
	 */
	private void BeginJsonCitisData(String cityJson) {
		if (!TextUtils.isEmpty(cityJson)) {
			try {
				JSONObject object = new JSONObject(cityJson);
				JSONArray array = object.getJSONArray("citylist");

				// ��ȡʡ�ݵ�����
				mProvinceDatas = new String[array.length()];
				String mProvinceStr = null;
				// ѭ������
				for (int i = 0; i < array.length(); i++) {

					// ѭ������ʡ�ݣ�����ʡ������mProvinceDatas[]��
					JSONObject mProvinceObject = array.getJSONObject(i);
					if (mProvinceObject.has("p")) {
						mProvinceStr = mProvinceObject.getString("p");
						mProvinceDatas[i] = mProvinceStr;
					} else {
						mProvinceDatas[i] = "unknown province";
					}

					JSONArray cityArray;
					String mCityStr = null;
					try {
						// ѭ������ʡ��Ӧ�µĳ���
						cityArray = mProvinceObject.getJSONArray("c");
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					mCitiesDatas = new String[cityArray.length()];
					for (int j = 0; j < cityArray.length(); j++) {
						// ѭ���������У��������б�����mCitiesDatas[]��
						JSONObject mCityObject = cityArray.getJSONObject(j);
						if (mCityObject.has("n")) {
							mCityStr = mCityObject.getString("n");
							mCitiesDatas[j] = mCityStr;
						} else {
							mCitiesDatas[j] = "unknown city";
						}

						// ѭ����������
						JSONArray areaArray;

						try {
							// �ж��Ƿ��е��������򻮷֣���û�У�����������ѭ��������ִ��ѭ����������
							areaArray = mCityObject.getJSONArray("a");
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}

						// ִ�����������˵�����ڵ���������
						mAreaDatas = new String[areaArray.length()];
						for (int m = 0; m < areaArray.length(); m++) {

							// ѭ���������������򣬲������򱣴���mAreaDatas[]��
							JSONObject areaObject = areaArray.getJSONObject(m);
							if (areaObject.has("s")) {
								mAreaDatas[m] = areaObject.getString("s");
							} else {
								mAreaDatas[m] = "unknown area";
							}
							Log.d(TAG, mAreaDatas[m]);
						}

						// �洢�ж�Ӧ�����е���������
						mAreaDataMap.put(mCityStr, mAreaDatas);
					}

					// �洢ʡ�ݶ�Ӧ��������
					mCitiesDataMap.put(mProvinceStr, mCitiesDatas);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ��assetĿ¼�¶�ȡ����json�ļ�ת��ΪString����
	 * 
	 * @Title: InitData
	 * @param
	 * @return void
	 * @throws @date
	 *             [2015��8��21�� ����9:40:00]
	 */
	private String InitData() {
		StringBuffer sb = new StringBuffer();
		AssetManager mAssetManager = this.getAssets();
		try {
			InputStream is = mAssetManager.open("city.json");
			byte[] data = new byte[is.available()];
			int len = -1;
			while ((len = is.read(data)) != -1) {
				sb.append(new String(data, 0, len, "gb2312"));
			}
			is.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;
	}

}
