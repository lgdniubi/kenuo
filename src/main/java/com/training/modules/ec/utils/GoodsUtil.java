package com.training.modules.ec.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.modules.ec.entity.GoodsAttributeMappings;
import com.training.modules.ec.entity.GoodsSpecImage;
import com.training.modules.ec.entity.GoodsSpecItem;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.service.GoodsSpecItemService;

/**
 * 商品帮助类
 * @author kele
 * @version 2016-6-24
 */
public class GoodsUtil {

	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(GoodsUtil.class);
	
	/**
	 * 修改时，查询赋值。
	 * 多个规格项数组排列组合（生成排列后的table内容）
	 * @param list	所有规格项数组集合
	 * @param arr	第一个规格项数组
	 * @param str	迭代时，规格项ID
	 * @param tablecontent	table内容
	 * @param goodsSpecItemService	规格项查询Service
	 */
	public static void updategoodsSpecItem(List<String[]> list, String[] arr, String str,List<GoodsSpecPrice> gspList, StringBuffer tablecontent,GoodsSpecItemService goodsSpecItemService) {
		for (int i = 0; i < list.size(); i++) {
			
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					
					st = str + st +"_";
					
					if (i < list.size() - 1) {
						updategoodsSpecItem(list, list.get(i + 1), st,gspList,tablecontent,goodsSpecItemService);
					} else if (i == list.size() - 1) {
												
						String specgroup = "";
						String specNameList = "";
						String stvalue = (String) st.substring(0, st.length()-1);
						
						GoodsSpecItem gsi = new GoodsSpecItem();
						String[] itemstr = stvalue.split("_");
						if(itemstr.length >= 1){
							Integer[] itemInt = new Integer[itemstr.length];
							for (int j = 0; j < itemstr.length; j++) {
								itemInt[j] = Integer.parseInt(itemstr[j]);
							}
							gsi.setSpecItemIdList(itemInt);
						}
						
						//根据规格项ID数组，子查询所有规格项内容
						List<GoodsSpecItem> specItemsList = goodsSpecItemService.findSpecItems(gsi);
						for (int j = 0; j < specItemsList.size(); j++) {
							gsi = specItemsList.get(j);
							tablecontent.append("<td>"+gsi.getItem()+"</td>");
							specgroup = specgroup +"+"+ gsi.getItem();
							specNameList = specNameList + gsi.getSpecName() + "_";
						}
						specgroup = specgroup.substring(specgroup.indexOf("+")+1, specgroup.length());
						specNameList = (String) specNameList.substring(0, specNameList.length()-1);
						GoodsSpecPrice gsp = checkObject(stvalue,gspList);
						logger.debug("#####[gsp]:"+ gsp);
						
						if(null != gsp){
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][price]\" value=\""+gsp.getPrice()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][market_price]\" value=\""+gsp.getMarketPrice()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][cost_price]\" value=\""+gsp.getCostPrice()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][cargo_price]\" value=\""+gsp.getCargoPrice()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][purchase_price]\" value=\""+gsp.getPurchasePrice()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][store_count_view]\" disabled=\"disabled\" value=\""+gsp.getStoreCount()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][bar_code]\" value=\""+gsp.getBarCode()+"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][goods_No]\" value=\""+gsp.getGoodsNo()+"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][suplier_goods_no]\" value=\""+gsp.getSuplierGoodsNo()+"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][goods_weight]\" value=\""+gsp.getGoodsWeight()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][service_times]\" value=\""+gsp.getServiceTimes()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" maxlength=\"3\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][expiring_date]\" value=\""+gsp.getExpiringDate()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" maxlength=\"2\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<input type=\"hidden\" name=\"item["+stvalue+"][key_name]\" value=\""+specgroup+"\" /><input type=\"hidden\" name=\"item["+stvalue+"][spec_name]\" value=\""+specNameList+"\" /></td>");
							tablecontent.append("<input name=\"item["+stvalue+"][store_count]\" type=\"hidden\" value=\""+gsp.getStoreCount()+"\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
						}else{
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][price]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][market_price]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][cost_price]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][cargo_price]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][purchase_price]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][store_count_view]\" disabled=\"disabled\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][bar_code]\" value=\"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][goods_No]\"  value=\"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][suplier_goods_no]\" value=\"\" /> ");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][goods_weight]\"  value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][service_times]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" maxlength=\"3\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
							tablecontent.append("<td><input style='width: 100%;' name=\"item["+stvalue+"][expiring_date]\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" maxlength=\"2\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/></td>");
							tablecontent.append("<input type=\"hidden\" name=\"item["+stvalue+"][key_name]\" value=\""+specgroup+"\" /><input type=\"hidden\" name=\"item["+stvalue+"][spec_name]\" value=\""+specNameList+"\" /></td>");
							tablecontent.append("<input name=\"item["+stvalue+"][store_count]\" type=\"hidden\" value=\"0\" onkeyup=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onpaste=\"this.value=this.value.replace(/[^\\d.]/g,&quot;&quot;)\" onfocus=\"if(value == '0')value=''\" onblur=\"if(this.value == '')this.value='0';\"/>");
						}
						tablecontent.append("</tr>");//结尾
					}
				}
			}
		}
	} 	
	
	/**
	 * 多个规格项数组排列组合(获取排列后的值)
	 * @param list	所有规格项数组集合
	 * @param arr	第一个规格项数组
	 * @param str	迭代时，规格项ID
	 * @param specitemlist	排列后的值
	 * @param goodsSpecItemService
	 */
	public static void goodsSpecItem(List<String[]> list, String[] arr, String str,List<String> specitemlist,GoodsSpecItemService goodsSpecItemService) {
		for (int i = 0; i < list.size(); i++) {
			String stvalue = "";
			// 取得当前的数组
			if (i == list.indexOf(arr)) {
				// 迭代数组
				for (String st : arr) {
					//根据规格项ID，查询其规格名称与其规格项名称
					GoodsSpecItem goodsSpecItem = new GoodsSpecItem();
					goodsSpecItem.setSpecItemId(Integer.parseInt(st));
					goodsSpecItem = goodsSpecItemService.get(goodsSpecItem);
					st = str + st +"_";
					if (i < list.size() - 1) {
						goodsSpecItem(list, list.get(i + 1), st,specitemlist,goodsSpecItemService);
					} else if (i == list.size() - 1) {
						stvalue = (String) st.substring(0, st.length()-1);
						specitemlist.add(stvalue);
					}
				}
			}
		}
	}
	
	/**
	 * 商品规格、属性修改时，校验对象是否存在
	 * @param stvalue
	 * @param gspList
	 * @return
	 */
	public static GoodsSpecPrice checkObject(String stvalue,List<GoodsSpecPrice> gspList){
		if(null != stvalue && null != gspList && gspList.size() > 0){
			for (int i = 0; i < gspList.size(); i++) {
				GoodsSpecPrice gsp = gspList.get(i);
				if(stvalue.equals(gspList.get(i).getSpecKey())){
					return gsp;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据属性ID，获取属性值
	 * @param typeId
	 * @param list
	 * @return
	 */
	public static GoodsAttributeMappings getAttributeObject(String attrId,List<GoodsAttributeMappings> list){
		for (int i = 0; i < list.size(); i++) {
			GoodsAttributeMappings gam = list.get(i);
			if(attrId.equals(String.valueOf(gam.getAttrId()))){
				return gam;
			}
		}
		return null;
	}
	
	/**
	 * 根据规格项ID，获取规格图片
	 * @param specItemId
	 * @param list
	 * @return
	 */
	public static GoodsSpecImage getSpecImgObject(String specItemId,List<GoodsSpecImage> list){
		for (int i = 0; i < list.size(); i++) {
			GoodsSpecImage gsi = list.get(i);
			if(specItemId.equals(String.valueOf(gsi.getSpecItemId()))){
				return gsi;
			}
		}
		return null;
	}
	
	
	/**
	 * list数组，去重复
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicateWithOrder(List<String> list) {
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add((String) element))
                newList.add((String) element);
        }
        return newList;
    }
	
	/**
	 * 去除map中value为null或者空的值
	 * 排序
	 * @param map
	 * @return
	 */
	public static Map<String, List<String>> removeMapNull(Map<String, List<String>> map){
		Map<String, List<String>> mapbegin = new LinkedHashMap<String, List<String>>();
		Map<String, List<String>> mapend = new LinkedHashMap<String, List<String>>();
		
		List<Integer> keylist = new ArrayList<Integer>();
		for(Entry<String, List<String>> entry: map.entrySet()) {
			if(entry.getValue().size() != 0){
				mapbegin.put(entry.getKey(), entry.getValue());
				keylist.add(Integer.parseInt(entry.getKey()));
			}
		}
		//排序
		Collections.sort(keylist);
		
		for (int i = 0; i < keylist.size(); i++) {
			String key = String.valueOf(keylist.get(i));
			List<String> value = mapbegin.get(key);
			mapend.put(key, value);
		}
		
		//排序
		return mapend;
	}
	
	/**
	 * list数组，去重复
	 * @param list
	 * @return
	 */
	public static List<String[]> removeListStringNull(List<String[]> list) {
		List<String[]> strlist = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {
			String[] s = list.get(i);
			List<String> ls = new ArrayList<String>();
			for (int j = 0; j < s.length; j++) {
				if(null != s[j] && !"".equals(s[j])){
					ls.add(s[j]);
				}
			}
			String[] s1 = new String[ls.size()];
			for (int j = 0; j < ls.size(); j++) {
				s1[j] = ls.get(j);
			}
			strlist.add(s1);
		}
		return strlist;
    }
	
	
	/**
	 * 获取规格项的值，并去重
	 * @param list
	 * @return
	 */
	public static List<String> getitemsvalue(List<String[]> list){
		Set<String> set = new HashSet<String>();
		List<String> newlist = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String[] s = list.get(i);
			if(s.length > 1){
				for (int j = 0; j < s.length; j++) {
					if (set.add(s[j])){
						newlist.add(s[j]);
					}
				}
			}else{
				if (set.add(s[0])){
					newlist.add(s[0]);
				}
			}
		}
		return newlist;
	}
	
	
	public static void main(String[] args) {
		/*String[] s1 = new String[5];
		s1[0] = "1";
		s1[1] = "2";
		
		String[] s2 = new String[2];
		s2[0] = "14";
		
		
		List<String[]> strlist = new ArrayList<String[]>();
		strlist.add(s1);
		strlist.add(s2);
		
		List<String[]> slist = removeListStringNull(strlist);
		System.out.println(slist);*/
		
	}
}
