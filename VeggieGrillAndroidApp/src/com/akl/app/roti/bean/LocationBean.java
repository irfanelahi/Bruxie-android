package com.akl.app.roti.bean;

import java.util.List;

public class LocationBean {
	String address = "";
	String app_display_text = "";
	String id = "";
	String latitude = "";
	String longitude = "";
	String name = "";
	String phone_number = "";
	String zipcode = "";
	String close_at = "";
	String day_of_week = "";
	String open_at = "";
	String order_link = "";
	
	
	LocationOfferBean locationOfferBean;
	List<LocationOfferBean> listLocationOfferBean;

	public List<LocationOfferBean> getListLocationOfferBean() {
		return listLocationOfferBean;
	}

	public void setListLocationOfferBean(
			List<LocationOfferBean> listLocationOfferBean) {
		this.listLocationOfferBean = listLocationOfferBean;
	}

//	public LocationOfferBean getLocationOfferBean() {
//		return locationOfferBean;
//	}
//
//	public void setLocationOfferBean(LocationOfferBean locationOfferBean) {
//		this.locationOfferBean = locationOfferBean;
//	}
	
	public String getOrderLink() {
        return order_link;
    }
	
	public void setOrderLink(String order_link) {
        if (order_link.equals("null")) {
            order_link = "";
        }
        this.order_link = order_link;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if (address.equals("null")) {
			address = "";
		}
		this.address = address;
	}

	public String getApp_display_text() {
		return app_display_text;
	}

	public void setApp_display_text(String app_display_text) {
		if (app_display_text.equals("null")) {
			app_display_text = "";
		}
		this.app_display_text = app_display_text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		if (latitude.equals("null")) {
			latitude = "";
		}
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		if (longitude.equals("null")) {
			longitude = "";
		}
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.equals("null")) {
			name = "";
		}
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		if (phone_number.equals("null")) {
			phone_number = "";
		}
		this.phone_number = phone_number;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		if (zipcode.equals("null")) {
			zipcode = "";
		}
		this.zipcode = zipcode;
	}

	public String getClose_at() {
		return close_at;
	}

	public void setClose_at(String close_at) {
		if (close_at.equals("null")) {
			close_at = "";
		}
		this.close_at = close_at;
	}

	public String getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(String day_of_week) {
		if (day_of_week.equals("null")) {
			day_of_week = "";
		}
		this.day_of_week = day_of_week;
	}

	public String getOpen_at() {
		return open_at;
	}

	public void setOpen_at(String open_at) {
		if (open_at.equals("null")) {
			open_at = "";
		}
		this.open_at = open_at;
	}
}
