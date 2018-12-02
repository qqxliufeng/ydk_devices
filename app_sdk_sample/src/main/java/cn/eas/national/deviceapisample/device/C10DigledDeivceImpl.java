package cn.eas.national.deviceapisample.device;

import android.content.Context;

import com.landicorp.android.eptapi.device.Digled;

/**
 * C10设备专用接口，用于与客屏的交互操作，只能在C10终端上使用。
 */

public abstract class C10DigledDeivceImpl extends BaseDevice {
    private Context context;
    private Digled digled = Digled.getInstance();
    private int flashStatus = 0; // 当前闪烁状态，0：金额灯亮，余额灯灭；1：金额灯灭，余额灯亮
    private Runnable flashLightRun = new Runnable() {
        @Override
        public void run() {
            flash();
        }
    };

    public C10DigledDeivceImpl(Context context) {
        this.context = context;
    }

    public void getDigledInfo() {
        // 获取当前机器存在哪些特殊指示灯
        displayInfo("=======>");
        int[] types = digled.getLightType();
        if (types != null && types.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("指示灯类型：");
            for (int type : types) {
                // 1：金额指示灯；2：余额指示灯；其他：其他指示灯
                if (type == 1) {
                    builder.append("金额指示灯, ");
                } else if (type == 2) {
                    builder.append("余额指示灯, ");
                } else {
                    builder.append("其他指示灯, ");
                }
            }
            String msg = builder.toString();
            displayInfo(msg.substring(0, msg.length() - 2));
        } else {
            displayInfo("当前机器不存在指示灯");
        }

        // 获取当前指示灯个数
        displayInfo("=======>");
        int number = digled.getLightNumber();
        displayInfo("当前指示灯个数：" + number);
        if (number > 0) {
            // 获取当前指定特殊指示灯状态，0：指示灯熄灭；1：指示灯点亮；其他：获取状态失败
            int status = digled.getLightStatus(Digled.LIGHT_AMOUT);
            displayInfo("金额指示灯状态：" + getLightStatusDesc(status));
            status = digled.getLightStatus(Digled.LIGHT_BALANCE);
            displayInfo("余额指示灯状态：" + getLightStatusDesc(status));
        }

        // 获取当前机器的数码管行数
        displayInfo("=======>");
        number = digled.getLineNumber();
        displayInfo("当前机器数码管行数：" + number);
        if (number > 0) {
            // 获取当前机器对应行号的数码管最大个数
            for (int i = 1; i <= number; i++) {
                int max = digled.getLineMax(i);
                displayInfo("行号" + i + "数码管最大个数" + (max > 0 ? "为：" + max : "获取失败"));
            }
        }
    }

    private String getLightStatusDesc(int status) {
        if (status == 0) {
            return "熄灭";
        } else if (status == 1) {
            return "点亮";
        }
        return "获取失败";
    }

    public void display() {
        int number = digled.getLineNumber();
        if (number > 0) {
            // 左对齐反向显示
            for (int i = 1; i <= number; i++) {
                boolean result = digled.dispaly(i, Digled.ALIGN_LEFT, "%0.2f", 1234.56f);
                displayInfo("显示第" + i + "行结果：" + (result ? "成功" : "失败"));
            }
            // 右对齐反向
//            for (int i = 1; i <= number; i++) {
//                boolean result = digled.dispaly(i, Digled.ALIGN_RIGHT, "%0.2f", 0.01f);
//                displayInfo("显示第" + i + "行结果：" + (result ? "成功" : "失败"));
//            }
            // 数码管段显示
//            boolean result = digled.displaySeg(1, Digled.ALIGN_LEFT, new int[] { 0x76, 0x79, 0x38, 0x38, 0x3F });
//            displayInfo("显示数码管“HELLO”字体：" + (result ? "成功" : "失败"));
        } else {
            displayInfo("数码管行数为0，无法显示");
        }
    }

    public void clear() {
        int number = digled.getLineNumber();
        if (number > 0) {
            for (int i = 1; i <= number; i++) {
                digled.clearLine(i);
            }
        }
    }

    public void flashLight() {
        int number = digled.getLightNumber();
        if (number > 0) {
            flash();
        } else {
            displayInfo("数码管无指示灯");
        }
    }

    private void flash() {
        if (flashStatus == 0) {
            digled.setLightStatus(Digled.LIGHT_AMOUT, 1);
            digled.setLightStatus(Digled.LIGHT_BALANCE, 0);
            flashStatus = 1;
        } else {
            digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
            digled.setLightStatus(Digled.LIGHT_BALANCE, 1);
            flashStatus = 0;
        }
        uiHandler.postDelayed(flashLightRun, 300);
    }

    public void stopFlash() {
        // 0：熄灭；1：点亮
        digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
        digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
        uiHandler.removeCallbacks(flashLightRun);
    }

}
