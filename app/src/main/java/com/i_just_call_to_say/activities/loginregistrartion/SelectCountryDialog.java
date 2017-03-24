package com.i_just_call_to_say.activities.loginregistrartion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import com.i_just_call_to_say.R;
import com.i_just_call_to_say.dto.CountryCodeDto;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SelectCountryDialog {

	private Activity activity;
	private Dialog dialog;
	private ListView lv_country;
	private EditText et_serachfield;
	private OnItemSelectListener onItemSelectListener;
	private List<CountryCodeDto> country_list;
	public ProgressBar pb_01;
	private CountryAdapter countryAdapter;

	public SelectCountryDialog(Activity mActivity) {
		this.activity = mActivity;
	}

	public void show() {
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.country_list);
//		dialog.setCancelable(false);
		
		lv_country = (ListView) dialog.findViewById(R.id.lv_country);
		et_serachfield=(EditText) dialog.findViewById(R.id.et_serachfield);
		pb_01 = (ProgressBar) dialog.findViewById(R.id.pb_01);
		GetCountry getCountry = new GetCountry();
		getCountry.execute();

		lv_country.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				onItemSelectListener.onItemSelected(((CountryCodeDto)lv_country.getAdapter().getItem(arg2)).getCode());
				dialog.dismiss();
			}
		});
		et_serachfield.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (countryAdapter != null) {
					countryAdapter.setCountryList(country_list);
					countryAdapter.getCustomFilter().filter(s.toString().trim());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});
		dialog.show();
	}

	public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener){
		this.onItemSelectListener = onItemSelectListener;
	}
	
	public void dismiss(){
		dialog.dismiss();
	}
	
	interface OnItemSelectListener{
		public void onItemSelected(String country_code);
	}
	
	private class CountryAdapter extends ArrayAdapter<CountryCodeDto> {  

		private LayoutInflater inflater;
		private List<CountryCodeDto> countryList;
		
		public CountryAdapter(Context context, List<CountryCodeDto> objects) {
			super(context, R.layout.country_list, objects);
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.countryList = objects;
		}
		
		public void setCountryList(List<CountryCodeDto> countryList){
			this.countryList = countryList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder viewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.country_list_row,parent, false);//b 1 2 3 b 4 5 6
				viewHolder = new ViewHolder(view);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			CountryCodeDto codeDto = countryList.get(position);
			viewHolder.tv_name.setText(codeDto.getCountry()+"   ("+codeDto.getCode()+")");
			return view;
		}
		
		private class ViewHolder {
			private TextView tv_name,tv_number;
		
			public ViewHolder(View v) {
				tv_name=(TextView)v.findViewById(R.id.tv_country_name);
				tv_number=(TextView)v.findViewById(R.id.tv_number);
			}
		}
		public CustomFilter getCustomFilter() {
			return new CustomFilter();
		}
		
		public class CustomFilter extends Filter{

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String searchStr = constraint.toString();
				FilterResults filterResults = new FilterResults();
				List<CountryCodeDto> tempList = new ArrayList<CountryCodeDto>();
				if (searchStr != null && searchStr.length() > 0) {
					for (CountryCodeDto countryName : countryList ) {
						if (countryName.getCountry() !=null && countryName.getCountry().toLowerCase(Locale.ENGLISH).startsWith(searchStr.toLowerCase(Locale.ENGLISH))) {
							tempList.add(countryName);
						}
					}
					filterResults.values = tempList;
				}else
					filterResults.values = country_list;
				
				return filterResults;
			}
			
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {
				ArrayList<CountryCodeDto> updateCountry = (ArrayList<CountryCodeDto>) results.values;
				countryAdapter = new CountryAdapter(activity,updateCountry);
				lv_country.setAdapter(countryAdapter);
			}
		}
	}
	
	
	private class GetCountry extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pb_01.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
//			ByteArrayOutputStream oS = null;
//			InputStream iS;
//			try {
//				iS = activity.getResources().getAssets().open("json/country_code.txt");
//				byte[] buffer = new byte[iS.available()];
//				// read the text file as a stream, into the buffer
//				iS.read(buffer);
//				// create a output stream to write the buffer into
//				oS = new ByteArrayOutputStream();
//				// write this buffer to the output stream
//				oS.write(buffer);
//				// Close the Input and Output streams
//				oS.close();
//				iS.close();
//				// return the output stream as a String
////				System.out.println("value--->" + oS.toString());
//				Gson gson = new Gson();
//				CountryCodeDto[] countryCodeWrapper = gson.fromJson(oS.toString(),CountryCodeDto[].class);
//				country_list = Arrays.asList(countryCodeWrapper);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			readFromExcel();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pb_01.setVisibility(View.GONE);
			countryAdapter = new CountryAdapter(activity,country_list);
			lv_country.setAdapter(countryAdapter);
		}
		
		private void readFromExcel() {
			country_list = new ArrayList<CountryCodeDto>();
			try {
				Workbook w = Workbook.getWorkbook(activity.getResources().openRawResource(R.raw.ccs));
				Sheet sheet = w.getSheet(0);
				for (int j = 0; j < sheet.getRows(); j++) {
					if(j==0)
						continue;
					CountryCodeDto codeDto = new CountryCodeDto();
					for (int i = 0; i < sheet.getColumns(); i++) {
						Cell cell = sheet.getCell(i, j);
						CellType type = cell.getType();
						if (type == CellType.LABEL) {
							codeDto.setCountry(cell.getContents());
						}
						if (type == CellType.NUMBER) {
							codeDto.setCode("+"+cell.getContents());
						}
					}
					country_list.add(codeDto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
