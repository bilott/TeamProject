package com.kikaz.project.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KakaoAPI {
   
   //토큰얻어오는 메소드
   public String getAccessToken(String code) {
      String accessToken = "";
      String refreshToken = "";
      String reqURL = "https://kauth.kakao.com/oauth/token";
      
      
      try {
         URL url = new URL(reqURL);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("POST");
         conn.setDoOutput(true);
         
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
         StringBuilder sb = new StringBuilder();
         sb.append("grant_type=authorization_code");
         sb.append("&client_id=d26bfdb2f6e276111cc3db2a11eec26b");
         sb.append("&redirect_uri=http://localhost:9999/login");
         sb.append("&code="+code);
         
         bw.write(sb.toString());
         bw.flush();
         
         int responseCode = conn.getResponseCode();
         System.out.println("response code = "+ responseCode);
         
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String line = "";
         String result = "";
         
         while((line = br.readLine())!=null){
            result +=line;
         }
         System.out.println("response body = "+result);
         
         JsonParser parser = new JsonParser();
         JsonElement element = parser.parse(result);
         
         accessToken = element.getAsJsonObject().get("access_token").getAsString();
         refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
         
         System.out.println("accessToken+++++++"+accessToken);
         System.out.println("refreshToken++++++"+refreshToken);
         
         br.close();
         bw.close();
         
      }catch (Exception e) {
         e.printStackTrace();
      }
      return accessToken;
   }
   
   //로그인할때 유저정보를 가져오는 메소드
   public HashMap<String, Object> getUserInfo(String accessToken) {
      HashMap<String, Object> userInfo = new HashMap<String, Object>();
      String reqUrl = "https://kapi.kakao.com/v2/user/me";
      try {
         URL url =new URL(reqUrl);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("POST");
         conn.setRequestProperty("Authorization","Bearer "+accessToken);
         
         int responsecode = conn.getResponseCode();
         
         System.out.println("responsecode : "+responsecode);
         
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         
         String line = "";
         String result = "";
         
         while((line = br.readLine())!=null) {
            result += line;
         }
         System.out.println("response body 2= "+result);
         
         JsonParser parser = new JsonParser();
         JsonElement element = parser.parse(result);
      
         JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
         JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
         
         
         System.out.println("=======properties"+properties);
         System.out.println("=======kakaoAccount"+kakaoAccount);
         
         
         String nickname = properties.getAsJsonObject().get("nickname").getAsString();
         String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
         
         System.out.println("======nickname"+nickname);
         System.out.println("======email"+email);
         
         userInfo.put("nickname", nickname);
         userInfo.put("email", email);
         
      }catch (Exception e) {
         e.printStackTrace();
      }
      return userInfo;
   }
   
   //얘를 빼도 잘돌아감 그냥 인덱스에서 카카오로그아웃으로 보내서 그냥돌아감 
   //로그아웃 메소드 근데 되는건지 모르겠음
//   public void kakaoLogout(String accessToken) {
//      String reqURL = "http://kapi.kakao.com/v1/user/logout";
//      try {
//         URL url = new URL(reqURL);
//         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         conn.setRequestMethod("POST");
//         conn.setRequestProperty("Authorization", "Bearer "+accessToken);
//         
//         System.out.println("======accessToken+"+accessToken);
//         System.out.println(conn);
//         
//         int responseCode = conn.getResponseCode();
//         System.out.println("responserCode = "+responseCode);
//         
//         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//         
//         String result = "";
//         String line = "";
//         
//         while((line = br.readLine())!=null) {
//            result+=line;
//         }
//         System.out.println(result);
//         
//      }catch (Exception e) {
//         e.printStackTrace();
//      }
//      
//   }
   
   



}