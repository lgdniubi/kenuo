package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

public class CategoryLesson extends DataEntity<CategoryLesson>{
	private static final long serialVersionUID = 1L;
	private String categoryId;		//类别ID
	private String parentId;		//父类ID
	private String name;			//类别名称
	private int priority;			//优先级
	private int status;				//状态

	private String lessonId;		//课程ID
	private String lessonName;		//课程名称
//	@JsonBackReference
//	@NotNull
//	public CategoryLesson getParent() {
//		return parent;
//	}
//	public void setParent(CategoryLesson parent) {
//		this.parent = parent;
//		
//	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
}
