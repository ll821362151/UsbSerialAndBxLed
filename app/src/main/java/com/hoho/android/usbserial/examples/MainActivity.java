package com.hoho.android.usbserial.examples;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.CommProByte;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.ThreadPoolExecutorUtils;
import com.liulang.testandroid11.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import kdthe.DynamicAreaUtils;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int WRITE_WAIT_MILLIS = 1000;
    private int READ_WAIT_MILLIS = 1000;
    private ScheduledThreadPoolExecutor poolExecutor;
    private AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
        } else {
            onBackStackChanged();
        }

        poolExecutor = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("read-" + atomicInteger.incrementAndGet());
                return thread;
            }
        });

        openDriver();
    }

    @Override
    public void onBackStackChanged() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            TerminalFragment terminal = (TerminalFragment) getSupportFragmentManager().findFragmentByTag("terminal");
            if (terminal != null) {
                terminal.status("USB device detected");
            }
        }
        super.onNewIntent(intent);
    }

    private void openDriver() {
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            Log.i(TAG, "openDriver: isEmpty");
            return;
        }
        Log.i(TAG, "size: " + availableDrivers.size());
        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        Log.i(TAG, "driver: " + driver.toString());
//        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Log.i(TAG, "connection is fail");
            return;
        }
        Log.i(TAG, "connection: " + connection.getSerial());
        // Most devices have just one port (port 0)
        Log.i(TAG, "port: " + driver.getPorts());
        UsbSerialPort port = driver.getPorts().get(0);
        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            /**
             * 将Android开发板用自定义的连接线与电脑端的串口连接好
             * 通过电脑端的串口助手，结合Android studio的日志来确定是否转换成功
             */
            send("hello world usb to serial", port);
            poolExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    read(port);
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "openDriver: " + e.toString());
//        } finally {
//            try {
//                port.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

    /**
     * 改变户外广告机页码
     */
    public static byte[] onChangeOutdoorAdPage(byte pageNumber) {

        byte[] sendByte = new byte[15];
        sendByte[0] = (byte) 0x05;
        sendByte[1] = (byte) 0x0D;
        sendByte[2] = (byte) 0xA1;
        sendByte[3] = (byte) 0xB1;
        sendByte[4] = (byte) 0xF1;
        sendByte[5] = (byte) 0x01;
        sendByte[6] = (byte) 0x00;
        sendByte[7] = (byte) 0x00;
        sendByte[8] = (byte) 0x00;
        sendByte[9] = (byte) 0x01;
        sendByte[10] = pageNumber;
        sendByte[11] = (byte) 0x01;
        sendByte[12] = (byte) 0x89;
        sendByte[13] = (byte) 0x9E;
        sendByte[14] = (byte) 0xFE;
        return sendByte;
    }

    private void send(String str, UsbSerialPort usbSerialPort) {
        byte[] data = (str + '\n').getBytes();
        send(data, usbSerialPort);
    }

    private void send(byte[] data, UsbSerialPort usbSerialPort) {
        try {
            SpannableStringBuilder spn = new SpannableStringBuilder();
            spn.append("send ").append(String.valueOf(data.length)).append(" bytes\n");
            spn.append(HexDump.dumpHexString(data)).append("\n");
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i(TAG, "send: " + spn);
            usbSerialPort.write(data, WRITE_WAIT_MILLIS);
        } catch (Exception e) {
            Log.e(TAG, "send: ", e);
        }

        ThreadPoolExecutorUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
//                DynamicAreaUtils.send("TakeScreenEvent:" + atomicInteger.incrementAndGet());
            }
        });
    }

    private void read(UsbSerialPort usbSerialPort) {
        try {
            byte[] buffer = new byte[8192];
            int len = usbSerialPort.read(buffer, READ_WAIT_MILLIS);
            receive(Arrays.copyOf(buffer, len));
        } catch (IOException e) {
            // when using read with timeout, USB bulkTransfer returns -1 on timeout _and_ errors
            // like connection loss, so there is typically no exception thrown here on error
            Log.e(TAG, "read: " + e.getMessage());
        }
    }

    private void receive(byte[] data) {
        SpannableStringBuilder spn = new SpannableStringBuilder();
        spn.append("receive ").append(String.valueOf(data.length)).append(" bytes\n");
        if (data.length > 0) {
            spn.append(HexDump.dumpHexString(data)).append("\n");
            Log.i(TAG, "receive: " + HexDump.dumpHexString(data));
            Log.i(TAG, "receive: " + CommProByte.bytes2String(data));
            Log.i(TAG, "receive: " + CommProByte.bytes2Ascii(data));
            Log.i(TAG, "receive: " + CommProByte.bytes2Text(data));
        }
    }
}
