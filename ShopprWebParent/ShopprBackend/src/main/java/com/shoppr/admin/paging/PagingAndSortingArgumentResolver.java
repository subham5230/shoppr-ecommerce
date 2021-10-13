package com.shoppr.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
		PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
		String sortDir = request.getParameter("sortDir");
		String sortField = request.getParameter("sortField");
		
		if(sortField.equals("")) sortDir = "";
		else if(sortDir.equals("")) sortField = "";
		
		String keyword = request.getParameter("keyword");
		
		String reverseSortDir = null;

		if(sortDir.equals("asc")) {
			reverseSortDir = "desc";
		}
		if(sortDir.equals("desc")) {
			reverseSortDir = "";
		}
		if(sortDir.isEmpty()) {
			reverseSortDir = "asc";
		}
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleURL", annotation.moduleURL());	
		
		
		return new PagingAndSortingHelper(model, annotation.listName(),
				sortField, sortDir, keyword);
	}

}
