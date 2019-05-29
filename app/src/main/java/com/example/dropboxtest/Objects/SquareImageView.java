package com.example.dropboxtest.Objects;

import android.content.Context;
import android.util.AttributeSet;

/**
 * This view class creates a square image view for the photo wall post homelist item
 **/
public class SquareImageView extends android.support.v7.widget.AppCompatImageView
   {
      public SquareImageView(Context context)
         {
            super(context);
         }


      public SquareImageView(Context context, AttributeSet attrs)
         {
            super(context, attrs);
         }


      public SquareImageView(Context context, AttributeSet attrs, int defStyle)
         {
            super(context, attrs, defStyle);
         }


      @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
         {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = getMeasuredWidth();
            setMeasuredDimension(width, width);
         }
   }
