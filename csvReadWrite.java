import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class csvReadWrite {
	//change file location
	private final static String Path = new File("").getAbsolutePath();
	private final static String SERVERTIME_FILE_LOCATION =Path+"\\ServerTime.csv";
	private final static String ACCOUNT_FILE_LOCATION =Path+"\\account_sample.csv";
	private final static String TRANSACTION_FILE_LOCATION =Path+"\\bank_sample_01.csv";
	private final static String USER_FILE_LOCATION =Path+"\\user_sample.csv";
	private final static String ERRORLOG_FILE_LOCATION =Path+"\\errorlog.csv";
	private static AccountFactory accountfactory = new AccountFactory();
	/**Start of Server Time Read Write
	 * view
	 * update*/
	public static Date viewServerTime() throws ParseException, IOException {
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(SERVERTIME_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		Date date = new Date();
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(row[0]);
				
			} catch (Exception e) {
				try {
					pw.append(br.readLine().toString()+","+e+"\n");
				}catch (Exception fe) {
					fe.printStackTrace();
					continue;
				}
				continue;
			}
		
		}
		br.close();
		pw.close();
		return date;
	}
	public static Date viewLastCompoundTime() throws ParseException, IOException {
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(SERVERTIME_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		Date date = new Date();
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(row[1]);
				
			} catch (Exception e) {
				try {
					pw.append(br.readLine().toString()+","+e+"\n");
				}catch (Exception fe) {
					fe.printStackTrace();
					continue;
				}
				continue;
			}
		
		}
		br.close();
		pw.close();
		return date;
	}
	public static void updateServerTime(Date newdate) throws IOException {
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(SERVERTIME_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
				tempCSVinString += String.format("%s,%s\n", new SimpleDateFormat("yyyy-MM-dd").format(newdate),row[1]);				
			
		}
		br.close();
		PrintWriter pw = new PrintWriter(SERVERTIME_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	public static void updateLastCompoundTime(Date newdate) throws IOException {
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(SERVERTIME_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
				tempCSVinString += String.format("%s,%s\n",row[0], new SimpleDateFormat("yyyy-MM-dd").format(newdate));				
			
		}
		br.close();
		PrintWriter pw = new PrintWriter(SERVERTIME_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	/**End of  Server Time  Read Write */
	/**Start of transaction Read Write
	 * view
	 * add
	 * no update for audit purpose*/
	public static List<Transaction> viewAllTransaction() throws ParseException, IOException {
		boolean firstLine = true;
		List<Transaction> list = new ArrayList<>();
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			if (firstLine) {
				firstLine = false;
				continue;
				} 
			try {
				String[] row = line.split(splitBy); // use comma as separator
				//Transaction(String accountNo, Date date, String transactionDetail, String chequeNo, 
				//Date valueDate,String withdrawAMT, String depositAMT, String balanceAMT)
				//check row length? if more than 8 means need merge description
				list.add(new Transaction(row[0].replace("'","")
						,new SimpleDateFormat("yyyy-MM-dd").parse(row[1])
						, row[2]
						, row[3]
						, new SimpleDateFormat("yyyy-MM-dd").parse(row[4])
						, row[5]
						, row[6]
						, row[7]));
			} catch (Exception e) {
				try {
					pw.append(br.readLine().toString()+","+e+"\n");
				}catch (Exception fe) {
					fe.printStackTrace();
					continue;
				}
				continue;
			}
		
		}
		br.close();
		pw.close();
		return list;
	}
	public static List<Transaction> viewAccountTransaction(String accountNo) throws ParseException, IOException {
		boolean firstLine = true;
		List<Transaction> list = new ArrayList<>();
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			if (firstLine) {
				firstLine = false;
				continue;
				} 
			String[] row = line.split(splitBy); // use comma as separator
			//System.out.println(row);
			//Transaction(String accountNo, Date date, String transactionDetail, String chequeNo, 
			//Date valueDate,String withdrawAMT, String depositAMT, String balanceAMT)
			if(row[0].replace("'","").equalsIgnoreCase(accountNo)) {				
				try {
					
					list.add(new Transaction(row[0].replace("'","")
							,new SimpleDateFormat("yyyy-MM-dd").parse(row[1])
							, row[2]
							, row[3]
							, new SimpleDateFormat("yyyy-MM-dd").parse(row[4])
							, row[5]
							, row[6]
							, row[7]));
				} catch (Exception e) {
					try {
						pw.append(br.readLine().toString()+","+e+"\n");
					}catch (Exception fe) {
						fe.printStackTrace();
						continue;
					}
					continue;
				}
			}
		} 
		br.close();
		pw.close();
		return list;
	}
	public static List<Transaction> viewAccountDateTransaction(String accountNo, Date dateFrom, Date dateTo) throws ParseException, IOException {
		boolean firstLine = true;
		List<Transaction> list = new ArrayList<>();
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(Path + TRANSACTION_FILE_LOCATION));
		PrintWriter pw = new PrintWriter(Path + ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			if (firstLine) {
				firstLine = false;
				continue;
				} 
			String[] row = line.split(splitBy); // use comma as separator
			//Transaction(String accountNo, Date date, String transactionDetail, String chequeNo, 
			//Date valueDate,String withdrawAMT, String depositAMT, String balanceAMT)
			if(row[0].replace("'","").equalsIgnoreCase(accountNo) 
					&& ((dateFrom.before(new SimpleDateFormat("yyyy-MM-dd").parse(row[1])) && dateTo.after(new SimpleDateFormat("yyyy-MM-dd").parse(row[1]))) 
					|| (dateFrom.before(new SimpleDateFormat("yyyy-MM-dd").parse(row[4])) && dateTo.after(new SimpleDateFormat("yyyy-MM-dd").parse(row[4]))))) {				
				try {
					
					list.add(new Transaction(row[0].replace("'","")
							,new SimpleDateFormat("yyyy-MM-dd").parse(row[1])
							, row[2]
							, row[3]
							, new SimpleDateFormat("yyyy-MM-dd").parse(row[4])
							, row[5]
							, row[6]
							, row[7]));
				} catch (Exception e) {
					try {
						pw.append(br.readLine().toString()+","+e+"\n");
					}catch (Exception fe) {
						fe.printStackTrace();
						continue;
					}
					continue;
				}
			}
		} 
		br.close();
		pw.close();
		return list;
	}
	public static void addTransaction(Transaction t) throws IOException {
		// System.out.println(TRANSACTION_FILE_LOCATION);	
		// File file = new File(".");	
		// System.out.println(file.getAbsolutePath());	
		// for(String fileNames : file.list()) System.out.println(fileNames);
		FileWriter fw = new FileWriter(TRANSACTION_FILE_LOCATION,true);
		fw.append(t.toCSV());
		fw.close();
	}
	public static void addTransaction(String accountNo, Date date, String transactionDetail, String chequeNo, Date valueDate,String withdrawAMT, String depositAMT, String balanceAMT) throws IOException {
		//Transaction(String accountNo, Date date, String transactionDetail, String chequeNo, Date valueDate,String withdrawAMT, String depositAMT, String balanceAMT)
		FileWriter fw = new FileWriter(TRANSACTION_FILE_LOCATION,true);
		fw.append(String.format("\n%s',%s,%s,%s,%s,%s,%s,%s", accountNo, new SimpleDateFormat("yyyy-MM-dd").format(date),transactionDetail,chequeNo,new SimpleDateFormat("yyyy-MM-dd").format(valueDate),withdrawAMT,depositAMT,balanceAMT));
		fw.close();
	}
	/**End of transaction Read Write */
	/**Start of Account Read Write
	 * view
	 * add
	 * update*/
	public static List<Account> viewAllAccount() throws ParseException, IOException {
	boolean firstLine = true;
	List<Account> list = new ArrayList<>();
	String line = ""; String splitBy = ","; 
	BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
	PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
	while((line = br.readLine()) != null) //returns a Boolean value 
	{ 
		//[0]Account No,[1]Account Type,[2]BALANCE AMT,[3]Withdrawal limit,[4]Withdrawal limit left,[5]Oversea Withdrawal limit,[6]Oversea Withdrawal limit left,[7]User ID
		if (firstLine) {
			firstLine = false;
			continue;
			} 
		String[] row = line.split(splitBy); // use comma as separator
		try {
			if(row[1].equalsIgnoreCase("CHECKING")) {
				list.add(accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6])));		
			}				
			else if(row[1].equalsIgnoreCase("SAVINGS")) {				
				list.add(accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6])));	
			}
		} catch (Exception e) {
			try {
				pw.append(br.readLine().toString()+","+e+"\n");
			}catch (Exception fe) {
				fe.printStackTrace();
				continue;
			}
			continue;
		}
	} 
	br.close();
	pw.close();
	return list;
	}
	public static Account viewAccount(String accountNo) throws ParseException, IOException {
		Account acc = null;
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			//[0]Account No,[1]Account Type,[2]BALANCE AMT,[3]Withdrawal limit,[4]Withdrawal limit left,[5]Oversea Withdrawal limit,[6]Oversea Withdrawal limit left,[7]User ID
			String[] row = line.split(splitBy); // use comma as separator
			if(row[0].replace("'","").equalsIgnoreCase(accountNo) && row[1].equalsIgnoreCase("CHECKING")) {	
				acc = accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6]));
			}
			else if(row[0].replace("'","").equalsIgnoreCase(accountNo) && row[1].equalsIgnoreCase("SAVINGS")) {				
				acc = accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6]));	
			}
		} 
		br.close();
		pw.close();
		return acc;
	}
	public static List<Account> viewUserAccounts(String userid) throws ParseException, IOException {
		List<Account> accList = new ArrayList<Account>();
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			//[0]Account No,[1]Account Type,[2]BALANCE AMT,[3]Withdrawal limit,[4]Withdrawal limit left,[5]Oversea Withdrawal limit,[6]Oversea Withdrawal limit left,[7]User ID
			String[] row = line.split(splitBy); // use comma as separator
			if(row[7].equalsIgnoreCase(userid) && row[1].equalsIgnoreCase("CHECKING")) {
				accList.add(accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6])));
			}
			else if(row[7].equalsIgnoreCase(userid) && row[1].equalsIgnoreCase("SAVINGS")) {				
				accList.add(accountfactory.createAccount(row[1],row[0].replace("'",""),Double.parseDouble(row[2]),Double.parseDouble(row[3]),Double.parseDouble(row[4]),Double.parseDouble(row[5]),Double.parseDouble(row[6])));	
			}
		} 
		br.close();
		pw.close();
		return accList;
	}
	public static void addAccount(Account account,String username) throws IOException {
		FileWriter fw = new FileWriter(ACCOUNT_FILE_LOCATION,true);
		fw.append(String.format("\n%s,%s",account.toCSV(),username));
		fw.close();
	}
	public static void updateAccount(String accountNo, String updateString) throws IOException {
		//DONE: update whole row of account (include update balance function)
		// Save CSV to string, CSV file for update
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			if(row[0].replace("'","").equalsIgnoreCase(accountNo)){
				tempCSVinString += String.format("%s,%s\n", updateString,row[7]);				
			}
			else {				
				tempCSVinString += String.format("%s\n",line);		
			}
		}
		br.close();
		PrintWriter pw = new PrintWriter(ACCOUNT_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	public static void updateAccount(Account account) throws IOException {
		//Done: update whole row of account (include update balance function)
		// Save CSV to string, CSV file for update
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			if(row[0].replace("'","").equalsIgnoreCase(account.getAccountNumber())){
				tempCSVinString += String.format("%s,%s\n", account.toCSV(),row[7]);				
			}
			else {				
				tempCSVinString += String.format("%s\n",line);		
			}
		}
		br.close();
		PrintWriter pw = new PrintWriter(ACCOUNT_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	public static void updateAccount(String accountNo, String balance, String interest) throws IOException {
		//Done: for compound interest only
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			if(row[0].replace("'","").equalsIgnoreCase(accountNo)){
				tempCSVinString += String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", row[0],row[1],balance,row[3],row[4],row[5],row[6],row[7],interest);								
			}
			else {				
				tempCSVinString += String.format("%s\n",line);		
			}
		}
		br.close();
		PrintWriter pw = new PrintWriter(ACCOUNT_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	public static double getInterest(String accountNo) throws IOException {
		//Done: for compound interest only
		boolean firstLine = true;
		double result = 0.0;
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION));
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			if (firstLine) {
				firstLine = false;
				continue;
				} 
			String[] row = line.split(splitBy); 
			if(row[0].replace("'","").equalsIgnoreCase(accountNo) ) {					
				try {
					result = Double.parseDouble(row[8]);
				} catch (Exception e) {
					try {
						pw.append(br.readLine().toString()+","+e+"\n");
					}catch (Exception fe) {
						fe.printStackTrace();
						continue;
					}
					continue;
				}
			}
		} 
		br.close();
		pw.close();
		return result;
	}
	/**End of Account Read Write */
	/**Start of User Read Write
	 * view output as hashmap
	 * add
	 * update*/
	public static HashMap<String, String> viewUser() throws ParseException, IOException {
		Customer cus = null;
		String line = ""; String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(USER_FILE_LOCATION)); 
		PrintWriter pw = new PrintWriter(ERRORLOG_FILE_LOCATION);
		HashMap<String, String> hm = new HashMap<String, String>();
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			String[] row = line.split(splitBy);
			hm.put(row[0],row[1]);						
		} 
		br.close();
		pw.close();
		return hm;
	}
	public static void addUser(User user) throws IOException {
		FileWriter fw = new FileWriter(USER_FILE_LOCATION,true);
		fw.append(String.format("\n%s,%s", user.toCSV(),""));//[0]User ID,[1]Pin,[2]salt
		fw.close();		
	}
	public static void addUser(String userID,String pin) throws IOException {
		FileWriter fw = new FileWriter(USER_FILE_LOCATION,true);
		fw.append(String.format("\n%s,%s,%s",userID,pin,""));//[0]User ID,[1]Pin,[2]salt
		fw.close();		
	}
	public static void updateUser(User user) throws IOException {
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(USER_FILE_LOCATION)); 
		String tempCSVinString = "";
		//[0]User ID,[1]Pin,[2]salt
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			if(row[0].equalsIgnoreCase(user.getUserID())){
				tempCSVinString += String.format("%s,%s\n", user.toCSV(),"");				
			}
			else {				
				tempCSVinString += String.format("%s\n",line);		
			}
		}
		br.close();
		PrintWriter pw = new PrintWriter(USER_FILE_LOCATION);
		pw.append(tempCSVinString);		
		pw.close();
		return;
	}
	public static void updateUser(String userid, String pin) throws IOException {
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(USER_FILE_LOCATION)); 
		String tempCSVinString = "";
		//[0]User ID,[1]Pin,[2]salt
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 	
			String[] row = line.split(splitBy);
			if(row[0].equalsIgnoreCase(userid)){
				tempCSVinString += String.format("%s,%s,%s\n",row[0],pin,"");				
			}
			else {				
				tempCSVinString += String.format("%s\n",line);		
			}
		}
		br.close();
		PrintWriter pw = new PrintWriter(USER_FILE_LOCATION);
		pw.append(tempCSVinString);		
		System.out.println(tempCSVinString);
		pw.close();
		return;
	}
	/**End of User Read Write */
	public static void refreshlimit() throws ParseException, IOException {
		String line = ""; 
		String splitBy = ","; 
		BufferedReader br = new BufferedReader(new FileReader(ACCOUNT_FILE_LOCATION)); 
		String tempCSVinString = "";
		while((line = br.readLine()) != null) //returns a Boolean value 
		{ 
			String[] row = line.split(splitBy); 
			//DONE: reset all limit left to equal daily limit (both withdrawal limit)
			//[0]Account No,[1]Account Type,[2]BALANCE AMT,[3]Withdrawal limit,[4]Withdrawal limit left,[5]Oversea Withdrawal limit,[6]Oversea Withdrawal limit left,[7]User ID
			
			tempCSVinString += String.format("%s,%s,%s,%s,%s,%s,%s,%s\n"
					,row[0],row[1],row[2],row[3],row[3],row[5],row[5],row[7]);	
		
		}
		br.close();
		PrintWriter pw = new PrintWriter(ACCOUNT_FILE_LOCATION);
		pw.append(tempCSVinString);	
		pw.close();
		return;
	}
}
