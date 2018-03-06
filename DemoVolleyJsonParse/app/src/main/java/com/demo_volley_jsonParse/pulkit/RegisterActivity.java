package com.demo_volley_jsonParse.pulkit;

import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo_volley_jsonParse.pulkit.utils.SnackbarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private Button create_account;
    private TextView username_font_icon, email_font_icon, phone_font_icon, password_font_icon,
            retype_password_font_icon, country_font_icon, city_font_icon, state_font_icon;

    private EditText et_username, et_email, et_phone, et_password, et_retype_password;
    private String str_username, str_email, str_phone, str_password, str_retype_password;

    private Typeface fontAwesomeFont;

    private HashMap<String, String> map = new HashMap<>();
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private Spinner sp_countries, sp_cities, sp_states;

    private ArrayList<Register> registercountryArrayList = new ArrayList<>();
    private ArrayList<Register> registercityArrayList = new ArrayList<>();
    private ArrayList<Register> registerstateArrayList = new ArrayList<>();

    private String country_id;
    private String city_id;
    private String state_id;
    private String my_country_id;
    private String my_city_id;
    private String my_state_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {

        queue = Volley.newRequestQueue(getApplicationContext());

        username_font_icon = (TextView) findViewById(R.id.username_font_icon);
        email_font_icon = (TextView) findViewById(R.id.email_font_icon);
        phone_font_icon = (TextView) findViewById(R.id.phone_font_icon);
        password_font_icon = (TextView) findViewById(R.id.password_font_icon);
        retype_password_font_icon = (TextView) findViewById(R.id.retype_password_font_icon);
        country_font_icon = (TextView) findViewById(R.id.country_font_icon);
        city_font_icon = (TextView) findViewById(R.id.city_font_icon);
        state_font_icon = (TextView) findViewById(R.id.state_font_icon);

        sp_countries = (Spinner) findViewById(R.id.sp_countries);
        sp_cities = (Spinner) findViewById(R.id.sp_cities);
        sp_states = (Spinner) findViewById(R.id.sp_states);


        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_retype_password = (EditText) findViewById(R.id.et_retype_password);
        create_account = (Button) findViewById(R.id.create_account);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        username_font_icon.setTypeface(fontAwesomeFont);
        email_font_icon.setTypeface(fontAwesomeFont);
        phone_font_icon.setTypeface(fontAwesomeFont);
        password_font_icon.setTypeface(fontAwesomeFont);
        retype_password_font_icon.setTypeface(fontAwesomeFont);
        country_font_icon.setTypeface(fontAwesomeFont);
        city_font_icon.setTypeface(fontAwesomeFont);
        state_font_icon.setTypeface(fontAwesomeFont);

        progress_bar.setVisibility(View.VISIBLE);
        getSpinnerCountryCityState("https://www.---.co" + "/---", map);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_username = et_username.getText().toString();
                str_email = et_email.getText().toString();
                str_phone = et_phone.getText().toString();
                str_password = et_password.getText().toString();
                str_retype_password = et_retype_password.getText().toString();

                my_country_id = registercountryArrayList.get(sp_countries.getSelectedItemPosition()).getId();
                my_city_id = registercityArrayList.get(sp_cities.getSelectedItemPosition()).getId();
                my_state_id = registerstateArrayList.get(sp_states.getSelectedItemPosition()).getId();

                if (str_username.equals(null) || str_username.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Username should not be empty");
                } else if (str_email.equals(null) || str_email.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Email number should not be empty");
                } else if (str_phone.equals(null) || str_phone.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Phone number should not be empty");
                } else if (str_password.equals(null) || str_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Password should not be empty");
                } else if (str_retype_password.equals(null) || str_retype_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Please Re-type your password");
                } else if (str_password.length() < 8) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Your password should be of minimum 8 characters");
                } else if (!str_email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Your email is invalid, use special characters like @,!,#,*", Toast.LENGTH_LONG).show();
                } else {
                    getResgistrationParameter(str_username, str_email, str_phone, str_password, my_country_id, my_city_id, my_state_id);
                }
            }
        });

    }

    private void getSpinnerCountryCityState(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String country = jsonObject.getString("country");
                    String city = jsonObject.getString("city");
                    String state = jsonObject.getString("state");

                    JSONArray countryJsonArray = new JSONArray(country);
                    for (int i = 0; i < countryJsonArray.length(); i++) {
                        JSONObject jsonObject1 = countryJsonArray.getJSONObject(i);

                        country_id = jsonObject1.getString("country_id");
                        String country_name = jsonObject1.getString("country_name");

                        registercountryArrayList.add(new Register(country_id, country_name));

                        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(registercountryArrayList);
                        sp_countries.setAdapter(countrySpinnerAdapter);

                    }

                    JSONArray cityJsonArray = new JSONArray(city);
                    for (int i = 0; i < cityJsonArray.length(); i++) {
                        JSONObject jsonObject2 = cityJsonArray.getJSONObject(i);

                        city_id = jsonObject2.getString("city_id");
                        String city_name = jsonObject2.getString("city_name");

                        registercityArrayList.add(new Register(city_id, city_name));

                        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(registercityArrayList);
                        sp_cities.setAdapter(countrySpinnerAdapter);

                    }

                    JSONArray stateJsonArray = new JSONArray(state);
                    for (int i = 0; i < stateJsonArray.length(); i++) {
                        JSONObject jsonObject3 = stateJsonArray.getJSONObject(i);

                        state_id = jsonObject3.getString("state_id");
                        String state_name = jsonObject3.getString("state_name");

                        registerstateArrayList.add(new Register(state_id, state_name));

                        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(registerstateArrayList);
                        sp_states.setAdapter(countrySpinnerAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_bar.setVisibility(View.INVISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar.setVisibility(View.INVISIBLE);
                try {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getResgistrationParameter(String str_username, String str_email, String str_phone, String str_password
            , String str_country, String str_city, String str_state) {

        progress_bar.setVisibility(View.VISIBLE);
        map.put("username", str_username);
        map.put("email", str_email);
        map.put("phone", str_phone);
        map.put("password", str_password);
        map.put("country", str_country);
        map.put("city", str_city);
        map.put("state", str_state);

        getRegisterReponse("https://www.---.co" + "/---", map);
    }


    public void getRegisterReponse(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    Toast.makeText(RegisterActivity.this, "username and password is invalid!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private class CountrySpinnerAdapter implements SpinnerAdapter {

        ArrayList<Register> registerArrayList;

        public CountrySpinnerAdapter(ArrayList<Register> registerArrayList) {
            this.registerArrayList = registerArrayList;
        }

        @Override
        public int getCount() {
            return registerArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return registerArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return android.R.layout.simple_spinner_dropdown_item;
        }

        /**
         * Returns the View that is shown when a element was selected.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            TextView textView = new TextView(getApplicationContext());
//            textView.setTextColor(getResources().getColor(R.color.spiner_color_techer));
//            textView.setPadding(10, 10, 0, 10);
//            textView.setTextSize(20);
//            textView.setText(registerArrayList.get(position).getCity_name());
//                    +" ("+arr_compose_list.get(position).getReciver_typ()+")");


            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_spinner, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Register register = (Register) getItem(position);

            viewHolder.countryName.setText(register.getName());

            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return this.getView(position, convertView, parent);
        }
    }

    private class ViewHolder {
        TextView countryName;

        public ViewHolder(View view) {
            countryName = (TextView) view.findViewById(R.id.text);
        }
    }


}
