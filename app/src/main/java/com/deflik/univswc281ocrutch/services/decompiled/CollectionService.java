package com.deflik.univswc281ocrutch.services.decompiled;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.google.gson.JsonObject;

// Частично декомпилированный и отрефакторенный класс из внутренних либ changan (incall)
public class CollectionService {
    public static boolean uploadData(IBinder service, String dataLocationId, int dataValue) throws RemoteException {
        // customButtonId, muteId
        // upload("1406", "4")
        var timestamp = Long.toString(System.currentTimeMillis() / 1000);
        var jsonObject = new JsonObject();
        jsonObject.addProperty("eid", dataLocationId);
        jsonObject.addProperty("ts", timestamp);
        jsonObject.addProperty("dt", "vehiclesetting");
        jsonObject.addProperty("dtn", "ca");
        jsonObject.addProperty("vn", "V.1.0.23_CD569LOG");
        jsonObject.addProperty("sc", Integer.valueOf(dataValue));
        var jsonData = jsonObject.toString();

        return uploadBehavior(service, "vehiclesetting", jsonData);
    }

    private static boolean uploadBehavior(IBinder service, String serviceName, String serviceData) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        Parcel parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("com.incall.apps.commoninterface.behavior.ICollectService");
            parcelObtain.writeString(serviceName);
            parcelObtain.writeString(serviceData);
            service.transact(1, parcelObtain, parcelObtain2, 0);
            parcelObtain2.readException();
            return parcelObtain2.readInt() != 0;
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }
}
