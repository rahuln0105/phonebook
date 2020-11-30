package com.pwc.addressbook.phonebook.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.Setter;

@JsonRootName("AddContactResource")
public class AddContactResponseType {
	
	@Getter
	@Setter
	@JsonProperty("ResponseMessage")
	public String responseMessage;

}
