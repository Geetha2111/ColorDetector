package com.example.rgbsample;

import android.R.color;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
	private Button btnLoadImage;
	private ImageView imageResult;

	private static final int RQS_IMAGE1 = 1;

	private Bitmap bitmapMaster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnLoadImage = (Button)findViewById(R.id.color_code);
		imageResult = (ImageView)findViewById(R.id.image);
		bitmapMaster = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		imageResult.setImageBitmap(bitmapMaster);
		btnLoadImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, 1);
			}});

		imageResult.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				int x = (int) event.getX();
				int y = (int) event.getY();
				switch(action){
				case MotionEvent.ACTION_DOWN:
					int color = getProjectedColor((ImageView)v, bitmapMaster, x, y);
					int redValue = Color.red(color);  
					int blueValue = Color.blue(color);  
					int greenValue = Color.green(color);
					btnLoadImage.setText(String.format("#%02x%02x%02x", redValue, greenValue,blueValue));
					btnLoadImage.setBackgroundColor(color);
					break;
				case MotionEvent.ACTION_MOVE:
					int color1 = getProjectedColor((ImageView)v, bitmapMaster, x, y);
					int redValue1 = Color.red(color1);  
					int blueValue1 = Color.blue(color1);  
					int greenValue1 = Color.green(color1);
					btnLoadImage.setText(String.format("#%02x%02x%02x", redValue1, greenValue1,blueValue1));
					btnLoadImage.setBackgroundColor(color1);
					break;
				case MotionEvent.ACTION_UP:
					int color2 = getProjectedColor((ImageView)v, bitmapMaster, x, y);
					int redValue2 = Color.red(color2);  
					int blueValue2 = Color.blue(color2);  
					int greenValue2 = Color.green(color2);
					btnLoadImage.setText(String.format("#%02x%02x%02x", redValue2, greenValue2,blueValue2));
					btnLoadImage.setBackgroundColor(color2);
					break;
				}
				/*
				 * Return 'true' to indicate that the event have been consumed.
				 * If auto-generated 'false', your code can detect ACTION_DOWN only,
				 * cannot detect ACTION_MOVE and ACTION_UP.
				 */
				return true;
			}});
	}

	/*
	 * Project position on ImageView to position on Bitmap
	 * return the color on the position 
	 */
	private int getProjectedColor(ImageView iv, Bitmap bm, int x, int y){
		if(x<0 || y<0 || x > iv.getWidth() || y > iv.getHeight()){
			//outside ImageView
			return color.background_light; 
		}else{
			int projectedX = (int)((double)x * ((double)bm.getWidth()/(double)iv.getWidth()));
			int projectedY = (int)((double)y * ((double)bm.getHeight()/(double)iv.getHeight()));

			btnLoadImage.setText(x + ":" + y + "/" + iv.getWidth() + " : " + iv.getHeight() + "\n" +
					projectedX + " : " + projectedY + "/" + bm.getWidth() + " : " + bm.getHeight());

			return bm.getPixel(projectedX, projectedY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode){
			case RQS_IMAGE1:
				bitmapMaster = (Bitmap) data.getExtras().get("data");
				btnLoadImage.setText(bitmapMaster+"");
				imageResult.setImageBitmap(bitmapMaster);
				break;
			}
		}
	}

}
