package com.picmessage.imagetouch;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;

public class TouchListner implements OnTouchListener{

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
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



    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here
    	
		  AbsoluteLayout.LayoutParams params  = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
		  AbsoluteLayout view  = (AbsoluteLayout) v.getParent();
		 // ImageView view= (ImageView) ((AbsoluteLayout) frame_lay.getChildAt(1)).getChildAt(0);
		//  ImageView view_two= (ImageView) ((AbsoluteLayout) frame_lay.getChildAt(3)).getChildAt(0);
			 
		  AbsoluteLayout main_view  = (AbsoluteLayout) view.getParent();
		  
		   ImageView img_one= (ImageView) ((AbsoluteLayout) view.getChildAt(1)).getChildAt(0);
        
    	  int centerXOnImage=img_one.getWidth()/2;
    	    int centerYOnImage=img_one.getHeight()/2;
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event,centerXOnImage,centerYOnImage);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
            	
            
                        newRot = rotation(event,centerXOnImage,centerYOnImage);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                        
                        
                        int m_scale=1;;
						int m_hightOfImage = (int) (m_scale + (img_one.getHeight()));
                		int m_widthOfImage =  (int) (m_scale + (img_one.getWidth()));
                		int commen_widthheight;
                		
                		if (m_hightOfImage>m_widthOfImage) {
                			commen_widthheight=m_hightOfImage+50;
						}else {
							commen_widthheight=m_widthOfImage+50;
						}
//                	
//                        LayoutParams m_layoutparams =null;
//                        m_layoutparams = new AbsoluteLayout.LayoutParams(commen_widthheight, commen_widthheight, 0, 0);
////                        img_one.setLayoutParams(m_layoutparams);
////                        img_two.setLayoutParams(m_layoutparams);
////                        
//                		m_layoutparams = new AbsoluteLayout.LayoutParams(
//                				AbsoluteLayout.LayoutParams.WRAP_CONTENT,
//                				AbsoluteLayout.LayoutParams.WRAP_CONTENT,
//                				main_view.getLeft(), main_view
//                				.getTop()); 
//                        main_view.setLayoutParams(m_layoutparams);
//                        
//                        
//                  	
//						
						float angle=Get_Angle();
						view.setRotation(angle);
//						img_one.setRotation(angle);
//						img_two.setRotation(angle);
						img_one.invalidate();
					
						   break;
                    
                
             
        }
        
        view.invalidate();
     
        return false;
    }

    private float Get_Angle() {
    			float[] v = new float[9];
				 matrix.getValues(v);
				 // translation is simple
				 float tx = v[Matrix.MTRANS_X];
				 float ty = v[Matrix.MTRANS_Y];
				
				 float[] values = new float[9];
				// calculate real scale
				 float scalex = values[Matrix.MSCALE_X];
				 float skewy = values[Matrix.MSKEW_Y];
				 float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);
				
				 // calculate the degree of rotation
				 float rAngle = Math.round(Math.atan2(v[Matrix.MSKEW_X], v[Matrix.MSCALE_X]) * (180 / Math.PI));
				return rAngle;
}

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
     * @param centerYOnImage 
     * @param centerXOnImage 
     * @return Degrees
     */
    private float rotation(MotionEvent event, int centerXOnImage, int centerYOnImage) {
        double delta_x = (event.getX(0) - centerXOnImage);
        double delta_y = (event.getY(0) -centerYOnImage);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
    
    
}