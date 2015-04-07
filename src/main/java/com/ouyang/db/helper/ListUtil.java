package com.ouyang.db.helper;
/**
 * 
 * @Classname: ListUtil
 * @Description: List工具，提供两组List数据之间的比较：交差，交集，并集；
 * @Author: kent(zhangtc@dreamtech.com.cn)
 * @Date: 2012-12-24 下午14:48:09
 * @Version:V6.0
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtil {

	/**
	 * @Methodname		intersect
	 * @Discription			获取两组List的交集；
	 * @param ls			List对象；
	 * @param ls2			List对象；
	 */
    public static List intersect(List ls, List ls2) { 
        List list = new ArrayList(Arrays.asList(new Object[ls.size()])); 
        Collections.copy(list, ls); 
        list.retainAll(ls2); 
        return list; 
    } 

	/**
	 * @Methodname		union
	 * @Discription			获取两组List的并集；
	 * @param ls			List对象；
	 * @param ls2			List对象；
	 */
    public static List union(List ls, List ls2) { 
        List list = new ArrayList(Arrays.asList(new Object[ls.size()])); 
        Collections.copy(list, ls); 
       list.addAll(ls2); 
        return list; 
    } 

	/**
	 * @Methodname		diff
	 * @Discription			获取两组List的差集；
	 * @param ls			List对象；
	 * @param ls2			List对象；
	 */
    public static List diff(List ls, List ls2) { 
    	List unionList = union(ls, ls2);
    	List interSectList = intersect(ls, ls2);
    	unionList.removeAll(interSectList);
    	return unionList; 
    } 
    
    /**
	 * @Methodname		ListPoor
	 * @Discription			获取两组List的差集；
	 * @param ls			List对象；
	 * @param ls2			List对象；
	 * @return String 		返回String集合；
	 */
    public static String listPoor(List l1,List l2)
    {
    	List list = diff(l1, l2);
    	int leng = list.size();
    	StringBuffer str = new StringBuffer("");
    	for(int a=0;a<leng;a++)
    	{
    		if(a == leng-1)
    		{
    			str.append(list.get(a));
    		}else{
    			str.append(list.get(a)+",");
    		}
    		////System.out.println(list.get(a));
    	}
    	return str.toString();
    }
    
    /**
	 * @Methodname		ListSame
	 * @Discription			获取两组List的交集；
	 * @param ls			List对象；
	 * @param ls2			List对象；
	 * @return String 		返回String集合；
	 */ 
    public static String listSame(List l1,List l2)
    {
    	  StringBuffer str = new StringBuffer("");
    	  List intersectList = intersect(l1, l2); 
          for (int i = 0; i < intersectList.size(); i++) 
          { 
        	  if(i == intersectList.size()-1)
        	  {
        		  str.append(intersectList.get(i));
        	  }else{
        		  str.append(intersectList.get(i)+",");
        	  }
              ////System.out.print(intersectList.get(i) + " "); 
          } 
          return str.toString();
    }
    
    
    
}
