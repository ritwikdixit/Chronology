package com.ritwik.madfbla201415;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Soham on 4/13/2015.
 */
public class DataStorage {
    public static void write(Object o, String filename, Context c){
        ObjectOutputStream out = null;
        FileOutputStream fos = null;
        try {
            fos = c.openFileOutput(filename, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null)
                    out.close();
                if (fos != null)
                    fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static Object read (String filename, Context c){
        ObjectInputStream out = null;
        FileInputStream fos = null;
        Object x = null;
        try {
            fos = c.openFileInput(filename);
            out = new ObjectInputStream(fos);
            x = out.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null)
                    out.close();
                if (fos != null)
                    fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return x;
    }
}
