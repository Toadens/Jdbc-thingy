/*
* Created by Johnny Edgett
* Interacts with a database to perform simple queries and other fun stuff
* Edit Date 10-12-2015
* Snippet of code under Berkeley License, see below

    Copyright 2003 Sun Microsystems, Inc. ALL RIGHTS RESERVED.
    Use of this software is authorized pursuant to the terms of the license found at
    http://developer.java.sun.com/berkeley_license.html.

    Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.  
    Redistribution and use in source and binary forms, with or without modification,
    are permitted provided that the following conditions are met: 

    - Redistribution of source code must retain the above copyright notice, 
    this list of conditions and the following disclaimer.

    - Redistribution in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

    Neither the name of Sun Microsystems, Inc. or the names of contributors may 
    be used to endorse or promote products derived from this software without
    specific prior written permission.

    This software is provided "AS IS," without a warranty of any kind.  
    ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
    ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
    NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICORSYSTEMS, INC. ("SUN")
    AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
    AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
    DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
    REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
    INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
    LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN
    IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

    You acknowledge that this software is not designed, licensed or intended for
    use in the design, construction, operation or maintenance of any nuclear
    facility.
*/

import java.sql.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.swing.JOptionPane;

public class jdbc {
    
// <editor-fold defaultstate="collapsed" desc="Variables, getters, setters"> - See more at: https://ui.netbeans.org/docs/ui/code_folding/cf_uispec.html#sthash.7ui0G0od.dpuf
    // Variable declarations - do not modify
    private String serverName;
    private int portNumber;
    private String databaseName;
    private String user;
    private String password;
    private String statement;
    boolean makeCon = true;
    // End of variable declarations
    
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
// </editor-fold>
    
    public void select() {
        //Initially setting up the conncetion
        Connection con = null; 
        
        try{
            //set the connection
            MysqlDataSource ds = new MysqlDataSource();
            ds.setServerName(getServerName());
            ds.setPortNumber(getPortNumber());
            ds.setDatabaseName(getDatabaseName());
            ds.setUser(getUser());
            ds.setPassword(getPassword());
            
            //get the connection
            con = ds.getConnection();
            
            //get database info
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Server name: " + meta.getDatabaseProductName());
            System.out.println("Server version: " + meta.getDatabaseProductVersion());

            // Parts of the following code covered under berkeley license (see top)
            // Creating the statement, result set, meta data
            try (
                    Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(getStatement());
                ResultSetMetaData rsmd = rs.getMetaData();
                PrintColumnTypes.printColTypes(rsmd);
                System.out.println("");
                
                int numColumns = rsmd.getColumnCount();
                
                for(int i = 1; i <= numColumns; i++){
                    if (i > 1) 
                        System.out.print(", ");
                    String columnName = rsmd.getColumnName(i);
                    System.out.print(columnName);
                }
                System.out.println("");
                
                while(rs.next()){
                    for (int i = 1; i <= numColumns; i++){
                        if (i > 1) System.out.print(", ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");
                }
                
                //Close the connection, result set, and statement
                con.close();
                rs.close();
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.err.println("Exception: " + e.getMessage());
        }
    }

}

class PrintColumnTypes  {

  public static void printColTypes(ResultSetMetaData rsmd) throws SQLException {
    int columns = rsmd.getColumnCount();
    for (int i = 1; i <= columns; i++) {
      int jdbcType = rsmd.getColumnType(i);
      String name = rsmd.getColumnTypeName(i);
      System.out.print("Column " + i + " is JDBC type " + jdbcType);
      System.out.println(", which the DBMS calls " + name);
    }
  }
}
