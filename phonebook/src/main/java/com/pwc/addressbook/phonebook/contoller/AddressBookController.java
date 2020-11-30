package com.pwc.addressbook.phonebook.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.addressbook.phonebook.request.AddressBookRequestType;
import com.pwc.addressbook.phonebook.response.AddContactResponseType;
import com.pwc.addressbook.phonebook.response.GetContactResponseType;
import com.pwc.addressbook.phonebook.service.AddressBookService;

@RestController
@RequestMapping("/phonebook_v1.0.0/phonebook")
public class AddressBookController {
	
	@Autowired
	private AddressBookService addressBookService;
	
	
	@RequestMapping(value="/addcontact", method=RequestMethod.POST, consumes="application/json", produces="application/json")	
	public AddContactResponseType addContact(@RequestBody AddressBookRequestType addressBook) {
		
		return addressBookService.addContact(addressBook);
	}
	
	@RequestMapping(value="/getcontact",method=RequestMethod.GET, produces="application/json")	
	public GetContactResponseType getContactDetails(@RequestParam String type,@RequestParam(required = false) String mobileNumber) {
		
		return addressBookService.getContactDetails(type,mobileNumber);
	}

}
