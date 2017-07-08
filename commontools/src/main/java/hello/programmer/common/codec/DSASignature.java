package hello.programmer.common.codec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * DSA算法签名实现
 *
 */
public class DSASignature {

	/**
	 * 编码格式为UTF-8
	 */
	public final static String CHARSET = "UTF-8";
	/**
	 * DSA算法
	 */
	public final static String ALGORITHM_DSA = "DSA";
	
	
	/**
	 * 私钥签名方法
	 * 方法默认以 UTF-8 进行编码
	 * @param data 待签名数据
	 * @param privateKey 私钥串
	 * @return 签名结果 Base64编码的签名串
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static String signEncrypt(String data, String privateKey)
			throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_DSA);
		EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes(CHARSET)));
		Signature signature = Signature.getInstance(ALGORITHM_DSA);
		signature.initSign(keyFactory.generatePrivate(keySpec));
		signature.update(data.getBytes(CHARSET));
		byte[] signBytes = signature.sign();
		return Base64.encodeBase64String(signBytes);
	}
	/**
	 * 公钥验证签名
	 * @param data 待签名数据
	 * @param publicKey 公钥串
	 * @param signChiper 报文签名串
	 * @return 验证结果
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static boolean signChiperVerify(String data, String publicKey,
			String signChiper) throws SignatureException,
			UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] signBytes = Base64.decodeBase64(signChiper.getBytes(CHARSET));
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_DSA);
		EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes(CHARSET)));
		Signature signature = Signature.getInstance(ALGORITHM_DSA);
		signature.initVerify(keyFactory.generatePublic(keySpec));
		signature.update(data.getBytes(CHARSET));
		return signature.verify(signBytes);
	}

		
}
