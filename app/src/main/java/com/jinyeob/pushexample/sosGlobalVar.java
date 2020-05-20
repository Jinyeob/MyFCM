package com.jinyeob.pushexample;

public class sosGlobalVar {
    private static boolean sos = false;
    static int workValue = 0;
    static int current = 0;

    public static boolean getSos() {
        return sos;
    }

    public static void setSos(boolean sos) {
        sosGlobalVar.sos = sos;
    }

    public static int getWorkValue() {
        return workValue;
    }

    public static void setWorkValue(int workValue) {
        sosGlobalVar.workValue = workValue;
    }

    public static int getCurrent() {
        return current;
    }

    public static void setCurrent(int current) {
        sosGlobalVar.current = current;
    }
}
