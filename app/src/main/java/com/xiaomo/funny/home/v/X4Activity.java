package com.xiaomo.funny.home.v;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.xiaomo.funny.home.R;
import com.xiaomo.funny.home.application.MyApp;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class X4Activity extends Activity {

    public static final String TAG = "cn.wch.wchusbdriver";
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";

    public readThread handlerThread;
    protected final Object ThreadLock = new Object();
    private EditText readText;
    private EditText writeText;
    private Spinner baudSpinner;
    private Spinner stopSpinner;
    private Spinner dataSpinner;
    private Spinner paritySpinner;
    private Spinner flowSpinner;
    private boolean isOpen;
    private Handler handler;
    private int retval;
//	private X4Activity activity;

    private Button writeButton, configButton, openButton, clearButton;

    public byte[] writeBuffer;
    public byte[] readBuffer;
    public int actualNumBytes;

    public int numBytes;
    public byte count;
    public int status;
    public byte writeIndex = 0;
    public byte readIndex = 0;

    public int baudRate;
    public byte baudRate_byte;
    public byte stopBit;
    public byte dataBit;
    public byte parity;
    public byte flowControl;

    public boolean isConfiged = false;
    public boolean READ_ENABLE = false;
    public SharedPreferences sharePrefSettings;
    public String act_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MyApp.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        initUI();
        if (!MyApp.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(X4Activity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                    int a = 1;
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持常亮的屏幕的状态
        writeBuffer = new byte[512];
        readBuffer = new byte[512];
        isOpen = false;
        configButton.setEnabled(false);
        writeButton.setEnabled(false);
//		activity = this;


        //打开流程主要步骤为ResumeUsbList，UartInit
        openButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isOpen) {
                    retval = MyApp.driver.ResumeUsbList();
                    if (retval == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
                    {
                        Toast.makeText(X4Activity.this, "打开设备失败!",
                                Toast.LENGTH_SHORT).show();
                        MyApp.driver.CloseDevice();
                    } else if (retval == 0) {
                        if (!MyApp.driver.UartInit()) {//对串口设备进行初始化操作
                            Toast.makeText(X4Activity.this, "设备初始化失败!",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(X4Activity.this, "打开" +
                                            "设备失败!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(X4Activity.this, "打开设备成功!",
                                Toast.LENGTH_SHORT).show();
                        isOpen = true;
                        openButton.setText("Close");
                        configButton.setEnabled(true);
                        writeButton.setEnabled(true);
                        new readThread().start();//开启读线程读取串口接收的数据
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(X4Activity.this);
                        builder.setIcon(R.drawable.icon);
                        builder.setTitle("未授权限");
                        builder.setMessage("确认退出吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
//								MainFragmentActivity.this.finish();
                                System.exit(0);
                            }
                        });
                        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        });
                        builder.show();

                    }
                } else {
                    MyApp.driver.CloseDevice();
                    openButton.setText("Open");
                    configButton.setEnabled(false);
                    writeButton.setEnabled(false);
                    isOpen = false;
                }
            }
        });

        configButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (MyApp.driver.SetConfig(baudRate, dataBit, stopBit, parity,//配置串口波特率，函数说明可参照编程手册
                        flowControl)) {

                    Toast.makeText(X4Activity.this, "串口设置成功!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(X4Activity.this, "串口设置失败!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        writeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//				byte[] to_send = toByteArray(writeText.getText().toString());
//				byte[] to_send = toByteArray2(writeText.getText().toString());
                byte[] to_send = writeText.getText().toString().getBytes();
                int retval = MyApp.driver.WriteData(to_send, to_send.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
                if (retval < 0)
                    Toast.makeText(X4Activity.this, "写失败!",
                            Toast.LENGTH_SHORT).show();
            }
        });

        handler = new Handler() {

            public void handleMessage(Message msg) {
//				readText.setText((String) msg.obj);
                readText.append((String) msg.obj);
            }
        };

    }

    //处理界面
    private void initUI() {
        readText = (EditText) findViewById(R.id.ReadValues);
        writeText = (EditText) findViewById(R.id.WriteValues);
        configButton = (Button) findViewById(R.id.configButton);
        writeButton = (Button) findViewById(R.id.WriteButton);
        openButton = (Button) findViewById(R.id.open_device);
        clearButton = (Button) findViewById(R.id.clearButton);

        baudSpinner = (Spinner) findViewById(R.id.baudRateValue);
        ArrayAdapter<CharSequence> baudAdapter = ArrayAdapter
                .createFromResource(this, R.array.baud_rate,
                        R.layout.my_spinner_textview);
        baudAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        baudSpinner.setAdapter(baudAdapter);
        baudSpinner.setGravity(0x10);
        baudSpinner.setSelection(9);
        /* by default it is 9600 */
        baudRate = 115200;

		/* stop bits */
        stopSpinner = (Spinner) findViewById(R.id.stopBitValue);
        ArrayAdapter<CharSequence> stopAdapter = ArrayAdapter
                .createFromResource(this, R.array.stop_bits,
                        R.layout.my_spinner_textview);
        stopAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        stopSpinner.setAdapter(stopAdapter);
        stopSpinner.setGravity(0x01);
        /* default is stop bit 1 */
        stopBit = 1;

		/* data bits */
        dataSpinner = (Spinner) findViewById(R.id.dataBitValue);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
                .createFromResource(this, R.array.data_bits,
                        R.layout.my_spinner_textview);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        dataSpinner.setAdapter(dataAdapter);
        dataSpinner.setGravity(0x11);
        dataSpinner.setSelection(3);
		/* default data bit is 8 bit */
        dataBit = 8;

		/* parity */
        paritySpinner = (Spinner) findViewById(R.id.parityValue);
        ArrayAdapter<CharSequence> parityAdapter = ArrayAdapter
                .createFromResource(this, R.array.parity,
                        R.layout.my_spinner_textview);
        parityAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        paritySpinner.setAdapter(parityAdapter);
        paritySpinner.setGravity(0x11);
		/* default is none */
        parity = 0;

		/* flow control */
        flowSpinner = (Spinner) findViewById(R.id.flowControlValue);
        ArrayAdapter<CharSequence> flowAdapter = ArrayAdapter
                .createFromResource(this, R.array.flow_control,
                        R.layout.my_spinner_textview);
        flowAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        flowSpinner.setAdapter(flowAdapter);
        flowSpinner.setGravity(0x11);
		/* default flow control is is none */
        flowControl = 0;

		/* set the adapter listeners for baud */
        baudSpinner.setOnItemSelectedListener(new MyOnBaudSelectedListener());
		/* set the adapter listeners for stop bits */
        stopSpinner.setOnItemSelectedListener(new MyOnStopSelectedListener());
		/* set the adapter listeners for data bits */
        dataSpinner.setOnItemSelectedListener(new MyOnDataSelectedListener());
		/* set the adapter listeners for parity */
        paritySpinner
                .setOnItemSelectedListener(new MyOnParitySelectedListener());
		/* set the adapter listeners for flow control */
        flowSpinner.setOnItemSelectedListener(new MyOnFlowSelectedListener());

        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                readText.setText("");
            }
        });
        findViewById(R.id.toweex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(X4Activity.this, WXActivity.class));
            }
        });
        return;
    }

    public class MyOnBaudSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            baudRate = Integer.parseInt(parent.getItemAtPosition(position)
                    .toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class MyOnStopSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            stopBit = (byte) Integer.parseInt(parent
                    .getItemAtPosition(position).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public class MyOnDataSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            dataBit = (byte) Integer.parseInt(parent
                    .getItemAtPosition(position).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public class MyOnParitySelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String parityString = new String(parent.getItemAtPosition(position)
                    .toString());
            if (parityString.compareTo("None") == 0) {
                parity = 0;
            }

            if (parityString.compareTo("Odd") == 0) {
                parity = 1;
            }

            if (parityString.compareTo("Even") == 0) {
                parity = 2;
            }

            if (parityString.compareTo("Mark") == 0) {
                parity = 3;
            }

            if (parityString.compareTo("Space") == 0) {
                parity = 4;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public class MyOnFlowSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String flowString = new String(parent.getItemAtPosition(position)
                    .toString());
            if (flowString.compareTo("None") == 0) {
                flowControl = 0;
            }

            if (flowString.compareTo("CTS/RTS") == 0) {
                flowControl = 1;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private class readThread extends Thread {

        public void run() {

            byte[] buffer = new byte[4096];

            while (true) {

                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = MyApp.driver.ReadData(buffer, 4096);
                if (length > 0) {
//					String recv = toHexString(buffer, length);
                    String recv = new String(buffer, 0, length);
                    msg.obj = recv;
                    handler.sendMessage(msg);
                }
            }
        }
    }


}
