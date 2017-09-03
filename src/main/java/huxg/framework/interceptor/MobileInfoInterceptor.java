/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 手机绑定验证
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class MobileInfoInterceptor extends HandlerInterceptorAdapter {

//	@Resource(name = "memberServiceImpl")
//	private MemberService memberService;
//	@Resource(name = "trainingAgencyServiceImpl")
//	private TrainingAgencyService trainingAgencyService;
//	
//	/** 登录URL */
//	private String applyMobile = "/front/register/apply_mobile.jhtml";
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		Member current = memberService.getCurrent();
//		if(StringUtil.isNotEmptyString(current.getMobile())){
//			return true;
//		}
//		current=memberService.find(current.getId());
//		Set<Order> orders = current.getOrders();
//		boolean flag=false;
//		if(orders!=null&&orders.size()>0){
//			label:
//			for(Order o:orders){
//				if(OrderStatus.completed==o.getOrderStatus()){
//					List<OrderItem> orderItems = o.getOrderItems();
//					if(orderItems!=null&&orderItems.size()>0){
//						for(OrderItem item:orderItems){
//							Product product = item.getProduct();
//							Record r = Db.findFirst("SELECT * FROM `t_training_class_goods` WHERE `goods_id` = ? limit 1",product.getId());
//							if(r!=null){
//								Long agencyId = r.getLong("agency_id");
//								TrainingAgency agency = trainingAgencyService.find(agencyId);
//								if(CommonAttributes.XIANXIA.longValue()==agency.getDict().getId().longValue()){
//									flag=true;
//									break label;
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		if(flag){
//			response.sendRedirect(request.getContextPath() + applyMobile);//激活手机页面
//			return false;
//		}else{
//			return true;
//		}
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//			
//	}
}