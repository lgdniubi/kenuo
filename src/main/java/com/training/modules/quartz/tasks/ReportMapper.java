package com.training.modules.quartz.tasks;
import com.training.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface ReportMapper {

	/**
	 * 清空会员及机构的欠款统计表
	 */
	public void dropTableUserAppArrearge();
	public void dropTableOfficeAppArrearge();

	/**
	 * 往会员欠款统计表及机构欠款统计表插入数据
	 */
	public void insertTableUserAppArrearge();
	public void insertTableOfficeAppArrearge();
}
