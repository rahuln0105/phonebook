package com.pwc.addressbook.phonebook.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class AddressBookRequestType {

	@Getter 
	@Setter
	private String name;
	
	@Getter 
	@Setter 
	@Min(value = 1000000000L, message ="invalid mobile number" )
	@Max(value = 9999999999L, message = "invalid mobile number")
	private Long number;
	
	@Getter 
	@Setter
	private String addressBookName;
		
}
