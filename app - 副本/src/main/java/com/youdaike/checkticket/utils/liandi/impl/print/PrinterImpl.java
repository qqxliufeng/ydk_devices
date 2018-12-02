package com.youdaike.checkticket.utils.liandi.impl.print;

import android.content.Context;
import android.support.annotation.NonNull;

import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.QrCode;
import com.youdaike.checkticket.utils.liandi.data.PrintConstants;
import com.youdaike.checkticket.utils.liandi.data.PrinterError;
import com.youdaike.checkticket.utils.liandi.impl.camera.BaseDevice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import static com.landicorp.android.eptapi.utils.QrCode.ECLEVEL_Q;

/**
 * 针对无打印机终端需使用外接打印机，如蓝牙打印机等。该示例针对内置打印机。
 */

public abstract class PrinterImpl extends BaseDevice {
    private Printer.Progress progress;
    private List<Printer.Step> stepList;
    private Context context;

    public PrinterImpl(Context context) {
        this.context = context;
    }

    public int getPrinterStatus() {
        try {
            int status = Printer.getInstance().getStatus();
            return status;
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return PrinterError.FAIL;
    }

    public void init() {
        stepList = new ArrayList<Printer.Step>();
    }

    /**
     * 重复打印
     *
     * @return
     */
    public boolean addTextType2(String json) {
        checkList();
        try {
            final JSONObject object = new JSONObject(json);
            if (!"200".equals(object.optString("status"))) {
                return false;
            }
            stepList.add(new Printer.Step() {
                @Override
                public void doPrint(Printer printer) throws Exception {
                    printer.setAutoTrunc(false);
                    Format format = new Format();

                    getTitleFormat(printer, format);
                    printer.printMid("优待客票务【重复打印】\n");
                    printer.printMid("(请核实此订单是否重复使用)\n");
                    printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");

                    Printer.Alignment alignment = getContentAlignment(printer, format);
                    JSONObject data = object.optJSONObject("data");
                    Iterator<String> keys = data.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = data.optString(key);
                        printer.printText(alignment, key + " : " + value + "\n");
                    }
                    printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");
                    printer.printText(alignment, "温馨提示:\n");
                    printer.printText(alignment, "1.请核实凭证号是否重复使用\n");
                    printer.printText(alignment, "2.此凭证相关人员签字后有效\n");
                    printer.printText(alignment, "客服电话:0531-85333222\n");
                    printer.printText(alignment, "景区工作人员签字：\n");
                    printer.printText(alignment, "客户签字：\n");
                    printer.printText(alignment, "客户电话：\n");
                    addEndSpaceLine(printer, alignment);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @NonNull
    private void getTitleFormat(Printer printer, Format format) throws Exception {
        format.setAscScale(Format.ASC_SC1x1);
        format.setAscSize(Format.ASC_DOT24x12);
        format.setHzScale(Format.HZ_SC1x1);
        format.setHzSize(Format.HZ_DOT32x24);
        printer.setFormat(format);
    }

    @NonNull
    private Printer.Alignment getContentAlignment(Printer printer, Format format) throws Exception {
        format.setAscScale(Format.ASC_SC1x1);
        format.setAscSize(Format.ASC_DOT24x12);
        format.setHzScale(Format.HZ_SC1x1);
        format.setHzSize(Format.HZ_DOT24x24);
        format.setYSpace(20);
        printer.setFormat(format);
        return Printer.Alignment.LEFT;
    }

    /**
     * 统计报表
     *
     * @return
     */
    public boolean addTextType4(String json) {
        checkList();
        try {
            final JSONObject object = new JSONObject(json);
            if (!"200".equals(object.optString("status"))) {
                return false;
            }
            stepList.add(new Printer.Step() {
                @Override
                public void doPrint(Printer printer) throws Exception {
                    printer.setAutoTrunc(false);
                    Format format = new Format();

                    getTitleFormat(printer, format);
                    printer.printMid("优待客票务【统计报表】\n");

                    Printer.Alignment alignment = getContentAlignment(printer, format);
                    JSONArray data = object.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject itemObj = data.optJSONObject(i);
                            printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");
                            Iterator<String> keys = itemObj.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = itemObj.optString(key);
                                switch (key) {
                                    case "date":
                                        key = "时间";
                                        break;
                                    case "title":
                                        key = "名称";
                                        break;
                                    case "num":
                                        key = "数量";
                                        break;
                                }
                                printer.printText(alignment, key + ":" + value + "\n");
                            }
                            printer.printText(alignment, "打印时间 : " + getTime() + "\n");
                            addEndSpaceLine(printer, alignment);
                        }
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * //验证时打印【商家凭证】
     *
     * @return
     */
    public boolean addTextType1(String json) {
        checkList();
        try {
            final JSONObject object = new JSONObject(json);
            if (!"200".equals(object.optString("status"))) {
                return false;
            }
            stepList.add(new Printer.Step() {
                @Override
                public void doPrint(Printer printer) throws Exception {
                    printer.setAutoTrunc(false);
                    Format format = new Format();

                    getTitleFormat(printer, format);
                    printer.printMid("优待客票务【商家凭证】\n");
                    printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");

                    Printer.Alignment alignment = getContentAlignment(printer, format);

                    JSONObject data = object.optJSONObject("data");
                    Iterator<String> keys = data.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = data.optString(key);
                        if ("手机".equals(key)) {
                            if (value != null && value.trim().length() == 11) {
                                value = value.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            }
                        }
                        printer.printText(alignment, key + " : " + value + "\n");
                    }
                    printer.printText(alignment, "打印时间 : " + getTime() + "\n");
                    printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");
                    printer.printText(alignment, "温馨提示:" + "\n");
                    printer.printText(alignment, "1.一经打印，不可退改" + "\n");
                    printer.printText(alignment, "2.验证当天有效，过期作废" + "\n");
                    printer.printText(alignment, "客服电话:0531-85333222" + "\n");
                    addEndSpaceLine(printer, alignment);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 凭证查询打印【凭证查询】
     *
     * @return
     */
    public boolean addTextType5(String json) {
        checkList();
        try {
            final JSONArray dataArray = new JSONArray(json);
            if (dataArray.length() <= 0) {
                return false;
            }
            stepList.add(new Printer.Step() {
                @Override
                public void doPrint(Printer printer) throws Exception {
                    printer.setAutoTrunc(false);
                    Format format = new Format();

                    getTitleFormat(printer, format);
                    printer.printMid("优待客票务【凭证查询】\n");
                    printer.printMid("(此凭证不能作为入园凭证使用)\n");
                    printer.printMid(PrintConstants.SEPARATOR_LINE + "\n");

                    Printer.Alignment alignment = getContentAlignment(printer, format);
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj_2 = dataArray.optJSONObject(i);
                        Iterator iterator_2 = dataObj_2.keys();
                        HashMap<Integer, String> sortValue = new HashMap<>();//重新排序
                        while (iterator_2.hasNext()) {
                            String key = iterator_2.next().toString();
                            String value = dataObj_2.optString(key);
                            switch (key) {
                                case "prname":
                                    key = "商家名称";
                                    sortValue.put(0, (key + ":" + value));
                                    break;
                                case "teamtitle":
                                    key = "产品名称";
                                    sortValue.put(1, (key + ":" + value));
                                    break;
                                case "fxs":
                                    key = "分销商";
                                    sortValue.put(2, (key + ":" + value));
                                    break;
                                case "couponno":
                                    key = "凭证号";
                                    sortValue.put(3, (key + ":" + value));
                                    break;
                                case "couponnum":
                                    key = "消费数量";
                                    sortValue.put(4, (key + ":" + value));
                                    break;
                                case "orderlink":
                                    key = "联系人";
                                    sortValue.put(5, (key + ":" + value));
                                    break;
                                case "ordermoblie":
                                    key = "手机";
                                    if (value != null && value.trim().length() == 11) {
                                        value = value.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                                    }
                                    sortValue.put(6, (key + ":" + value));
                                    break;
                                case "ordertime":
                                    key = "购票时间";
                                    sortValue.put(7, (key + ":" + value));
                                    break;
                                case "createtime":
                                    key = "消费时间";
                                    sortValue.put(8, (key + ":" + value));
                                    break;
                                case "teamtotime":
                                    key = "期限";
                                    sortValue.put(9, (key + ":" + value));
                                    break;
                            }
                        }
                        for (int j = 0; j < sortValue.size(); j++) {
                            printer.printText(alignment, sortValue.get(j) + "\n");
                        }
                        printer.printText(alignment, "打印时间:" + getTime() + "\n");
                    }
                    printer.printText(alignment, "温馨提示:\n");
                    printer.printText(alignment, "1.查询结果不能作为入园凭证\n");
                    printer.printText(alignment, "2.特殊情况，请认真核实\n");
                    printer.printText(alignment, "客服电话:0531-85333222\n");
                    printer.printText(alignment, "客户签字:\n");
                    printer.printText(alignment, "客户电话:\n");
                    addEndSpaceLine(printer, alignment);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void checkList() {
        if (stepList == null) {
            stepList = new ArrayList<>();
        }
        stepList.clear();
    }

    private void addEndSpaceLine(Printer printer, Printer.Alignment alignment) throws Exception {
        printer.printText(alignment, "  \n");
    }

    private String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public boolean addBitmap() {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return false;
        }
        stepList.add(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                InputStream inputStream = context.getAssets().open("pay.bmp");
                printer.printImage(Printer.Alignment.LEFT, inputStream);
            }
        });
        return true;
    }

    public boolean addBarcode() {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return false;
        }
        stepList.add(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                printer.printBarCode("1234567890");
            }
        });
        return true;
    }

    public boolean addQRcode() {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return false;
        }
        stepList.add(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                printer.printQrCode(Printer.Alignment.CENTER,
                        new QrCode("福建联迪商用设备有限公司", ECLEVEL_Q),
                        200);
            }
        });
        return true;
    }

    public boolean feedLine(final int line) {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return false;
        }
        stepList.add(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                printer.feedLine(line);
            }
        });
        return true;
    }

    public boolean cutPaper() {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return false;
        }
        stepList.add(new Printer.Step() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                printer.cutPaper();
            }
        });
        return true;
    }

    public void startPrint() {
        if (stepList == null) {
            displayInfo("printer has not inited!");
            return;
        }
        progress = new Printer.Progress() {
            @Override
            public void doPrint(Printer printer) throws Exception {
                // never call
            }

            @Override
            public void onFinish(int error) {
                stepList.clear();
                if (error == Printer.ERROR_NONE) {
                    displayInfo("打印成功");
                } else {
                    String errorDes = getDescribe(error);
                    displayInfo("打印出错：" + errorDes);
                }
            }

            @Override
            public void onCrash() {
                stepList.clear();
                onDeviceServiceCrash();
            }
        };
        for (Printer.Step step : stepList) {
            progress.addStep(step);
        }
        try {
            progress.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDescribe(int error) {
        switch (error) {
            case Printer.ERROR_BMBLACK:
                return "黑标探测器检测到黑色信号";
            case Printer.ERROR_BUFOVERFLOW:
                return "缓冲模式下所操作的位置超出范围";
            case Printer.ERROR_BUSY:
                return "打印机处于忙状态";
            case Printer.ERROR_COMMERR:
                return "手座机状态正常，但通讯失败 (520针打特有返回值)";
            case Printer.ERROR_CUTPOSITIONERR:
                return "切纸刀不在原位 (自助热敏打印机特有返回值)";
            case Printer.ERROR_HARDERR:
                return "硬件错误";
            case Printer.ERROR_LIFTHEAD:
                return "打印头抬起 (自助热敏打印机特有返回值)";
            case Printer.ERROR_LOWTEMP:
                return "低温保护或AD出错 (自助热敏打印机特有返回值)";
            case Printer.ERROR_LOWVOL:
                return "低压保护";
            case Printer.ERROR_MOTORERR:
                return "打印机芯故障(过快或者过慢)";
            case Printer.ERROR_NOBM:
                return "没有找到黑标";
            case Printer.ERROR_NONE:
                return "正常状态，无错误";
            case Printer.ERROR_OVERHEAT:
                return "打印头过热";
            case Printer.ERROR_PAPERENDED:
                return "缺纸，不能打印";
            case Printer.ERROR_PAPERENDING:
                return "纸张将要用尽，还允许打印 (单步进针打特有返回值)";
            case Printer.ERROR_PAPERJAM:
                return "卡纸";
            case Printer.ERROR_PENOFOUND:
                return "自动定位没有找到对齐位置,纸张回到原来位置";
            case Printer.ERROR_WORKON:
                return "打印机电源处于打开状态";
            case Printer.ERROR_CUTCLEAN:
                return "纸仓堵纸，需清理纸仓";
            case Printer.ERROR_CUTERROR:
                return "切纸刀卡刀";
            case Printer.ERROR_CUTFAULT:
                return "切纸刀故障";
            case Printer.ERROR_OPENCOVER:
                return "纸仓被打开";
            default:
                return "未知错误";
        }
    }
}
