package src;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "user")
@RequestScoped
public class User implements Serializable{

    int id;
    String name;
    String email;
    String password;
    String gender;
    String address;
    ArrayList usersList;
    Connection con;
    Map<String,Object> sessionMap=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public ArrayList getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList userList) {
        this.usersList = userList;
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    
    public Connection getConnection()
    {
        try
        {
           Class.forName("oracle.jdbc.driver.OracleDriver");
           con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
        }catch(Exception e)
        {
            
        }
        return con;
    }
    
    public ArrayList usersList()
    {
        try
        {
            usersList=new ArrayList();
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("select * from users");
            ResultSet rs=stmt.executeQuery();
            
            while(rs.next())
            {
                User u=new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setEmail(rs.getString(3));
                u.setPassword(rs.getString(4));
                u.setGender(rs.getString(5));
                u.setAddress(rs.getString(6));
                usersList.add(u);
            }
            con.close();
        }
        catch(Exception e)
        {
            
        }
        
        return usersList; 
    }
    
    public String save()
    {
        int result=0;
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("insert into users values(?,?,?,?,?,?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, gender);
            stmt.setString(6, address);
            result=stmt.executeUpdate();
            con.close();
        }catch(Exception e){}
        
        if(result!=0)
        {
            return "index.xhtml?faces-redirect=true";
        }
        else
        {
            return "create.xhtml?faces-redirect=true";
        }
    }

    public String edit(int id)
    {
        User user=null;
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("select * from users where id=?");
            stmt.setInt(1, id);
            ResultSet rs=stmt.executeQuery();
            rs.next();
            user=new User();
            user.setId(rs.getInt(1));
            user.setName(rs.getString(2));
            user.setEmail(rs.getString(3));
            user.setPassword(rs.getString(4));
            user.setGender(rs.getString(5));
            user.setAddress(rs.getString(6));
            sessionMap.put("editUser", user);
            con.close();
        }catch(Exception e)
        {
            
        }
        
        return "/edit.xhtml?faces-redirect=true";
    }
    
    public String update(User u)
    {
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("update users set name=?, email=?, password=?, gender=?, address=? where id=?");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getGender());
            stmt.setString(5, u.getAddress());
            stmt.setInt(6, u.getId());
            stmt.executeUpdate();
            con.close();
        }
        catch(Exception e)
        {
            
        }
        return "/index.xhtml?faces-redirect=true";
    }
    
    public void delete(int id)
    {
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("delete from users where id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            con.close();
        }
        catch(Exception e)
        {
            
        }
    }
    
    
}
