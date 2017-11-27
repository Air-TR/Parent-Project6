/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tr.common.exception;

import com.tr.common.result.ResultEnum;

/**
 * DaoException异常处理
 * 
 * @author taorun
 * @date 2017年11月23日 下午3:19:22
 *
 */

public class DaoException extends MyException {

	private static final long serialVersionUID = 8732177852385268444L;

	public DaoException(ResultEnum resultEnum, Exception exception) {
        super(resultEnum, exception);
    }
    
    /**
     * 默认DB异常
     * 
     * @param exception
     * @return
     */
    public static DaoException defaultDBException(Exception exception){
        DaoException daoException = new DaoException(ResultEnum.DAO_EXCEPTION, exception);
        return daoException;
    }

}
