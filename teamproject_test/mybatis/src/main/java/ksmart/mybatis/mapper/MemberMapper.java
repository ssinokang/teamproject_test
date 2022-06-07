package ksmart.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;

/*매퍼임을 명시해야 함 bean 등록을 위해*/
@Mapper
public interface MemberMapper {
	
	//로그인 이력 조회(페이징)
	public List<Map<String, Object>> getLoginHistory(Map<String, Object> paramMap);
	
	//로그인 이력테이블 총 row(튜플) 수
	public int getLoginHistoryCount();
	
	//판매자 목록 조회 (복잡한 결과 맵핑 has many 관계 : collection)
	public List<Member> getSellerInfoList();
	
	//회원목록조회 (복잡한 결과 맵핑 has one 관계 : association)
	public List<Member> getMemberInfoList();
	
	//회원 검색
	public List<Member> getSearchMemberList(String searchKey, String searchValue);
	
	//회원 삭제
	//tb_order(상품코드에 연관된 튜플 삭제 - join) - 판매자
	public int removeOrderByGCode(String memberId);
	//tb_goods(판매자가 등록한 상품 목록 삭제) - 판매자
	public int removeGoodsById(String memberId);
	//tb_order(구매자가 구매한 주문내역 삭제) - 구매자
	public int removeOrderById(String memberId);
	//tb_login(회원이 로그인한 이력 삭제) - 회원
	public int removeLoginHistoryById(String memberId);
	//tb_member(회원 탈퇴) - 회원
	public int removeMemberById(String memberId);
	
	
	//회원 수정
	public int modifyMember(Member member);
	
	//회원 상세정보 조회
	public Member getMemberInfoById(String memberId);
	
	//회원 등록
	// insert , update, delete 는 익스큐트 업데이트 사용 했지? 리턴 타입은 int 였지? 그래서 매퍼의 리턴 타입도 int!
	public int addMember(Member mamber);
	
	/*자바단에서 리턴할 수 있는 리턴타입을 지정해야함*/
	// 회원 목록 조회
	public List<Member> getMemberList();
	
	// 회원 등급 조회
	public List<MemberLevel> getMemberLevelList();
}
