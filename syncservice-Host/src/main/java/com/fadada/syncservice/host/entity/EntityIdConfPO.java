package com.fadada.syncservice.host.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 *
 * </p>
 *
 * @author 宋江涛
 * @since 2018-01-03
 */
@TableName("entity_id_conf")
public class EntityIdConfPO extends Model<EntityIdConfPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id代码
     */
    @TableId("id_code")
	private String idCode;
    /**
     * id分组
     */
	private String kind;
    /**
     * 固定前缀
     */
	@TableField("fix_prefix")
	private String fixPrefix;
    /**
     * 日期模式
     */
	@TableField("date_pattern")
	private String datePattern;
    /**
     * 是否有业务前缀
     */
	@TableField("include_biz_prefix")
	private Boolean includeBizPrefix;
    /**
     * 数字序列部分位数
     */
	@TableField("num_digit")
	private Integer numDigit;
    /**
     * 内存池大小
     */
	@TableField("pool_size")
	private Integer poolSize;
    /**
     * 最后一次更新时日期前缀
     */
	@TableField("date_prefix")
	private String datePrefix;
    /**
     * 下一个值
     */
	@TableField("next_batch_start_value")
	private Long nextBatchStartValue;
    /**
     * 最后同步内存生成的id
     */
	@TableField("last_generated_id")
	private String lastGeneratedId;
    /**
     * 备注
     */
	private String comment;
    /**
     * 是否有效
     */
	@TableField("is_valid")
	private Boolean isValid;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 创建者登录名
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 修改者登录名
     */
	@TableField("modified_by")
	private String modifiedBy;


	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getFixPrefix() {
		return fixPrefix;
	}

	public void setFixPrefix(String fixPrefix) {
		this.fixPrefix = fixPrefix;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public Boolean getIncludeBizPrefix() {
		return includeBizPrefix;
	}

	public void setIncludeBizPrefix(Boolean includeBizPrefix) {
		this.includeBizPrefix = includeBizPrefix;
	}

	public Integer getNumDigit() {
		return numDigit;
	}

	public void setNumDigit(Integer numDigit) {
		this.numDigit = numDigit;
	}

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}

	public String getDatePrefix() {
		return datePrefix;
	}

	public void setDatePrefix(String datePrefix) {
		this.datePrefix = datePrefix;
	}

	public Long getNextBatchStartValue() {
		return nextBatchStartValue;
	}

	public void setNextBatchStartValue(Long nextBatchStartValue) {
		this.nextBatchStartValue = nextBatchStartValue;
	}

	public String getLastGeneratedId() {
		return lastGeneratedId;
	}

	public void setLastGeneratedId(String lastGeneratedId) {
		this.lastGeneratedId = lastGeneratedId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getValid() {
		return isValid;
	}

	public void setValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	protected Serializable pkVal() {
		return this.idCode;
	}

	@Override
	public String toString() {
		return "EntityIdConfPO{" +
			", idCode=" + idCode +
			", kind=" + kind +
			", fixPrefix=" + fixPrefix +
			", datePattern=" + datePattern +
			", includeBizPrefix=" + includeBizPrefix +
			", numDigit=" + numDigit +
			", poolSize=" + poolSize +
			", datePrefix=" + datePrefix +
			", nextBatchStartValue=" + nextBatchStartValue +
			", lastGeneratedId=" + lastGeneratedId +
			", comment=" + comment +
			", isValid=" + isValid +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
