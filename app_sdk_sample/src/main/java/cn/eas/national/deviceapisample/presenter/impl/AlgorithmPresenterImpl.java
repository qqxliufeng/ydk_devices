package cn.eas.national.deviceapisample.presenter.impl;

import com.landicorp.android.eptapi.algorithm.Algorithm;
import com.landicorp.android.eptapi.algorithm.RSAPrivateKey;
import com.landicorp.android.eptapi.utils.BytesBuffer;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.AlgorithmImpl;
import cn.eas.national.deviceapisample.presenter.IAlgorithmPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class AlgorithmPresenterImpl implements IAlgorithmPresenter{
    private static final byte[] DATA = ByteUtil.hexString2Bytes("11111111111111111111111111111111");
    private static final byte[] KEY = ByteUtil.hexString2Bytes("31313131313131313131313131313131");

    private IDeviceView view;
    private AlgorithmImpl algorithm;

    public AlgorithmPresenterImpl(final IDeviceView deviceView) {
        this.view = deviceView;
        this.algorithm = new AlgorithmImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void calcData() {
        view.displayInfo("data明文：" + ByteUtil.bytes2HexString(DATA));
        view.displayInfo("key明文：" + ByteUtil.bytes2HexString(KEY));
        view.displayInfo("--------------------------------------");
        // 计算mac
        byte[] result = algorithm.calcMac(Algorithm.EM_alg_MACALGORITHMDEFAULT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("计算MAC，默认填充模式（模式1）：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // AES算法
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_CBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES算法CBC模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_ECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES算法ECB模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_MACMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES算法MAC模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_ENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES算法加密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_DECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("AES算法解密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // TDES算法
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESTCBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES算法CBC模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESTECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES算法ECB模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES算法加密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESDECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES算法解密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM4算法
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4ENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4密钥加密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4DECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4密钥解密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4ENCRYPTMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4算法加密：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4TCBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4算法TCBC模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4TECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4算法TECB模式：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcMacWithSM4(Algorithm.EM_alg_MACALGORITHMDEFAULT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4规范计算MAC：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM3算法
        result = algorithm.sm3(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM3求杂凑：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM2算法
        BytesBuffer priKey = new BytesBuffer();
        BytesBuffer pubKey = new BytesBuffer();
        int ret = algorithm.genSm2Keys(32, pubKey, priKey);
        if (ret == 0) {
            view.displayInfo("sm2 public key：" + ByteUtil.bytes2HexString(pubKey.getData()));
            view.displayInfo("sm2 private key：" + ByteUtil.bytes2HexString(priKey.getData()));
            ret = algorithm.sm2SetVersion(Algorithm.EM_alg_SMS2VERDEFAULT);
            view.displayInfo("sm2设置版本[default]" + (ret == 0 ? "成功" : "失败"));
            result = algorithm.sm2Encrypt(DATA, pubKey.getData());
            if (result != null && result.length != 0) {
                view.displayInfo("sm2 public key加密：");
                view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                result = algorithm.sm2Decrypt(result, priKey.getData());
                if (result != null && result.length != 0) {
                    view.displayInfo("sm2 private key解密：");
                    view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                }
            }
            byte[] hash = algorithm.sm2SignHash(Algorithm.EM_DEFAULT_SM2_IDA, DATA, pubKey.getData());
            if (hash != null && hash.length != 0) {
                view.displayInfo("sm2获取hash值：");
                view.displayInfo("hash：" + ByteUtil.bytes2HexString(hash));
                result = algorithm.sm2SignEnd(hash, priKey.getData());
                if (result != null && result.length != 0) {
                    view.displayInfo("sm2签名结果：");
                    view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                    ret = algorithm.sm2VerifyEnd(hash, result, pubKey.getData());
                    view.displayInfo("sm2签名验证结果：" + (ret == 0 ? "成功" : "失败"));
                }
            } else {
                view.displayInfo("sm2获取hash值失败，ret = " + ret);
            }
        } else {
            view.displayInfo("生成sm2公私钥失败，ret = " + ret);
        }
        view.displayInfo("--------------------------------------");
        // RSA算法
        RSAPrivateKey key = algorithm.generateRsaPrivateKey();
        if (key != null) {
            result = algorithm.calcWithRSAPrivateKey(key, DATA);
            if (result != null && result.length != 0) {
                view.displayInfo("RSA算法private key加密：");
                view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
            }
        }
        view.displayInfo("--------------------------------------");
        // SHA1算法
        result = algorithm.calcWithSHA1(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA1算法：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        // SHA256算法
        result = algorithm.calcWithSHA256(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA256算法：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        // SHA512算法
        result = algorithm.calcWithSHA512(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA512算法：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
    }
}
