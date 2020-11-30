package com.pwc.addressbook.phonebook.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Contact {
	
	@Getter
	@Setter
	private Map<String , List<ContactDetails>> contact;

}
