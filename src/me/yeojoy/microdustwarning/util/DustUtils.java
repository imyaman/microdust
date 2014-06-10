package me.yeojoy.microdustwarning.util;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class DustUtils implements DustConstants {

    public static final String TAG = DustUtils.class.getSimpleName();

    public static void sendNotification(Context context, STATUS[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append("미세먼지 : ").append(status[0]).append("\n");
        sb.append("초미세먼지 : ").append(status[1]).append("\n");
        sb.append("오존 : ").append(status[2]).append("\n");
        sb.append("이산화질소 : ").append(status[3]).append("\n");
        sb.append("일산화탄소 : ").append(status[4]).append("\n");
        sb.append("아황산가스 : ").append(status[5]).append("\n");
        sb.append("통합지수 : ").append(status[6]);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("환경지수")
                .setContentText(sb.subSequence(0, 20));

        NotificationCompat.BigPictureStyle style1 = new NotificationCompat.BigPictureStyle();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1);
        style1.bigLargeIcon(icon).bigPicture(picture);
        style1.setBigContentTitle("큰화면에서 보여주는 거");

        NotificationCompat.BigTextStyle style2 = new NotificationCompat.BigTextStyle();
        style2.setBigContentTitle(sb.subSequence(0, 10));
        style2.bigText(sb);

        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.noti);
        views2.setTextViewText(R.id.tv_noti_msg, sb);
        Notification noti = mBuilder.build();
        noti.bigContentView = views2;

        NotificationCompat.InboxStyle style3 = new NotificationCompat.InboxStyle();


        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.notify(100, noti);
    }
    
    
    public enum STATUS {
        GOOD, NORMAL, BAD, WORSE, WORST, NONE
    };
    
    /**
     * 받은 data로 미세먼지 확인 후 Notification으로 알려준다.
     * @param data
     */
    public static STATUS[] analyzeMicroDust(String data) {
        Log.i(TAG, "analyzeMicroDust()");
        String[] array = data.split(" ");
        // TEST DATA
        // 동네 미세먼지 초미세먼지 오존 이산화질소 일산화탄소 아황산가스 등급 지수 지수결정물질
        // 관악구 60 39 0.012 0.051 0.6 0.005 보통 85 NO2
        STATUS[] status = new STATUS[7];
        status[0] = getMicroDustDegree(array[1]);
        status[1] = getNanoDustDegree(array[2]);
        status[2] = getOzonDegree(array[3]);
        status[3] = getNO2Degree(array[4]);
        status[4] = getCODegree(array[5]);
        status[5] = getSO2Degree(array[6]);
        status[6] = getTotalDegree(array[8]);

        return status;
    }
    
    /**
     * 미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getMicroDustDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float microDustValue = Float.parseFloat(value);
        
        if (microDustValue > MICRO_DUST_WORSE)
            return STATUS.WORST;
        else if (microDustValue > MICRO_DUST_BAD)
            return STATUS.WORSE;
        else if (microDustValue > MICRO_DUST_NORMAL)
            return STATUS.BAD;
        else if (microDustValue > MICRO_DUST_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 초미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getNanoDustDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float microDustValue = Float.parseFloat(value);
        
        if (microDustValue > NANO_DUST_WORSE)
            return STATUS.WORST;
        else if (microDustValue > NANO_DUST_BAD)
            return STATUS.WORSE;
        else if (microDustValue > NANO_DUST_NORMAL)
            return STATUS.BAD;
        else if (microDustValue > NANO_DUST_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 오존 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getOzonDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float ozonValue = Float.parseFloat(value);
        
        if (ozonValue > O3_WORSE)
            return STATUS.WORST;
        else if (ozonValue > O3_BAD)
            return STATUS.WORSE;
        else if (ozonValue > O3_NORMAL)
            return STATUS.BAD;
        else if (ozonValue > O3_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 이산화질소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getNO2Degree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float no2Value = Float.parseFloat(value);
        
        if (no2Value > NO2_WORSE)
            return STATUS.WORST;
        else if (no2Value > NO2_BAD)
            return STATUS.WORSE;
        else if (no2Value > NO2_NORMAL)
            return STATUS.BAD;
        else if (no2Value > NO2_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 일산화탄소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getCODegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float coValue = Float.parseFloat(value);
        
        if (coValue > CO_WORSE)
            return STATUS.WORST;
        else if (coValue > CO_BAD)
            return STATUS.WORSE;
        else if (coValue > CO_NORMAL)
            return STATUS.BAD;
        else if (coValue > CO_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 아황산가스 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getSO2Degree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float so2Value = Float.parseFloat(value);
        
        if (so2Value > SO2_WORSE)
            return STATUS.WORST;
        else if (so2Value > SO2_BAD)
            return STATUS.WORSE;
        else if (so2Value > SO2_NORMAL)
            return STATUS.BAD;
        else if (so2Value > SO2_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 통합대기지수가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getTotalDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;
        
        float totalValue = Float.parseFloat(value);
        
        if (totalValue > TOTAL_DEGREE_WORSE)
            return STATUS.WORST;
        else if (totalValue > TOTAL_DEGREE_BAD)
            return STATUS.WORSE;
        else if (totalValue > TOTAL_DEGREE_NORMAL)
            return STATUS.BAD;
        else if (totalValue > TOTAL_DEGREE_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
}