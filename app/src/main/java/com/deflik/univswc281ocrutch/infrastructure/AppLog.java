package com.deflik.univswc281ocrutch.infrastructure;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppLog {
    public static final String LogTag = "UnivKludge";
    private static final Queue<String> logBuffer = new ConcurrentLinkedQueue<>();
    private static WeakReference<TextView> targetTextViewRef = new WeakReference<>(null);
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void i(String message) {
        log("INFO", message);
    }

    public static void e(String message) {
        log("ERROR", message);
    }

    public static void log(String prefix, String message) {
        var timestamp = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        final var logMessage = "[" + timestamp.format(new Date()) + "] " + prefix + " " + message + "\n";
        var textView = targetTextViewRef.get();

        Log.i(LogTag, logMessage);
        logBuffer.add(logMessage);
        if (textView != null)
            mainHandler.post(() -> textView.append(logMessage));
    }

    public static void bindTextView(TextView textView) {
        targetTextViewRef = new WeakReference<>(textView);

        var builder = new StringBuilder();
        String log;
        while ((log = logBuffer.poll()) != null)
            builder.append(log);

        if (builder.length() > 0)
            mainHandler.post(() -> textView.setText(builder.toString()));
    }

    public static void unbindTextView() {
        targetTextViewRef.clear();
    }
}

