package hello.programmer.common.utils;

import com.asset.bean.guandu.*;
import com.asset.consts.GuanduConstant;
import com.asset.enums.guandu.ReturnCodeEnum;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GuanduUtil {

	/**
	 * 解析官渡报文
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param msg
	 * @return DataExchangePackage
	 * @throws @author liunian
	 * @create 2016年9月28日 下午3:50:41
	 */
	public static DataExchangePackage msgXmlToBean(String msg) throws Exception {
		XStream stream = new XStream();
		stream.alias("dataExchangePackage", DataExchangePackage.class);
		stream.alias("transferInfo", TransferInfo.class);
		stream.alias("contentControl", ContentControl.class);
		stream.alias("packageInfo", PackageInfo.class);
		stream.alias("returnState", ReturnState.class);
		DataExchangePackage dataExchangePackage = (DataExchangePackage) stream.fromXML(msg);
		return dataExchangePackage;
	}

	/**
	 * 内容XML转换成bean
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param xml
	 * @return
	 * @return GcLoanCreditTransferPushInfoBean
	 * @throws @author liunian
	 * @create 2016年8月2日 下午5:25:43
	 */
	public static GcLoanCreditTransferPushInfoBeanList contentXmlToJavaBean(String xml) throws Exception {
		XStream stream = new XStream();
		stream.alias("gcLoanCreditTransferPushInfoBeanList", GcLoanCreditTransferPushInfoBeanList.class);
		stream.alias("gcLoanCreditTransferPushInfoBean", GcLoanCreditTransferPushInfoBean.class);
		stream.alias("gcLoanCreditBean", GcLoanCreditBean.class);
		stream.alias("ciWarrantsBaseInfoBean", CiWarrantsBaseInfoBean.class);
		stream.alias("lmtLimitInfoBean", LmtLimitInfoBean.class);
		stream.alias("limitCollInfoBean", LimitCollInfoBean.class);
		stream.alias("houseInfoBean", HouseInfoBean.class);
		stream.alias("ciBorrowerBaseInfoBean", CiBorrowerBaseInfoBean.class);
		stream.alias("ciCompanyBaseInfoBean", CiCompanyBaseInfoBean.class);
		stream.addImplicitCollection(CoBorrowerList.class, "coBorrowerList");
		stream.addImplicitCollection(CoCompanyList.class, "coCompanyList");
		// 设置隐含转换集合
		// LimitCollInfoList是父节点，对应LimitCollInfoList.class这个类，里面的"limitCollInfoList"是一个集合
		// stream.addImplicitCollection(gcLoanCreditTransferPushInfoList.class,
		// "gcLoanCreditTransferPushInfoBeanList");
		stream.addImplicitCollection(LimitCollInfoList.class, "limitCollInfoList");
		stream.addImplicitCollection(HouseInfoList.class, "houseInfoList");
		GcLoanCreditTransferPushInfoBeanList gcLoanCreditTransferPushInfoBeanList = (GcLoanCreditTransferPushInfoBeanList) stream
				.fromXML(xml);
		return gcLoanCreditTransferPushInfoBeanList;
	}

//	private static ExtendInfoBean contentXmlToExtendInfoBean(String xml) throws Exception {
//		XStream stream = new XStream();
//		stream.alias("extendInfoBean", ExtendInfoBean.class);
//
//		ExtendInfoBean extendInfoBean = (ExtendInfoBean) stream.fromXML(xml);
//		return extendInfoBean;
//	}

	/**
	 * 将请求的xml转化成bean,只能转较为简单的，使用前先测试
	 * @param xml
	 * @param nodeName
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> T contentXmlToRequestBean(String xml,String nodeName,Class<T> clazz) throws Exception {
		XStream stream = new XStream();
		stream.alias(nodeName, clazz);

		return  (T) stream.fromXML(xml);
	}

    /**
     * 将请求的xml转化成bean,支持多个名称对应的类
     */
    public static <T> T contentXmlToRequestBean(String xml, Map<String, Class> nodeNameClassMap) throws Exception {
        XStream stream = new XStream();
        for (Map.Entry<String, Class> entry : nodeNameClassMap.entrySet()) {
            stream.alias(entry.getKey(), entry.getValue());
        }
        return  (T) stream.fromXML(xml);
    }

	/**
	 * 生成随机数字
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @return String
	 * @throws
	 * @author liunian
	 * @create 2016年9月29日 下午12:45:30
	 */
	public static String getRandomString() {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 加密方法
	 * 
	 * @param text
	 *            明文
	 * @param text
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String text) throws Exception {
		if (text == null) {
			text = "";
		}
		byte[] keyBase64DecodeBytes = Base64.decode(GuanduConstant.encryptKey);// base64解码key
		DESKeySpec desKeySpec = new DESKeySpec(keyBase64DecodeBytes);// 前8个字节做为密钥
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] textBytes = text.getBytes(GuanduConstant.charset);// 明文UTF-8格式
		byte[] bytes = cipher.doFinal(textBytes);// DES加密

		return new String(Base64.encode(bytes));
	}



	/**
	 * 解密方法
	 * 
	 * @param text
	 *            密文
	 * @param text
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String text) throws Exception {
		byte[] keyBase64DecodeBytes = Base64.decode(GuanduConstant.encryptKey);
		DESKeySpec desKeySpec = new DESKeySpec(keyBase64DecodeBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] textBytes = Base64.decode(text);
		byte[] bytes = cipher.doFinal(textBytes);
		return new String(bytes, GuanduConstant.charset);
	}

	/**
	 * 生成DataExchangePackage字符串返回给官渡
	 * @param interfaceCode 接口编码,见GuanduConstant中的businessType
	 * @param sourceMessageID
	 * @param returnCode
	 * @param returnMessage
	 * @author liwei
	 * @create 2017年5月10日 下午1:45:30
	 */
	public static DataExchangePackage generateMsg(String interfaceCode,String sourceMessageID, String returnCode, String returnMessage) {
		return getDataExchangePackage(interfaceCode,returnMessage,sourceMessageID,returnCode,returnMessage);
	}


	private static DataExchangePackage getDataExchangePackage(String interfaceCode,String content,String sourceMessageID, String returnCode, String returnMessage) {

		String todayDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String todayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

		DataExchangePackage dataExchangePackage = new DataExchangePackage();
		EnvelopInfo envelopInfo = new EnvelopInfo();
		envelopInfo.setBusinessType(interfaceCode);
		envelopInfo.setDestinationID(GuanduConstant.senderID);
		envelopInfo.setDestinationAppID(GuanduConstant.senderAppID);
		envelopInfo.setSourceID(GuanduConstant.receiverID);
		envelopInfo.setSourceAppID(GuanduConstant.receiverAppID);
		String eightWaterNum = GuanduUtil.getRandomString();
		envelopInfo.setMessageID(envelopInfo.getSourceID() + envelopInfo.getBusinessType() + todayDate + eightWaterNum);
		envelopInfo.setGlobalBusinessID(envelopInfo.getSourceID() + envelopInfo.getDestinationAppID() + todayDate + eightWaterNum);
		envelopInfo.setSyncFlag(GuanduConstant.syncFlag);
		dataExchangePackage.setEnvelopInfo(envelopInfo);

		TransferInfo transferInfo = new TransferInfo();
		transferInfo.setSenderID(GuanduConstant.receiverID);
		transferInfo.setSenderAppID(GuanduConstant.receiverAppID);
		transferInfo.setReceiverID(GuanduConstant.senderID);
		transferInfo.setReceiverAppID(GuanduConstant.senderAppID);
		transferInfo.setSourceMessageID(sourceMessageID);
		transferInfo.setIsRetry("0");
		transferInfo.setSendTime(todayTime);
		dataExchangePackage.setTransferInfo(transferInfo);

		ContentControl contentControl = new ContentControl();
		contentControl.setEncrytType("1");
		contentControl.setIsEncrypt("0");
		contentControl.setIsZip("1");
		contentControl.setZipType("1");
		dataExchangePackage.setContentControl(contentControl);

		PackageInfo packageInfo = new PackageInfo();
		packageInfo.setSequence("1");

		boolean contentContainXml = ReturnCodeEnum.SUCCESS.getValue().equals(returnCode) &&
				content.indexOf("<") != -1 && content.indexOf(">") != -1;
		String _content = contentContainXml ? "<![CDATA[" + content + "]]>" : content;
		packageInfo.setContent(_content);

		dataExchangePackage.setPackageInfo(packageInfo);

		ReturnState returnState = new ReturnState();
		returnState.setReturnCode(returnCode);
		returnState.setReturnMessage(returnMessage);
		returnState.setSourceMessageId(sourceMessageID);
		dataExchangePackage.setReturnState(returnState);

		return dataExchangePackage;
	}

	/**
	 * 生成xml字符串返回给官渡
	 * @param interfaceCode 接口编码,见GuanduConstant中的businessType
	 * @param content 返回内容
	 * @param sourceMessageID
	 * @param returnCode
	 * @param returnMessage
	 * @author liwei
	 * @create 2017年5月10日 下午2:45:30
	 */
	public static String generateXmlMsg(String interfaceCode,String content,String sourceMessageID, String returnCode, String returnMessage) {

		DataExchangePackage dataExchangePackage = getDataExchangePackage(interfaceCode,content,sourceMessageID,returnCode,returnMessage);

		XStream xstream = new XStream();
		xstream.alias("dataExchangePackage", DataExchangePackage.class);
		xstream.alias("envelopInfo", EnvelopInfo.class);
		xstream.alias("transferInfo", TransferInfo.class);
		xstream.alias("contentControl", ContentControl.class);
		xstream.alias("packageInfo", PackageInfo.class);
		xstream.alias("returnState", ReturnState.class);
		String xml = (xstream.toXML(dataExchangePackage).replaceAll("&lt;","<")
				.replaceAll("&gt;",">").replaceAll("&gt;",">"));
		return xml;
	}

	/**
	 * QueryAssetResponseBean 转化成 xml
	 * @param responseBean QueryAssetResponseBean
	 * @return xml
	 * @author liwei
	 * @create 2017年5月9日 下午2:45:30
	 */
	public static String queryAssetResponseBean2Xml(QueryAssetResponseBean responseBean){
		XStream xstream = new XStream();
		xstream.alias("dataExchangePackage", DataExchangePackage.class);
		xstream.alias("queryAssetResponseBean", QueryAssetResponseBean.class);
		xstream.alias("projectInfoBean", ProjectInfoBean.class);
		xstream.alias("repayPlanList", List.class);
		xstream.alias("repayPlanBean", RepayPlanBean.class);
		return xstream.toXML(responseBean);
	}

	/**
     * QueryExtendAssetResponseBean 转化成 xml
     * @param responseBean QueryExtendAssetResponseBean
     * @return xml
     * @author liwei
     * @create 2017年5月9日 下午2:45:30
     */
    public static String queryExtendAssetResponseBean2Xml(QueryExtendAssetResponseBean responseBean){
        XStream xstream = new XStream();
        xstream.alias("dataExchangePackage", DataExchangePackage.class);
        xstream.alias("queryExtendAssetResponseBean", QueryExtendAssetResponseBean.class);
        xstream.alias("projectInfoBean", ProjectInfoBean.class);
        xstream.alias("repayPlanList", List.class);
        xstream.alias("repayPlanBean", RepayPlanBean.class);
        return xstream.toXML(responseBean);
    }

    /**
     * ProjectReconciliationBean 转化成 xml
     * @param projectReconciliationBean 汇总信息实体
     * @return xml
     * @author wulinsong
     * @create 2017年10月28日
     */
    public static String projectReconciliationBean2Xml(ProjectReconciliationBean projectReconciliationBean){
        if (projectReconciliationBean == null) {
            return "";
        }
        XStream xstream = new XStream();
        xstream.alias("projectReconciliationBean", ProjectReconciliationBean.class);
        xstream.alias("prepayReconciliationBean", PrepayReconciliationBean.class);
        return xstream.toXML(projectReconciliationBean);
    }

	public static void main(String[] args) throws Exception {
		ApplyPrepayProjectBean applyPrepayProjectBean=GuanduUtil.contentXmlToRequestBean(
				"<applyPrepayProjectBean><fundCode>aaaabbbbcccc</fundCode><preRepayDate>2017-01-02</preRepayDate></applyPrepayProjectBean>",
				"applyPrepayProjectBean",
				ApplyPrepayProjectBean.class);
		System.out.println(applyPrepayProjectBean.getFundCode());
		System.out.println(applyPrepayProjectBean.getPreRepayDate());
	}
	public static void main2(String[] args) {
		String encryptMsg = "<dataExchangePackage><envelopInfo><sourceID>YKZC</sourceID><destinationID>YKZC</destinationID><sourceAppID>"
				+ "CRMS00</sourceAppID><destinationAppID>GCT002</destinationAppID><globalBusinessID>YKZCGCT0022016092900000002"
				+ "</globalBusinessID><messageID>YKZCGCT0022016092900000002</messageID><businessType>GCT002</businessType><syncFlag>1"
				+ "</syncFlag></envelopInfo><transferInfo><senderID>YKZC</senderID><receiverID>YKZC</receiverID><senderAppID>CRMS00"
				+ "</senderAppID><receiverAppID>GCT002</receiverAppID><sourceMessageID>YKZCGCT0022016092900000002</sourceMessageID>"
				+ "<isRetry>0</isRetry><sendTime>2016-09-29 14:26:59</sendTime></transferInfo><contentControl><isZip>1</isZip><zipType>1"
				+ "</zipType><isEncrypt>0</isEncrypt><encrytType>1</encrytType></contentControl><packageInfo><sequence>1</sequence><content>"
				+ "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><gcLoanCreditTransferPushInfoBeanList>"
				+ "<gcLoanCreditTransferPushInfoBean><ciBorrowerBaseInfoBean><address></address><bankCardNo>meB0wyLOPfo=</bankCardNo>"
				+ "<carProperty>0</carProperty><certNumber>nLWF0U1wcy2vNuuqlIzA0SzAnRmWjhkJ</certNumber><certType>01</certType><child>N"
				+ "</child><creditLimit>v7YEMSRyiuw=</creditLimit><currentIncome>0.0</currentIncome><education>04</education><houseProperty>"
				+ "0</houseProperty><hukouAddress>热吻瑞特人特瑞特人</hukouAddress><id>00000000000000001441</id><isHaveOtherProperty>0"
				+ "</isHaveOtherProperty><loanNumber>0</loanNumber><maritalStatus>01</maritalStatus><mobileNumber>meB0wyLOPfo=</mobileNumber>"
				+ "<name>t4CxbWaf3GY=</name><overdueAmount>1.0</overdueAmount><overdueNum>1</overdueNum><sex>02</sex>"
				+ "</ciBorrowerBaseInfoBean><ciCompanyBaseInfoBean><accountLine></accountLine><bankAccount></bankAccount><busiLicCode>"
				+ "</busiLicCode><businessddress></businessddress><businessremises></businessremises><certNumber></certNumber><companyName>"
				+ "</companyName><companyel></companyel><contactInformation></contactInformation><contactName></contactName>"
				+ "<establishmentDate></establishmentDate><id></id><legalPerson></legalPerson><orgCode></orgCode><registapital>0.0"
				+ "</registapital></ciCompanyBaseInfoBean><ciWarrantsBaseInfoBean><bankAccount>3OjFE9IlcKktdeTTsxQHEr+2BDEkcors"
				+ "</bankAccount><birthday>1993-03-16 00:00:00</birthday><certNumber>URlRpQL6TLg1B/VRn5KOvB7SPruDomJj</certNumber>"
				+ "<contactPhone>meB0wyLOPfo=</contactPhone><id>00000000000000000011</id><name>mJGz997AOaM=</name><sex>01</sex>"
				+ "</ciWarrantsBaseInfoBean><gcLoanCreditBean><amount>1.0E7</amount><bondProportion>100000.0</bondProportion>"
				+ "<borrowingRate>8.0</borrowingRate><busiType>0001</busiType><code>YFJR201603210001001001</code><contAmount>1.0E7"
				+ "</contAmount><contCode>YFJR201603210001</contCode><contractApplication>融时代存量数据此字段无内容</contractApplication>"
				+ "<endDate>2016-12-20 00:00:00</endDate><name>融时代（南昌）分公司</name><payBalance>1.0E7</payBalance><paymentSource>"
				+ "融时代存量数据此字段无内容</paymentSource><rateUnit>02</rateUnit><repayMethod>01</repayMethod><startDate>"
				+ "2015-12-21 00:00:00</startDate><term>1</term><termUnit>01</termUnit><transferDate>2016-09-14 14:19:47</transferDate>"
				+ "</gcLoanCreditBean><lmtLimitInfoBean><contractCode>YFJR201603210001</contractCode><internalPriceToal>1.353E7"
				+ "</internalPriceToal><limitCollInfoBeanList><limitCollInfoBean><area> 北京市北京市朝阳区</area><buildArea>100.0"
				+ "</buildArea><checkLeaseContract>0</checkLeaseContract><collateralPrice>1.353E7</collateralPrice><houseAge>0</houseAge>"
				+ "<houseInfoList><houseInfoBean><hourseNumber>京房权证第4832号</hourseNumber><name>张艺</name></houseInfoBean>"
				+ "</houseInfoList><housePosition>符合当今司法解释的看法就开始犯困了</housePosition><houseType>01</houseType><isOnlyOne>0"
				+ "</isOnlyOne><mortgageSituation>1</mortgageSituation><planPurpose>04</planPurpose></limitCollInfoBean>"
				+ "</limitCollInfoBeanList><mortgageRate>148.0</mortgageRate><quickTurnoverToalMax>1.2E7</quickTurnoverToalMax>"
				+ "<quickTurnoverToalMin>1.1E7</quickTurnoverToalMin></lmtLimitInfoBean></gcLoanCreditTransferPushInfoBean>"
				+ "</gcLoanCreditTransferPushInfoBeanList>]]></content></packageInfo>"
				+ "<returnState/></dataExchangePackage>";

//		try {
//			DataExchangePackage dataExchangePackage = GuanduUtil.msgXmlToBean(encryptMsg);
//			GcLoanCreditTransferPushInfoBeanList gcLoanCreditTransferPushInfoBeanList = contentXmlToJavaBean(
//					dataExchangePackage.getPackageInfo().getContent());
//			System.out.println(JSON.toJSONString(gcLoanCreditTransferPushInfoBeanList));
//			 System.out.println("--- " +decrypt(gcLoanCreditTransferPushInfoBeanList.getGcLoanCreditTransferPushInfoBean().getCiBorrowerBaseInfoBean().getName()));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		 String name="中投盛源";
		 try {
		 String encryptString= encrypt(name) ;
		 System.out.println("name:"+encryptString);
		 encryptString= encrypt("中航信托1") ;
		 System.out.println("accountName:"+encryptString);
//		 encryptString= encrypt("641255656598659") ;
//		 System.out.println("bankCardNo:"+encryptString);	 
//		 encryptString = encrypt("441623198912012412");
//		 System.out.println("id:"+encryptString);
//		 encryptString = encrypt("13269696666");
//		 System.out.println("phone:"+encryptString);
//		 encryptString = encrypt("广东发展银行");
//		 System.out.println("bankName:"+encryptString);
//		 System.out.println(ss);
//		 System.out.println( decrypt("jZsjsz764H6oMIWIox0tsA=="));
		
		 } catch (Exception e) {
		 e.printStackTrace(); //To change body of catch statement use File |
//		 Settings | File Templates.
		 }
	}

	public static String getErrorMsgByCode(int code) {
		switch(code) {
            case GuanduConstant.SUCCESS:
			    return ReturnCodeEnum.SUCCESS.getDesc();
			case GuanduConstant.ARGUMENT_INVALID:
				return GuanduConstant.ARGUMENT_INVALID_DESCRIPTION;
			case GuanduConstant.PROJECT_INFO_EXCEPTION:
				return GuanduConstant.PROJECT_INFO_EXCEPTION_DESCRIPTION;
			case GuanduConstant.PROJECT_FINISHED:
				return GuanduConstant.PROJECT_FINISHED_DESCRIPTION;
			case GuanduConstant.PROJECT_PREPAYING:
				return GuanduConstant.PROJECT_PREPAYING_DESCRIPTION;
			case GuanduConstant.PROJECT_IN_OTHER_BUSINESS:
				return GuanduConstant.PROJECT_IN_OTHER_BUSINESS_DESCRIPTION;
            case GuanduConstant.GUARANTEE_ACCOUNT_ISNULL:
                return GuanduConstant.GUARANTEE_ACCOUNT_ISNULL_DESCRIPTION;
            case GuanduConstant.ASSET_CHECK_FAIL:
                return GuanduConstant.ASSET_CHECK_FAIL_DESCRIPTION;
            case GuanduConstant.HAS_UNPAID_PHASE:
                return GuanduConstant.HAS_UNPAID_PHASE_DESCRIPTION;
            case GuanduConstant.HAS_UNPAID_SERVICE_FEE:
                return GuanduConstant.HAS_UNPAID_SERVICE_FEE_DESCRIPTION;
			case GuanduConstant.REQUEST_BE_LIMITED:
				return GuanduConstant.REQUEST_BE_LIMITED_DESCRIPTION;
			case GuanduConstant.PROJECT_NO_PREPAY_RECORD:
			    return GuanduConstant.PROJECT_NO_PREPAY_RECORD_DESCRIPTION;
			default:
				return GuanduConstant.SYSTEM_ERROR_DESCRIPTION;
		}
	}

	private static String getErrorMsgByparams(String errorMsg, Map<String, String> params) {
	    for (Map.Entry<String, String> entry : params.entrySet()) {
	        if (StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue())) {
	            return null;
            }
            String replaceKey = "{" + entry.getKey() + "}";
            if (!errorMsg.contains(replaceKey)) {
	            continue;
            }
            errorMsg = errorMsg.replace(replaceKey, entry.getValue());
        }
        return errorMsg;
    }

	public static String getErrorMsgByCodeWithParams(int code, Map<String, String> params) {
        switch(code) {
            case GuanduConstant.APPLY_DELAY_TIME:
                return getErrorMsgByparams(GuanduConstant.APPLY_DELAY_TIME_DESCRIPTION, params);
            default:
                return null;
        }
    }
}
