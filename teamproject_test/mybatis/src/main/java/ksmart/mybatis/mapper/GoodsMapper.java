package ksmart.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.dto.Member;

@Mapper
public interface GoodsMapper {
	
	public Member getMember(String goodsSellerId);
	
	public int removeGoods(String goodsCode, String goodsSellerId, String memberPw);
	
	public int addGoods(Goods goods);
	
	public List<Goods> getSearchGoodsList(String searchKey, String searchValue);
	
	public List<Goods> getGoodsList();
}
