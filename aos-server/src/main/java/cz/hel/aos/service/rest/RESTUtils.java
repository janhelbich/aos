package cz.hel.aos.service.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import cz.hel.aos.service.SimpleCriteria;
import cz.hel.aos.service.SimpleCriteria.SimpleOrderBy;
import cz.hel.aos.service.SimpleCriteria.SimpleOrderType;

public class RESTUtils {

	public static SimpleCriteria parseHeadersCriteria(HttpHeaders headers) {
		SimpleCriteria sc = new SimpleCriteria();
		
		List<String> filter = headers.getRequestHeader("X-Filter");
		List<String> order = headers.getRequestHeader("X-Order");
		List<String> firstResult = headers.getRequestHeader("X-Base");
		List<String> offset = headers.getRequestHeader("X-Offset");
		
		if(!order.isEmpty()) {
			SimpleOrderBy sob = new SimpleOrderBy();
			sob.attrName = order.get(0).split(":")[0];
			sob.orderType = SimpleOrderType.valueOf(order.get(0).split(":")[1].toUpperCase());
			sc.order = sob;
		}
		
		if (!firstResult.isEmpty()) {
			sc.firstResult = Integer.parseInt(firstResult.get(0));
		}
		
		if (!offset.isEmpty()) {
			sc.offset = Integer.parseInt(offset.get(0));
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		if (!filter.isEmpty()) {
			List<String> conditions = new ArrayList<>();
			String filterString = filter.get(0);
			if (filterString.contains(", ")) {
				conditions.addAll(Arrays.asList(filterString.split((", "))));
			}
			for (String f : conditions) {
				try {
					String[] split = f.split("=");
					if (split[0].toLowerCase().contains("to")) {
						sc.dateTo = df.parse(split[1]);
					} else if (split[0].toLowerCase().contains("from")) {
						sc.dateFrom = df.parse(split[1]);
					}
				} catch (ParseException e) {
					e.printStackTrace(); // TODO
				}
			}
		}
		
		return sc;
	}
	

	
}
