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

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.service.GoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	private GoodsService goodsService;
	
	public GoodsController(GoodsService goodsService) {
		this.goodsService = goodsService;
	}
	
	
	@GetMapping("/removeGoods")
	public String removeGoods(@RequestParam(name = "goodsCode", required = false)String goodsCode
							 ,@RequestParam(name = "goodsName", required = false)String goodsName
							 ,@RequestParam(name = "goodsSellerId", required = false)String goodsSellerId
							 ,Model model) {
		
		model.addAttribute("goodsCode", goodsCode);
		model.addAttribute("goodsName", goodsName);
		model.addAttribute("goodsSellerId", goodsSellerId);
		
		return "goods/removeGoods";
	}
	
	@PostMapping("/addGoods")
	public String addGoods(Goods goods) {
		
		log.info("goods : {}", goods);
		
		goodsService.addGoods(goods);
		
		return "redirect:/goods/goodsList";
	}
	
	@GetMapping("/addGoods")
	public String addGoods() {
		return "goods/addGoods";
	}
	
	@PostMapping("/goodsList")
	public String getSearchGoodsList(@RequestParam(name = "searchKey", required = false)String searchKey
									,@RequestParam(name = "searchValue", required = false)String searchValue
									,Model model) {
		
		if("goodsSellerId".equals(searchKey)) {
			searchKey = "g.g_seller_id";
		}else if("goodsName".equals(searchKey)) {
			searchKey = "g.g_name";
		}
		
		List<Goods> goodsList = goodsService.getSearchGoodsList(searchKey, searchValue);
		log.info("search goodsList : {}", goodsList);
		model.addAttribute("goodsList", goodsList);
		
		return "goods/goodsList";
	}
	
	@GetMapping("/goodsList")
	public String getGoodsList(Model model) {
		
		List<Goods> goodsList = goodsService.getGoodsList();
		log.info("goodsList : {}", goodsList);
		model.addAttribute("goodsList", goodsList);
		
		return "goods/goodsList";
	}
}
