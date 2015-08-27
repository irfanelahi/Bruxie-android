package com.ak.app.roti.reward;

public class RewardClaim {

	String created_at = "";
	String name = "";
	String id = "";
	String points = "";
	String chain_id = "";
	String claim_date = "";

	public String getClaim_date() {
		return claim_date;
	}

	public void setClaim_date(String claim_date) {
		this.claim_date = claim_date;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getChain_id() {
		return chain_id;
	}

	public void setChain_id(String chain_id) {
		this.chain_id = chain_id;
	}

}
