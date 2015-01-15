package com.picmessage.imagetouch;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Sample_MyTouchListener implements OnTouchListener{
	


	    // we can be in one of these 3 states
	    private static final int NONE = 0;
	    private static final int DRAG = 1;
	    private static final int ZOOM = 2;
	    private int mode = NONE;
	    // remember some things for zooming
	    private PointF start = new PointF();
	    private PointF mid = new PointF();
	    
	    private float oldDist = 1f;
	    private float d = 0f;
	    private float newRot = 0f;
	    private float[] lastEvent = null;
	
	

	float prev_angle=0,current_angle=0;
	

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
    
   
    float x=0,y=0;
    private int r = 0;
    float scale;
  
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	
		  AbsoluteLayout.LayoutParams params  = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
		  AbsoluteLayout frame_lay  = (AbsoluteLayout) v.getParent();
		    
        // handle touch events here
		  AbsoluteLayout abs_two= (AbsoluteLayout) ((AbsoluteLayout) frame_lay.getChildAt(1)).getChildAt(0);
        ImageView view = (ImageView)abs_two.getChildAt(2);

       // ((ImageView) ((AbsoluteLayout) m_absLayout.getChildAt(1)).getChildAt(0))
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            	
            	
                 
                oldDist = spacing(event);
                if (oldDist > 10f) {
                  
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                
            
                break;
            case MotionEvent.ACTION_UP:
            	
            	 break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
            	r = r + 2;
                if (mode == DRAG) {
                	
                	drag(v, event,view);
                }
                
                else if (mode == ZOOM) 
                {
                	
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                    	
//                        matrix.set(savedMatrix);
                         scale = (newDist / oldDist);
                         Zoom(v, event, view);
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                        view.setImageMatrix(matrix);
//                        params.leftMargin =  (int) mid.x;
//                        params.topMargin = (int) mid.y;
//                        v.setLayoutParams(params);
//                        view.invalidate();
                         break;
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {

                           rotate(v, event, view);
                           break;
                    }
                }
                break;
        }
        frame_lay.invalidate();
      //  view.setImageMatrix(matrix);
        return true;
    
	}
	
	private void rotate(View v, MotionEvent event,ImageView img) {
		
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
        img.setDrawingCacheQuality(NONE);
        img.buildDrawingCache();
		
		Bitmap bitmap=img.getDrawingCache();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
        
        
        
		Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
				true);
		BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
		img.setImageDrawable(bdr);
	}
	
	private void drag(View v, MotionEvent event,ImageView mMainImg) {
		FrameLayout.LayoutParams mParams = (FrameLayout.LayoutParams) mMainImg.getLayoutParams();
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		mParams.leftMargin = x - 150;
		mParams.topMargin = y - 210;
		mMainImg.setLayoutParams(mParams);
		

	}
	
	
	private void Zoom(View v, MotionEvent event,ImageView img) {
		img.buildDrawingCache();
		
		Bitmap bitmap=img.getDrawingCache();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		  matrix.postScale(scale, scale, mid.x, mid.y);
		Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
				true);
		BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
		img.setImageDrawable(bdr);
	}

}
