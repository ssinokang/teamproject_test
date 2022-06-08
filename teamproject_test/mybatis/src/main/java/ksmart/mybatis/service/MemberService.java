package ksmart.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;
import ksmart.mybatis.mapper.MemberMapper;

/*서비스(비즈니스로직)임을 명시해야 함 bean 등록을 위해 / 트랜젝션 처리*/
@Service
@Transactional
public class MemberService {
	
	// DI (의존성 주입)
	// 1. 필드주입방식 - 참조 문제로 사용하지 않는다(순환 참조 - 매퍼와 서비스가 순환적으로 계속 호출하는 상황이 생겨서 루프가 생겨 메모리가 뻗어버림)
	/*
	 @Autowired
	 private MemberMapper memberMapper;
	 */
	
	// 2. setter 메소드 주입방식
	/*
	 @Autowired
	 private MemberMapper memberMapper;
	  
	 public void setMemberMapper(MemberMapper memberMapper) { 
	 	this.memberMapper = memberMapper;
	 }
	 */
	
	// 3. 생성자 메소드 주입 방식 - 가장 많이 쓰임
	private final MemberMapper memberMapper;
	
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper; 
	}
	
	/**
	 * 로그인 이력 조회(페이징 처리)
	 */
	public Map<String, Object> getLoginHistory(int currentPage){
		
		// 몇개 보여줄지
		int rowPerPage = 5;
		
		// 총 행의 개수
		double rowCount = memberMapper.getLoginHistoryCount();
		
		// 마지막 페이지
		int lastPage = (int)Math.ceil(rowCount/rowPerPage);
		
		//페이징 처리
		int startRow = (currentPage - 1) * rowPerPage;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startRow", startRow);
		paramMap.put("rowPerPage", rowPerPage);
		
		int startPageNum = 1;
		int endPageNum = 10;
		
		if(lastPage > 10) {
			if(currentPage >= 6) {
				startPageNum = currentPage - 4;
				endPageNum = currentPage + 5;
				
				if(endPageNum >= lastPage) {
					startPageNum = lastPage - 9;
					endPageNum = lastPage;
				}
			}
		}else {
			endPageNum = lastPage;
		}
			
		List<Map<String, Object>> loginHistoryList = memberMapper.getLoginHistory(paramMap);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("lastPage", 			lastPage);
		resultMap.put("loginHistoryList",	loginHistoryList);
		resultMap.put("startPageNum",		startPageNum);
		resultMap.put("endPageNum",			endPageNum);
		
		return resultMap;
	}
	
	/**
	 * 판매자 상품목록 조회
	 */
	public List<Member> getSellerInfoList(){
		
		List<Member> sellerInfoList = memberMapper.getSellerInfoList();
		
		return sellerInfoList;
	}
	
	/**
	 * 회원 전체 정보 조회
	 */
	public List<Member> getMemberInfoList(){
		
		List<Member> memberInfoList = memberMapper.getMemberInfoList();
		
		return memberInfoList;
	}
	
	/**
	 * 회원 탈퇴
	 * @param searchKey
	 * @param searchValue
	 * @return
	 */
	public List<Member> getSearchMemberList(String searchKey, String searchValue){
		List<Member> searchMemberList = memberMapper.getSearchMemberList(searchKey, searchValue);
		
		return searchMemberList;
	}
	
	/**
	 * 회원탈퇴
	 * @return
	 */
	public boolean removeMember(String memberId, String memberPw) {
		
		boolean memberCheck = false;
		
		Member member = memberMapper.getMemberInfoById(memberId);
		
		if(member != null) {
			String memberPwCheck = member.getMemberPw();
			String memberLevel = member.getMemberLevel();
			
			if(memberPw.equals(memberPwCheck)) {
					memberCheck = true;
					
				//삭제 로직
				/*
				 * if(memberLevel != null) { 
				 * //null 허용이기 때문 -> 스키마 구조를 잘 봐야함!
				 * //null인데 관리자가 실수로 권한을 허용했다면 삭제할게 달라지겠지! 
				 */
				
				//등급 별로 삭제
				/*if("1".equals(memberLevel)) {
					//관리자 : (tb_member, tb_login)
					//중복되는 부분을 빼면 필요가 없네!
				}*/
					
				if("2".equals(memberLevel)) {
					//판매자 : (tb_member, tb_login,)
					// 1.tb_order(상품코드에 연관된 튜플 삭제)
					memberMapper.removeOrderByGCode(memberId);
					// 2.tb_goods(판매자가 등록한 상품 목록 삭제)
					memberMapper.removeGoodsById(memberId);
				}else if("3".equals(memberLevel)) {
					//구매자 : (tb_member, tb_login,)
					// 1.tb_order(구매자가 구매한 주문내역 삭제)
					memberMapper.removeOrderById(memberId);
				}
					/*
				 else { 
				 	//회원 : (tb_member, tb_login)
				 	//중복되는 부분을 빼면 필요가 없네!
				}*/
				//중복되는 부분 :
				// 1.tb_login(회원이 로그인한 이력 삭제)
				memberMapper.removeLoginHistoryById(memberId);
				// 2.tb_member(회원 탈퇴)
				memberMapper.removeMemberById(memberId);
			}
		}
		return memberCheck;
	}
	
	/**
	 * 회원 업데이트
	 * @param member
	 * @return
	 */
	public int modifyMember(Member member) {
		
		int result = memberMapper.modifyMember(member);
		
		return result;
	}
	
	/**
	 * 회원 상세정보
	 */
	public Member getMemberInfoById(String memberId) {
		
		Member member = memberMapper.getMemberInfoById(memberId);
		
		return member;
	}
	
	/**
	 * 회원 가입
	 * @param member
	 * @return
	 */
	public int addMember(Member member) {
		
		int result = memberMapper.addMember(member);
		
		return result;
	}
	
	/**
	 * 회원 등급 목록 조회
	 * @return
	 */
	public List<MemberLevel> getMemberLevelList(){
		
		List<MemberLevel> memberLevelList = memberMapper.getMemberLevelList();
		
		return memberLevelList;
	}
	/**
	 * 회원 목록 조회
	 * @return 회원 전체 목록 List<Membr>
	 */
	public List<Member> getMemberList(){
		
		List<Member> memberList = memberMapper.getMemberList();
		
		if(memberList != null) {
			/**
			int memberSize = memberList.size();
			for(int i=0; i < memberSize; i++) {
				if(memberList.get(i).getMemberLevel().equals("1")) {
					memberList.get(i).setMemberLevel("관리자");
				}else if(memberList.get(i).getMemberLevel().equals("2")) {
					memberList.get(i).setMemberLevel("판매자");
				}else if(memberList.get(i).getMemberLevel().equals("3")) {
					memberList.get(i).setMemberLevel("구매자");
				}
			}
			 * 
			 * 
			 */
			
			//향상된 for문
			for(Member member : memberList) {
				String memberLevel = member.getMemberLevel();
				//equals 는 String에서 쓸 수 있으므로 null 인지 확인
				if(memberLevel != null) {
					if("1".equals(memberLevel)) {
						member.setMemberLevel("관리자");
					}else if("2".equals(memberLevel)) {
						member.setMemberLevel("판매자");
					}else if("3".equals(memberLevel)) {
						member.setMemberLevel("구매자");
					}else {
						member.setMemberLevel("일반회원");
					}
				}
			}
		}
		
		return memberList;
	}
}
