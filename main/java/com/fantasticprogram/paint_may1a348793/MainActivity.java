package com.fantasticprogram.paint_may1a348793;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
class pen_size_view extends View {
    int pen_width;
    int pen_index;
    int pen_color = 0xFF000000;
    public pen_size_view(Context context) {
        super(context);
    }

    public pen_size_view(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public boolean onTouchEvent(MotionEvent e) {
        float fx = e.getX();
        float fy = e.getY();
        float w = this.getWidth();
        float h = this.getHeight();
        int x = (int) ((fx*2.0f)/w);
        int y = (int) ((fy*2.0f)/h);
        pen_index = (y*2)+x;
        view434 v4 = (view434) findViewById(R.id.image43);
        pen_width = 2 << pen_index;
        return true;

    }
    protected void onDraw(Canvas c) {
        int w = this.getWidth();
        int h = this.getHeight();
        Paint p = new Paint();
        p.setColor(pen_color);
        //c.drawColor(0xFFFFFFFF);
        for (int x = 0;x < 2;x++) {
            for (int y = 0;y < 2;y++) {
                int i = (y*2)+x;
                float s = (float) Math.exp(Math.log(1.5f)*(i-3.0f));
                float mx = (((x*2)+1)*w)/4;
                float my = (((y*2)+1)*h)/4;
                float sx = ((s*w)/4);
                float sy = ((s*h)/4);
                c.drawOval(mx-sx,my-sy,mx+sx,my+sy,p);
            }
        }
    }
}
class view434 extends View  {
    Random rand = new Random();
    int color1;
    int color2;
    int color3;
    int Cx;
    int Cy;
    int Lx;
    int Ly;
    SeekBar redbar;
    SeekBar greenbar;
    SeekBar bluebar;
    SeekBar alphabar;
    Bitmap bmp = null;
    Canvas canvas;
    pen_size_view pen_view;
    //boolean modified = false;
    //int pen_width = 1;
    String last_saved;
    SimpleDateFormat last_saved_format;
    public view434(Context context) {
        super(context);
        init();
    }
    public view434(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }
    void init() {
        //random_colors();
        random_colors();
        last_saved_format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date now = Calendar.getInstance().getTime();
        last_saved = last_saved_format.format(now);
    }
    void auto_save() {
        Date now_d = Calendar.getInstance().getTime();
        String now = last_saved_format.format(now_d);
        if (now.equals(last_saved) == false) {
            save_picture();
            last_saved = now;
        }
    }
    void save_picture() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/paint_may1");
        if (!folder.exists()) {
            boolean folder_created = folder.mkdir();
            Log.v(MainActivity.LOG_TAG,"folder was created: " + folder_created);

        }

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date now = Calendar.getInstance().getTime();
        String filename = folder + "/pic" + f.format(now) + ".png";
        Log.v(MainActivity.LOG_TAG,"folder.exists: " + folder.exists());
        Log.v(MainActivity.LOG_TAG,filename);
        try {
            bmp.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (bmp != null) {
            save_picture();
            Log.v(MainActivity.LOG_TAG,"bmp save");
        } else {
            Log.v(MainActivity.LOG_TAG,"bmp null");
        }

        bmp = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bmp);
        canvas.drawColor(0xFFFFFFFF);

    }
    public boolean onTouchEvent(MotionEvent e) {
        Cx = (int) e.getX();
        Cy = (int) e.getY();
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            random_colors();
        }
        if (e.getActionMasked() == MotionEvent.ACTION_UP) {
            auto_save();
        }
        Paint p = new Paint();

        //p.setARGB(255,);
        get_bar_color3();
        p.setColor(color3);
        p.setStrokeWidth(pen_view.pen_width);
        //canvas.drawOval(Cx-3,Cy-3,Cx+3,Cy+3,p);
        if (e.getActionMasked() == MotionEvent.ACTION_MOVE) {
            canvas.drawLine(Lx, Ly, Cx, Cy, p);
        }
        invalidate();
        Lx = Cx;Ly = Cy;
        return true;
    }
    void random_colors() {
        color1 = rand.nextInt(0x1000000) | 0xFF000000;
        color2 = rand.nextInt(0x1000000) | 0xFF000000;
    }
    void get_bar_color3() {
        color3 = alphabar.getProgress();
        color3 = (color3 << 8) | redbar.getProgress();
        color3 = (color3 << 8) | greenbar.getProgress();
        color3 = (color3 << 8) | bluebar.getProgress();
    }
    protected void onDraw(Canvas c) {
        //super.onDraw(c);
        //c.drawColor(color1);
        Paint p = new Paint();
        p.setColor(0xFFFFFFFF);
        c.drawBitmap(bmp, 0, 0, p);
        //p.setColor(color2);
        //c.drawOval(0,0,1200,1200,p);
    }
}

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    View ib43;
    int color_bars_visible = 4;
    view434 v4;
    //View sb[] = new View[4];
    final static String LOG_TAG = "Paint_may1";
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        v4.get_bar_color3();
        v4.pen_view.pen_color = v4.color3;
        v4.pen_view.invalidate();
        v4.auto_save();
    }
    public void	onStartTrackingTouch(SeekBar seekBar) {}
    public void	onStopTrackingTouch(SeekBar seekBar) {}
    public void onClick(View view) {
        //   for (int i = 0;i < 4;i++) {
        //if (view.equals(sb[i])) {
        //          v4.pen_width = 1 << i;
        //}
        //   }
    }
    void print_date() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        Date d = Calendar.getInstance().getTime();

        Log.v(LOG_TAG,f.format(d));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        print_date();
        setContentView(R.layout.activity_main);
        ib43 = findViewById(R.id.imageButton);
        v4 = (view434) findViewById(R.id.image43);
        v4.pen_view = (pen_size_view) findViewById(R.id.pen_sizes_v);
        v4.redbar = (SeekBar) findViewById(R.id.Red_Bar);
        v4.greenbar = (SeekBar) findViewById(R.id.Green_Bar);
        v4.bluebar = (SeekBar) findViewById(R.id.Blue_Bar);
        v4.alphabar = (SeekBar) findViewById(R.id.Alpha_Bar);
        //sb[0] = findViewById(R.id.sizeButton1);
        //sb[1] = findViewById(R.id.sizeButton2);
        //sb[2] = findViewById(R.id.sizeButton3);
        //sb[3] = findViewById(R.id.sizeButton4);
        //for (int i = 0;i < 4;i++) {
        //  sb[i].setOnClickListener(this);
        //}
        v4.redbar.setOnSeekBarChangeListener(this);
        v4.greenbar.setOnSeekBarChangeListener(this);
        v4.bluebar.setOnSeekBarChangeListener(this);
        v4.alphabar.setOnSeekBarChangeListener(this);
        ib43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(this,Main2Activity.class));
                int v = color_bars_visible;
                v4.redbar.setVisibility(v);
                v4.greenbar.setVisibility(v);
                v4.bluebar.setVisibility(v);
                v4.alphabar.setVisibility(v);
                //for (int i = 0;i < 4;i++) {
                //    sb[i].setVisibility(v);
                //}
                v4.pen_view.setVisibility(v);
                v4.auto_save();
                color_bars_visible = v ^ 4;
            }
        });
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.m43);
        //setSupportActionBar(myToolbar);
        //Button b = new Button(this);
        // b.setText("color");
        //b.setHeight(20);
        //b.setWidth(40);
        //myToolbar.addView(b);

    }
}
