package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

public interface ItemService {
	List<Item> findAll();

	
	
	 EasyUIResult findItemByPage(Integer page,Integer rows);
	 
	 void saveItem(Item item,String desc);
	 
	 void updateItem(Item item);
	 
	 
	 void deleteItem(Long[] ids);



	void updateStatus(int status, Long[] ids);



	ItemDesc findItemDesc(Long itemId);



	Item finditemById(Long itemId);



	void deleteItems(Long[] ids);



	void updateItem(Item item, String desc);



	List<Item> findItemAll();
	 
}
