package com.akl.app.roti.bean;

public class MyGoodieRewardsBean {
	String POSCode = "";
	String chain_id = "";
	String effectiveDate = "";
	String fineprint = "";
	String id = "";
	String name = "";
	String points = "";
	String reward_type = "";
	String survey_id = "";
	String expired = "";
	String sort_by_id = "";
	String expirestate = "";
	String gifter = "";
	String image_url = "";

	String[] add_info_app_text;
	String[] add_info_description;
	String[] add_info_id;
	String[] add_info_title;
	int totalAdditionalInfoLength = 0;
	boolean is_additional_exist = false;

	public String getGifter() {
		return gifter;
	}

	public void setArrayLengthOfAdditionalInfo(int totalLength) {
		add_info_app_text = new String[totalLength];
		add_info_description = new String[totalLength];
		add_info_id = new String[totalLength];
		add_info_title = new String[totalLength];
		totalAdditionalInfoLength = totalLength;
	}

	public int getArrayLengthOfAdditionalInfo() {
		return totalAdditionalInfoLength;
	}

	public void setAddInfoAppText(String app_text, int index) {
		this.add_info_app_text[index] = app_text;
	}

	public void setAddInfoDescription(String desc, int index) {
		this.add_info_description[index] = desc;
	}

	public void setAddInfoId(String id, int index) {
		this.add_info_id[index] = id;
	}

	public void setAddInfoTitle(String title, int index) {
		this.add_info_title[index] = title;
	}

	public String getAddInfoAppText(int index) {
		return add_info_app_text[index];
	}

	public String getAddInfoDescription(int index) {
		return add_info_description[index];
	}

	public String getAddInfoId(int index) {
		return add_info_id[index];
	}

	public String getAddInfoTitle(int index) {
		return add_info_title[index];
	}

	public void setGifter(String gifter) {
		this.gifter = gifter;
	}

	public String getExpirestate() {
		return expirestate;
	}

	public void setExpirestate(String expirestate) {
		this.expirestate = expirestate;
	}

	String expiryDate = "";

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	// String = "";
	// String = "";
	// String = "";
	// String = "";
	public String getPOSCode() {
		return POSCode;
	}

	public void setPOSCode(String pOSCode) {
		POSCode = pOSCode;
	}

	public String getChain_id() {
		return chain_id;
	}

	public void setChain_id(String chain_id) {
		this.chain_id = chain_id;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getFineprint() {
		return fineprint;
	}

	public void setFineprint(String fineprint) {
		this.fineprint = fineprint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getImageURL() {
		return image_url;
	}

	public void setIsAdditionalInfoExist(boolean isExist) {
		this.is_additional_exist = isExist;
	}

	public void setImageURL(String imageUrl) {
		this.image_url = imageUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getReward_type() {
		return reward_type;
	}

	public void setReward_type(String reward_type) {
		this.reward_type = reward_type;
	}

	public boolean isAdditionalInfoExist() {
		return is_additional_exist;
	}

	public String getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(String survey_id) {
		this.survey_id = survey_id;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getSort_by_id() {
		return sort_by_id;
	}

	public void setSort_by_id(String sort_by_id) {
		this.sort_by_id = sort_by_id;
	}
}
