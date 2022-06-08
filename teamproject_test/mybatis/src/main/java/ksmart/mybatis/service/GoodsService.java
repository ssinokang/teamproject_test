package ksmart.mybatis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.mapper.GoodsMapper;

@Service
public class GoodsService {
	
	private GoodsMapper goodsMapper;
	
	public GoodsService(GoodsMapper goodsMapper) {
		this.goodsMapper = goodsMapper;
	}
	
	public int removeGoods(String goodsCode, String goodsSellerId, String memberPw) {
		
		int result = goodsMapper.removeGoods(goodsCode, goodsSellerId, memberPw);
		
		return result;
	}
	
	public int addGoods(Goods goods) {
		
		int result = goodsMapper.addGoods(goods);
		
		return result;
	}
	
	public List<Goods> getSearchGoodsList(String searchKey, String searchValue){
		
		List<Goods> goodsList = goodsMapper.getSearchGoodsList(searchKey, searchValue);
		
		return goodsList;
	}
	
	public List<Goods> getGoodsList() {
		
		List<Goods> goodsList = goodsMapper.getGoodsList();
		
		return goodsList;
	}
}
