package com.deflik.univswc281ocrutch.services.decompiled;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

// Частично декомпилированный и отрефакторенный класс из внутренних либ changan (incall)
public class PersonalCenterManager {
    private final static String PersonalSettingsDescriptor = "com.incall.apps.commoninterface.userservice.IPersonalService";

    public static boolean saveOtherAppInfoByOther(IBinder service, int i, String dataLocationId, String str2, String str3) throws RemoteException {
        var parcelObtain = Parcel.obtain();
        var parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken(PersonalSettingsDescriptor);
            parcelObtain.writeInt(i);
            parcelObtain.writeString(dataLocationId);
            parcelObtain.writeString(str2);
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
