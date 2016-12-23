package com.training.modules.train.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;
/**
 * 所有试题Entity
 * @author Superman
 *
 */
public class Exercises extends DataEntity<Exercises>{
	private static final long serialVersionUID = 1L;
	
	private String exerciseId;
	private int exerciseType;           //试题类型   1 单选题 ２ 多选题 ３ 是非题  
	private String exerciseTitle;		//试题名称
	private String exerciseContent;		//试题内容
	private String exerciseResult;		//试题答案
	private Date createtime;			//上传时间
	private int status;					//状态		0  显示   -1  不显示
	private String categoryId;			//课程ID
	private String tags; 				//快捷搜索关键字
	private String officeCode;			//机构管理CODE
	private String createuser;			//创建者ID（用户ID）
	
	//试题导入
	private String categoryName;		//课程名称
	private String type;				//试题类型
	private String A;					//答案A				
	private String B;					//答案B
	private String C;					//答案C
	private String D;					//答案D
	private String result;				//正确答案
	
	public String getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getExerciseType() {
		return exerciseType;
	}
	public void setExerciseType(int exerciseType) {
		this.exerciseType = exerciseType;
	}
	
	@JsonIgnore
	@NotNull(message="试题题目不能为空")
	@ExcelField(title="试题题目", type=0, align=2, sort=40)
	public String getExerciseTitle() {
		return exerciseTitle;
	}
	public void setExerciseTitle(String exerciseTitle) {
		this.exerciseTitle = exerciseTitle;
	}
	
	
	public String getExerciseContent() {
		return exerciseContent;
	}
	public void setExerciseContent(String exerciseContent) {
		this.exerciseContent = exerciseContent;
	}
	public String getExerciseResult() {
		return exerciseResult;
	}
	public void setExerciseResult(String exerciseResult) {
		this.exerciseResult = exerciseResult;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	@ExcelField(title="关键字", type=0, align=2, sort=65)
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	@JsonIgnore
	@NotNull(message="所属分类不能为空")
	@ExcelField(title="所属分类", type=0, align=2, sort=30)
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@JsonIgnore
	@NotNull(message="试题类型不能为空")
	@ExcelField(title="试题类型", type=0, align=2, sort=35)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@ExcelField(title="A", type=0, align=2, sort=45)
	public String getA() {
		return A;
	}
	public void setA(String a) {
		A = a;
	}
	@ExcelField(title="B", type=0, align=2, sort=50)
	public String getB() {
		return B;
	}
	public void setB(String b) {
		B = b;
	}
	@ExcelField(title="C", type=0, align=2, sort=55)
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	@ExcelField(title="D", type=0, align=2, sort=60)
	public String getD() {
		return D;
	}
	public void setD(String d) {
		D = d;
	}
	@JsonIgnore
	@NotNull(message="答案不能为空")
	@ExcelField(title="答案", type=0, align=2, sort=70)
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
