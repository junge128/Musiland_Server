package com.musicland.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.musicland.db.DataBaseManage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/GetMusic")
public class GetMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter printWriter;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html,charset='utf-8'");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String method = request.getParameter("method");
		String name = request.getParameter("name");
		String OffSet = request.getParameter("OffSet");
		String result = null;
		JSONObject jsonObject = null;
		
		switch(method){
		    case "song" :
				printWriter = response.getWriter();
				result = HttpPost("http://fm.rpsofts.com/api.php","name=" + name + "&OffSet=" + OffSet);
				result = result.substring(1);
				System.out.println(result);
				jsonObject = JSONObject.fromObject(result);
				printWriter.print(jsonObject);
				printWriter.flush();
				break;
		    case "lyric" :
				printWriter = response.getWriter();
				result = HttpGet("http://api.mtnhao.com/lyric?id=" + name);
				System.out.println(result);
				jsonObject = JSONObject.fromObject(result);
				printWriter.print(jsonObject);
				printWriter.flush();
				break;
			case "GetHotSongs":
				printWriter = response.getWriter();
				result = HttpGet("https://c.y.qq.com/splcloud/fcgi-bin/gethotkey.fcg?g_tk_new_20200303=1840488902&g_tk=1840488902&loginUin=993169459&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0");
				System.out.println(result);
				jsonObject = JSONObject.fromObject(result);
				printWriter.print(jsonObject);
				printWriter.flush();
				break;
		}
		

		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

    public static String HttpGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// ���ؽ���ַ���
        try {
            // ����Զ��url���Ӷ���
            URL url = new URL(httpurl);
            // ͨ��Զ��url���Ӷ����һ�����ӣ�ǿת��httpURLConnection��
            connection = (HttpURLConnection) url.openConnection();
            // �������ӷ�ʽ��get
            connection.setRequestMethod("GET");
            // �������������������ĳ�ʱʱ�䣺15000����
            connection.setConnectTimeout(15000);
            // ���ö�ȡԶ�̷��ص�����ʱ�䣺60000����
            connection.setReadTimeout(60000);
            // ��������
            connection.connect();
            // ͨ��connection���ӣ���ȡ������
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // ��װ������is����ָ���ַ���
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // �������
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // �ر���Դ
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
 
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
 
            connection.disconnect();// �ر�Զ������
        }
 
        return result;
    }
    
    public static String HttpPost(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // ͨ��Զ��url���Ӷ��������
            connection = (HttpURLConnection) url.openConnection();
            // ������������ʽ
            connection.setRequestMethod("POST");
            // ��������������������ʱʱ�䣺15000����
            connection.setConnectTimeout(15000);
            // ���ö�ȡ�����������������ݳ�ʱʱ�䣺60000����
            connection.setReadTimeout(60000);

            // Ĭ��ֵΪ��false������Զ�̷�������������/д����ʱ����Ҫ����Ϊtrue
            connection.setDoOutput(true);
            // Ĭ��ֵΪ��true����ǰ��Զ�̷����ȡ����ʱ������Ϊtrue���ò������п���
            connection.setDoInput(true);
            // ���ô�������ĸ�ʽ:�������Ӧ���� name1=value1&name2=value2 ����ʽ��
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // ���ü�Ȩ��Ϣ��Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // ͨ�����Ӷ����ȡһ�������
            os = connection.getOutputStream();
            // ͨ����������󽫲���д��ȥ/�����ȥ,����ͨ���ֽ�����д����
            os.write(param.getBytes("UTF-8"));
            // ͨ�����Ӷ����ȡһ������������Զ�̶�ȡ
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // ��������������а�װ:charset���ݹ�����Ŀ���Ҫ��������
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // ѭ������һ��һ�ж�ȡ����
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // �ر���Դ
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // �Ͽ���Զ�̵�ַurl������
            connection.disconnect();
        }
        return result;
    }
	
}
