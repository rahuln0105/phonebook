package com.pwc.addressbook.phonebook.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.pwc.addressbook.phonebook.model.ContactDetails;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

@Component
public class AddressBookCsvParser {

	public List<ContactDetails> getContactDetailsList(String filePath) throws IOException{
		List<ContactDetails> contactDetailsList = new ArrayList<>();
		
		File resource = new File(filePath);
		try (
			CSVReader reader = new CSVReader(new FileReader(resource.getPath()),
					CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER,'\0',0,
					CSVParser.DEFAULT_STRICT_QUOTES,true)) {
				String[] nextline;
				while ((nextline = reader.readNext()) != null) {
					String mobileNumber = nextline[0];
					if (!mobileNumber.isEmpty()) {
						ContactDetails contactDetails = new ContactDetails();
						contactDetails.setMoblieNumber(Long.valueOf(mobileNumber));
						contactDetails.setName(nextline[1]);
						contactDetailsList.add(contactDetails);
					}
				}
			}
		return contactDetailsList;
		}
	
	
	
	}

