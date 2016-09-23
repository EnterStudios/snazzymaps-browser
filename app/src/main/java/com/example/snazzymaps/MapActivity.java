package com.example.snazzymaps;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Activity that displays a full styled map when one of the styled static maps
 * is clicked on. It also provides a dialog that shows information about the
 * style, such as its author and URL.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String JSON_ID = "json_id";

    /**
     * Initial location for the map.
     */
    private static final LatLng LAT_LNG = new LatLng(-33.8688, 151.2093);

    private SnazzyMapsStyle mStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Styles the map when its available, using the style JSON provided by the
     * previous activity.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStyle = new SnazzyMapsStyle(bundle.getString(JSON_ID));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG, 14));
            mStyle.applyToMap(googleMap);
            getSupportActionBar().setTitle(mStyle.name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Back button in action bar.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    /**
     * Click handler for the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_dialog:  // Info button clicked, show the info dialog.
                showInfoDialog();
                return true;
            case android.R.id.home:  // Back button clicked, finish the activity.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shows a dialog that provides information about the style, such as its author and URL.
     */
    private void showInfoDialog() {
        String desc = mStyle.description.split(" ").length > 3 ? mStyle.description + "\n\n" : "";
        final SpannableString message = new SpannableString(getString(
                R.string.info_dialog_message, desc, mStyle.url));
        Linkify.addLinks(message, Linkify.ALL);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle(getString(R.string.info_dialog_title, mStyle.name, mStyle.author))
                .setMessage(message)
                .create();
        dialog.show();

        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}