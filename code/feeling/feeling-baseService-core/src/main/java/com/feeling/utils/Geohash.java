package com.feeling.utils;
 
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.HashMap;
 
/**
 * 维度相同下 经度每隔0.00001度，距离相差约1米
 * 经度相同下 纬度每隔0.00001度，距离相差约1.1米
 * 如果geohash的位数是6位数的时候，大概为附近1千米
 * @author Lenovo
 *
 */
public class Geohash {//tb8ch6nhr0e9
	//tb8ch6nhr0
    private static int numbits = 24;//越大越精确-不要大于32
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
 
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
    static {
        int i = 0;
        for (char c : digits) {
            lookup.put(c, i++);
        }
    }
 
    public static void main(String[] args) {
        
    	double lon1=-30;   
        double lat1=20.34;  
        double lon2=10.23;  
        double lat2=-61.65;
        
        double lon3=30;  
        double lat3=17; 
        
        double lon4=29.99996;  
        double lat4=20; 
        
        
        double dist;  
        String geocode;  
          
        dist=getPointDistance(lat1, lon1, lat2, lon2);   
        dist=2041423011.4567;
        long i = new Double(dist).longValue();
        System.out.println("两点相距2：" + i + " 米");  
        dist=getPointDistance(lat1, lon1, lat3, lon3);  
        System.out.println("两点相距3：" + dist + " 米"); 
        
        dist=getPointDistance(lat1, lon1, lat4, lon4);  
        System.out.println("两点相距4：" + dist + " 米"); 
          
        Geohash geohash = new Geohash();  
        geocode=geohash.encode(lat1, lon1);  
        long a= geohash.enLongCode(lat1, lon1);
        System.out.println(a);
        System.out.println("当前位置编码1：" + geocode);  
         
        geocode=geohash.encode(lat2, lon2);  
        System.out.println(geohash.enLongCode(lat2, lon2)-a);
        System.out.println("远方位置编码2：" + geocode); 
        
        geocode=geohash.encode(lat3, lon3);  
        System.out.println(geohash.enLongCode(lat3, lon3)-a);
        System.out.println("远方位置编码3：" + geocode); 
        geocode=geohash.encode(lat4, lon4);  
        System.out.println(geohash.enLongCode(lat4, lon4)-a);
        System.out.println("远方位置编码4：" + geocode); 
    }
 
    /**
     * 将Geohash字串解码成经纬值
     * 
     * @param geohash
     *            待解码的Geohash字串
     * @return 经纬值数组 [维度，经度]
     */
    public double[] decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {
            int i = lookup.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }
 
        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();
 
        // even bits
        int j = 0;
        for (int i = 0; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            lonset.set(j++, isSet);
        }
 
        // odd bits
        j = 0;
        for (int i = 1; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            latset.set(j++, isSet);
        }
 
        double lat = decode(latset, -90, 90);
        double lon = decode(lonset, -180, 180);
 
        DecimalFormat df = new DecimalFormat("0.00000");
        return new double[] { Double.parseDouble(df.format(lat)), Double.parseDouble(df.format(lon)) };
    }
 
    /**
     * 根据二进制编码串和指定的数值变化范围，计算得到经/纬值
     * 
     * @param bs
     *            经/纬二进制编码串
     * @param floor
     *            下限
     * @param ceiling
     *            上限
     * @return 经/纬值
     */
    private double decode(BitSet bs, double floor, double ceiling) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            mid = (floor + ceiling) / 2;
            if (bs.get(i))
                floor = mid;
            else
                ceiling = mid;
        }
        return mid;
    }
 
    /**
     * 根据经纬值得到Geohash字串
     * 
     * @param lat
     *            纬度值
     * @param lon
     *            经度值
     * @return Geohash字串
     */
    public String encode(double lat, double lon) {
        return base32(enLongCode(lat,lon));
    }
    /**
     * 根据经纬度合并二进制后，返回十进制数字
     * @param lat 纬度值
     * @param lon 经度值
     * @return
     */
    public long enLongCode(double lat, double lon){
    	BitSet latbits = getBits(lat, -90, 90);
        BitSet lonbits = getBits(lon, -180, 180);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < numbits; i++) {
            buffer.append((lonbits.get(i)) ? '1' : '0');
            buffer.append((latbits.get(i)) ? '1' : '0');
        }
        return Long.parseLong(buffer.toString(), 2);
    }
 
    /**
     * 将二进制编码串转换成Geohash字串
     * 
     * @param i
     *            二进制编码串
     * @return Geohash字串
     */
    public static String base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative)
            i = -i;
        while (i <= -32) {
            buf[charPos--] = digits[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = digits[(int) (-i)];
 
        if (negative)
            buf[--charPos] = '-';
        return new String(buf, charPos, (65 - charPos));
    }
 
    /**
     * 得到经/纬度对应的二进制编码
     * 
     * @param lat
     *            经/纬度
     * @param floor
     *            下限
     * @param ceiling
     *            上限
     * @return 二进制编码串
     */
    private BitSet getBits(double lat, double floor, double ceiling) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            double mid = (floor + ceiling) / 2;
            if (lat >= mid) {
                buffer.set(i);
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return buffer;
    }
    private static final double EARTH_RADIUS = 6378.137;  
    
     
    /**
     * 两点计算距离，传入两点的经纬度
     * @param lat1 维度1
     * @param lng1 经度1
     * @param lat2 维度2
     * @param lng2 经度2
     * @return double 米
     */
    public static double getPointDistance(double lat1,double lng1,double lat2,double lng2){  
        double result = 0 ;  
          
        double radLat1 = radian(lat1);  
          
        double ratlat2 = radian(lat2);  
        double a = radian(lat1) - radian(lat2);  
        double b = radian(lng1) - radian(lng2);  
          
        result = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(ratlat2)*Math.pow(Math.sin(b/2), 2)));  
        result = result*EARTH_RADIUS;     
      
        result = Math.round(result*1000);   //返回的单位是米，四舍五入  
          
        return result;  
    }  
      
    /**由角度转换为弧度*/   
    private static double radian(double d){  
        return (d*Math.PI)/180.00;  
    }  
}