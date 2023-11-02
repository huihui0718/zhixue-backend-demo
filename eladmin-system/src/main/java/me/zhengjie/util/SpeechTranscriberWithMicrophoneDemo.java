package me.zhengjie.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * 此示例演示了：
 * ASR实时识别API调用。
 * 动态获取token。获取Token具体操作，请参见：https://help.aliyun.com/document_detail/450514.html
 * 通过本地模拟实时流发送。
 * 识别耗时计算。
 */
@Component
@RequiredArgsConstructor
public class SpeechTranscriberWithMicrophoneDemo {

    private String appKey;
    private String accessToken;
    private NlsClient client;
    private SpeechTranscriber transcriber;
    private boolean isRecording;
    private String result="";
    @Autowired
    private  WebSocketServer webSocketServer;

    public SpeechTranscriberWithMicrophoneDemo(String appKey, String id, String secret, String url) throws Exception {
        this.appKey = appKey;
        //应用全局创建一个NlsClient实例，默认服务地址为阿里云线上服务地址。
        //获取token，实际使用时注意在accessToken.getExpireTime()过期前再次获取。
        AccessToken accessToken = new AccessToken(id, secret);
        this.accessToken=accessToken.getToken();
        try {
            accessToken.apply();
            System.out.println("get token: " + ", expire time: " + accessToken.getExpireTime());
            if(url.isEmpty()) {
                client = new NlsClient(accessToken.getToken());
            }else {
                client = new NlsClient(url, accessToken.getToken());
            }
            transcriber = new SpeechTranscriber(client, getTranscriberListener());
            transcriber.setAppKey(appKey);
            // 输入音频编码方式
            transcriber.setFormat(InputFormatEnum.PCM);
            // 输入音频采样率
            transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 是否返回中间识别结果
            transcriber.setEnableIntermediateResult(true);
            // 是否生成并返回标点符号
            transcriber.setEnablePunctuation(true);
            // 是否将返回结果规整化，比如将一百返回为100
            transcriber.setEnableITN(false);
            transcriber.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpeechTranscriberListener getTranscriberListener() {
        SpeechTranscriberListener listener = new SpeechTranscriberListener() {
            //识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
                // 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                System.out.println("name: " + response.getName() +
                        //状态码 20000000 表示正常识别
                        ", status: " + response.getStatus() +
                        //句子编号，从1开始递增
                        ", index: " + response.getTransSentenceIndex() +
                        //当前的识别结果
                        ", result: " + response.getTransSentenceText() +
                        //当前已处理的音频时长，单位是毫秒
                        ", time: " + response.getTransSentenceTime());
            }

            @Override
            public void onTranscriberStart(SpeechTranscriberResponse response) {
                System.out.println("task_id: " + response.getTaskId() +
                        "name: " + response.getName() +
                        ", status: " + response.getStatus());
            }

            @Override
            public void onSentenceBegin(SpeechTranscriberResponse response) {
                System.out.println("task_id: " + response.getTaskId() +
                        "name: " + response.getName() +
                        ", status: " + response.getStatus());
            }

            //识别出一句话.服务端会智能断句,当识别到一句话结束时会返回此消息
            @Override
            public void onSentenceEnd(SpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        //状态码 20000000 表示正常识别
                        ", status: " + response.getStatus() +
                        //句子编号，从1开始递增
                        ", index: " + response.getTransSentenceIndex() +
                        //当前的识别结果
                        ", result: " + response.getTransSentenceText() +
                        //置信度
                        ", confidence: " + response.getConfidence() +
                        //开始时间
                        ", begin_time: " + response.getSentenceBeginTime() +
                        //当前已处理的音频时长，单位是毫秒
                        ", time: " + response.getTransSentenceTime());
//                webSocketServer.sendAllMessage(response.getTransSentenceText());
                result=response.getTransSentenceText();
            }

            //识别完毕
            @Override
            public void onTranscriptionComplete(SpeechTranscriberResponse response) {
                System.out.println("task_id: " + response.getTaskId() +
                        ", name: " + response.getName() +
                        ", status: " + response.getStatus());
            }

            @Override
            public void onFail(SpeechTranscriberResponse response) {
                // 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                System.out.println(
                        "task_id: " + response.getTaskId() +
                                //状态码 20000000 表示识别成功
                                ", status: " + response.getStatus() +
                                //错误信息
                                ", status_text: " + response.getStatusText());
            }
        };

        return listener;
    }

    public void startTranscribing() {
        try {
            // 开始录音
            isRecording = true;
            AudioFormat audioFormat = new AudioFormat(16000.0F, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("You can speak now!");

            int bufferSize = 3200; // 你可以根据需要调整缓冲区大小
            byte[] buffer = new byte[bufferSize];
            while (isRecording) {
                int numBytesRead = targetDataLine.read(buffer, 0, bufferSize);
                if (numBytesRead > 0) {
                    // 发送音频数据到阿里云实时语音转写服务
                    sendAudioData(buffer, numBytesRead);
                }
            }

            // 录音结束，停止并关闭音频流
            targetDataLine.stop();
            targetDataLine.close();

            // 通知服务端音频流结束
            transcriber.stop();

            // 等待结果返回并关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            client.shutdown();
        }
    }

    public void stopTranscribing() {
        isRecording = false;
    }

    private void sendAudioData(byte[] audioData, int length) {
        try {
            // 使用WebSocket发送音频数据到阿里云实时语音转写服务
            transcriber.send(audioData, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String processAudioData(byte[] audioData) {
        try {
            // 使用WebSocket发送音频数据到阿里云实时语音转写服务
            transcriber.send(audioData, audioData.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setResult(){
        result="";
    }

//    public static void main(String[] args) {
//        String appKey = "your_app_key";
//        String accessKeyId = "your_access_key_id";
//        String accessKeySecret = "your_access_key_secret";
//        String serviceUrl = ""; // 若为空，则使用默认服务地址
//
//        SpeechTranscriberWithMicrophoneDemo demo = new SpeechTranscriberWithMicrophoneDemo(appKey, accessKeyId, accessKeySecret, serviceUrl);
//        demo.startTranscribing();
//    }
}