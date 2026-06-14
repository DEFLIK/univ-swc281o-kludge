package com.deflik.univswc281ocrutch.services.decompiled;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

// Частично декомпилированный и отрефакторенный класс из внутренних либ changan (incall)
public class PersonalCenterManager {
    // ?, customButtonDataLocationId, muteButton, ?
    public static boolean saveOtherAppInfoByOther(IBinder service, int i, String dataLocationId, String dataValue, String str3) throws RemoteException {
        // saveOtherAppInfoByOther(3, 589824, "0x1003", "") mute
        var parcelObtain = Parcel.obtain();
        var parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("com.incall.apps.commoninterface.userservice.IPersonalService");
            parcelObtain.writeInt(i);
            parcelObtain.writeString(dataLocationId);
            parcelObtain.writeString(dataValue);
            parcelObtain.writeString(str3);
            service.transact(16, parcelObtain, parcelObtain2, 0);
            parcelObtain2.readException();
            return parcelObtain2.readInt() != 0;
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }
}
