package com.transempiric.webfluxTemplate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.transempiric.webfluxTemplate.error.UserServiceException;
import org.springframework.http.HttpStatus;

/**
 * {@code Address} is an immutable object.
 * <p>
 * Use {@code Address.ofCountry(String)} builder to create a new address.
 * <p>
 * Use {@code Address.from(myAddress)} builder to initiate a builder with a copy of myAddress then
 * update its fields with one of the {@code with*()} methods before calling {@code build()} method.<br>
 * <p>
 * Example:<br>
 * {@code Address myAddress = Address.ofCountry("Canada").withStreetNumber(100).build();}<br>
 * {@code myAddress = Address.from(myAddress).withStreetNumber(200).build(); // Street number changed}
 *
 */
@JsonInclude(Include.NON_NULL)
public final class Address {

	private Integer streetNumber;
	private String streetName;
	private String city;
	private String zipcode;
	private String stateOrProvince;
	private String country;

	private Address() {
		// Needed for Spring Data and Jackson serialization
	}

	private Address(Integer streetNumber, String streetName, String city, String zipcode, String stateOrProvince,
			String country) {
		this.streetNumber = streetNumber;
		this.streetName = streetName;
		this.city = city;
		this.zipcode = zipcode;
		this.stateOrProvince = stateOrProvince;
		this.country = country;
	}

	static public Builder ofCountry(String country) {
		return new Builder(country);
	}

	static public Builder from(Address address) {
		final Builder builder = new Builder(address.getCountry());
		builder.streetNumber = address.getStreetNumber();
		builder.streetName = address.getStreetName();
		builder.zipcode = address.getZipcode();
		builder.city = address.getCity();
		builder.stateOrProvince = address.getStateOrProvince();
		return builder;
	}

	public static final class Builder {

		private Integer streetNumber;
		private String streetName;
		private String city;
		private String zipcode;
		private String stateOrProvince;
		private String country;

		public Builder(String country) {
			if (country == null) {
				throw new UserServiceException(HttpStatus.BAD_REQUEST, "Country can not be null.");
			}
			this.country = country;
		}

		public Builder withStreetNumber(Integer streetNumber) {
			this.streetNumber = streetNumber;
			return this;
		}

		public Builder withStreetName(String streetName) {
			this.streetName = streetName;
			return this;
		}

		public Builder withCity(String city) {
			this.city = city;
			return this;
		}

		public Builder withZipcode(String zipcode) {
			this.zipcode = zipcode;
			return this;
		}

		public Builder withStateOrProvince(String stateOrProvince) {
			this.stateOrProvince = stateOrProvince;
			return this;
		}

		public Address build() {
			return new Address(streetNumber, streetName, city, zipcode, stateOrProvince, country);
		}
	}

	public Integer getStreetNumber() {
		return streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getCity() {
		return city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getStateOrProvince() {
		return stateOrProvince;
	}

	public String getCountry() {
		return country;
	}
}
