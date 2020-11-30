package com.pwc.addressbook.phonebook.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonRootName("ContactDetail")
@JsonInclude(Include.NON_EMPTY)
public class ContactDetailType {
	
	@Getter
	@Setter
	@JsonProperty("Name")
	private String name;
	
	@Getter
	@Setter
	@JsonProperty("MobileNumber")
	private String mobileNumber;
	
	@Getter
	@Setter
	@JsonProperty("AddressBookName")
	private String addressBookName;

}
