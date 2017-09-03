package huxg.framework.plugin.fileupload;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import huxg.framework.entity.BaseEntity;

/**
 * 个人资料表
 * 
 * @author huxg
 * 
 */
public class TFile extends BaseEntity {
	@Id
	@Column(name = "C_BH")
	private String CBh;

	// 资料类型：1-保留，2-文档, 3-题目, 4-视频,5-其他
	@Column(name = "C_LX")
	private String CLx;
	
	// 缩略图url
	@Column(name = "C_SLT_URL")
	private String CSltUrl;

	// 文件扩展名
	@Column(name = "C_KZM")
	private String CKzm;

	// 原文件名 ，用户上传时文件的名字
	@Column(name = "C_YWJM")
	private String CYwjm;

	// 原文件绝对路径
//	@Column(name = "C_YWJLJ_JD")
//	private String CWjljJd;

	// 源文件相对路径，即apache的相对路径
	@Column(name = "C_YWJLJ_XD")
	private String CWjljXd;

	// 转换后的文件绝对路径
	@Column(name = "C_ZWJLJ_JD")
	private String CZwjljJd;

	// 转换后的文件相对路径
	@Column(name = "C_ZWJLJ_XD")
	private String CZwjljXd;

	// 来源 1-微博，2-学习平台 3-微课程
	@Column(name = "N_LY")
	private Integer NLy;

	// 所属领域
	@Column(name = "C_SSLY")
	private String CSsly;

	// 作者
	@Column(name = "C_ZZBH")
	private String CZzbh;

	// 作者名称
	@Column(name = "R_C_ZZMC")
	private String RCZzmc;

	// 分类编号，上传文件默认上传到 一个文件夹中，此处为该文件夹的编号
	@Column(name = "C_FLBH")
	private String CFlbh;

	// 上传日期
	@Column(name = "D_SCSJ")
	private Date DScsj;

	// 点击次数
	@Column(name = "N_DJCS")
	private Long NDjcs;

	// 收藏次数
	@Column(name = "N_SCCS")
	private Long NSccs;

	// 下载次数
	@Column(name = "N_XZCS")
	private Long NXzcs;

	// 分享次数
	@Column(name = "N_FXCS")
	private Long NFxcs;

	// 评论次数
	@Column(name = "N_PLCS")
	private Long NPlcs;

	// 分值，代表了视频的星星个数
	@Column(name = "N_FZ")
	private Integer NFz;

	// 是否开放，开放视频可在微课中看到
	@Column(name = "N_SFKF")
	private Integer NSfkf;

	// 原文件大小
	@Column(name = "N_YWJDX")
	private Long NYwjdx;

	// 转换后视频时长
	@Column(name = "C_SPSC")
	private String CSpsc;

	// 视频
	@Column(name = "C_ZYSM")
	private String CZysm;

	// 前状态
	@Column(name = "N_DQZT")
	private Integer NDqzt;

	// 文件标示
	@Column(name = "C_WJBS")
	private String CWjbs;
	
	public String getCBh() {
		return CBh;
	}

	public void setCBh(String cBh) {
		CBh = cBh;
	}

	public String getCLx() {
		return CLx;
	}

	public void setCLx(String cLx) {
		CLx = cLx;
	}

	public String getCSltUrl() {
		return CSltUrl;
	}

	public void setCSltUrl(String cSltUrl) {
		CSltUrl = cSltUrl;
	}

	public String getCKzm() {
		return CKzm;
	}

	public void setCKzm(String cKzm) {
		CKzm = cKzm;
	}

	public String getCYwjm() {
		return CYwjm;
	}

	public void setCYwjm(String cYwjm) {
		CYwjm = cYwjm;
	}

//	public String getCWjljJd() {
//		return CWjljJd;
//	}
//
//	public void setCWjljJd(String cWjljJd) {
//		CWjljJd = cWjljJd;
//	}

	public String getCWjljXd() {
		return CWjljXd;
	}

	public void setCWjljXd(String cWjljXd) {
		CWjljXd = cWjljXd;
	}

	public String getCZwjljJd() {
		return CZwjljJd;
	}

	public void setCZwjljJd(String cZwjljJd) {
		CZwjljJd = cZwjljJd;
	}

	public String getCZwjljXd() {
		return CZwjljXd;
	}

	public void setCZwjljXd(String cZwjljXd) {
		CZwjljXd = cZwjljXd;
	}

	public Integer getNLy() {
		return NLy;
	}

	public void setNLy(Integer nLy) {
		NLy = nLy;
	}

	public String getCSsly() {
		return CSsly;
	}

	public void setCSsly(String nSsly) {
		CSsly = nSsly;
	}

	public String getCZzbh() {
		return CZzbh;
	}

	public void setCZzbh(String cZzbh) {
		CZzbh = cZzbh;
	}

	public String getCFlbh() {
		return CFlbh;
	}

	public void setCFlbh(String cFlbh) {
		CFlbh = cFlbh;
	}

	public Date getDScsj() {
		return DScsj;
	}

	public void setDScsj(Date dScsj) {
		DScsj = dScsj;
	}

	public Long getNDjcs() {
		return NDjcs;
	}

	public void setNDjcs(Long nDjcs) {
		NDjcs = nDjcs;
	}

	public Long getNSccs() {
		return NSccs;
	}

	public void setNSccs(Long nSccs) {
		NSccs = nSccs;
	}

	public Long getNPlcs() {
		return NPlcs;
	}

	public void setNPlcs(Long nPlcs) {
		NPlcs = nPlcs;
	}

	public Integer getNFz() {
		return NFz;
	}

	public void setNFz(Integer nFz) {
		NFz = nFz;
	}

	public Integer getNSfkf() {
		return NSfkf;
	}

	public void setNSfkf(Integer nSfkf) {
		NSfkf = nSfkf;
	}

	public Long getNYwjdx() {
		return NYwjdx;
	}

	public void setNYwjdx(Long nYwjdx) {
		NYwjdx = nYwjdx;
	}

	public String getCSpsc() {
		return CSpsc;
	}

	public void setCSpsc(String cSpsc) {
		CSpsc = cSpsc;
	}

	public String getCZysm() {
		return CZysm;
	}

	public void setCZysm(String cZysm) {
		CZysm = cZysm;
	}

	public Integer getNDqzt() {
		return NDqzt;
	}

	public void setNDqzt(Integer nDqzt) {
		NDqzt = nDqzt;
	}

	public String getCWjbs() {
		return CWjbs;
	}

	public void setCWjbs(String cWjbs) {
		CWjbs = cWjbs;
	}

	public String getRCZzmc() {
		return RCZzmc;
	}

	public void setRCZzmc(String rCZzmc) {
		RCZzmc = rCZzmc;
	}

	@Override
	public String getId() {
		return this.getCBh();
	}

	public Long getNXzcs() {
		return NXzcs;
	}

	public void setNXzcs(Long nXzcs) {
		NXzcs = nXzcs;
	}

	public Long getNFxcs() {
		return NFxcs;
	}

	public void setNFxcs(Long nFxcs) {
		NFxcs = nFxcs;
	}

}
