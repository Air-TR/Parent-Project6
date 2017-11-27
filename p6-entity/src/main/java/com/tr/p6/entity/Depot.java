package com.tr.p6.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 仓库实体类
 * 
 * @author taorun
 * @date 2017年11月27日 上午10:59:46
 *
 */

@Entity
@Table(name = "depot")
public class Depot {
	
    /** 主键id */
	@Id
    private Integer id;

    /** 仓库名 */
    private String name;

    /** 产地 */
    private String place;

    /** 热量 */
    private Double heat;

    /** 硫份 */
    private Double sulphur;

    /** 含碳量 */
    private Double carbon;

    /** 灰份 */
    private Double ash;

    /** 挥发物 */
    private Double volatiles;

    /** 水份 */
    private Double water;

    /** 总量 */
    @Column(name = "total_amount")
    private Double totalAmount;

    /** 单价 */
    @Column(name = "unit_price")
    private Double unitPrice;

    /** 确认人 */
    @Column(name = "confirm_person")
    private String confirmPerson;

    /** 备注 */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public Double getHeat() {
        return heat;
    }

    public void setHeat(Double heat) {
        this.heat = heat;
    }

    public Double getSulphur() {
        return sulphur;
    }

    public void setSulphur(Double sulphur) {
        this.sulphur = sulphur;
    }

    public Double getCarbon() {
        return carbon;
    }

    public void setCarbon(Double carbon) {
        this.carbon = carbon;
    }

    public Double getAsh() {
        return ash;
    }

    public void setAsh(Double ash) {
        this.ash = ash;
    }

    public Double getVolatiles() {
        return volatiles;
    }

    public void setVolatiles(Double volatiles) {
        this.volatiles = volatiles;
    }

    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getConfirmPerson() {
        return confirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        this.confirmPerson = confirmPerson == null ? null : confirmPerson.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    
}