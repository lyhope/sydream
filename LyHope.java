package wwhm.com.lyhopesyzxy.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Ly utils
 * 六月的工具
 */
public class LyHope {

    /**
     * 显示  show
     * 显示数据
     * 默认通用吐司打印
     * 吐司打印
     *
     * @param activity Activity
     * @param str      要打印的字符串
     */
    public static void showToast(final Activity activity, final String str) {
        //判断是不是主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
        } else {
            //利用Activity.runOnUiThread(Runnable)把更新ui的代码创建在Runnable中
            // 然后在需要更新ui时，把这个Runnable对象传给Activity.runOnUiThread(Runnable)
            //这样Runnable对像就能在ui程序中被调用。如果当前线程是UI线程,那么行动是立即执行 如果当前线程不是UI线程,操作是发布到事件队列的UI线程
            //就是方便更新ui
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    /**
     * 判断
     * 判断传入的字符串是不是空
     * @param str 字符串
     * @return 如果是空返回true 如果不是空返回false
     */
    public static boolean judgeIsStrNull(String str) {
        if (str == null && str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断
     * 判断传入的多个字符串是不是空
     * @param str 多个字符串
     * @return 如果有一个为空返回false 如果全不是空返回true
     */
    public static boolean judgeIsAllNotNull(String... str) {
        for (String item : str) {
            //判断是不是
            if (judgeIsStrNull(item)) {
                //如果有一个为空 那么返回 false
                return  false;
            }
        }
        return true;
    }


//    /**
//     * 判断传入组件的文本内容是否全部为非空
//     * @param tv
//     * @return
//     */
//    public  static boolean judgeComponentAllIsNotNull(TextView... tv){
//
//    }



    /**
     * 组件 component
     * 获取组件内容的字符串
     * @param tv  获取组件内容的字符串
     * @return   文本内容的字符串
     */
    public static String componentGetString(TextView tv){
        return tv.getText().toString().trim();
    }


    /**
     * 网络
     * 请求接口内容                         get interface content
     * 注意必须接口返回类型为json            result interface content type is json
     *
     * @param path 网络接口地址             network interface path
     * @return 得到json  失败 null          success result json error null
     */
    public static JSONObject getNetworkUrlContent(String path) {
        //取流
        InputStream is = getNetworkUrlInputStream(path);
        //转字符串
        String json = conversionStreamToString(is);
        return conversionStringToJSONObject(json);
    }

    /**
     * 网络
     * 根据传入url 获取流
     * 默认3000超时 GET请求
     *
     * @param path 网络接口地址
     * @return 流
     */
    public static InputStream getNetworkUrlInputStream(String path) {
        //用于返回
        InputStream is = null;
        try {
            //创建url对象
            URL url = new URL(path);
            //获取表示到 URL 所引用的远程对象的连接
            //如果 URL 的协议（例如，HTTP 或 JAR）存在属于以下包或其子包之一的公共、专用 URLConnection
            // 子类：java.lang、java.io、java.util、java.net，返回的连接将为该子类的类型。
            // 例如，对于 HTTP，将返回 HttpURLConnection，对于 JAR，将返回 JarURLConnection。
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置conn超时时间
            conn.setConnectTimeout(3000);
            //设置请求方法
            conn.setRequestMethod("GET");
            //获取状态码
            int code = conn.getResponseCode();
            //成功
            if (code == 200) {
                //实例化
                is = conn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return is;
        }
    }


    /**
     * 类型转换
     * 把一个流里面的内容
     * 转化成一个字符串
     *
     * @param is 流里面的内容
     * @return null解析失败
     */
    public static String conversionStreamToString(InputStream is) {
        try {
            //可以捕获内存缓冲区的数据，转换成字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //创建位数组 用来读is数据
            byte[] buffer = new byte[1024];
            //标识
            int len = -1;
            //len 每次被赋值为is结果 然后用buffer来读 只要不是-1(结束)
            while ((len = is.read(buffer)) != -1) {
                // 是将buffer中的数据，写入到对象byteArrayOutputStream中，
                // byteArrayOutputStream.toByteArray()方法返回Byte数组
                byteArrayOutputStream.write(buffer, 0, len);
            }
            //is流关闭
            is.close();
            //返回数组(字符串)
            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 类型转换
     * 字符串转换json
     *
     * @param json json字符串
     * @return json对象
     */
    public static JSONObject conversionStringToJSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            //转换
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }
    }

    /**
     * 类型转换
     * R资源转换String
     *
     * @param activity 传入调用activity
     * @param rid      R资源
     * @return String
     */
    public static String conversionRIdTOString(Context activity, int rid) {
        return activity.getResources().getString(rid);
    }

}
