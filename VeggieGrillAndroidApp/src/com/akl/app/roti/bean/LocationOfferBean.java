package com.akl.app.roti.bean;

public class LocationOfferBean {
	String bonus_points = "";
	String bonus_points_ftu = "";
	String chain_id = "";
	String created_at = "";
	String daysOfWeek = "";
	String deleted_at = "";
	String effectiveDate = "";
	String expiryDate = "";
	String fineprint = "";
	String id = "";
	String isActive = "";
	String multiplier = "";
	String name = "";
	String survey_id = "";
	String timeEnd = "";
	String timeStart = "";
	String updated_at = "";

	public String getBonus_points() {
		return bonus_points;
	}

	public void setBonus_points(String bonus_points) {
		if(bonus_points.equals("null"))
		{
			bonus_points = "";
		}
		this.bonus_points = bonus_points;
	}

	public String getBonus_points_ftu() {
		return bonus_points_ftu;
	}

	public void setBonus_points_ftu(String bonus_points_ftu) {
		if(bonus_points_ftu.equals("null"))
		{
			bonus_points_ftu = "";
		}
		this.bonus_points_ftu = bonus_points_ftu;
	}

	public String getChain_id() {
		return chain_id;
	}

	public void setChain_id(String chain_id) {
		if(chain_id.equals("null"))
		{
			chain_id = "";
		}
		this.chain_id = chain_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		if(created_at.equals("null"))
		{
			created_at = "";
		}
		this.created_at = created_at;
	}

	public String getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(String daysOfWeek) {
		if(daysOfWeek.equals("null"))
		{
			daysOfWeek = "";
		}
		this.daysOfWeek = daysOfWeek;
	}

	public String getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(String deleted_at) {
		if(deleted_at.equals("null"))
		{
			deleted_at = "";
		}
		this.deleted_at = deleted_at;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		if(effectiveDate.equals("null"))
		{
			effectiveDate = "";
		}
		this.effectiveDate = effectiveDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		if(expiryDate.equals("null"))
		{
			expiryDate = "";
		}
		this.expiryDate = expiryDate;
	}

	public String getFineprint() {
		return fineprint;
	}

	public void setFineprint(String fineprint) {
		if(fineprint.equals("null"))
		{
			fineprint = "";
		}
		this.fineprint = fineprint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id.equals("null"))
		{
			id = "";
		}
		this.id = id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		if(isActive.equals("null"))
		{
			isActive = "";
		}
		this.isActive = isActive;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(String multiplier) {
		if(multiplier.equals("null"))
		{
			multiplier = "";
		}
		this.multiplier = multiplier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.equals("null"))
		{
			name = "";
		}
		this.name = name;
	}

	public String getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(String survey_id) {
		if(survey_id.equals("null"))
		{
			survey_id = "";
		}
		this.survey_id = survey_id;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		if(timeEnd.equals("null"))
		{
			timeEnd = "";
		}
		this.timeEnd = timeEnd;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		if(timeStart.equals("null"))
		{
			timeStart = "";
		}
		this.timeStart = timeStart;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		if(updated_at.equals("null"))
		{
			updated_at = "";
		}
		this.updated_at = updated_at;
	}
}
