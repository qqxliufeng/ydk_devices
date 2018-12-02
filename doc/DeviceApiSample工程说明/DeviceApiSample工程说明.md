# DeviceApiSample工程说明
## 工程结构
### 开发环境
使用Adnroid Studio开发工具
### 框架模型
工程使用MVP模型
![mvp](http://owirld9zt.bkt.clouddn.com/image/md/mvp.png)

## 工程模块说明
DeviceApiSample工程说明：cn.eas.national.deviceapisample
### 工程分包说明
基本包名：cn.eas.national.deviceapisample，子包说明如下：

| 包名 | 描述 |
|--------|--------|
|activity|各模块界面类（view）|
|presenter|各模块presenter接口类（presenter）|
|presenter.impl|各模块presenter实现类|
|device|各设备模块具体操作实现类|
|data|工程使用的常量数据&错误码定义类|
|util|工程工具类|
|view|工程中使用的自定义控件类|

该工程包含Landi智能终端所有模块的示例，主要模块及其示例代码分布如下:

| 模块名 | 界面类 | 模块实现类 |
|--------|--------|--------|
|磁条卡读卡器|MagCardActivity|MagCardReaderImpl|
|接触式cpu卡读卡器|ICCpuCardActivity|ICCpuCardReaderImpl|
|同步卡读卡器|SyncCardActivity|AT1604CardReaderImpl<br>AT1608CardReaderImpl<br>AT24CxxCardReaderImpl<br>SIM4428CardReaderImpl<br>SIM4442CardReaderImpl|
|psam卡读卡器|PsamCardActivity|PsamCardReaderImpl|
|非接触式cpu卡读卡器|RFCpuCardActivity|RFCpuCardReaderImpl|
|非接触式mifare one卡读卡器|MifareCardActivity|MifareCardReaderImpl|
|二代证读卡器|IDCardActivity|IDCardReaderImpl|
|打印机|PrinterActivity|PrinterImpl|
|密码键盘|PinpadActivity|PinpadImpl|
|内置扫码器|InnerScannerActivity|InnerScannerImpl|
|摄像头扫码器|CameraScannerActivity|CameraScannerImpl|
|外接扫码枪|ScannerActivity|ScannerImpl|
|LED灯|LedActivity|LedImpl|
|串口|SerialPortActivity|SerialPortImpl|
|蜂鸣器|BeeperActivity|BeeperImpl|
|钱箱|CashBoxActivity|CashBoxImpl|
|签字板|SignPanelActivity|SignPanelImpl|
|C10设备客屏接口|C10SubscreenDeviceActivity|C10SubscreenDeviceImpl|
|C10数码管显示设备接口|C10DigledDeviceActivity|C10DigledDeviceImpl|
|算法示例|AlgorithmActivity|AlgorithmImpl|
|系统接口|SystemDeviceActivity|SystemDeviceImpl|
|par参数文件接口|ParActivity|ParManagerImpl|