package com.example.ash.floatingviewoverthescreen;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingViewService extends Service implements View.OnClickListener, View.OnTouchListener {

    private View floatingWidget;
    private WindowManager windowManager;
    private  View collapsedView;
    private  View expandedView;
    private View rootView;
    private WindowManager.LayoutParams wdParams;
    int startXPOs;
    int startYPOs;
    float startTouchX;
    float startTouchY;
    @Override
    public void onCreate() {
        super.onCreate();

        floatingWidget = LayoutInflater.from(FloatingViewService.this).inflate(R.layout.float_view_layout, null);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

         wdParams = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        wdParams.gravity = Gravity.TOP | Gravity.LEFT;
        wdParams.x = 200;
        wdParams.y = 200;

        windowManager.addView(floatingWidget, wdParams);

        collapsedView = floatingWidget.findViewById(R.id.collapsed_view);

        ImageView collapsedCloseButton = floatingWidget.findViewById(R.id.collapsed_close_button);
        collapsedCloseButton.setOnClickListener(FloatingViewService.this);

        expandedView = floatingWidget.findViewById(R.id.expanded_view);

        ImageView lionImage = floatingWidget.findViewById(R.id.lionImage);
        lionImage.setOnClickListener(FloatingViewService.this);

        ImageView previousButton = floatingWidget.findViewById(R.id.btnPrevious);
        previousButton.setOnClickListener(FloatingViewService.this);

        ImageView leopardImage = floatingWidget.findViewById(R.id.leopardImage);
        leopardImage.setOnClickListener(FloatingViewService.this);

        ImageView nextButton = floatingWidget.findViewById(R.id.btnNext);
        nextButton.setOnClickListener(FloatingViewService.this);

        ImageView expandedCloseButton = floatingWidget.findViewById(R.id.close_button_expanded);
        expandedCloseButton.setOnClickListener(FloatingViewService.this);

        ImageView openButton = floatingWidget.findViewById(R.id.open_button);
        openButton.setOnClickListener(FloatingViewService.this);

        rootView = floatingWidget.findViewById(R.id.root_view);
        rootView.setOnTouchListener(FloatingViewService.this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.collapsed_close_button:
                stopSelf();
                Toast.makeText(FloatingViewService.this, "The service is stopped completely", Toast.LENGTH_SHORT).show();
                break;

            case R.id.lionImage:
                Toast.makeText(FloatingViewService.this, "Lion is tapped", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPrevious:
                Toast.makeText(FloatingViewService.this, "Previous button is tapped", Toast.LENGTH_SHORT).show();
                break;
            case R.id.leopardImage:
                Toast.makeText(FloatingViewService.this, "Leopard is tapped", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnNext:
                Toast.makeText(FloatingViewService.this, "Next button is tapped", Toast.LENGTH_SHORT).show();
                break;
            case R.id.close_button_expanded:
                Toast.makeText(FloatingViewService.this, "Expanded Close button is tapped", Toast.LENGTH_SHORT).show();

                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);

                break;
            case R.id.open_button:
                Toast.makeText(FloatingViewService.this, "Open button is tapped", Toast.LENGTH_SHORT).show();

                Intent openAppIntent = new Intent(FloatingViewService.this, MainActivity.class);
                openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openAppIntent);

                stopSelf();
                break;


        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {



        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                startXPOs = wdParams.x;
                startYPOs = wdParams.y;
                startTouchX = event.getRawX();
                startTouchY = event.getRawY();

                return  true;

            case MotionEvent.ACTION_UP:

                int startToEndXDifference = (int) (event.getRawX() - startTouchX);
                int startToEndYDifference = (int) (event.getRawY() - startTouchY);

                if(startToEndXDifference < 5 && startToEndYDifference < 5){

                    if(isWidgetCollapsed()){

                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                    }
                }

                return true;

            case MotionEvent.ACTION_MOVE:

                wdParams.x = startXPOs + (int) (event.getRawX() - startTouchX);
                wdParams.y = startYPOs + (int) (event.getRawY() - startTouchY);
                windowManager.updateViewLayout(floatingWidget, wdParams);

                return true;



        }


        return false;




    }

    private boolean isWidgetCollapsed() {

        return collapsedView.getVisibility() == View.VISIBLE;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(floatingWidget != null){

            windowManager.removeView(floatingWidget);
        }
    }
}
