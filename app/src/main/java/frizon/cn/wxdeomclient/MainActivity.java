package frizon.cn.wxdeomclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private static final int SHOW_RESPONSE = 0;

    private EditText mReqMessage;
    private Button mSend;
    private TextView mRespMessage;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    mRespMessage.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReqMessage = (EditText) findViewById(R.id.req_message);
        mSend = (Button) findViewById(R.id.send);
        mRespMessage = (TextView) findViewById(R.id.resp_message);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReqMessage();
            }
        });

    }

    private void sendReqMessage() {
        if (mReqMessage != null && !mReqMessage.getText().toString().equals("")) {
            //发送数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL("http://www.baidu.com");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();
                        Log.e("WXText","connection");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            builder.append(line);
                        }
                        Log.e("WXText",builder.toString());
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = builder.toString();
                        handler.sendMessage(message);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(connection != null){
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }
    }
}
