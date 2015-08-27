package com.ak.app.roti.reward;

public class RewardActivity {
	String created_at = "";
	String status = "";
	String id = "";
	String total_points_earned = "";
	String admin_id = "";
	String amount = "";
    String address = "";
	String type = "";
	
	public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotal_points_earned() {
		return total_points_earned;
	}

	public void setTotal_points_earned(String total_points_earned) {
		this.total_points_earned = total_points_earned;
	}

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
}