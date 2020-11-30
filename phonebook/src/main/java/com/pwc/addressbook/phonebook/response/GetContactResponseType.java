package com.pwc.addressbook.phonebook.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_EMPTY)
@JsonRootName("GetContactResource")
public class GetContactResponseType {
	
	@Getter
	@Setter
	@JsonProperty("ContactDetails")
	private List<ContactDetailType> contactDetails;

}
