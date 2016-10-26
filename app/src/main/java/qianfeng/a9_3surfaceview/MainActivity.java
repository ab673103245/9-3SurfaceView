package qianfeng.a9_3surfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SurfaceView:是一个可以在子线程中更新UI的控件
        // 使用方式也很简单，就是在xml中直接使用即可
        surfaceView = ((SurfaceView) findViewById(R.id.surfaceView));

        // Surface:是SurfaceView中的一小块
        // SurfaceHolder,用来操作SrufaceView中的一小块的Surface

        //获取SurfaceHolder，SurfaceHolder用来操作SurfaceView中的surface
        surfaceHolder = surfaceView.getHolder();

        // surfaceHolder是直接操作Surface的，所以有回调方法。更改完毕的Surface，再把它放进SurfaceView中
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            // 当SurfaceView中的surface被创建成功时，回调这个方法
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isRunning = true;
                //holder.lockCanvas():如果不指定，就是整个SurfaceView的大小。 如果指定了，就是指定的矩形的大小。
                surfaceHolder = holder;
                // 开启子线程绘制SurfaceView
                new Thread(new MyRunnable()).start();

            }

            // 当SurfaceView中的surface的大小发生改变时，回调这个方法，不过一般情况下都不会调用这个方法。
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            // 当SurfaceView中的surface被销毁时，回调这个方法
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRunning = false;
            }
        });




    }


    public class MyRunnable implements Runnable {
        private int count = 0;
        @Override
        public void run() {
            while(isRunning)
            {
                // 注意画布对象是要这样获取的，通过surfaceHolder
                Canvas canvas = surfaceHolder.lockCanvas();

                try {
                    // 设置画布的背景颜色
                    canvas.drawColor(Color.BLUE);

                    Paint paint = new Paint();
                    paint.setTextSize(36);
                    paint.setColor(Color.RED);
                    canvas.drawText(String.valueOf(count++),180f,180f,paint);
                } finally {
                    // 不管有没有出现异常，这个方法肯定是要被执行的，否则如果没有提交出去，将会影响下一次的绘制。
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                // 这个的唯一好处就是不会抛异常，还是要在子线程中做的。
                SystemClock.sleep(1000);
            }


        }
    }


}
