package com.pwc.addressbook.phonebook.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pwc.addressbook.phonebook.model.Contact;
import com.pwc.addressbook.phonebook.model.ContactDetails;
import com.pwc.addressbook.phonebook.parser.AddressBookCsvParser;
import com.pwc.addressbook.phonebook.request.AddressBookRequestType;
import com.pwc.addressbook.phonebook.response.AddContactResponseType;
import com.pwc.addressbook.phonebook.response.ContactDetailType;
import com.pwc.addressbook.phonebook.response.GetContactResponseType;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

@Service("addressBookService")
@Slf4j
public class AddressBookService {

	@Value("${csvDataDirectoryPath}")
	private String csvDataDirectoryPath;

	@Value("${csvAddressBooks}")
	private String csvAddressBooks;

	@Autowired
	private AddressBookCsvParser addressBookCsvParser;

	private Contact contact;

	@PostConstruct
	public void init() throws IOException {
		log.info("******* API Startup Loading contacts*********");
		Map<String, List<ContactDetails>> contactsMap = new HashMap<>();
//		File csvFolder = new File(csvDataDirectoryPath);
//		for (File fileEntry : csvFolder.listFiles()) {
//			{
//				if (fileEntry.getName().contains(".csv")) {
//					List<ContactDetails> contactDetailsList = addressBookCsvParser
//							.getContactDetailsList(fileEntry.getPath());
//					contactsMap.put(fileEntry.getName(), contactDetailsList);
//
//				}
//			}
//			if (!contactsMap.isEmpty()) {
//				contact.setContact(contactsMap);
//			}
//		}

		List<String> addressBookList = Arrays.asList(csvAddressBooks.split(","));
		for (String currentBook : addressBookList) {
			List<ContactDetails> contactDetailsList = addressBookCsvParser
					.getContactDetailsList(csvDataDirectoryPath.concat(currentBook).concat(".csv"));
			contactsMap.put(currentBook, contactDetailsList);
		}
		if (!contactsMap.isEmpty()) {
			contact = new Contact();
			contact.setContact(contactsMap);
		}
	}

	public AddContactResponseType addContact(AddressBookRequestType addressBook) {
		AddContactResponseType addResponse = new AddContactResponseType();
		if (contact.getContact().get(addressBook.getAddressBookName()) == null
				|| contact.getContact().get(addressBook.getAddressBookName()).isEmpty()) {
			addResponse.setResponseMessage("Provided address book is not available. Please contact administrator");
		} else {
			boolean isContactAlreadyAvailable = false;
			List<ContactDetails> contactDetailsList = contact.getContact().get(addressBook.getAddressBookName());
			for (ContactDetails currentContactDetails : contactDetailsList) {
				if (currentContactDetails.getMoblieNumber() == addressBook.getNumber()
						|| currentContactDetails.getName().equalsIgnoreCase(addressBook.getName())) {
					isContactAlreadyAvailable = true;
					break;
				}
			}
			if (!isContactAlreadyAvailable) {
				ContactDetails contactDetails = new ContactDetails();
				contactDetails.setMoblieNumber(addressBook.getNumber());
				contactDetails.setName(addressBook.getName());
				contact.getContact().get(addressBook.getAddressBookName()).add(contactDetails);
				try {
					String filePath = csvDataDirectoryPath.concat(addressBook.getAddressBookName()).concat(".csv");
					File resource = new File(filePath);
					CSVWriter writer = new CSVWriter(new FileWriter(resource.getPath(), true),
							CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
							CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
					String[] record = (addressBook.getNumber() + "," + addressBook.getName()).split(",");
					writer.writeNext(record);
					writer.close();
					addResponse.setResponseMessage("Provided contact details added to "
							.concat(addressBook.getAddressBookName()).concat(" address book."));
				} catch (IOException e) {
					log.error("Exception occured while writing data into CSV file: {}", e);
				}
			} else {
				addResponse.setResponseMessage(
						"Provided Name or Mobile Number is already available in mentioned address book.");
			}
		}
		return addResponse;
	}

	@SuppressWarnings("unlikely-arg-type")
	public GetContactResponseType getContactDetails(String type, String mobileNumber) {
		GetContactResponseType getContactResponse = null;
		List<String> addressBookList = Arrays.asList(csvAddressBooks.split(","));
		List<ContactDetailType> contactDetailTypeList = new ArrayList<>();
		if (type.equalsIgnoreCase("CONTACT")) {
			for (String currentAddressBook : addressBookList) {

				List<ContactDetails> contactDetailsList = contact.getContact().get(currentAddressBook);
				getContactResponse = new GetContactResponseType();

				for (ContactDetails currentContactDetails : contactDetailsList) {
					if (currentContactDetails.getMoblieNumber().toString().equals(mobileNumber)) {
						ContactDetailType contactDetailType = new ContactDetailType();
						contactDetailType.setAddressBookName(currentAddressBook);
						contactDetailType.setMobileNumber(mobileNumber);
						contactDetailType.setName(currentContactDetails.getName());
						contactDetailTypeList.add(contactDetailType);
					}
				}

			}
		} else if (type.equalsIgnoreCase("UNIQUE")) {
			for (int i = 0; i < addressBookList.size(); i++) {
				List<ContactDetails> contactDetailsList = contact.getContact().get(addressBookList.get(i));
				List<ContactDetailType> currentContactDetailTypeList = new ArrayList<>();
				getContactResponse = new GetContactResponseType();
				for (ContactDetails currentContactDetails : contactDetailsList) {
					if (i == 0) {
						ContactDetailType contactDetailType = new ContactDetailType();
						contactDetailType.setAddressBookName(addressBookList.get(i));
						contactDetailType.setMobileNumber(currentContactDetails.getMoblieNumber().toString());
						contactDetailType.setName(currentContactDetails.getName());
						currentContactDetailTypeList.add(contactDetailType);
					} else {
						boolean isExist = false;
						for (int j = 0; j < contactDetailTypeList.size(); j++) {
							if (currentContactDetails.getMoblieNumber().toString()
									.equalsIgnoreCase(contactDetailTypeList.get(j).getMobileNumber())
									|| currentContactDetails.getName()
											.equalsIgnoreCase(contactDetailTypeList.get(j).getName())) {
								log.info("{} is already exist in response list.",
										contactDetailTypeList.get(j).getMobileNumber());
								String existingBook = contactDetailTypeList.get(j).getAddressBookName();
								List<String> existingBookList = Arrays.asList(existingBook.split(","));
								contactDetailTypeList.remove(j);
								ContactDetailType contactDetailType = new ContactDetailType();
								if (existingBookList.size() == addressBookList.size()) {
									contactDetailType.setAddressBookName(existingBook);
								} else {
									contactDetailType.setAddressBookName(
											existingBook.concat(",").concat(addressBookList.get(i)));
								}
								contactDetailType.setMobileNumber(currentContactDetails.getMoblieNumber().toString());
								contactDetailType.setName(currentContactDetails.getName());
								contactDetailTypeList.add(contactDetailType);
								isExist = true;
							} else {
								isExist = false;
							}
						}
						if (!isExist) {
							ContactDetailType contactDetailType = new ContactDetailType();
							contactDetailType.setAddressBookName(addressBookList.get(i));
							contactDetailType.setMobileNumber(currentContactDetails.getMoblieNumber().toString());
							contactDetailType.setName(currentContactDetails.getName());
							currentContactDetailTypeList.add(contactDetailType);
						}
					}
				}
				contactDetailTypeList.addAll(currentContactDetailTypeList);
			}

		} else if (type.equalsIgnoreCase("ALL")) {
			for (String currentAddressBook : addressBookList) {
				List<ContactDetails> contactDetailsList = contact.getContact().get(currentAddressBook);
				getContactResponse = new GetContactResponseType();
				for (ContactDetails currentContactDetails : contactDetailsList) {
					ContactDetailType contactDetailType = new ContactDetailType();
					contactDetailType.setAddressBookName(currentAddressBook);
					contactDetailType.setMobileNumber(mobileNumber);
					contactDetailType.setName(currentContactDetails.getName());
					contactDetailTypeList.add(contactDetailType);
				}
			}
		} else {
			log.info("Contact details are not available for provide request.");
		}

		if (getContactResponse != null && !contactDetailTypeList.isEmpty()) {
			contactDetailTypeList.sort(Comparator.comparing(ContactDetailType::getName));
			getContactResponse.setContactDetails(contactDetailTypeList);
		}
		return getContactResponse;
	}

}
