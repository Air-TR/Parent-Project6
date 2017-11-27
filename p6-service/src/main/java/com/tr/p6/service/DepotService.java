package com.tr.p6.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tr.common.hibernate.BaseService;
import com.tr.common.hibernate.pojo.HqlQuerys;
import com.tr.common.pagination.hibernate.PageReq;
import com.tr.common.pagination.hibernate.PageRes;
import com.tr.p6.entity.Depot;

@Service
public class DepotService extends BaseService<Depot> {
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public PageRes<List<Depot>> pageList(PageReq<Depot> depot) {
	    	if (depot.getData() == null) {
	    		depot.setData(new Depot());
	    	}
	    	HqlQuerys hq = new HqlQuerys(true)
	    			.init(depot) // DISTINCT(a)
	    			.setQueryString("FROM Depot a WHERE 1=1")
	    			.setCountString("SELECT COUNT(a.id) FROM Depot a WHERE 1=1")
	    			.setOrderAlias("a")
	    			// .addParam("name", like(depot.getData().getName()), " and a.name like :name ")
	    			// .addParam("delf", Delf.N, " and a.delf = :delf ")
	    			// .addParam("roleId", clothType.getData().getRoleId(), " and rs.id = :roleId ")
	    			// .setGroup(" group by a.id ")
	    			;
	    	PageRes<List<Depot>> res = super.pageList(hq);
	    	return res;
    }

}
