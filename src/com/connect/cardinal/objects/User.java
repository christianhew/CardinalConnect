package com.connect.cardinal.objects;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.connect.cardinal.hibernate.HibernateUtil;
import com.connect.cardinal.util.ObjectRetriever;
import com.google.gson.annotations.Expose;

/**
 * @author jmackin
 *
 */
@Entity
@Table(name = "user")
public class User extends DatabaseObject
{
	@Expose
	private String firstName;
	
	@Expose
	private String middleName;
	
	@Expose
	private String lastName;
	
	@Expose
	private String email;
	
	// password is not exposed
	private String password;	
	
	@Expose
	private String focus;
	
	@Expose
	private String company;
	
	@Expose
	private UserStatus status;
	
	@Expose
	private int active;
	
	
	//TODO: Would be more secure by ID instead of username
	public static User getUserByUsername(String username)
	{
		System.out.println(username);
		Session session = HibernateUtil.getSession();
		
		// query string
		String usernames = ObjectRetriever.FROM + "User E where E.email = '" + username + "'";
		
		Query query = session.createQuery(usernames);
		List user = query.list();
		
		if(user != null && user.size() == 1)
			return (User) user.get(0);
		return null;
	}
	
	
	/**
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public User(String firstName, String middleName, String lastName, String email, String password, UserStatus status) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.status = status;
		this.focus = "";
		this.company = "";
		setActive(0);
	}
	
	public User()
	{
		this.firstName = null;
		this.middleName = null;
		this.lastName = null;
		this.email = null;
		this.password = null;
		this.status = null;
		this.focus = null;
		this.company = null;
		setActive(0);
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	/**
	 * @param parameters
	 * @return
	 */
	public static String editStudentOrAlum(Map<String, String> parameters) 
	{
		String idString = parameters.get("ACCOUNT_ID");
		//String studentOrAlum = parameters.get("studentOrAlum");
		
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();


		User user = session.load(User.class, Long.parseLong(idString));
		//user.setStatus(studentOrAlum);
		
		session.merge(user);
		tx.commit();
		session.flush();
		
		return "UPDATED STATUS";
	}

	/**
	 * @param parameters
	 * @return
	 */
	public static String editFocus(Map<String, String> parameters) 
	{
		String idString = parameters.get("ACCOUNT_ID");
		String studentOrAlum = parameters.get("focus");
		
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();


		User user = session.load(User.class, Long.parseLong(idString));
		user.setFocus(studentOrAlum);
		
		session.merge(user);
		tx.commit();
		session.flush();
		
		return "UPDATED FOCUS";
	}

	/**
	 * @param parameters
	 * @return
	 */
	public static String editCompany(Map<String, String> parameters) 
	{
		String idString = parameters.get("ACCOUNT_ID");
		String studentOrAlum = parameters.get("company");
		
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();


		User user = session.load(User.class, Long.parseLong(idString));
		user.setCompany(studentOrAlum);
		
		session.merge(user);
		tx.commit();
		session.flush();
		
		return "UPDATED COMPANY";
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * @param parameters
	 * @return
	 */
	public static String AcceptOrDeny(Map<String, String> parameters)
	{
		String accountId = parameters.get("accountId");
		String accepted = parameters.get("accepted");
		
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();

		User user = session.load(User.class, Long.parseLong(accountId));
		
		if(accepted.equals("true"))
		{
			user.setActive(1);
		}
		else if(accepted.equals("false"))
		{
			user.setActive(0);
		}
		else
		{
			System.out.println("unhandled case: " + accepted);
			tx.commit();
			session.flush();
			return "INTERNAL_ERROR";
		}
		
		session.merge(user);
		tx.commit();
		session.flush();
		
		return "UPDATED_ACCOUNT";
	}
}
