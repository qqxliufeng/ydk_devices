package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.algorithm.Algorithm;
import com.landicorp.android.eptapi.algorithm.RSAPrivateKey;
import com.landicorp.android.eptapi.utils.BytesBuffer;

import cn.eas.national.deviceapisample.data.AlgorithmError;

/**
 * 该模块列举一般常用的几种算法，如：AES/MAC/TDES/SM4/SHA1/SHA256/RSA等
 */

public abstract class AlgorithmImpl extends BaseDevice {

    public AlgorithmImpl() {
    }

    public byte[] calcMac(long mode, byte[] key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.calcMAC(mode, key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc mac fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithAES(long mode, byte[] key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.AES(mode, key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with aes fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithTDES(long mode, byte[] key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.TDES(mode, key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with tdes fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithSM4(long mode, byte[] key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SMS4(mode, key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with sm4 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcMacWithSM4(long mode, byte[] key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SMS4CalculateMac(mode, key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc mac with sm4 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] sm3(byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SM3(data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("sm3 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public int genSm2Keys(int priKeyLen, BytesBuffer pubKey, BytesBuffer priKey) {
        return Algorithm.genSm2Keys(priKeyLen, pubKey, priKey);
    }

    public int sm2SetVersion(long version) {
        return Algorithm.sm2SetVersion(version);
    }

    public byte[] sm2Encrypt(byte[] in, byte[] pubKey) {
        BytesBuffer out = new BytesBuffer();
        int ret = Algorithm.sm2Encrypt(in, pubKey, out);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("sm2Encrypt fail, ret = " + ret);
            return null;
        }
        return out.getData();
    }

    public byte[] sm2Decrypt(byte[] in, byte[] priKey) {
        BytesBuffer out = new BytesBuffer();
        int ret = Algorithm.sm2Decrypt(in, priKey, out);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("sm2Decrypt fail, ret = " + ret);
            return null;
        }
        return out.getData();
    }

    public byte[] sm2SignHash(byte[] ida, byte[] in, byte[] pubKey) {
        BytesBuffer out = new BytesBuffer();
        int ret = Algorithm.sm2SignHash(ida, in, pubKey, out);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("sm2SignHash fail, ret = " + ret);
            return null;
        }
        return out.getData();
    }

    public byte[] sm2SignEnd(byte[] hash, byte[] priKey) {
        BytesBuffer out = new BytesBuffer();
        int ret = Algorithm.sm2SignEnd(hash, priKey, out);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("sm2SignHash fail, ret = " + ret);
            return null;
        }
        return out.getData();
    }

    public int sm2VerifyEnd(byte[] hash, byte[] in, byte[] pubKey) {
        return Algorithm.sm2VerifyEnd(hash, in, pubKey);
    }

    public RSAPrivateKey generateRsaPrivateKey() {
        RSAPrivateKey key = new RSAPrivateKey();
        int ret = Algorithm.generateRSAKey(Algorithm.EM_MIN_RSA_MODULUS_BITS, Algorithm.EM_RSA_EXP_10001, key);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("generate rsa private key fail, ret = " + ret);
            return null;
        }
        return key;
    }

    public byte[] calcWithRSAPrivateKey(RSAPrivateKey key, byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.RSAPrivateCalc(key, data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with rsa private key fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithSHA1(byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SHA1(data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with sha1 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithSHA256(byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SHA256(data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with sha256 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }

    public byte[] calcWithSHA512(byte[] data) {
        BytesBuffer buffer = new BytesBuffer();
        int ret = Algorithm.SHA512(data, buffer);
        if (ret != AlgorithmError.SUCCESS) {
            displayInfo("calc with sha512 fail, ret = " + ret);
            return null;
        }
        return buffer.getData();
    }
}
