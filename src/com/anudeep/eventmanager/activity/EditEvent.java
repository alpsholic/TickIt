
package com.anudeep.eventmanager.activity;

import com.anudeep.eventmanager.R;
import com.anudeep.eventmanager.dao.EventDao;
import com.anudeep.eventmanager.model.Event;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditEvent extends Activity {

    private EditText mCodeText;
    private EditText mNameText;
    private EditText mVenueText;
    private Long mEventId;
    private EventDao eventDao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventDao = new EventDao(this);
        eventDao.open();
        
        setContentView(R.layout.edit_event);
        setTitle(R.string.edit_event);

        mCodeText = (EditText) findViewById(R.id.eventCode);
        mNameText = (EditText) findViewById(R.id.eventName);
        mVenueText = (EditText) findViewById(R.id.eventVenue);
        
        Button confirmButton = (Button) findViewById(R.id.eventConfirm);

        if (mEventId == null) {
			Bundle extras = getIntent().getExtras();
			mEventId = extras != null ? extras.getLong(EventDao.KEY_EVENT_ID)
									: null;
		}
        mCodeText.setEnabled(null == mEventId);
	
        populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	saveState();
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mEventId != null) {
            Cursor event = eventDao.fetchEvent(mEventId.longValue());
            startManagingCursor(event);
        	mCodeText.setText(event.getString(
                    event.getColumnIndexOrThrow(EventDao.KEY_EVENT_CODE)));
            mNameText.setText(event.getString(
                    event.getColumnIndexOrThrow(EventDao.KEY_EVENT_NAME)));
            mVenueText.setText(event.getString(
                    event.getColumnIndexOrThrow(EventDao.KEY_EVENT_VENUE)));
        	
//        	Event event = eventDao.getEvent(mEventId.longValue());
//        	if(event!=null){
//	        	mCodeText.setText(event.getCode());
//	        	mNameText.setText(event.getName());
//	        	mVenueText.setText(event.getVenue());
//	        }
        }
    }

    private void saveState() {
        
        String code = mCodeText.getText().toString();
    	String name = mNameText.getText().toString();
        String venue = mVenueText.getText().toString();
        if (mEventId == null) {
            Event event  = eventDao.createEvent(code,name,venue);
            long id = event.getId();
            if (id > 0) {
                mEventId = id;
            }
        } else {
            eventDao.updateEvent(new Event(mEventId.longValue(),code,name,venue));
        }
    }

}
