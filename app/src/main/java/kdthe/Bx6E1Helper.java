package kdthe;

import android.app.Application;
import android.util.Log;

import j2a.awt.AwtEnv;
import onbon.bx06.Bx6GEnv;



public class Bx6E1Helper {
    public static void initBx(Application application) {
        try {
            // init java.awt
            AwtEnv.link(application);
            // enable AntiAlias or not
            // 是否启用抗锯齿
            AwtEnv.configPaintAntiAliasFlag(false);
            // 初始化六代SDK
            // init bx06 sdk
            Bx6GEnv.initial();
        } catch (Exception ex) {
            Log.e("Bx6E1Helper", "initBx: " + ex);
        }
    }
}
