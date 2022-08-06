package com.book.recommend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.common.ApiCommonUtil;
import com.book.common.ApiParam;
import com.book.model.Recommend;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Service("recommendService")
public class RecommendServiceImpl implements RecommendService {
	@Value("${aladin.host}")
	String aladinHost;
	@Value("${aladin.accept-category}")
	String aladinAcceptCategory;

	@Autowired
	private RecommendDAO recommendDAO;

	@Autowired
	private RecommendRepository recommendRepository;

	@Override
	public List<Map<String,Object>> getRecommendList(String user_id) throws Exception{
		List<Map<String, Object>> filteredList = recommendDAO.getFilteredList(user_id);
		if(filteredList.size()==0) {
			return null;
		}
			
		List<Map<String,Object>> recommendList = recommendDAO.getRecommendList(filteredList);
		
		for(Map filteredItem : filteredList) {
			long fitem_id = (long)filteredItem.get("item_id");
			ArrayList commentBriefList = new ArrayList();
			ArrayList commentDetailList = new ArrayList();
			
			for(Map recommendItem : recommendList) {
				long ritem_id = (long)recommendItem.get("item_id");  
				if(fitem_id==ritem_id) {
					//filteredItem.put("recommend_brief", recommendItem.get("comment"));
					commentBriefList.add(recommendItem.get("comment"));
					commentDetailList.add(recommendItem.get("comment_detail"));
				}
			}
			filteredItem.put("comment_brief", commentBriefList);
			filteredItem.put("comment_detail", commentDetailList);
		}
		return filteredList;
	}
	@Override
	public List<RecommendVO> recommendList2(String userId){
		String uri = aladinHost + "/ttb/api/ItemList.aspx";
		RestTemplate rt = new RestTemplate();
		ApiParam apiParam = ApiParam.builder().querytype("BestSeller").build();
		URI url = UriComponentsBuilder.fromHttpUrl(uri)
				.queryParams(apiParam.getApiParamMap())
				.encode().build().toUri();
		RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, url);
		ResponseEntity<AladinMaster> response = rt.exchange(requestEntity,new ParameterizedTypeReference<AladinMaster>(){});

		AladinMaster aladinMaster = response.getBody();

		List<String> aladinAcceptCategoryList = Arrays.asList(aladinAcceptCategory.split(","));

		List<AladinBook> aladinBestSellerBooks = aladinMaster.getItem();

		aladinBestSellerBooks.stream().filter(i-> aladinAcceptCategoryList.contains(i.getCategoryId()));

		System.out.println(aladinBestSellerBooks);
		return null;
	}

	@Override
	public Object getSearchBookList(HttpServletRequest request, ApiParam apiParam){
		String query = request.getParameter("query");
		String itemId = request.getParameter("itemId");
		String queryType="Title";

		String uri = aladinHost + "/ttb/api/ItemSearch.aspx";
		RestTemplate rt = new RestTemplate();
		//rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		URI url = UriComponentsBuilder.fromHttpUrl(uri)
				.queryParams(apiParam.getApiParamMap())
				.encode().build().toUri();
		RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, url);
		ResponseEntity response = rt.exchange(requestEntity,new ParameterizedTypeReference<>(){});
		if(!ApiCommonUtil.isConnected(response)){
			return null;
		}

		return response.getBody();
	}
	@Override
	public Map<String,Object> selectDetail(int itemId) throws Exception{
		String uri = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbhack45691028002&itemIdType=itemId&ItemId="+itemId+"&output=js&Version=20131101&OptResult=reviewList";
		Map<String,Object> map = new HashMap<>();
		try {
			URL url = new URL(uri); 
		    URLConnection conn = url.openConnection(); 
		    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); 
		    String line = br.readLine();
		    
		    JSONParser parser = new JSONParser();
		    JSONObject ja= null;

		    ja = (JSONObject)parser.parse(line);
		    map = getMapFromJSONObject(ja);
		} catch (IOException e) {
	    	e.printStackTrace(); 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return map;
	}
	
	@Override
	public int saveBook(Map map) throws Exception{
		Map cMap = changeToRbMap(map); 
		return recommendDAO.saveBook(cMap);
	}
	
	@Override
	public int saveRecommend(RecommendVO recommendVO) throws Exception{
		return recommendDAO.saveRecommend(recommendVO);
	}

	@Override
	public Recommend saveRecommend(Recommend recommend) throws Exception{
		return recommendRepository.save(recommend);
	}
	
	@Override
	public void saveHistory(long itemId) throws Exception{
		recommendDAO.saveHistory(itemId);
	}
	
	public static Map<String, Object> getMapFromJSONObject(JSONObject obj) {
		if (obj==null) { 
			throw new IllegalArgumentException(String.format("BAD REQUEST obj %s", obj)); 
		} try { 
			return new ObjectMapper().readValue(obj.toJSONString(), Map.class); 
		} catch (Exception e) {
			throw new RuntimeException(e); } 
	}
	
	public Map changeToRbMap(Map map) {
		ArrayList list = (ArrayList)map.get("item");
		Map sMap = (HashMap<String,Object>)list.get(0);
		map.putAll(sMap);
		return map;
	}
}
