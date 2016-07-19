package com.weibo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "geo" })
public class Status {
	private String created_at;

	private long id;
	private long mid;
	private String idstr;
	private List<String> pic_ids = new ArrayList<String>();

	public List<String> getPic_ids() {
		return pic_ids;
	}

	public void setPic_ids(List<String> pic_ids) {
		this.pic_ids = pic_ids;
	}

	int position;
	String mark;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	private String result;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private int read_count;
	private int mlevel;

	private String cover_image;
	private int state;
	private String deleted;

	private boolean liked;
	private boolean truncated;

	private String rid;
	private int mblogtype;
	private String ctrlType;
	private List<String> cmt_picids = new ArrayList<String>();
	private long uid;

	long expire_time;

	private int source_type;
	private int source_allowclick;
	private long original_mblog_read_count;
	private long original_article_read_count;
	private long original_status_count;
	private long original_article_count;
	private String type;
	private String text;
	private String pid;
	private String source;

	private boolean favorited;

	private String in_reply_to_status_id;

	private String in_reply_to_user_id;

	private String in_reply_to_screen_name;

	private String thumbnail_pic;

	private String bmiddle_pic;

	private String original_pic;

	private Status retweeted_status;
	
	public long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(long expire_time) {
		this.expire_time = expire_time;
	}

	public int getSource_type() {
		return source_type;
	}

	public void setSource_type(int source_type) {
		this.source_type = source_type;
	}

	public long getOriginal_mblog_read_count() {
		return original_mblog_read_count;
	}

	public void setOriginal_mblog_read_count(long original_mblog_read_count) {
		this.original_mblog_read_count = original_mblog_read_count;
	}

	public long getOriginal_article_read_count() {
		return original_article_read_count;
	}

	public void setOriginal_article_read_count(long original_article_read_count) {
		this.original_article_read_count = original_article_read_count;
	}

	public long getOriginal_status_count() {
		return original_status_count;
	}

	public void setOriginal_status_count(long original_status_count) {
		this.original_status_count = original_status_count;
	}

	public long getOriginal_article_count() {
		return original_article_count;
	}

	public void setOriginal_article_count(long original_article_count) {
		this.original_article_count = original_article_count;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public Status getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(Status retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public int getMblogtype() {
		return mblogtype;
	}

	public void setMblogtype(int mblogtype) {
		this.mblogtype = mblogtype;
	}

	public String getCtrlType() {
		return ctrlType;
	}

	public void setCtrlType(String ctrlType) {
		this.ctrlType = ctrlType;
	}

	public List<String> getCmt_picids() {
		return cmt_picids;
	}

	public void setCmt_picids(List<String> cmt_picids) {
		this.cmt_picids = cmt_picids;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public int getSource_allowclick() {
		return source_allowclick;
	}

	public void setSource_allowclick(int source_allowclick) {
		this.source_allowclick = source_allowclick;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	

	

	public int getRead_count() {
		return read_count;
	}

	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
