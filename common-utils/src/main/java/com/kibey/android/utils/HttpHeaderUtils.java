package com.kibey.android.utils;


import java.util.TreeMap;

/**
 * @author mchwind
 * @version V1.0
 * @since 16/7/26
 * http header封装，以后有header相关逻辑都在这里写
 */
public class HttpHeaderUtils {

	static TreeMap<String, String> sAuthHeaders;

	public static void setAuthHeader(TreeMap<String, String> heades) {
		sAuthHeaders = heades;
	}

    private HttpHeaderUtils() {
    }

    public static HttpHeaderUtils getInstance() {
        return InstanceHolder.sInstance;
    }

    private static class InstanceHolder {
        private static final HttpHeaderUtils sInstance = new HttpHeaderUtils();
    }

    static int i = 0;

    /**
     * 设置echo定义的http请求签名
     * http://redmine.kibey.com/projects/echo/wiki/_echo%E6%96%B0%E7%89%88%E5%85%A8%E5%B1%80%E5%8F%82%E6%95%B0%EF%BC%88%E7%AD%BE%E5%90%8D%E7%B3%BB%E7%BB%9F%EF%BC%89_
     *
     * @param header
     * @param params
     */
    public static void buildHeader(TreeMap<String, String> header, TreeMap<String, String> params) {
        long t = System.currentTimeMillis() / 1000; // 原始时间戳

        byte[] appSign = (APPConfig.S1 + t).getBytes();
        String uid = DeviceUtils.DeviceInfoUtil.getUniqueNumber();
        String dt = uid;
        String sign = Md5Util.encryptToSHA(appSign); // 原始时间戳和app签名混合生产密钥1

        // 单独制作用于签名的signHeader,防止header中有其他字段污染签名
        TreeMap<String, String> signHeader = new TreeMap<>();
        signHeader.put("x-av", APPConfig.API_VERSION + "");
        signHeader.put("x-c", "2");
        signHeader.put("x-dt", dt);
        signHeader.put("x-net", NetworkUtils.getNetWorkType());
        signHeader.put("x-uuid", uid);
        signHeader.put("x-v", APPConfig.getVersionCode() + "");
        signHeader.put("x-vs", APPConfig.getVersionName());
        signHeader.put("x-at", APPConfig.getPackageName().contains("plus") ? "3" : "1");

        String[] signs = SignUtils.sign(signHeader, params, t, sign);
        header.putAll(signHeader);

		// 联通代理auth header
		if (null != sAuthHeaders) {
			header.putAll(sAuthHeaders);
		}

        if (Logs.IS_DEBUG) {
            signs[0] = "b679c23816cb5e4c589135ef1d1d35ef"; // debug用，万能签名
        }
        header.put("x-sn", signs[0]); // 用密钥1和参数混合生成api签名
        header.put("x-a-sn", sign); //app密钥，动态
        header.put("User-Agent", APPConfig.getUserAgent());
//        header.put("x-gv", GameConfig.getGameVersion().trim());//游戏版本号
        if (sIAddExtraHead != null) {
            sIAddExtraHead.add(header);
        }
    }

    static IAddExtraHead sIAddExtraHead;

    public static void setAddExtraHead(IAddExtraHead IAddExtraHead) {
        sIAddExtraHead = IAddExtraHead;
    }

    public interface IAddExtraHead {
        void add(TreeMap<String, String> header);
    }
}
