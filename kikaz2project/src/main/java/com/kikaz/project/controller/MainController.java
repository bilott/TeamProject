package com.kikaz.project.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.kikaz.project.model.Reservation;
import com.kikaz.project.repository.CompanyRepository;
import com.kikaz.project.repository.ReservationRepository;
import com.kikaz.project.service.KakaoAPI;
import com.kikaz.project.service.ReservationService;

import java.util.Locale;


@Controller
public class MainController {
	@Autowired
	ReservationService reService;
	@Autowired
	ReservationRepository reRepository;
	@Autowired
	CompanyRepository comRepository;

	KakaoAPI kakaoapi = new KakaoAPI();

	
	@GetMapping("/")
	public String main1(Model model,HttpSession session) {
		
		model.addAttribute("userId", session.getAttribute("userId"));
		
		return "main";
	}

	@GetMapping("/ress")
	public String res(Model model) {

		model.addAttribute("company", comRepository.findAll());
		return "ress";
	}

	@GetMapping("/payment")
	public String payment(Model model, Reservation reservation) {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; // hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

		reservation.setCompany(comRepository.findById(reservation.getCid()).orElse(null));
		reservation.setPrice((reservation.getAdult_num() * 6000) + (reservation.getChild_num() * 20000));
		reservation.setRid(formatter.format(today));
		model.addAttribute("res", reservation);
		return "payment";
	}

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("res", reRepository.findAll());

		return "/list";
	}

	@GetMapping("/main")
	public String main(Model model) {
		model.addAttribute("company", comRepository.findAll());
		return "reservation";
	}

	@GetMapping("/map")
	public String login() {
		return "map";
	}

	

	@PostMapping("/insert")
	@ResponseBody
	public ResponseEntity<Reservation> insert(@RequestBody Reservation reservation) {

		reservation.setCompany(comRepository.findById(reservation.getCid()).orElse(null));
		System.out.println(reservation);
		reRepository.save(reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
	}

	// 카카오로그인 api관련된거

	// 카카오로그인
	@RequestMapping(value = "/login")
	public ModelAndView login(@RequestParam("code") String code, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		// 1. 인증코드 요청
		String accessToken = kakaoapi.getAccessToken(code);

		// System.out.println("======accessToken"+accessToken);

		// 2. 인증코드로 토큰 전달
		HashMap<String, Object> userInfo = kakaoapi.getUserInfo(accessToken);

		System.out.println("login info : " + userInfo.toString());

		if (userInfo.get("email") != null) {
			session.setAttribute("userId", userInfo.get("email"));
			session.setAttribute("accessToken", accessToken);
		}

		System.out.println("----------Token" + accessToken);

		mav.addObject("userId", userInfo.get("email"));
		
		mav.setViewName("main");
		return mav;
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mav = new ModelAndView();

		// 원래는 api에서 만들어서 해야했지만 그냥 빼서 로그아웃보내도 돌아가긴함
		// kakaoapi.kakaoLogout((String)session.getAttribute("accessToken"));
		session.removeAttribute("accessToken");
		session.removeAttribute("userId");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping(value = "/info")
	public String info(Model model, @RequestParam(value = "rid")String rid) {
		System.out.println("======"+rid);
		Reservation res = reRepository.findByrid(rid);	
		model.addAttribute("res",res);
		return"/information";
	}
	
	//삭제처리하는거
			@PostMapping(value = "/delete")
			public ResponseEntity<?> deleteBoard(@RequestParam("rid") String rid){
				System.out.println(rid);
				try {
					
					reService.deleteByRid(rid);
				}catch(Exception e){
					System.out.println("예외처리==>"+e);
				}
				return new ResponseEntity<>("{}", HttpStatus.OK);
			}

	@GetMapping("/qrex")
	public String prEx() {
		return "qrEx";
	}

	@GetMapping("/qr")
	public Object createQr(@RequestParam String url) throws IOException,Exception {
		int width = 200;
		int height = 200;
		Reservation reservation = reRepository.findById(url).orElse(null);
		String a = "kikaz"+reservation.getRid();
		System.out.println(a);
		BitMatrix matrix = new MultiFormatWriter().encode(a, BarcodeFormat.QR_CODE, width, height);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			MatrixToImageWriter.writeToStream(matrix, "PNG", out);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
		}
	}

}
