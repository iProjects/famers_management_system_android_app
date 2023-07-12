package com.tech.nyax.farmers_information_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import com.tech.nyax.farmers_information_management.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cropslistactivity extends AppCompatActivity {

    private final static String TAG = cropslistactivity.class.getSimpleName();
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    List<cropdto> lstcropdtos;
    ListView cropslistview;
    cropslistadapter _cropslistadapter;
    AutoCompleteTextView txtautocompletecrop;
    cropsautocompleteadapter _cropsautocompleteadapter;
    private cropdto _selectedautocompletedto;
    private ProgressDialog simpleWaitDialog;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView.OnClickListener clickListener;
    private SearchView.OnCloseListener closeListener;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mStart = 0, mEnd = 20;
    private List<cropdto> studentList;
    private List<cropdto> mTempCheck;
    public static int pageNumber;
    public int total_size = 0;

    //The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_list_layout);
		
		/*final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

		/* CharSequence title = "crops";
		SpannableString s = new SpannableString(title);
		s.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(),
		Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); */

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        //getSupportActionBar().setBackgroundDrawable(R.drawable.edit);

        getSupportActionBar().setIcon(R.drawable.delete);
        getSupportActionBar().setLogo(R.drawable.edit);

        // cropslistview = findViewById(R.id.lstcrops);
        // cropslistview.setAdapter(_cropslistadapter);


        try {

            new fetchautocompleteBackgroundAsyncTask().execute();
            refreshlistfromdbonfilter("");

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crops_list_menu, menu);
        /* MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setSubmitButtonEnabled(true);
            SearchAdapter searchAdapter = new SearchAdapter(this);
//            searchView.setSuggestionsAdapter(searchAdapter);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            // optional: set the letters count after which the search will begin to 1
            // the default is 2
            try {
                int autoCompleteTextViewID = getResources().getIdentifier("android:id/search_src_text", null, null);
                AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView)
                        searchView.findViewById(autoCompleteTextViewID);
                searchAutoCompleteTextView.setThreshold(1);

                _cropsautocompleteadapter = new cropsautocompleteadapter(getApplicationContext(), R.layout.crops_list_layout, autoCompleteTextViewID, lstcropdtos);
                searchAutoCompleteTextView.setAdapter(_cropsautocompleteadapter);
                searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        //this is the way to find selected object/item
                        _selectedautocompletedto = (cropdto) adapterView.getItemAtPosition(pos);
                        refreshlistfromdbonfilter(_selectedautocompletedto.getcrop_name());
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "failed to set search view letters threshold");
            }

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String query) {
                    Log.i("onQueryTextChange", query);
                    //refreshlistfromdbonfilter(query);
//                   
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    refreshlistfromdbonfilter(query);
					
                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setOnSearchClickListener(clickListener);
            searchView.setOnCloseListener(closeListener);
        } */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.delete_all_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching delete_all_activity...", TAG, 1, 1);

                    final Intent delete_all_activity = new Intent(this, delete_all_activity.class);
                    delete_all_activity.putExtra(DBContract._dto_entity_type, DBContract.app_entities_wrapper.crops);
                    startActivity(delete_all_activity);
                    return true;
                case R.id.create_crop_activity:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching createcropactivity...", TAG, 1, 1);

                    final Intent createcropactivity = new Intent(this, createcropactivity.class);
                    startActivity(createcropactivity);
                    return true;
                case R.id.home_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity...", TAG, 1, 1);

                    final Intent _MainActivity = new Intent(this, MainActivity.class);
                    startActivity(_MainActivity);
                    return true;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }

    public void refreshlistfromdbonfilter(String _searchterm) {
        try {
            if (_searchterm != null) {
                if (_searchterm.isEmpty()) {

                    new fetchallBackgroundAsyncTask().execute();

                } else {

                    new filterBackgroundAsyncTask().execute(_searchterm);
                }
            } else {

                new fetchallBackgroundAsyncTask().execute();
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar
            //simpleWaitDialog = ProgressDialog.show(cropslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(cropslistactivity.this);
            db.openDataBase();
            lstcropdtos = db.getallcrops();
            db.close();

            Collections.reverse(lstcropdtos);

            return lstcropdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */
                    _cropslistadapter = new cropslistadapter(getApplicationContext(), lstcropdtos);

                    cropslistview = findViewById(R.id.lstcrops);
                    cropslistview.setAdapter(_cropslistadapter);
                    _cropslistadapter.notifyDataSetChanged();

                    int _count = lstcropdtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("crops [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    cropslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int position, long id) {

                            utilz.getInstance(getApplicationContext()).globalloghandler("cropslistview.setOnItemClickListener", TAG, 1, 1);

                            cropdto _selectedautocompletedtodto = (cropdto) adapterView.getItemAtPosition(position);

                            // getting values from selected ListItem
                            String cropid = ((TextView) view.findViewById(R.id.txtcropid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("cropid", cropid);
//                            dataBundle.putParcelable("selectedcropdto", _selectedautocompletedtodto);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editcropactivity.class);
                            // sending cropid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }

                    });


                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstcropdtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }

                }
            });

            //simpleWaitDialog.dismiss();

        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar
            //simpleWaitDialog = ProgressDialog.show(cropslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            lstcropdtos = new ArrayList<cropdto>();
            _cropslistadapter = new cropslistadapter(getApplicationContext(), lstcropdtos);
            _cropslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(cropslistactivity.this);
            db.openDataBase();
            lstcropdtos = db.filtercropsgivenname(_searchterm[0]);
            db.close();

            Log.e(TAG, String.valueOf(lstcropdtos.size()));

            Collections.reverse(lstcropdtos);

            return lstcropdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */
                    _cropslistadapter = new cropslistadapter(getApplicationContext(), lstcropdtos);

                    cropslistview = findViewById(R.id.lstcrops);
                    cropslistview.setAdapter(_cropslistadapter);
                    _cropslistadapter.notifyDataSetChanged();

                    int _count = lstcropdtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("crops [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    cropslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // getting values from selected ListItem
                            String cropid = ((TextView) view.findViewById(R.id.txtcropid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("cropid", cropid);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editcropactivity.class);
                            // sending cropid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }
                    });


                    //simpleWaitDialog.dismiss();

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstcropdtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                }
            });

            /*txtautocompletecrop = findViewById(R.id.txtautocompletecrop);
			txtautocompletecrop.setVisibility(View.GONE);
            txtautocompletecrop.setThreshold(1);
            _cropsautocompleteadapter = new cropsautocompleteadapter(getApplicationContext(), R.layout.crops_list_layout, R.id.txtautocompletecrop, lstcropdtos);
            txtautocompletecrop.setAdapter(_cropsautocompleteadapter);
            txtautocompletecrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//this is the way to find selected object/item
                    _selectedautocompletedto = (cropdto) adapterView.getItemAtPosition(pos);
                    refreshlistfromdbonfilter(_selectedautocompletedto.getcrop_name());
                }
            });*/

            //simpleWaitDialog.dismiss();

        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchautocompleteBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar
            //simpleWaitDialog = ProgressDialog.show(cropslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(cropslistactivity.this);
            db.openDataBase();
            List<cropdto> lstdtos = db.getallcrops();
            db.close();

            Collections.reverse(lstdtos);

            return lstdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ui
                     * */

                    txtautocompletecrop = findViewById(R.id.txtautocompletecrop);
                    txtautocompletecrop.setVisibility(View.VISIBLE);
                    txtautocompletecrop.setThreshold(1);

                    _cropsautocompleteadapter = new cropsautocompleteadapter(getApplicationContext(), R.layout.crops_list_layout, R.id.txtautocompletecrop, lstcropdtos);

                    txtautocompletecrop.setAdapter(_cropsautocompleteadapter);

                    txtautocompletecrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                            //this is the way to find selected object/item
                            _selectedautocompletedto = (cropdto) adapterView.getItemAtPosition(pos);
                            refreshlistfromdbonfilter(_selectedautocompletedto.getcrop_name());
                        }
                    });

                    txtautocompletecrop.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            refreshlistfromdbonfilter(s.toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                }
            });

            //simpleWaitDialog.dismiss();

        }

    }

    void asyncupdaterecyclerviewonPostExecuterunOnUiThread(final List<cropdto> lstcropdtos) {

        try {

            //get handle
            RecyclerView _recyclerview = findViewById(R.id.crops_list_recycler_view);

            //set a layout manager (LinearLayoutManager in this example)
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            _recyclerview.setLayoutManager(mLayoutManager);

            _recyclerview.setHasFixedSize(true);

            _recyclerview.setItemAnimator(new DefaultItemAnimator());
            //_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            //specify an adapter
            cropslistrecyclerviewadapter _recyclerviewadapter = new cropslistrecyclerviewadapter(getApplicationContext(), lstcropdtos)/*{
			@Override
			public void load() {
			 do your stuff here */
			/* This method is automatically call while user reach at end of your list. 
			}}*/;

            _recyclerview.setAdapter(_recyclerviewadapter);

            // adding custom divider line
            _recyclerview.addItemDecoration(new SimpleBlueDivider(getApplicationContext()));

            // row click listener
			/* _recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), _recyclerview, new RecyclerTouchListener.ClickListener() {

				@Override
				public void onClick(View view, int position) {

					Toast.makeText(getApplicationContext(), _dto.getcrop_name() + " is selected!", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onLongClick(View view, int position) {

				}
			})); */


            if (_recyclerview.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager _linearLayoutManager = (LinearLayoutManager)
                        _recyclerview.getLayoutManager();

                _recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = _linearLayoutManager.getItemCount();

                        lastVisibleItem = _linearLayoutManager.findLastVisibleItemPosition();

                        if (!loading && totalItemCount <= (lastVisibleItem +
                                visibleThreshold)) {

                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }

                            loading = true;
                        }
                    }
                });
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }


}
