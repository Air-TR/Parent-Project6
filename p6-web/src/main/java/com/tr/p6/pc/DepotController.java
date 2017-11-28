package com.tr.p6.pc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tr.common.pagination.hibernate.PageReq;
import com.tr.common.pagination.hibernate.PageRes;
import com.tr.common.result.Result;
import com.tr.p6.jpa.DepotRepository;
import com.tr.p6.entity.Depot;
import com.tr.p6.service.DepotService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Depot")
@RestController
public class DepotController {
	
	@Autowired
	private DepotRepository depotRepository;
	
	@Autowired
	private DepotService depotService;
	
	@ApiOperation(value = "分页列表查询")
	@PostMapping("/depot/pageList")
	public PageRes<List<Depot>> pageListMgr(@RequestBody PageReq<Depot> depot) {
		return depotService.pageList(depot);
	}
	
	@ApiOperation(value = "根据id获取")
	@GetMapping("/depot/get/{id}")
	public Result<Depot> get(@PathVariable Integer id) {
		return Result.success(depotRepository.findOne(id));
	}
	
	@ApiOperation(value = "根据产地获取")
	@GetMapping("/depot/listByPlace")
	public Result<List<Depot>> listByPlace(@RequestParam String place) {
		return Result.success(depotRepository.listByPlace(place));
	}

}
