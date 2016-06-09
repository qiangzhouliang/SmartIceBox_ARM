package com.friendlyarm.Utils;
/**
 * Created by Q on 2016-04-21.
 */
public class util {
    private static String str;
    private static int[] in = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private static char[] ch = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    private static String[] strr;
    private static String[] rett = new String[512];
    private static String[] st = new String[512];
    public static String[] hex(byte[] x,int m) {
        int temp, a, b;
        int len = x.length;
        int ch_len = ch.length;
        char c1 = ' ',c2 = ' ';
        strr = new String[len];
        for (int i = 0; i < len; i++) {
            if (x[i] < 0) {
                temp = 128 + Math.abs(x[i]) - 10;
                a = temp / 16;
                b = temp % 16;
                for (int j = 0; j < ch_len; j++) {
                    if(a == in[j]){
                        c1 = ch[j];
                    }
                    if(b == in[j]){
                        c2 = ch[j];
                    }
                }
            } else {
                temp = x[i];
                a = temp / 16;
                b = temp % 16;
                for (int k = 0; k < ch_len; k++) {
                    if (a == in[k]) {
                        c1 = ch[k];
                    }
                    if (b == in[k]) {
                        c2 = ch[k];
                    }
                }
            }
            str = String.valueOf(c1)+String.valueOf(c2);
            strr[i] = str;
        }
        if(m == 0){
            return AnalyzeReplyData_JT2880(strr);
        }else if(m == 1){
            return strr;
        }
        return new String[0];
    }

    //分析包
    public static String[] AnalyzeReplyData_JT2880(String[] s){
        int k = 0;
            for (int i = 0; i < s.length; i++) {
                if(s[i].equals("AA"))   //寻找包头
                {
                    System.out.println("找到包头");
                    if(!s[ i +2 ].equals("FF"))  //不是失败返回
                    {
                        System.out.println("不是错误数据");
                        if(s[ i + 8 ].equals("6E"))   //标签号的第一个字节,标签头
                        {
                            System.out.println("标签号的第一个字节,标签头");
                            int j,m = 0;
                            for (j = k; j < 9+k; j++) {
                                st[j] = s[i+8+m];
                                m++;
                            }
                            k = j;
                            m = 0;
                        }
                    }
                }
            }
        System.out.println(st.toString());
        return st;
    }

    //byte 转十六进制
    public static String[] Bytes2HexString(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex.toUpperCase();
            rett[i] = ret;
            ret = "";
        }
        return AnalyzeReplyData_JT2880(rett);
    }

    //byte 转十六进制
    public static String[] Bytes2HexString_1(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex.toUpperCase();
            rett[i] = ret;
            ret = "";
        }
        return rett;
    }

    //byte 转十六进制
    public static String[] Bytes2HexString_2(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex.toUpperCase();
            rett[i] = ret;
            ret = "";
        }
        return rett;
    }
}
