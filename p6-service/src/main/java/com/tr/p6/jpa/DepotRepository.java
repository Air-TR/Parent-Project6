package com.tr.p6.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tr.p6.entity.Depot;

@Transactional // 事务注解，jpa中自定义删除或修改操作需要添加事务注解
public interface DepotRepository extends JpaRepository<Depot, Integer> {
	
	public List<Depot> findByName(String name);
	
	@Query("FROM Depot a WHERE a.place=?1") // 这里完全可以采用上一方法的方式实现，只是一种方法使用方式介绍
	public List<Depot> listByPlace(String place);
	
	@Modifying // jpa中自定义删除操作需要添加该注解
	@Query("delete from Depot a where a.name=?1")
	public int deleteByName(String name);
	
	@Modifying // jpa中自定义修改操作需要添加该注解
	@Query("update Depot a set a.confirmPerson=?2 where a.id=?1")
	public int updateConfirmPersonById(Integer id, String confirmPerson);

}