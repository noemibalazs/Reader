package com.example.android.reader.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.reader.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String DATE = "date";
    public static final String PHOTO = "photo";
    public static final String BODY = "body";

    private TextView titleDetail;
    private TextView bylineDetail;
    private TextView bodyDetail;
    private ImageView photoDetail;
    private FloatingActionButton fButton;
    private String date;
    private ImageButton up;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent == null){
            finish();
        }

        final String title = intent.getStringExtra(TITLE);
        String author = intent.getStringExtra(AUTHOR);
        final String body = intent.getStringExtra(BODY);
        date = intent.getStringExtra(DATE);
        String photo = intent.getStringExtra(PHOTO);

        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf");


        fButton = findViewById(R.id.fab_button);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(DetailActivity.this)
                        .setType("text/plain")
                        .setChooserTitle(title)
                        .setText(body)
                        .getIntent(), getString(R.string.action_share))
                );
            }
        });


        titleDetail = findViewById(R.id.detail_title);
        bylineDetail = findViewById(R.id.detail_byline);
        bodyDetail = findViewById(R.id.detail_body);
        up = findViewById(R.id.up);

        photoDetail = findViewById(R.id.image_cover);
        Picasso.get()
                .load(photo)
                .placeholder(R.drawable.ec)
                .error(R.drawable.ec)
                .into(photoDetail);

        titleDetail.setText(title);
        bodyDetail.setText(body);
        bodyDetail.setTypeface(typeface);

        Date publishedDate = dateFormat();

        if (!publishedDate.before(START_OF_EPOCH.getTime())) {

            bylineDetail.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + "<br/>" + " by "
                            + author ));
        } else {
            bylineDetail.setText(Html.fromHtml(
                    outputFormat.format(publishedDate)
                            + "<br/>" + " by "
                            + author ));
        }


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });



    }

    private Date dateFormat(){
        try{
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e("TAG", ex.getMessage());
            Log.i("TAG", "passing today's date");
            return new Date();
        }
    }

}
