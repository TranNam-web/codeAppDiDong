package com.example.doandidong.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.doandidong.R;
import com.example.doandidong.activity.MainActivity;

import java.util.Map;

public class FirebaseMessagerReceiver extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String title = null, body = null;

        // 1. Thử lấy data payload trước
        Map<String, String> data = message.getData();
        if (data != null) {
            title = data.get("title");
            body  = data.get("body");
        }

        // 2. Nếu không có data, fallback sang notification payload
        if ((title == null || body == null) && message.getNotification() != null) {
            title = message.getNotification().getTitle();
            body  = message.getNotification().getBody();
        }

        // 3. Chỉ hiển thị khi đã có đủ title & body
        if (title != null && body != null) {
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        // Tạo Intent mở MainActivity khi nhấn notification
        Intent intent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId   = "default_channel_id";
        String channelName = "Thông báo chung";

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Tạo channel nếu cần (Oreo+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (nm.getNotificationChannel(channelId) == null) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Kênh thông báo chung");
                nm.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_mobile_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 250, 250, 250})
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Dùng timestamp làm ID để tránh ghi đè
        nm.notify((int) System.currentTimeMillis(), builder.build());
    }
}
