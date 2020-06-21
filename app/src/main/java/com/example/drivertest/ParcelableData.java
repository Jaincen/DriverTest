package com.example.drivertest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ParcelableData implements Parcelable {
    private DataBean cartoon;
    private static final String MSG = "MESSAGE";

    public ParcelableData(DataBean cartoon){
        Log.i(MSG, "ParcelableCartoon::ParcelableCartoon@Cartoon");
        this.cartoon = cartoon;
    }

    // 将对象写入Parcel容器中去
    // 完成对对象的序列化
    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.i(MSG, "ParcelableCartoon::writeToParcel");
        dest.writeString(cartoon.question_id);
        dest.writeString(cartoon.media_type);
        dest.writeString(cartoon.label);
        dest.writeString(cartoon.question);
        dest.writeInt(cartoon.answer);
        dest.writeString(cartoon.optionA);
        dest.writeString(cartoon.optionB);
        dest.writeString(cartoon.optionC);
        dest.writeString(cartoon.optionD);

        dest.writeInt(cartoon.media.length);
        dest.writeByteArray(cartoon.media);


    }


    // 完成对序列化的对象反序列化
    public static final Parcelable.Creator<ParcelableData> CREATOR = new Parcelable.Creator<ParcelableData>(){
        // 从Parcel容器中获取序列化的对象，并将其反序列化，得到该对象的实例
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public ParcelableData createFromParcel(Parcel source) {
            int i = Log.i(MSG, "ParcelableCartoon::Parcelable.Creator::createFromParcel");

            return new ParcelableData(source);
        }

        @Override
        public ParcelableData[] newArray(int size) {
            Log.i(MSG, "ParcelableCartoon::Parcelable.Creator::newArray");
            return new ParcelableData[size];
        }

    };

    public ParcelableData(Parcel in){
        Log.i(MSG, "ParcelableCartoon::ParcelableCartoon@Parcel");
        cartoon = new DataBean();

        cartoon.question_id = in.readString();
        cartoon.media_type = in.readString();
        cartoon.label = in.readString();
        cartoon.question = in.readString();
        cartoon.answer = in.readInt();
        cartoon.optionA = in.readString();
        cartoon.optionB = in.readString();
        cartoon.optionC = in.readString();
        cartoon.optionD = in.readString();

        int length = in.readInt();
        byte[] data = new byte[length];
        in.readByteArray(data);
        cartoon.media = data;

    }

    @Override
    public int describeContents() {
        Log.i(MSG, "ParcelableCartoon::describeContents");
        return 0;
    }
    public DataBean getDataBean(){
        return cartoon;
    }

}
