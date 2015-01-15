package com.picmessage.imagetouch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {
	private Context m_context;
	private Bitmap m_bitmap;
	Button btn_zoom;
	ImageView imgview_zoom;
	AbsoluteLayout m_absolutelayout,m_absZoomlayout,m_ImageBorderLayout;
	int m_absHeight;
	
	AbsoluteLayout.LayoutParams Zoom_Layoutparam;
	
	private float m_oldDist = 1f, m_scale, m_oldX = 0, m_oldY = 0, m_dX, m_dY,
			m_posX, m_posY, m_prevX = 0, m_prevY = 0, m_newX, m_newY;
	 private float newRot = 0f;
	private OnTouchListener DragImagr_OnTouchListner,Zoom_OntouchListner;
	private Display m_screen;
	private int m_DisplayWidth;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_context = MainActivity.this;
		m_absolutelayout=(AbsoluteLayout)findViewById(R.id.abs_layout);
		
		//Get Bitmap
		m_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blackhair);
		
		
		m_screen = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		m_DisplayWidth = m_screen.getWidth();
	
		
		
		
		//Dynamic Control
		m_ImageBorderLayout = new AbsoluteLayout(m_context);
		m_absZoomlayout = new AbsoluteLayout(m_context);
		btn_zoom=new Button(m_context);
		imgview_zoom = new ImageView(m_context);
		

		//Layout Params
		Zoom_Layoutparam = new AbsoluteLayout.LayoutParams(40, 40, 80,80);
		m_ImageBorderLayout.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, 0, 0));
		m_absZoomlayout.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT, 0, 0));
				
		RelativeLayout.LayoutParams rl_pr = new LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		
		rl_pr.addRule(RelativeLayout.ABOVE, R.id.linear_footer);
		rl_pr.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		
		m_absolutelayout.setLayoutParams(rl_pr);
		
		
	
		
		// Border Button Zoom
		btn_zoom.setLayoutParams(Zoom_Layoutparam);
		btn_zoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.zoom_ico));
		btn_zoom.setId(0);
		
		
		//
		imgview_zoom.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
		imgview_zoom.setImageBitmap(Bitmap.createScaledBitmap(m_bitmap, 90, 90,true));
		
		
		
		
	
		m_absZoomlayout.addView(imgview_zoom);
		
		m_ImageBorderLayout.addView(btn_zoom);
		m_ImageBorderLayout.addView(m_absZoomlayout);
		m_ImageBorderLayout.setDrawingCacheEnabled(true);
		m_ImageBorderLayout.setClickable(true);
		m_ImageBorderLayout.setId(0);
		
		m_absolutelayout.addView(m_ImageBorderLayout);
		m_absHeight = m_absolutelayout.getHeight();
		
		
		
		
		
		
		
		
		
		// Listener for the arrow ontouch of arrow ZoomIn and ZoomOut the image.
		Zoom_OntouchListner = new OnTouchListener() {
		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
		View view;
		// RemoveBorders();
		view = v;
		v.setClickable(true);
		v.setDrawingCacheEnabled(true);
		AbsoluteLayout m_absLayout = null;
		android.widget.AbsoluteLayout.LayoutParams m_layoutparams;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		m_oldX = event.getX();
		m_oldY = event.getY();
		break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		break;
		case MotionEvent.ACTION_MOVE:
		m_newX = event.getX();
		m_newY = event.getY();
		float newDist = m_newX - m_oldX;
		if (m_newX > m_oldX && m_newY > m_oldY) {
		if (newDist > 0.0f) {
		m_scale = 1;
		m_absLayout = (AbsoluteLayout) v.getParent();
		int m_hightOfImage = (int) (m_scale + (((ImageView) ((AbsoluteLayout) m_absLayout.getChildAt(1)).getChildAt(0)).getHeight()));
		int m_widthOfImage = (int) (m_scale + (((ImageView) ((AbsoluteLayout) m_absLayout.getChildAt(1)).getChildAt(0)).getWidth()));
		
		int abs_bottom=m_absLayout.getBottom();
		
		int imgzoom_bot=(imgview_zoom.getBottom());
		
		int abs_right=m_absLayout.getRight();
		if (abs_right <= (m_DisplayWidth)) {
		m_layoutparams = new AbsoluteLayout.LayoutParams(m_widthOfImage, m_hightOfImage, 0, 0);
		((ImageView) ((AbsoluteLayout) m_absLayout.getChildAt(1)).getChildAt(0)).setLayoutParams(m_layoutparams);
		
		m_layoutparams = new AbsoluteLayout.LayoutParams(
		AbsoluteLayout.LayoutParams.WRAP_CONTENT,
		AbsoluteLayout.LayoutParams.WRAP_CONTENT,
		m_absLayout.getLeft(), m_absLayout
		.getTop());
		m_absLayout.setLayoutParams(m_layoutparams);
		((Button) m_absLayout.getChildAt(0)).setLayoutParams(new AbsoluteLayout.LayoutParams(50	,50,m_widthOfImage, m_hightOfImage));
		
		m_hightOfImage = (int) (m_scale + (((AbsoluteLayout) m_absLayout.getChildAt(1)).getHeight()));
		m_widthOfImage = (int) (m_scale + (((AbsoluteLayout) m_absLayout.getChildAt(1)).getWidth()));
		m_layoutparams = new AbsoluteLayout.LayoutParams(m_widthOfImage, m_hightOfImage,((AbsoluteLayout) m_absLayout.getChildAt(1)).getLeft(),((AbsoluteLayout) m_absLayout.getChildAt(1)).getTop());
		((AbsoluteLayout) m_absLayout.getChildAt(1)).setLayoutParams(m_layoutparams);
		}
		}
		}
		if (m_newX < m_oldX && m_newY < m_oldY) {
		m_absLayout = (AbsoluteLayout) view.getParent();
		int m_hightOfImage = (int) (((ImageView) ((AbsoluteLayout) m_absLayout
		.getChildAt(1)).getChildAt(0)).getHeight() - m_scale);
		int m_widthOfImage = (int) (((ImageView) ((AbsoluteLayout) m_absLayout
		.getChildAt(1)).getChildAt(0)).getWidth() - m_scale);

		m_layoutparams = new AbsoluteLayout.LayoutParams(
		m_widthOfImage, m_hightOfImage, 0, 0);
		((ImageView) ((AbsoluteLayout) m_absLayout
		.getChildAt(1)).getChildAt(0))
		.setLayoutParams(m_layoutparams);
		
		m_layoutparams = new AbsoluteLayout.LayoutParams(
		AbsoluteLayout.LayoutParams.WRAP_CONTENT,
		AbsoluteLayout.LayoutParams.WRAP_CONTENT,
		m_absLayout.getLeft(), m_absLayout.getTop());
		m_absLayout.setLayoutParams(m_layoutparams);
		((Button) m_absLayout.getChildAt(0)).setLayoutParams(new AbsoluteLayout.LayoutParams(50,50,m_widthOfImage, m_hightOfImage));

		m_hightOfImage = (int) ((((AbsoluteLayout) m_absLayout
		.getChildAt(1)).getHeight()) - m_scale);
		m_widthOfImage = (int) ((((AbsoluteLayout) m_absLayout
		.getChildAt(1)).getWidth()) - m_scale);
		m_layoutparams = new AbsoluteLayout.LayoutParams(
		m_widthOfImage, m_hightOfImage,
		((AbsoluteLayout) m_absLayout.getChildAt(1))
		.getLeft(),
		((AbsoluteLayout) m_absLayout.getChildAt(1))
		.getTop());
		((AbsoluteLayout) m_absLayout.getChildAt(1))
		.setLayoutParams(m_layoutparams);
		}
		break;
		}
		return false;
		}
		};
		
		
		DragImagr_OnTouchListner = new OnTouchListener() {
			private float d;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
					m_oldX = event.getX();
					m_oldY = event.getY();
					break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
					break;
					
            case MotionEvent.ACTION_POINTER_DOWN:

                d = rotation(event);
                break;
			case MotionEvent.ACTION_MOVE:
				
				
				  if ((event.getPointerCount() == 3)) {
					  AbsoluteLayout m_absLayout = (AbsoluteLayout) v.getParent();
					  int m_hightOfImage = (int) (m_scale + ((AbsoluteLayout)  v).getHeight());
						int m_widthOfImage = (int) (m_scale + ((AbsoluteLayout)  v).getWidth());
						
					  
						ImageView view=(ImageView)m_absLayout.getChildAt(0);
						 rotate(v, event, view,d);

                  }else
                  {
					m_dX = event.getX() - m_oldX;
					m_dY = event.getY() - m_oldY;
					m_posX = m_prevX + m_dX;
					m_posY = m_prevY + m_dY;
					if (m_posX > 0
					&& m_posY > 0
					&& (m_posX + v.getWidth()) < m_absolutelayout
					.getWidth()
					&& (m_posY + v.getHeight()) < m_absolutelayout
					.getHeight()) {
					v.setLayoutParams(new AbsoluteLayout.LayoutParams(v
					.getMeasuredWidth(), v.getMeasuredHeight(),
					(int) m_posX, (int) m_posY));
					m_prevX = m_posX;
					m_prevY = m_posY;
					}
					}
					
					
					
					break;
			}
			return false;
		}
		};
	
		btn_zoom.setOnTouchListener(Zoom_OntouchListner);
		m_ImageBorderLayout.setOnTouchListener(DragImagr_OnTouchListner);
		
		
		
		
		
		
		
		
		
		
	}
	private void rotate(View v, MotionEvent event,ImageView img, float d) {
		
		Matrix matrix = new Matrix();
	    newRot = rotation(event);
        float r = newRot - d;
        float[] values = new float[9];
        matrix.getValues(values);
        float tx = values[2];
        float ty = values[5];
        float sx = values[0];
        float xc = (img.getWidth() / 2) * sx;
        float yc = (img.getHeight() / 2) * sx;
        matrix.postRotate(r, tx + xc, ty + yc);
        img.setImageMatrix(matrix);
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
		
		Bitmap bitmap=img.getDrawingCache();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
        
        
        
		Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
				true);
		BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
		img.setImageDrawable(bdr);
	}
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
