/*
 * JSON is very light weight, structured, easy to parse and much human readable. 
 * JSON is best alternative to XML when your android app needs to interchange data 
 * with your server.
 * In this example we are going to learn how to parse JSON in android.
 */

package org.turntotech.samplejson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	public Timer timer;

	ScrollView sv;
	TableLayout myTableLayout;

	private final String TAG = "Sample JSON";

	private static String url = "http://finance.google.com/finance/info?q=";

	private String [][] companies = {
			{"Amazon", "AMZN", "1", "2"},
			{"Apple", "AAPL", "3", "4"},
			{"Facebook", "FB", "5", "6"},
			{"Google", "GOOG", "7", "8"},
			{"Microsoft", "MSFT", "9", "10"},
			{"Intel", "INTC", "11", "12"},
			{"AMD", "AMD", "13", "14"},
			{"Yahoo", "YHOO", "15", "16"},
			{"Starbucks", "SBUX", "17", "18"},
			{"Dunkin Donuts", "DNKN", "19", "20"},
			{"Verizon", "VZ", "21", "22"},
			{"AT&T", "T", "23", "24"},
			{"Netflix", "NFLX", "25", "26"},
			{"IBM", "IBM", "27", "28"},
			{"American Express", "AXP", "29", "30"},
			{"Mastercard", "MA", "31", "32"},
			{"Hewlett-Packard", "HPQ", "33", "34"},
			{"Boeing", "BA", "35", "36"},
			{"JetBlue", "JBLU", "37", "38"},
			{"GAP", "GPS", "39", "40"},
			{"Ford", "F", "41", "42"},
			{"Pepsi", "PEP", "47", "48"},
			{"Coca-Cola", "COKE", "49", "50"}};


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sv = new ScrollView(this);
		myTableLayout = new TableLayout (this);

		drawScreen();
		
        Log.i("TurnToTech", "Project Name - Sample JSON");

		timer = new Timer(300000, 60000);

	}

	public void buttonClicked() {

		String totalCompanies = "";

		for (int i = 0; i < companies.length; i++) {
			totalCompanies += companies[i][1] + ",";
		}
		totalCompanies = totalCompanies.substring(0,totalCompanies.length()-1);

		new MyAsyncTask().execute(url + totalCompanies);
	}

	public void drawScreen()
	{
		// this might be a redraw, so we clean the view's container
		myTableLayout.removeAllViews();
		sv.removeAllViews();

		// special rows
		TableRow buttonRow = new TableRow(this);
		TableRow titleRow = new TableRow(this);

		// margins
		buttonRow.setPadding( 0,10,0,10);
		titleRow.setPadding( 0,10,0,10);

		// the title
		TextView title = new TextView(this);
		title.setText(R.string.title);
		title.setTextSize(26);
		title.setTextColor(Color.BLACK);
		title.setGravity(Gravity.CENTER_HORIZONTAL);

		titleRow.addView(title);
		titleRow.setGravity(Gravity.CENTER);

		// the title tablelayout
		TableLayout titleTableLayout = new TableLayout(this);
		titleTableLayout.addView(titleRow);

		// we add the title layout to the main one
		myTableLayout.addView(titleTableLayout);

		// input company rows
		for (int i = 0; i < companies.length; i++) {
			inputRow(myTableLayout, companies[i][0], Integer.parseInt(companies[i][2]), Integer.parseInt(companies[i][3]));
		}

		// the buttons table layout
		// purpose : right align for both buttons
		TableLayout buttonsLayout = new TableLayout(this);
		buttonRow.setPadding(0, 10, 0, 0);

		// the accept and cancel buttons
		Button refresh = new Button(this);
		Button stop = new Button(this);
		refresh.setText(R.string.get_data);
		stop.setText(R.string.stop);

		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				timer.start();

			}
		});
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				onStop();

			}
		});

		buttonRow.addView(refresh);
		buttonRow.addView(stop);

		buttonRow.setGravity(1);

		buttonsLayout.addView(buttonRow);
		
		myTableLayout.addView(buttonsLayout);
		sv.addView(myTableLayout);

		// set the screen content to table layout's
		setContentView(sv);
	}

	public void inputRow( TableLayout tl, String company, int companyID, int priceID )
	{
		TableRow inputRow = new TableRow(this);
		TextView code = new TextView(this);
		TextView price = new TextView(this);

		// some margin
		inputRow.setPadding(30, 10, 0, 0);
		code.setText(company + " Code");
		price.setText(company + " Price");
		code.setId(companyID);
		price.setId(priceID);
		code.setLayoutParams(new TableRow.LayoutParams(0,  TableRow.LayoutParams.WRAP_CONTENT, 1f));
		price.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
		code.setTag(companyID);
		price.setTag(priceID);
		code.setTextSize(14);
		price.setTextSize(14);

		inputRow.addView(code);
		inputRow.addView(price);

		tl.addView(inputRow);
	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		TextView code;
		TextView price;

		@Override
		protected String doInBackground(String... params) {
			
			// http client
			DefaultHttpClient httpclient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpGet httpget = new HttpGet(params[0]);
			HttpResponse response;
			try {
				// Calling async task to get json
				response = httpclient.execute(httpget);
				return EntityUtils.toString(response.getEntity());

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			
			//Log.d(TAG, "JSON Raw Data" + result);

			try {
				// Correcting the result eliminating the '// '
				result = result.substring(3);
				//Log.d(TAG, "JSON Corrected Data" + result);
				
				JSONArray jarray = new JSONArray(result);

				// Get JSON data from JSON object and set the data in our text views.
				for (int i = 0; i < companies.length; i++) {

					JSONObject json = jarray.getJSONObject(i);

					code = (TextView) myTableLayout.findViewWithTag(Integer.parseInt(companies[i][2]));
					price = (TextView) myTableLayout.findViewWithTag(Integer.parseInt(companies[i][3]));

					code.setText(companies[i][0] + " - " + json.getString("t"));
					price.setText("$" + json.getString("l_cur"));

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class Timer extends CountDownTimer {
		public Timer (long millisInFuture, long countDownInterval){
			super(millisInFuture,countDownInterval);
		}
		@Override
		public void onTick(long millisUntilFinished) {

			buttonClicked();

		}
		@Override
		public void onFinish() {

			timer.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		timer.cancel();
		Log.d(TAG, "Stopped");
	}

}
