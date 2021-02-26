package com.musicland.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.musicland.db.User;
import com.musicland.db.Song;
import com.musicland.db.DataBaseManage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class UserManage
 */
@WebServlet("/UserManage")
public class UserManage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter printWriter;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html,charset='utf-8'");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		printWriter = response.getWriter();
		String method = request.getParameter("method");
		Song song = new Song();
		ArrayList<Song> list = null;
		JSONArray data = null;
		boolean result = false;
		
		switch (method) {
			case "login":
				User user = new User();
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));
				
				boolean flag = DataBaseManage.login(user);
				if(flag){
					printWriter.print("Success");
				}else{
					printWriter.print("Wrong");
				}
				printWriter.flush();
				break;
			case "register":
				User user2 = new User();
				user2.setUsername(request.getParameter("username"));
				user2.setPassword(request.getParameter("password"));
				
				boolean isRegister = DataBaseManage.register(user2);
				String isReg = "";
				if (isRegister) {
					isReg = "success";
				} else {
					isReg = "error";
				}
				printWriter.print(isReg);
				printWriter.flush();
				break;
			case "addsong":
				song.setUsername(request.getParameter("username"));
				song.setSong_name(request.getParameter("song_name"));
				song.setSong_artist(request.getParameter("song_artist"));
				song.setSong_time(request.getParameter("song_time"));
				song.setSong_id(request.getParameter("song_id"));
				song.setSong_img(request.getParameter("song_img"));
				
			    result = DataBaseManage.AddSong(song);
			    
			    if(result){
					printWriter.print("Success");
			    }else{
					printWriter.print("repeat");
			    }
				printWriter.flush();
				break;
			case "addhistory":
				song.setUsername(request.getParameter("username"));
				song.setSong_name(request.getParameter("song_name"));
				song.setSong_artist(request.getParameter("song_artist"));
				song.setSong_time(request.getParameter("song_time"));
				song.setSong_id(request.getParameter("song_id"));
				song.setSong_img(request.getParameter("song_img"));
				
			    result = DataBaseManage.AddHistory(song);
			    
			    if(result){
					printWriter.print("Success");
			    }else{
					printWriter.print("repeat");
			    }
				printWriter.flush();
				break;
			case "GetLoveSong":
				list = DataBaseManage.GetLoveSong(request.getParameter("username"));
				data = JSONArray.fromObject(list);
				printWriter.print(data);
				printWriter.flush();
				break;
			case "GetHistorySong":
				list = DataBaseManage.GetHistorySong(request.getParameter("username"));
				data = JSONArray.fromObject(list);
				printWriter.print(data);
				printWriter.flush();
				break;
			default:
				break;
		 }
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
