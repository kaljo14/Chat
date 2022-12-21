import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class SocketClient extends JFrame implements ActionListener, Runnable {
    JTextArea textArea = new JTextArea();
    JScrollPane jp = new JScrollPane(textArea);
    JTextField input_Text = new JTextField();
    JMenuBar menuBar = new JMenuBar();

    Socket sk;
    BufferedReader br;
    PrintWriter pw;
    String name;
    String password;

    public SocketClient() {
        super("My Chat");
        setFont(new Font("Arial Black", Font.PLAIN, 12));
        setForeground(new Color(251, 37, 118));
        setBackground(new Color(251, 37, 118));
        textArea.setToolTipText("Chat History");
        textArea.setForeground(new Color(251, 37, 118));
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 13));

        textArea.setBackground(new Color(63, 0, 113));

/*
         JMenu helpMenu = new JMenu("Help");
        JMenuItem update = new JMenuItem("Update Information");
        JMenuItem connect_List = new JMenuItem("Visitor List");

        helpMenu.add(update);
        helpMenu.add(connect_List);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
*/
        getContentPane().add(jp, "Center");
        //input_Text.setText("Enter your Message:");
        input_Text.setToolTipText("Enter your Message");
        input_Text.setForeground(new Color(0, 0, 0));
        input_Text.setFont(new Font("Tahoma", Font.BOLD, 11));
        input_Text.setBackground(new Color(230, 230, 250));
        
        getContentPane().add(input_Text, "South");
        setSize(325, 411);
        setVisible(true);

        input_Text.requestFocus(); //Place cursor at run time, work after screen is shown

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        input_Text.addActionListener(this); //Event registration
    }
    public void SaveUser(String credentials,String filepath) {
    	try {
            FileWriter writer =new FileWriter(filepath,true);
            writer.write(credentials);
            writer.close();
            
            }catch(IOException e) {System.out.println(e);}
    	
    }
    private static Scanner x;
    public static boolean verifyUsername(String filepath,String username)throws IOException{
    	boolean found = false;
    	String tempUsername;
    	
    	try {
    		x = new Scanner(new File(filepath));
    		x.useDelimiter("[,\n]");
    		while(x.hasNext()&& !found) {
    			tempUsername=x.next();
    		
    			
    			if (tempUsername.trim().equals(username.trim())) {
    				found = true;
    			}
    		}
    		x.close();
    		//System.out.println(found);
    		
    	}catch(Exception e) {System.out.println(e);}
    	
    	return found;
    }
  
    public static boolean verifyLogin(String username,String password,String filepath) {
    	boolean found = false;
    	String tempUsername;
    	String tempPassword;
    	try {
    		x = new Scanner(new File(filepath));
    		x.useDelimiter("[,\n]");
    		while(x.hasNext()&& !found) {
    			tempUsername=x.next();
    			tempPassword=x.next();
    			
    			if (tempUsername.trim().equals(username.trim()) && tempPassword.trim().equals(password.trim())) {
    				found = true;
    			}
    		}
    		x.close();
    		//System.out.println(found);
    		
    	}catch(Exception e) {System.out.println(e);}
    	
    	return found;
    }
 
   
    public void serverConnection() {
        try {
            //String IP = JOptionPane.showInputDialog(this, "Please enter a server IP.", JOptionPane.INFORMATION_MESSAGE);
            String IP = "127.0.0.1";
        	String filepath ="/Users/kaloyanivanov/eclipse-workspace/Chat/newfile.txt";

        	sk = new Socket(IP, 1234);
        	
        	String name;
        	String password;
        	
        	 String[] options = new String[] {"Login","Create A New Account"};
        	    int response = JOptionPane.showOptionDialog(null, "Message", "Title",
        	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        	        null, options, options[0]);
        	  

        	    
        	if (response == 0) {
	            Login(filepath,sk);
        	}
        	else {
        		CreateUser(filepath,sk);

        	}

            //boolean verified =verifyLogin(name,password,filepath);



           
            
    
            
          //  new Thread(this).start();

        } catch (Exception e) {
            System.out.println(e + " Socket Connection error");
        }
    }
    public void Pw(Socket sk,String name) {
				    	try {
					    	 br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
					         
					         //writing
					        pw = new PrintWriter(sk.getOutputStream(), true);
					        pw.println(name); // Send to ser
					        new Thread(this).start();
				    	} 
				    	catch (Exception e) {
				    		System.out.println(e + " Socket Connection error");
				    	}
    	
    }
    
    public void CreateUser(String filepath,Socket sk ) {
    	 name = JOptionPane.showInputDialog(this, "Create a new account", JOptionPane.INFORMATION_MESSAGE);
         password = JOptionPane.showInputDialog(this, "Please enter password", JOptionPane.INFORMATION_MESSAGE);
         boolean check;
		 try {
				check = verifyUsername(filepath,name);
				if (check) {
		             JOptionPane.showMessageDialog(null, "Username alredy exists please enter e new one", "Error Window Title", JOptionPane.ERROR_MESSAGE);
		             CreateUser(filepath,sk);
				}else {
				
				String credentials =name+","+password;
				SaveUser(credentials,filepath);
				Pw(sk,name);
				}
		 } 
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
       
    	
    }
    public void Login(String filepath,Socket sk) {
    	try {
    				name = JOptionPane.showInputDialog(this, "Please enter a nickname", JOptionPane.INFORMATION_MESSAGE);
    	//        	String name ="";
    	//        	while (name.length() < 7) {
    	//                name = JOptionPane.showInputDialog(this, "Please enter a nickname.(7 characters or less)", JOptionPane.INFORMATION_MESSAGE);
    	//            }
    	             password = JOptionPane.showInputDialog(this, "Please enter password", JOptionPane.INFORMATION_MESSAGE);
    	          
    	          //read
    	        	//String filepath ="/Users/kaloyanivanov/eclipse-workspace/Chat/newfile.txt";
    	            boolean verified =verifyLogin(name,password,filepath);
    	            int i =1;
    	            while(!verified) {
    	            if (!verified) {
    	            	//JOptionPane.showMessageDialog (null, "Incorrect username or password", "Title", JOptionPane.WARNING_MESSAGE);
    	            	//JOptionPane.showMessageDialog (null, "Message", "Title", JOptionPane.ERROR_MESSAGE);
    	            	JLabel messageLabel = new JLabel("<html><body><p style='width: 300px;'>"+"Invalid Passowrd!"+ "Atempt number:"+i+"</p></body></html>");
    	                Timer timer = new Timer(10000, 
    	                    new ActionListener()
    	                    {   
    	                        @Override
    	                        public void actionPerformed(ActionEvent event)
    	                        {
    	                            SwingUtilities.getWindowAncestor(messageLabel).dispose();
    	                        }
    	                    });
    	               
    	                i++;
    	                timer.setRepeats(false);
    	               
    	                timer.start();
    	                JOptionPane.showMessageDialog(null, messageLabel, "Error Window Title", JOptionPane.ERROR_MESSAGE);
    	                if (i>=4) {
        	                JOptionPane.showMessageDialog(null, "You will have to wait 30 seconds before trying agin.Please press ok to start timer", "Error Window Title", JOptionPane.ERROR_MESSAGE);
        	                TimeUnit.SECONDS.sleep(30);
    	                }
    	
    	                name = JOptionPane.showInputDialog(this, "Please enter a name", JOptionPane.INFORMATION_MESSAGE);
    	
    	               password = JOptionPane.showInputDialog(this, "Please enter password", JOptionPane.INFORMATION_MESSAGE);
    	               verified =verifyLogin(name,password,filepath);
    	            }else {verified=false;}
    	            }
    	            Pw(sk,name);
    	}
    	catch (Exception e) {
            System.out.println(e + "--> Client run fail");
        }
    }


    public static void main(String[] args) {
        new SocketClient().serverConnection(); //Method call at the same time object creation
    }

    @Override
    public void run() {
        String data = null;
       
        try {
            while ((data = br.readLine()) != null) {
                textArea.append(data + "\n"); //textArea Decrease the position of the box's scroll bar by the length of the text entered
                textArea.setCaretPosition(textArea.getText().length());
            }
        } catch (Exception e) {
            System.out.println(e + "--> Client run fail");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String data = input_Text.getText();
        pw.println(data); // Send to server side
        input_Text.setText("");
    }
    
}