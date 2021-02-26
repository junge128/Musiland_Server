package com.musicland.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.musicland.db.User;
import com.musicland.db.Song;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class DataBaseManage {
	public static final String url = "jdbc:mysql://127.0.0.1:3306/musicland?characterEncoding=utf8";
	public static final String driver = "com.mysql.jdbc.Driver";
	public static final String user = "musicland";
	public static final String password = "root";
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	
	public DataBaseManage(String sql){
		try {
			Class.forName(driver);
			connection = (Connection) DriverManager.getConnection(url, user, password);
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean login(User user){
		String sql = "select * from user where username=? and password=?";
		DataBaseManage dbHelper = new DataBaseManage(sql);
		boolean result = false;
		try {
			dbHelper.preparedStatement.setString(1, user.getUsername());
			dbHelper.preparedStatement.setString(2, user.getPassword());
			System.out.println(dbHelper);
			ResultSet rel = dbHelper.preparedStatement.executeQuery();
			if(rel.next()){
				result = true;
			}else{
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static ArrayList<Song> GetLoveSong(String username){
		ArrayList<Song> list = new ArrayList<>();
		String sql = "select * from love_song where username=?";
		DataBaseManage dbHelper = new DataBaseManage(sql);
		try {
			dbHelper.preparedStatement.setString(1, username);
			ResultSet rel = dbHelper.preparedStatement.executeQuery();
			while(rel.next()){
				Song song = new Song();
				song.setId(rel.getInt("id"));
				song.setUsername(rel.getString("username"));
				song.setSong_name(rel.getString("song_name"));
				song.setSong_artist(rel.getString("song_artist"));
				song.setSong_time(rel.getString("song_time"));
				song.setSong_id(rel.getString("song_id"));
				song.setSong_img(rel.getString("song_img"));
				list.add(song);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<Song> GetHistorySong(String username){
		ArrayList<Song> list = new ArrayList<>();
		String sql = "select * from history_song where username=?";
		DataBaseManage dbHelper = new DataBaseManage(sql);
		try {
			dbHelper.preparedStatement.setString(1, username);
			ResultSet rel = dbHelper.preparedStatement.executeQuery();
			while(rel.next()){
				Song song = new Song();
				song.setId(rel.getInt("id"));
				song.setUsername(rel.getString("username"));
				song.setSong_name(rel.getString("song_name"));
				song.setSong_artist(rel.getString("song_artist"));
				song.setSong_time(rel.getString("song_time"));
				song.setSong_id(rel.getString("song_id"));
				song.setSong_img(rel.getString("song_img"));
				list.add(song);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean register(User user){
		boolean flag = false;
		flag = check_is_repeat(user);
		if(flag == true){
			String sql = "insert into user(username,password) values(?,?)";
			DataBaseManage dbHelper = new DataBaseManage(sql);
			try {
				dbHelper.preparedStatement.setString(1, user.getUsername());
				dbHelper.preparedStatement.setString(2, user.getPassword());
				int a = dbHelper.preparedStatement.executeUpdate();
				if (a>0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public static boolean check_is_repeat(User user){
		String sql = "select * from user where username=?";
		DataBaseManage dbHelper = new DataBaseManage(sql);
		Boolean result = null;
		try {
			dbHelper.preparedStatement.setString(1, user.getUsername());
			ResultSet rel = dbHelper.preparedStatement.executeQuery();
			result = !rel.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回true为new user
		return result;
	}
	
	public static boolean AddSong(Song song){
		boolean flag = false;
		flag = check_is_repeat_song("love_song",song.getSong_id());
		if(flag == true){
			String sql = "insert into love_song(username,song_name,song_artist,song_time,song_id,song_img) values(?,?,?,?,?,?)";
			DataBaseManage dbHelper = new DataBaseManage(sql);
			try {
				dbHelper.preparedStatement.setString(1, song.getUsername());
				dbHelper.preparedStatement.setString(2, song.getSong_name());
				dbHelper.preparedStatement.setString(3, song.getSong_artist());
				dbHelper.preparedStatement.setString(4, song.getSong_time());
				dbHelper.preparedStatement.setString(5, song.getSong_id());
				dbHelper.preparedStatement.setString(6, song.getSong_img());
				int a = dbHelper.preparedStatement.executeUpdate();
				if (a>0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	
	public static boolean AddHistory(Song song){
		boolean flag = true;
		//flag = check_is_repeat_song("history_song",song.getSong_id());
		if(flag == true){
			String sql = "insert into history_song(username,song_name,song_artist,song_time,song_id,song_img) values(?,?,?,?,?,?)";
			DataBaseManage dbHelper = new DataBaseManage(sql);
			try {
				dbHelper.preparedStatement.setString(1, song.getUsername());
				dbHelper.preparedStatement.setString(2, song.getSong_name());
				dbHelper.preparedStatement.setString(3, song.getSong_artist());
				dbHelper.preparedStatement.setString(4, song.getSong_time());
				dbHelper.preparedStatement.setString(5, song.getSong_id());
				dbHelper.preparedStatement.setString(6, song.getSong_img());
				int a = dbHelper.preparedStatement.executeUpdate();
				
				if (a>0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public static boolean check_is_repeat_song(String table,String song_id){
		String sql = "select * from love_song where song_id=?";
		if(table == "history_song"){
			sql = "select * from history_song where song_id=?";
		}
		DataBaseManage dbHelper = new DataBaseManage(sql);
		Boolean result = null;
		try {
			dbHelper.preparedStatement.setString(1, song_id);
			System.out.println(dbHelper.preparedStatement);
			ResultSet rel = dbHelper.preparedStatement.executeQuery();
			result = !rel.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回true为new love_song
		return result;
	}
	
}
