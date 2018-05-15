package com.kibey.android.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ContactUtils {
	private static String tag = "contact_tag == ";

	/**
	 * 获取通讯录中联系人名称、电话号码、来电头像uri
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<ContactPeople> getContact(Context context,
			String str) {
		long t1 = System.currentTimeMillis();
		Logs.i(tag + "key：" + str);
		Map<String, ContactPeople> names = new HashMap<String, ContactPeople>();
		Map<String, ContactPeople> phones = new HashMap<String, ContactPeople>();
		ArrayList<ContactPeople> tempContact = new ArrayList<ContactPeople>();
		List<String> id = new ArrayList<String>();
		ContentResolver cr = context.getContentResolver();
		// 取得通讯录的光标
		String orderBy = PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		Cursor cursor;
		cursor = cr.query(Contacts.CONTENT_URI, null, null,
				null, orderBy);

		// 遍历通讯录
		for (int i = 0; i < cursor.getCount(); i++) {
			ContactPeople contacts = new ContactPeople();
			cursor.moveToPosition(i);
			// 取得联系人名字
			int nameFieldColumnIndex = cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			// 取得联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(BaseColumns._ID));
			// if (!StringUtils.isEmpty(name)) {
			contacts.setContactId(contactId);
			contacts.setLocal_name(name);
			id.add(contactId);
			// setPhone(context, contacts);
			names.put(contactId, contacts);
			// }
		}
		cursor.close();

		phones = setPhone(context);
		Iterator<String> it = id.iterator();
		while (it.hasNext()) {
			String type = it.next();
			ContactPeople contactPeople = phones.get(type);
			ContactPeople temp = names.get(type);
			if (null != temp && null != contactPeople) {
				contactPeople.setLocal_name(temp.getLocal_name());
				tempContact.add(contactPeople);
			}
		}
		// ////////////////
		Logs.i(tag + (System.currentTimeMillis() - t1) + "|" + names.size()
				+ "|" + phones.size());
		return searchByKey(tempContact, str);
	}

	private static Map<String, ContactPeople> setPhone(Context context) {
		long t1 = System.currentTimeMillis();
		Cursor phone;
		Map<String, ContactPeople> listContacts = new HashMap<String, ContactPeople>();
		phone = context.getContentResolver().query(
				Phone.CONTENT_URI, null, null,
				null, null);
		String number = "";
		// 取得电话号码(可能存在多个号码)
		for (int j = 0; j < phone.getCount(); j++) {
			ContactPeople contacts = new ContactPeople();
			phone.moveToPosition(j);
			String strPhoneNumber = phone
					.getString(phone
							.getColumnIndex(Phone.NUMBER));
			String contactId = phone
					.getString(phone
							.getColumnIndex(Phone.CONTACT_ID));
			// strPhoneNumber = strPhoneNumber.replace("-", "");
			// if (StringUtils.isPhoneNum(strPhoneNumber))
			// number += strPhoneNumber;
			// Logs.v(tag + strPhoneNumber + ">>>>" + number);
			strPhoneNumber = strPhoneNumber.replace(" ", "").replace("+86", "");
			if (strPhoneNumber.length() == 16) {
				strPhoneNumber = strPhoneNumber.replace("17951", "");
			}
			contacts.setAccount(strPhoneNumber.replace(" ", "").replace("+86",
					""));
			contacts.setContactId(contactId);
			listContacts.put(contactId, contacts);
		}
		phone.close();
		Logs.i(tag + (System.currentTimeMillis() - t1));
		return listContacts;
	}

	/**
	 * 按关键字模糊匹配
	 * 
	 * @param tempContact
	 *            原始列表
	 * @param str
	 *            关键字
	 * @return
	 */
	private static ArrayList<ContactPeople> searchByKey(
			ArrayList<ContactPeople> tempContact, String str) {
		Logs.i(tag + str);
		if (TextUtils.isEmpty(str)) {
			return tempContact;
		}
		ArrayList<ContactPeople> temp = new ArrayList<ContactPeople>();
		Iterator<ContactPeople> it = tempContact.iterator();
		while (it.hasNext()) {
			ContactPeople communityPeople = it.next();
			boolean flagNum = false;
			boolean flagName = false;
			if (communityPeople.getAccount().contains(str))
				flagNum = true;
			if (communityPeople.getLocal_name().contains(str))
				flagName = true;
			if (flagNum || flagName) {
				temp.add(communityPeople);
			}
		}
		return temp;
	}

	/**
	 * 根据联系人头像uri得到头像bitmap
	 * 
	 * @param context
	 * @param contactsPhotoUri
	 * @return
	 */
	public static Bitmap getBmpFromContacts(Context context,
			Uri contactsPhotoUri) {
		ContentResolver cr = context.getContentResolver();
		Bitmap bmp = null;
		if (null != contactsPhotoUri) {
			InputStream is = Contacts
					.openContactPhotoInputStream(cr, contactsPhotoUri);
			bmp = BitmapFactory.decodeStream(is);
		} else {
			Logs.i(tag + "is:" + "null");
		}
		return bmp;
	}

	/**
	 * 根据联系人头像uri得到头像bitmap
	 * 
	 * @param context
	 * @param contactsPhotoUri
	 * @return
	 */
	public static InputStream getIsFromContacts(Context context,
			Uri contactsPhotoUri) {
		ContentResolver cr = context.getContentResolver();
		InputStream is = null;
		if (null != contactsPhotoUri) {
			is = Contacts.openContactPhotoInputStream(cr,
					contactsPhotoUri);
		}
		return is;
	}

	// phone
	private static final String[] PROJECTION_PHONENUMBER_CONTACT = {
			Phone.NUMBER, Phone.TYPE, Phone.LABEL };
	/* DISPLAY_NAME唯一性 */
	private static final String[] PROJECTION_DISPLAYNAME_CONTACT = { StructuredName.DISPLAY_NAME };
	// Email
	private static final String[] PROJECTION_EAMIL_CONTACT = { Email.DATA1,
			Email.TYPE, Email.LABEL };
	// IM
	private static final String[] PROJECTION_IM_CONTACT = new String[] {
			Im.DATA, Im.TYPE, Im.LABEL, Im.PROTOCOL };
	// address
	private static final String[] PROJECTION_ADDRESS_CONTACT = new String[] {
			StructuredPostal.STREET, StructuredPostal.CITY,
			StructuredPostal.REGION, StructuredPostal.POSTCODE,
			StructuredPostal.COUNTRY, StructuredPostal.TYPE,
			StructuredPostal.LABEL, StructuredPostal.POBOX,
			StructuredPostal.NEIGHBORHOOD, };
	// Organization
	private static final String[] PROJECTION_ORGANIZATION_CONTACT = new String[] {
			Organization.COMPANY, Organization.TYPE, Organization.LABEL,
			Organization.TITLE };
	// note
	private static final String[] PROJECTION_NOTES_CONTACT = new String[] { Note.NOTE };
	// nickname
	private static final String[] PROJECTION_NICKNAMES_CONTACT = new String[] {
			Nickname.NAME, Nickname.TYPE, Nickname.LABEL };
	// website
	private static final String[] PROJECTION_WEBSITES_CONTACT = new String[] {
			Website.URL, Website.TYPE, Website.LABEL };

	public static final String[] PROJECTION_CONTACTS = { Contacts._ID,
			Contacts.PHOTO_ID, Contacts.IN_VISIBLE_GROUP,
			Contacts.HAS_PHONE_NUMBER, Contacts.DISPLAY_NAME,
			Contacts.CUSTOM_RINGTONE };

	/**
	 * wu0wu
	 * 
	 * 功能：查询所有联系人PROJECTION_CONTACTS信息
	 * 
	 * */
	public static void _getContacts(ContentResolver cr) {
		Cursor cursorContact = null;

		try {
			cursorContact = cr.query(Contacts.CONTENT_URI,
					PROJECTION_CONTACTS, Contacts.IN_VISIBLE_GROUP + "=1",
					null, null);
			Log.e("wu0wu", "联系人个数=" + cursorContact.getCount());
			int[] indexs = getColumnIndexs(PROJECTION_CONTACTS, cursorContact);

			while (cursorContact.moveToNext()) {
				Log.e("wu0wu", "------------------------------------");
				for (int i = 0; i < PROJECTION_CONTACTS.length; i++) {
					String value = cursorContact.getString(indexs[i]);
					Log.e("wu0wu", PROJECTION_CONTACTS[i] + "=" + value);
				}
			}
		} catch (Exception e) {
			Log.e("wu0wu", e.toString());
		} finally {
			if (cursorContact != null) {
				cursorContact.close();
			}
		}
	}

	private static int[] getColumnIndexs(String[] projections, Cursor c) {
		int[] ret = new int[projections.length];
		for (int i = 0; i < projections.length; i++) {
			ret[i] = c.getColumnIndex(projections[i]);
		}
		return ret;
	}

	/**
	 * 新建联系人
	 * 
	 *            ContentResolver
	 * @param accountName
	 * @param accountType
	 * @param displayName
	 *            联系人名称
	 * @param phone
	 *            联系人电话
	 * @param email
	 * @param im
	 * @param address
	 * @param organization
	 * @param notes
	 * @param nickname
	 * @param website
	 * @return
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	public static String _insertContact(Context context, String accountName,
			String accountType, String displayName, ArrayList<String[]> phone,
			ArrayList<String[]> email, ArrayList<String[]> im,
			ArrayList<String[]> address, ArrayList<String[]> organization,
			ArrayList<String[]> notes, ArrayList<String[]> nickname,
			ArrayList<String[]> website) throws RemoteException,
			OperationApplicationException {
		ContentResolver cr = context.getContentResolver();
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		String rawId = "";
		long rawContactId = insertRawContact(cr, accountName, accountType);
		rawId = Long.toString(rawContactId);

		if (displayName != null) {
			insertContactDisplayname(ops, StructuredName.CONTENT_ITEM_TYPE,
					rawId, displayName);
		}
		if (phone != null) {
			for (int j = 0; j < phone.size(); j++) {
				String[] item = phone.get(j);
				insertItemToContact(cr, ops, Phone.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_PHONENUMBER_CONTACT, item);
			}
		}
		if (email != null) {
			for (int j = 0; j < email.size(); j++) {
				String[] item = email.get(j);
				insertItemToContact(cr, ops, Email.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_EAMIL_CONTACT, item);
			}
		}
		if (im != null) {
			for (int j = 0; j < im.size(); j++) {
				String[] item = im.get(j);
				insertItemToContact(cr, ops, Im.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_IM_CONTACT, item);
			}
		}
		if (address != null) {
			for (int j = 0; j < address.size(); j++) {
				String[] item = address.get(j);
				insertItemToContact(cr, ops,
						StructuredPostal.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_ADDRESS_CONTACT, item);
			}
		}
		if (organization != null) {
			for (int j = 0; j < organization.size(); j++) {
				String[] item = organization.get(j);
				insertItemToContact(cr, ops, Organization.CONTENT_ITEM_TYPE,
						rawId, PROJECTION_ORGANIZATION_CONTACT, item);
			}
		}
		if (notes != null) {
			for (int j = 0; j < notes.size(); j++) {
				String[] item = notes.get(j);
				insertItemToContact(cr, ops, Note.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_NOTES_CONTACT, item);
			}
		}
		if (nickname != null) {
			for (int j = 0; j < nickname.size(); j++) {
				String[] item = nickname.get(j);
				insertItemToContact(cr, ops, Nickname.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_NICKNAMES_CONTACT, item);
			}
		}
		if (website != null) {
			for (int j = 0; j < website.size(); j++) {
				String[] item = website.get(j);
				insertItemToContact(cr, ops, Website.CONTENT_ITEM_TYPE, rawId,
						PROJECTION_WEBSITES_CONTACT, item);
			}
		}
		cr.applyBatch(ContactsContract.AUTHORITY, ops);
		Logs.i(tag + "...............");
		return rawId;
	}

	/**
	 * 往ROWCONTACT里插入数据，并返回rawId
	 * 
	 * @param cr
	 * @param accountName
	 *            一般为null
	 * @param accountType
	 *            一般为null
	 * @return
	 */
	private static long insertRawContact(ContentResolver cr,
			String accountName, String accountType) {

		ContentValues values = new ContentValues();
		values.put(RawContacts.ACCOUNT_NAME, accountName);
		values.put(RawContacts.ACCOUNT_TYPE, accountType);
		Uri rawContactUri = cr.insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		return rawContactId;
	}

	/**
	 * 插入联系人名字
	 * 
	 * @param ops
	 * @param mimeType
	 * @param rawContactId
	 * @param displayName
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	private static void insertContactDisplayname(
			ArrayList<ContentProviderOperation> ops, String mimeType,
			String rawContactId, String displayName) throws RemoteException,
			OperationApplicationException {

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValue(Data.MIMETYPE, mimeType)
				.withValue(Data.RAW_CONTACT_ID, rawContactId)
				.withValue(StructuredName.DISPLAY_NAME, displayName).build());

	}

	/**
	 * 
	 * @param cr
	 * @param ops
	 * @param mimeType
	 * @param rawContactId
	 * @param PROJECTION_CONTACT
	 * @param item
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	private static void insertItemToContact(ContentResolver cr,
			ArrayList<ContentProviderOperation> ops, String mimeType,
			String rawContactId, String[] PROJECTION_CONTACT, String[] item)
			throws RemoteException, OperationApplicationException {
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, mimeType);
		for (int i = 0; i < PROJECTION_CONTACT.length; i++) {
			values.put(PROJECTION_CONTACT[i], item[i]);
		}
		Uri dataUri = cr.insert(ContactsContract.Data.CONTENT_URI, values);

		Builder builder = ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withYieldAllowed(true);
		builder.withValue(Data.RAW_CONTACT_ID, rawContactId);
		builder.withValue(Data.MIMETYPE, mimeType);
		for (int i = 0; i < PROJECTION_CONTACT.length; i++) {
			builder.withValue(PROJECTION_CONTACT[i], item[i]);
		}
		ops.add(builder.build());
	}
}
