package ksmart.mybatis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;
import ksmart.mybatis.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

	
	// DI(의존성 주입) 3. 생성자 메소드 주입 방식
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("sellerInfoList")
	public String getSellerInfoList(Model model) {
		
		List<Member> sellerInfoList = memberService.getSellerInfoList();
		
		log.info("sellerInfoList: {}", sellerInfoList);
		
		model.addAttribute("sellerInfoList", sellerInfoList);
		
		return "member/sellerList";
	}
	
	@GetMapping("/memberInfoList")
	public String getMemberInfoList(Model model) {
		
		List<Member> memberInfoList = memberService.getMemberInfoList();
		
		log.info("memberInfoList: {}", memberInfoList);
		
		model.addAttribute("memberInfoList", memberInfoList);
		
		return "member/memberInfoList";
	}
	
	
	@PostMapping("/memberList")
	public String getSearchMemberList(@RequestParam(name = "searchKey")String searchKey
									 ,@RequestParam(name = "searchValue", required = false)String searchValue
									 ,Model model) {
		
		log.info("searchKey: {}", searchKey);
		log.info("searchValue: {}", searchValue);
		
		if("memberId".equals(searchKey)) {
			searchKey = "m.m_id";
		}else if("memberLevel".equals(searchKey)) {
			searchKey = "m.m_level";
		}else if("memberName".equals(searchKey)) {
			searchKey = "m.m_name";
		}else {
			searchKey = "m.m_email";
		}
		
		List<Member> searchMemberList = memberService.getSearchMemberList(searchKey, searchValue);
		
		if(searchMemberList != null) model.addAttribute("memberList", searchMemberList);
		
		return "member/memberList";
	}
	
	/**
	 * 회원 탈퇴
	 * @param memberId
	 * @param memberPw
	 * @param reAttr  RedirectAttributes : model과 비슷하게 쓰임 URI ?키=값 형식(get방식)으로 보내줌 / URI 에 직접 적으면 호환성 문제로 인코딩 깨질 수도 있음
	 * @return
	 */
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(name="memberId") String memberId
							  ,@RequestParam(name="memberPw") String memberPw
							  ,RedirectAttributes reAttr){
		
		// 비밀번호 일치 시 삭제 : true, 비밀번호 불일치 시 : false
		boolean isRemove = memberService.removeMember(memberId, memberPw);
		
		if(isRemove) {
			return "redirect:/member/memberList";
		}else {
			reAttr.addAttribute("result", "입력하신 회원의 정보가 일치하지 않습니다.");
		}
		
		
		// 리디렉션 주소요청 시 /member/removeMember?memberId=id001 로 들어감
		reAttr.addAttribute("memberId", memberId);
		
		// 일치하지 않았을 때 /member/removeMember?result=입력하신+회원의+정보가+일치하지+않습니다.
		return "redirect:/member/removeMember";
	}
	
	@GetMapping("/removeMember")
	public String removeMember(Model model
								,@RequestParam(name="memberId", required = false) String memberId
								,@RequestParam(name="result", required = false)String result){
		
		model.addAttribute("memberId", memberId);
		log.info("result :  {}", result);
		if(result != null) model.addAttribute("result", result);
		
		return "member/removeMember";
	}
	
	/**
	 * 
	 * @param memberId
	 * @return ajax는 응답 데이터를 요구
	 * http요청페이지의 헤더(network 헤더에서 확인 가능), @ResponseBody 로 특정 객체를 넘기면 알아서 타입에 맞춰(json 등) 파싱하여
	 * http요청페이지의 body(network response에서 확인 가능)로 넘겨주도록 spring 에서 제공
	 */
	@PostMapping("/idCheck")
	@ResponseBody
	public boolean idCheck(@RequestParam(name="memberId") String memberId) {
		
		log.info("아이디 중복 체크: {}", memberId);
		
		//변수명으로 뭘 검사하는지 어떤 내용의 값인지 알려주는게 좋다! 나중에 보더라도 한번에 이해되겠지
		// true : 아이디 중복 o, false : 아이디 중복 x
		boolean isIdCheck = false;
		
		Member member = memberService.getMemberInfoById(memberId);
		
		if(member != null) {
			isIdCheck = true;
		}
		
		return isIdCheck;
		
		/* 리턴 타입 Member로 response 확인
		 * Member member = new Member();
		 * member.setMemberId(memberId);
		 * member.setMemberPw("pw001");
		 * member.setMemberName("홍01");
		 * return member;
		 */
		
		/* 리턴 타입 List<Member>로 response 확인
		 * List<Member> memberList = memberService.getMemberList();
		 * return memberList;
		 */
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		
		log.info("수정화면에서 입력받은 data: {}", member);
		
		memberService.modifyMember(member);
		
		return "redirect:/member/memberList";
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(name = "memberId", required = false) String memberId
								, Model model) {
		
		log.info("화면에서 입력받은 data : {}", memberId);
		Member member = memberService.getMemberInfoById(memberId);
		List<MemberLevel> memberLevelList= memberService.getMemberLevelList();
		
		model.addAttribute("member", member);
		model.addAttribute("memberLevelList", memberLevelList);
		
		//테스트용
		//return "redirect:/";
		return "member/modifyMember";
	}
	
	
	/**
	 * 
	 * 커맨드객체 : http 통신 시 data(key, value) => DTO(맴버변수와 일치 시) 자동으로 바인딩하는 객체 / spring boot 에서 제공 하는 것
	 * String memberId = request.getParameter("memberId")
	 * Member member = new Member();
	 * member.setMemberId(memberId);
	 * 아래 속성 중 name 의 별칭으로 value 사용 가능 -> value="memberId)
	 * @RequestParam(name="memberId") String memberId (==) String memberId = request.getParameter("memberId") 와 같은 의미
	 * -> 객체 생성을 안하고 키가 같을 때 값만 받는 방법 => 메모리 절약되겠지
	 * required = false -> 위와 같이 할 경우엔 매개변수 값이 꼭 들어가야함(null 불가), required = false로 값을 안 넣어도 되도록 설정 가능
	 * defaultValue = "id001" -> 매개변수에 입력값 없을 시 id001을 넣어서 보내준다 / 단, HttpServletRequest request를 기본으로 만들어 졌기 때문에 똑같이 리턴 타입은 String 이다
	 * 즉, defaultValue 의 값도 String만 들어갈 수 있다
	 * @param Member member(커맨드객체)
	 * @return redirect:/    => return 타입은 String 이지만, "redirect:/" == response.sendRedirect("/") 와 같은 의미
	 */
	@PostMapping("/addMember")
	public String addMember(Member member
							,@RequestParam(name="memberId", required = false, defaultValue = "id001") String memberId) {
		log.info("회원가입화면에서 입력한 data : {}", member);
		log.info("회원가입화면에서 입력한 memberId : {}", memberId);
		
		int result = memberService.addMember(member);
		log.info("insert 실행 결과 : {}", result);
		
		return "redirect:/member/memberList";
	}
	
	
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();
		log.info("회원 등급 목록: {}", memberLevelList);
		model.addAttribute("memberLevelList", memberLevelList);
		
		return "member/addMember";
	}
	
	
	/* 
	 * @RequestMapping("/member") 에 의해
	 * 
	 * /member/memberList 형태로
	 * */
	@GetMapping("/memberList")
	public String getMemberList(Model model) {
		
		List<Member> memberList = memberService.getMemberList();
		log.info("회원 전체 목록: {}", memberList);
		model.addAttribute("memberList", memberList);
		
		return "member/memberList";
	}
	
}
