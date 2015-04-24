package com.appmodel.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.duowan.mobile.utils.YLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Persistence<T> {

    private String folder = "persistence";
    private T data;
    private String mName;
    private Application application;
    private Handler handler;

    public interface OnLoadComplete {
        void onPersistence(Persistence persistence);
    }

    public Persistence(Application context, Handler handler, final OnLoadComplete listener, final String name, final Class clazz) {
        this.application = context;
        this.handler = handler;
        mName = name;
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    File dir = application.getDir(folder, Context.MODE_PRIVATE);
                    File file = new File(dir, mName);
                    YLog.debug(this, "Persistence start loading %s", file.getAbsolutePath());
                    ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
                    PersistenceData<T> persistence = (PersistenceData<T>) objIn.readObject();
                    objIn.close();
                    data = persistence.data;
                    YLog.debug(this, "Persistence init success: %s", persistence.data);
                } catch (FileNotFoundException e1) {
                    YLog.debug(this, "File not found for persistence: %s", name);
                } catch (Exception e) {
                    YLog.error(this, e);
                    try {
                        data = (T) clazz.newInstance();
                    } catch (Exception e1) {
                        YLog.error(this, "can not newInstance", e1);
                    }
                }
                listener.onPersistence(Persistence.this);
            }
        });

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public void save() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                PersistenceData persistence = new PersistenceData();
                persistence.data = data;
                File dir = application.getDir(folder, Context.MODE_PRIVATE);
                File file = new File(dir, mName);
                ObjectOutputStream objOut;
                try {
                    objOut = new ObjectOutputStream(new FileOutputStream(file));
                } catch (IOException e) {
                    YLog.error(this, "IOException", e);
                    return;
                }
                try {
                    objOut.writeObject(persistence);
                    YLog.debug(this, "PersistenceData Map write success: %s", persistence.data);
                } catch (Exception e) {
                    YLog.error(this, e);
                } finally {
                    IOUtils.closeQuietly(objOut);
                }
            }
        });

    }

    public static class PersistenceData<T> implements Serializable {
        public T data;
        public Date time = new Date();

        public PersistenceData() {

        }
    }


}
