package com.example.testotg;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextName);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            // Find all available drivers from attached devices.
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (availableDrivers.isEmpty()) {
                return;
            }

            // Open a connection to the first available driver.
            UsbSerialDriver driver = availableDrivers.get(0);
//            manager.requestPermission(device, mPermissionIntent);
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(BuildConfig.APPLICATION_ID + ".GRANT_USB"), 0);
            manager.requestPermission(driver.getDevice(),usbPermissionIntent );
            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
//                PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(BuildConfig.APPLICATION_ID + ".GRANT_USB"), 0);
//                manager.requestPermission(driver.getDevice(),usbPermissionIntent );
                return;
            }

            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
            try {
                port.open(connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                port.write(editText.getText().toString().getBytes(), 399);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                port.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void calibrate(View view) {
        EditText textOn=findViewById(R.id.editNumberON);
        EditText textOff=findViewById(R.id.editNumberOff);

        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
//            manager.requestPermission(device, mPermissionIntent);
        PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(BuildConfig.APPLICATION_ID + ".GRANT_USB"), 0);
        manager.requestPermission(driver.getDevice(),usbPermissionIntent );
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
//                PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(BuildConfig.APPLICATION_ID + ".GRANT_USB"), 0);
//                manager.requestPermission(driver.getDevice(),usbPermissionIntent );
            return;
        }

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            port.open(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            String string = "$" +
                    "ON" +
                    textOn.getText().toString() +
                    "OF" +
                    textOff.getText().toString() +
                    "$";

            port.write(string.getBytes(), 399);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            port.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}