package com.training.modules.quartz.tasks.pay.config.alipay;

/**
 * 支付宝-基础配置类
 * 2017年6月9日
 * @author coffee
 *
 */
public class AlipayConfig {
	
	//公共参数 https统一路径
	public static final String downloadAccount = "https://openapi.alipay.com/gateway.do";
	// APP应用ID
	public static final String appid = "2017042006848196";
	// 应用私钥
	public static final String your_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ5omdHA4d7iXNb0Z/5f5tHXT"
													+ "/MoH2OZ1SigzTk0Rjd62gWdsadQ7JijatNOFFusa8Pmyv/DLltCeV92CtLmxNR0ZNQDb3GeZ"
													+ "mMHovaX6bgCSa+y75AVd5ovybZ9W7i6396Z2VUFTl1VvqoKNSAM+UAOZ0nsBM235vV4xiygD"
													+ "CJDAgMBAAECgYAOTefF9yLuW4aCqqNRZxuSy1ye2nqrJdMHzi16AuxsSh2x8CGAuGAFEIyu9"
													+ "BgGhzVcBVVDbz6aYxBOvHwRu2D+oa2YkgcH4u0yVy271cKWhsqtXC9ezfqYIFpk39Fy5ADUsC"
													+ "wVLuyAyh2NeqxXHbhC+cag8rYD9lw8EeaWB6uZoQJBAOWNNHCirbDQ4cMiadVzcNu9SOsDcuJ"
													+ "d58KO1+RDN7a066uHV/s18axTdUlnnERDpa+M16upU/k3oeSouh5RzJsCQQCwqPsJbh7xxsOb"
													+ "4TsGL37kvpL0A4RawCYOpVE44FMZor5JNwX+V8MwXlUvz21ftMtWOAZlFbb7Uui5zriSyZd5A"
													+ "kEAizOLv1oHNhgVL73oq/XrVHV+iHV472i+qC7zIIraeENSPpw+cCoQOc4Ka88W5haXnNMt3f0"
													+ "63QAtfnoLE2PLTQJAJDyVOcr31/pAd4IMvGkTq1IYDKuIA6F0bP6mGXeSNCj4xUXfGdvgstQ2v"
													+ "xbaRY5tQyM81JFOtmC8UhjALYPiaQJBALgWz4H3NREZA6XdkKdao2vo7xSKhyUOBdOoHyOMTnz"
													+ "gviFvcnvziVKqZH6OVd5QnxHkiptu2VLNEgbxW1nyOnA="; 
	// 支付宝公钥
	public static final String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3v"
													+ "F1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhU"
													+ "rCmZYI/FCEa3/cNMW0QIDAQAB";
	// 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	public static final String sign_type = "RSA";
	// 字符编码格式 目前支持utf-8
	public static final String input_charset = "utf-8"; 
}
