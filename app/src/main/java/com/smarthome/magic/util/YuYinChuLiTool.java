package com.smarthome.magic.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.smarthome.magic.R;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.inter.YuYinInter;
import com.smarthome.magic.model.ResultModel;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.smarthome.magic.activity.shuinuan.Y.getResources;
import static com.smarthome.magic.activity.shuinuan.Y.getString;
import static com.smarthome.magic.config.MyApplication.CAR_CTROL;

public class YuYinChuLiTool {
    private String TAG = YuYinChuLiTool.class.getSimpleName();
    private Context context;
    private VoiceWakeuper mIvw;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    private int curThresh = 1450;
    private String threshStr = "门限值：";
    private String keep_alive = "1";
    private String ivwNetMode = "0";
    // 唤醒结果内容
    private String resultString;
    ResultModel resultModel;
    String chazuo = "";
    String caozuo = "";
    String weizhi = "";
    String shebei = "";

    //当前状态，取值参考Constant中STATE定义
    private int mState;
    private int mAIUIState = AIUIConstant.STATE_IDLE;


    private AIUIAgent mAIUIAgent = null;
    private String mSyncSid = "";

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + getString(R.string.app_id) + ".jet");
        Log.d(TAG, "resPath: " + resPath);
        return resPath;
    }


    public YuYinChuLiTool(Context context) {
        this.context = context;
        mIvw = VoiceWakeuper.createWakeuper(context, null);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
    }

    YuYinInter yuYinInter = null;

    //唤醒功能
    public void beginWakeUp(YuYinInter yuYinInter) {
        this.yuYinInter = yuYinInter;

        //   非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            //setRadioEnable(false);
            resultString = "";
            // textView.setText(resultString);

            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
            // 启动唤醒
            /*	mIvw.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");*/

            mIvw.startListening(mWakeuperListener);
				/*File file = new File(Environment.getExternalStorageDirectory().getPath() + "/msc/ivw1.wav");
				byte[] byetsFromFile = getByetsFromFile(file);
				mIvw.writeAudio(byetsFromFile,0,byetsFromFile.length);*/
            //	mIvw.stopListening();

        } else {
            // showTip("唤醒未初始化");
            UIHelper.ToastMessage(context, "唤醒未初始化");
        }
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");

            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private void showTip(final String str) {
        UIHelper.ToastMessage(context, str);
    }

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if (!"1".equalsIgnoreCase(keep_alive)) {
                //  setRadioEnable(true);
            }
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }
            //textView.setText(resultString);
            Log.i("RESULT_STRING", resultString);
            mIvw.stopListening();

            yuYinInter.showMianBan();

            int code = mTts.startSpeaking("请说", mTtsListener);
//            Intent nlpIntent = new Intent(getActivity(), NlpDemo.class);
//            startActivity(nlpIntent);

            //激活唤醒，开启语音识别
            createAgent();
            startVoiceNlp();
        }


        @Override
        public void onError(SpeechError error) {
            //showTip(error.getPlainDescription(true));
            UIHelper.ToastMessage(context, error.getPlainDescription(true));
            // setRadioEnable(true);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    Log.i(TAG, "ivw audio length: " + audio.length);
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
            Log.d(TAG, "开始播放：" + System.currentTimeMillis());
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
//            // 合成进度
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                    yuYinInter.dismissMianBan();
                    mIvw.startListening(mWakeuperListener);
                    stopVoiceNlp();
                }

            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                Log.d(TAG, "session id =" + sid);
            }

            //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
        }
    };

    private void startVoiceNlp() {
        if (null == mAIUIAgent) {
            showTip("AIUIAgent为空，请先创建");
            return;
        }

        Log.i(TAG, "start voice nlp");
        //mNlpText.setText("");

        // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
        // 默认为oneshot模式，即一次唤醒后就进入休眠。可以修改aiui_phone.cfg中speech参数的interact_mode为continuous以支持持续交互
        if (AIUIConstant.STATE_WORKING != mAIUIState) {
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

        // 打开AIUI内部录音机，开始录音。若要使用上传的个性化资源增强识别效果，则在参数中添加pers_param设置
        // 个性化资源使用方法可参见http://doc.xfyun.cn/aiui_mobile/的用户个性化章节
        // 在输入参数中设置tag，则对应结果中也将携带该tag，可用于关联输入输出
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage startRecord = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null);

        mAIUIAgent.sendMessage(startRecord);
    }

    private void stopVoiceNlp() {
        if (null == mAIUIAgent) {
            showTip("AIUIAgent 为空，请先创建");
            return;
        }

        Log.i(TAG, "stop voice nlp");
        // 停止录音
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage stopRecord = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, params, null);

        mAIUIAgent.sendMessage(stopRecord);
    }

    private void createAgent() {
        if (null == mAIUIAgent) {
            Log.i(TAG, "create aiui agent");

            mAIUIAgent = AIUIAgent.createAgent(context, getAIUIParams(), mAIUIListener);
        }

        if (null == mAIUIAgent) {
            final String strErrorTip = "创建AIUIAgent失败！";
            showTip(strErrorTip);

            //  mNlpText.setText(strErrorTip);
        } else {
            showTip("AIUIAgent已创建");
        }
    }

    private String getAIUIParams() {
        String params = "";

        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);

            JSONObject paramsJson = new JSONObject(params);

            params = paramsJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            Log.i(TAG, "on event: " + event.eventType);

            switch (event.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    showTip("已连接服务器");
                    break;

                case AIUIConstant.EVENT_SERVER_DISCONNECTED:
                    showTip("与服务器断连");
                    break;

                case AIUIConstant.EVENT_WAKEUP:
                    showTip("进入识别状态");
                    break;

                case AIUIConstant.EVENT_RESULT: {
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");

                            // 获取该路会话的id，将其提供给支持人员，有助于问题排查
                            // 也可以从Json结果中看到
                            String sid = event.data.getString("sid");
                            String tag = event.data.getString("tag");

                            showTip("tag=" + tag);

                            // 获取从数据发送完到获取结果的耗时，单位：ms
                            // 也可以通过键名"bos_rslt"获取从开始发送数据到获取结果的耗时
                            long eosRsltTime = event.data.getLong("eos_rslt", -1);
                            // mTimeSpentText.setText(eosRsltTime + "ms");

                            if (TextUtils.isEmpty(cntStr)) {
                                return;
                            }

                            JSONObject cntJson = new JSONObject(cntStr);

                            // if (mNlpText.getLineCount() > 1000) {
                            //     mNlpText.setText("");
                            // }

                            // mNlpText.append("\n");
                            // mNlpText.append(cntJson.toString());
                            // mNlpText.setSelection(mNlpText.getText().length());

                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                String resultStr = cntJson.optString("intent");
                                Log.i(TAG, "语义结果: " + resultStr);

                                resultModel = new Gson().fromJson(resultStr, ResultModel.class);
                                // 设置参数
                                setParam();
                                if (resultModel != null) {
                                    if (resultModel.getAnswer() != null) {
                                        ResultModel.SemanticBean semanticBean = resultModel.getSemantic().get(0);
                                        yuYinInter.yuYinResult(resultModel.getText());
                                        int code = mTts.startSpeaking(resultModel.getVoice_answer().get(0).getContent(), mTtsListener);
                                        Log.i("语义结果", "code" + code);
                                        String topic = "zn/server/1/aaaaaaaaaaaaaaaa90140018";
                                        YuYinMqtt yuYinMqtt = new YuYinMqtt(context, topic);
                                        yuYinMqtt.subscribeMingLing();

                                        /**
                                         *     /**
                                         *      * answer : {"text":"正在为您操作","type":"T"}
                                         *      * category : OS8569425439.ceshi
                                         *      * data : {"result":null}
                                         *      * intentType : custom
                                         *      * rc : 0
                                         *      * semantic : [{"entrypoint":"ent","hazard":false,"intent":"dengl","score":1,"slots":[{"begin":0,"end":2,"name":"caozuo","normValue":"打开","value":"打开"},{"begin":2,"end":4,"name":"weizhi","normValue":"客厅","value":"客厅"},{"begin":4,"end":5,"name":"shebei","normValue":"灯","value":"灯"}],"template":"{caozuo}{weizhi}{shebei}"}]
                                         *      * semanticType : 0
                                         *      * service : OS8569425439.ceshi
                                         *      * sessionIsEnd : false
                                         *      * shouldEndSession : true
                                         *      * sid : atn04b05556@dx00011341fd48a11100
                                         *      * state : null
                                         *      * text : 打开客厅灯
                                         *      * uuid : atn04b05556@dx00011341fd48a11100
                                         *      * vendor : OS8569425439
                                         *      * version : 7.0
                                         *      * voice_answer : [{"content":"正在为您操作","type":"TTS"}]
                                         *      */

                                        String msg = "";
                                        switch (resultModel.getSemantic().get(0).getIntent()) {
                                            case "dengl":

                                                for (int i = 0; i < semanticBean.getSlots().size(); i++) {
                                                    if (semanticBean.getSlots().get(i).getName().equals("caozuo")) {
                                                        caozuo = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("shebei")) {
                                                        shebei = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("weizhi")) {
                                                        weizhi = semanticBean.getSlots().get(i).getNormValue();
                                                    }
                                                }
                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    msg = "j{'intentName':" + "actionByRoom" + ",'operate':" + caozuo + ",'device':" + shebei + ",'room':" + weizhi + "}.";
                                                    yuYinMqtt.pushMingLing(msg);
                                                    caozuo = "";
                                                    shebei = "";
                                                    weizhi = "";

                                                }

                                                Log.i("语义结果", msg);
                                                break;
                                            case "weiy":
                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    String circle = "0" + semanticBean.getSlots().get(0).getNormValue() + "c";
                                                    msg = "j{'intentName':" + "feedingFish" + ",'operate':" + "打开" + ",'device':" + "喂鱼" + ",'room':" + "客厅" + ",'time':" + circle + "}.";
                                                    Log.i("语义结果", msg);
                                                    yuYinMqtt.pushMingLing(msg);
                                                }

                                                break;
                                            case "dkcl":

                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    String caozuo = semanticBean.getSlots().get(0).getNormValue();
                                                    msg = "j{'intentName':actionByRoom,'operate':" + caozuo + ",'device':窗帘电机,'room':客厅}.";
                                                    Log.i("语义结果", msg);
                                                    yuYinMqtt.pushMingLing(msg);
                                                }
                                                break;

                                            case "dkcz":

                                                for (int i = 0; i < semanticBean.getSlots().size(); i++) {

                                                    if (semanticBean.getSlots().get(i).getName().equals("caozuo")) {
                                                        caozuo = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("chazuo")) {
                                                        chazuo = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("weizhi")) {
                                                        weizhi = semanticBean.getSlots().get(i).getNormValue();
                                                    }
                                                }

                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    // Log.i("语义结果", "caozuo:" + caozuo + "插座:" + chazuo + "位置:" + weizhi);
                                                    String msg1 = "j{'intentName':actionByRoom,'operate':" + caozuo + ",'device':插座,'room':" + weizhi + "}.";
                                                    Log.i("语义结果", msg1);
                                                    yuYinMqtt.pushMingLing(msg1);
                                                    caozuo = "";
                                                    chazuo = "";
                                                    weizhi = "";
                                                }
                                                break;

                                            case "jiaohwy":

                                                for (int i = 0; i < semanticBean.getSlots().size(); i++) {

                                                    if (semanticBean.getSlots().get(i).getName().equals("caozuo")) {
                                                        caozuo = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("chazuo")) {
                                                        shebei = semanticBean.getSlots().get(i).getNormValue();
                                                    }
                                                }

                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    // Log.i("语义结果", "caozuo:" + caozuo + "插座:" + chazuo + "位置:" + weizhi);
                                                    String msg1 = "j{'intentName':actionByRoom,'operate':" + caozuo + ",'device':灯,'room':" + "客厅" + "}.";
                                                    Log.i("语义结果", msg1);
                                                    yuYinMqtt.pushMingLing(msg1);
                                                    caozuo = "";
                                                    shebei = "";
                                                }
                                                break;
                                            case "zhuckt":
                                                for (int i = 0; i < semanticBean.getSlots().size(); i++) {

                                                    if (semanticBean.getSlots().get(i).getName().equals("caozuo")) {
                                                        caozuo = semanticBean.getSlots().get(i).getNormValue();
                                                    } else if (semanticBean.getSlots().get(i).getName().equals("kt")) {
                                                        shebei = semanticBean.getSlots().get(i).getNormValue();
                                                    }
                                                }

                                                if (resultModel.getAnswer().getText().equals("正在为您操作")) {
                                                    // Log.i("语义结果", "caozuo:" + caozuo + "插座:" + chazuo + "位置:" + weizhi);
                                                    String msg1 = "j{'intentName':actionByRoom,'operate':" + caozuo + ",'device':空调,'room':" + "客厅" + "}.";
                                                    Log.i("语义结果", msg1);
                                                    yuYinMqtt.pushMingLing(msg1);
                                                    caozuo = "";
                                                    shebei = "";
                                                }
                                                break;

                                            default:
                                                throw new IllegalStateException("Unexpected value: " + resultModel.getSemantic().get(0).getIntent());
                                        }


                                    }
                                }

                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        // mNlpText.append("\n");
                        // mNlpText.append(e.getLocalizedMessage());
                    }

                    // mNlpText.append("\n");
                }
                break;

                case AIUIConstant.EVENT_ERROR: {
                    // mNlpText.append("\n");
                    // mNlpText.append("错误: " + event.arg1 + "\n" + event.info);
                    Log.i(TAG, "ERROR: " + event.arg1 + "\n" + event.info);
                }
                break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        showTip("找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        showTip("找到vad_eos");
                    } else {
                        //  showTip("" + event.arg2);
                    }
                }
                break;

                case AIUIConstant.EVENT_START_RECORD: {
                    showTip("已开始录音");
                }
                break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    showTip("已停止录音");
                }
                break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    mAIUIState = event.arg1;

                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
                        showTip("STATE_IDLE");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
                        showTip("STATE_READY");
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
                        showTip("STATE_WORKING");
                    }
                }
                break;

                case AIUIConstant.EVENT_CMD_RETURN: {
                    if (AIUIConstant.CMD_SYNC == event.arg1) {    // 数据同步的返回
                        int dtype = event.data.getInt("sync_dtype", -1);
                        int retCode = event.arg2;

                        switch (dtype) {
                            case AIUIConstant.SYNC_DATA_SCHEMA: {
                                if (AIUIConstant.SUCCESS == retCode) {
                                    // 上传成功，记录上传会话的sid，以用于查询数据打包状态
                                    // 注：上传成功并不表示数据打包成功，打包成功与否应以同步状态查询结果为准，数据只有打包成功后才能正常使用
                                    mSyncSid = event.data.getString("sid");

                                    // 获取上传调用时设置的自定义tag
                                    String tag = event.data.getString("tag");

                                    // 获取上传调用耗时，单位：ms
                                    long timeSpent = event.data.getLong("time_spent", -1);
                                    if (-1 != timeSpent) {
                                        //mTimeSpentText.setText(timeSpent + "ms");
                                        Log.i(TAG, "调用耗时" + timeSpent);
                                    }

                                    showTip("上传成功，sid=" + mSyncSid + "，tag=" + tag + "，你可以试着说“打电话给刘德华”");
                                } else {
                                    mSyncSid = "";
                                    showTip("上传失败，错误码：" + retCode);
                                }
                            }
                            break;
                        }
                    } else if (AIUIConstant.CMD_QUERY_SYNC_STATUS == event.arg1) {    // 数据同步状态查询的返回
                        // 获取同步类型
                        int syncType = event.data.getInt("sync_dtype", -1);
                        if (AIUIConstant.SYNC_DATA_QUERY == syncType) {
                            // 若是同步数据查询，则获取查询结果，结果中error字段为0则表示上传数据打包成功，否则为错误码
                            String result = event.data.getString("result");

                            showTip(result);
                        }
                    }
                }
                break;

                default:
                    break;
            }
        }

    };


    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认云端发音人
    public static String voicerCloud = "xiaoyan";
    // 默认本地发音人
    public static String voicerLocal = "xiaoyan";
    public static String voicerXtts = "xiaoyan";

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);

        } else if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerXtts);
        }
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");


    }


    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        String type = "tts";
        if (mEngineType.equals(SpeechConstant.TYPE_XTTS)) {
            type = "xtts";
        }
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        if (mEngineType.equals(SpeechConstant.TYPE_XTTS)) {
            tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voicerXtts + ".jet"));
        } else {
            tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voicerLocal + ".jet"));
        }

        return tempBuffer.toString();
    }

    private void setInputState(int state) {
        mState = state;
    }

    public void closeMianBan() {
        mIvw.startListening(mWakeuperListener);
        stopVoiceNlp();
    }

    // 打开语音助手5秒 没有接收到语音关闭语音助手页面
    // 一次交互完成后，5秒没有接收到语音，关闭语音助手

}
