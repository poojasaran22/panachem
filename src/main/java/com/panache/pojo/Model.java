package com.panache.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model {

    private Integer id;
    private String name;
    private Double height;
    private String hair;
    private String eyes;
    private Integer bust;
    private Integer waist;
    private Integer shoes;
    private String country;
    private String type;
    private String experience;
    private Date added_date;
    private String field;
    private String extra_tag;
    private String betweenHeight;
    private String looksLike;
    
	public Model(Integer id, String name, Double height, String hair,
			String eyes, Integer bust, Integer waist, Integer shoes,
			String country, String type, String experience, Date added_date,
			String field, String extra_tag, String betweenHeight,
			String looksLike) {
		super();
		this.id = id;
		this.name = name;
		this.height = height;
		this.hair = hair;
		this.eyes = eyes;
		this.bust = bust;
		this.waist = waist;
		this.shoes = shoes;
		this.country = country;
		this.type = type;
		this.experience = experience;
		this.added_date = added_date;
		this.field = field;
		this.extra_tag = extra_tag;
		this.betweenHeight = betweenHeight;
		this.looksLike = looksLike;
	}

	public Model() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getHair() {
		return hair;
	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public String getEyes() {
		return eyes;
	}

	public void setEyes(String eyes) {
		this.eyes = eyes;
	}

	public Integer getBust() {
		return bust;
	}

	public void setBust(Integer bust) {
		this.bust = bust;
	}

	public Integer getWaist() {
		return waist;
	}

	public void setWaist(Integer waist) {
		this.waist = waist;
	}

	public Integer getShoes() {
		return shoes;
	}

	public void setShoes(Integer shoes) {
		this.shoes = shoes;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getExtra_tag() {
		return extra_tag;
	}

	public void setExtra_tag(String extra_tag) {
		this.extra_tag = extra_tag;
	}

	public String getBetweenHeight() {
		return betweenHeight;
	}

	public void setBetweenHeight(String betweenHeight) {
		this.betweenHeight = betweenHeight;
	}

	public String getLooksLike() {
		return looksLike;
	}

	public void setLooksLike(String looksLike) {
		this.looksLike = looksLike;
	}

	
    
}
