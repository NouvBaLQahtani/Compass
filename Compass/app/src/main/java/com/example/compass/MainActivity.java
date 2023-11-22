package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView imageView;
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];

    private float azmuthAngle;
    private int delayNormal;
    private boolean successfullyRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        delayNormal = SensorManager.SENSOR_DELAY_NORMAL;
    }


    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, delayNormal);
        sensorManager.registerListener(this, sensorMagneticField, delayNormal);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            floatGravity = event.values;
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            floatGeoMagnetic = event.values;
        if (floatGravity !=null && floatGeoMagnetic!=null )
            successfullyRead = SensorManager.getRotationMatrix(floatRotationMatrix,
                    null, floatGravity, floatGeoMagnetic);
        if (successfullyRead) {
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
            imageView.setRotation((float) (-floatOrientation[0] * 180 / 3.14159));
            azmuthAngle = floatOrientation[0];
            float degree = ((azmuthAngle * 180f) / 3.14f);
            int degreeInt = Math.round(degree);
            textView.setText(degreeInt + " To absolute North");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}



